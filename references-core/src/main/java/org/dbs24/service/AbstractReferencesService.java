/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.AnnotationFuncs;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import static org.dbs24.application.core.sysconst.SysConst.BOOLEAN_TRUE;
import static org.dbs24.application.core.sysconst.SysConst.OBJECT_NULL;
import org.dbs24.persistence.core.PersistenceEntityManager;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.api.ReferenceSyncOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.GenericApplicationContext;
import org.dbs24.references.core.CachedReferencesClasses;
import org.dbs24.references.api.ReferenceProcessor;

@Data
@Slf4j
public abstract class AbstractReferencesService extends AbstractApplicationService {

    @Value("${entity.core.ref.synchronize:true}")
    private Boolean refSynchronize = BOOLEAN_TRUE;

    @Autowired
    GenericApplicationContext genericApplicationContext;

    @Autowired
    PersistenceEntityManager persistenceEntityManager;

    @PostConstruct
    public void loadSysReferences() {

        Class<? extends AbstractReferencesService> clAss = this.getClass();

        log.debug("Loading system references ({})", clAss.getCanonicalName());

        while (NullSafe.notNull(clAss)) {

            final Class<? extends AbstractReferencesService> servFinalClass = clAss;
            final Annotation annotation = AnnotationFuncs.<CachedReferencesClasses>getAnnotation(clAss, CachedReferencesClasses.class);

            if (NullSafe.notNull(annotation)) {

                final Class[] classes = AnnotationFuncs.<CachedReferencesClasses>getAnnotation(clAss, CachedReferencesClasses.class
                ).classes();

                Arrays.stream(classes)
                        .sorted((refClass1, refClass2) -> { // достаем признак порядкового номера из аннотации

                            final Integer order_num1 = (NullSafe.create(OBJECT_NULL, NullSafe.DONT_THROW_EXCEPTION)
                                    .execute2result(() -> (AnnotationFuncs.getAnnotation(refClass1, ReferenceSyncOrder.class)).order_num(), Integer.valueOf("10000"))).<Integer>getObject();

                            final Integer order_num2 = (NullSafe.create(OBJECT_NULL, NullSafe.DONT_THROW_EXCEPTION)
                                    .execute2result(() -> (AnnotationFuncs.getAnnotation(refClass2, ReferenceSyncOrder.class)).order_num(), Integer.valueOf("10000"))).<Integer>getObject();

                            return order_num1.compareTo(order_num2);
                        })
                        .forEach(clazz -> {

                            if (!ServiceFuncs.getMapValue(AbstractRefRecord.REF_CACHE, mapEntry -> mapEntry.getKey().equals(clazz)).isPresent()) {

                                // синхронизировать справочник в БД
                                if (refSynchronize) {
                                    NullSafe.create(this.findRegisterMethod(servFinalClass, String.format("get%sCollection", clazz.getSimpleName())))
                                            .safeExecute(ns_method -> {
                                                synchronized (clazz) {

                                                    log.info("Register reference '{}'", clazz.getCanonicalName());
                                                    // коллекция записей справочника
                                                    final Collection collection = (Collection) ((Method) ns_method).invoke(null);

                                                    log.info("Reference {} is registered ({} recs) ",
                                                            clazz.getCanonicalName(),
                                                            collection.size()
                                                    );
                                                    // сохранение в бд
                                                    getPersistenceEntityManager()
                                                            .executeTransaction(em -> collection
                                                            .stream()
                                                            .forEach(record -> em.merge(record)));
                                                }
                                            }).throwException();
                                }

                                // перечитываем справочники
                                NullSafe.create()
                                        .execute(stmt -> {
                                            final Collection<? extends AbstractRefRecord> collection = this.getPersistenceEntityManager()
                                                    .getEntityManager()
                                                    .createQuery("Select t from " + clazz.getSimpleName() + " t")
                                                    .getResultList();

                                            AbstractRefRecord.REF_CACHE.put(clazz, collection);
                                            log.debug(String.format("Reference '%s' is loaded ",
                                                    clazz.getCanonicalName()));

                                        })
                                        .catchException(e -> log.error("Can't load refernce '{}' ({}) ", clazz.getCanonicalName(), e.getMessage()))
                                        .throwException();
                            }
                        });
            }
            clAss = (Class<? extends AbstractReferencesService>) clAss.getSuperclass();
        }

        log.info("There are [{}] system reference(s) loaded '{}' ",
                AbstractRefRecord.REF_CACHE.size(),
                this.getClass().getCanonicalName());
    }

    //--------------------------------------------------------------------------
    private Method findRegisterMethod(
            Class<? extends AbstractReferencesService> serviceClass,
            String methodName) {

        final String key = String.format("%s.%s",
                serviceClass.getCanonicalName(),
                methodName);

        log.debug("Lookup for '{}' method", key);

        return (NullSafe.create()
                .execute2result(() -> serviceClass.getMethod(methodName))
                .catchMsgException(errMsg -> {
                    log.error("method not found ('{}', '{}')",
                            methodName,
                            key,
                            errMsg);
                }))
                .<Method>getObject();
    }

    //==========================================================================
    protected static <T extends AbstractRefRecord> Collection<T> getGenericCollection(
            Class<T> clazz,
            String[][] refArray,
            ReferenceProcessor<T> referenceProcessor) {

        final Collection<T> recordCollection = ServiceFuncs.<T>createCollection();

        Arrays.stream(refArray)
                .unordered()
                .forEach(stringRow -> recordCollection.add(NullSafe.<T>createObject(
                clazz, object -> referenceProcessor.processRef(object, stringRow))));

        return recordCollection;
    }
}
