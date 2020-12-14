/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.AnnotationFuncs;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import static org.dbs24.consts.SysConst.BOOLEAN_TRUE;
import static org.dbs24.consts.SysConst.OBJECT_NULL;
import org.dbs24.component.PersistenceEntityManager;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.api.ReferenceSyncOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.GenericApplicationContext;
import org.dbs24.references.core.CachedReferencesClasses;
import org.dbs24.references.api.ReferenceProcessor;
import org.dbs24.stmt.StmtProcessor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

@Data
@Log4j2
public abstract class AbstractReferencesService extends AbstractApplicationService {

    @Value("${entity.core.ref.synchronize:true}")
    private Boolean refSynchronize = BOOLEAN_TRUE;

    @Autowired
    GenericApplicationContext genericApplicationContext;

    @Autowired
    PersistenceEntityManager persistenceEntityManager;

    @Autowired
    ReferenceRecordsManager referenceRecordsManager;

    //==========================================================================
    @PostConstruct
    public void loadSysReferences() {

//
        Class<? extends AbstractReferencesService> clAss = this.getClass();

        log.debug("Loading system references ({})", clAss.getCanonicalName());

        while (NullSafe.notNull(clAss)) {

            final Class<? extends AbstractReferencesService> servFinalClass = clAss;

            Optional.ofNullable(AnnotationFuncs.<CachedReferencesClasses>getAnnotation(clAss, CachedReferencesClasses.class))
                    .ifPresent(annotation -> {

                        log.debug("process annotation {}", annotation);

                        Flux
                                .fromArray(AnnotationFuncs.<CachedReferencesClasses>getAnnotation(servFinalClass, CachedReferencesClasses.class).classes())
                                .filter(clazz -> !ServiceFuncs.getMapValue(AbstractRefRecord.REF_CACHE, mapEntry -> mapEntry.getKey().equals(clazz)).isPresent())
                                .sort((refClass1, refClass2) -> { // достаем признак порядкового номера из аннотации

                                    final Integer order_num1 = (NullSafe.create(OBJECT_NULL, NullSafe.DONT_THROW_EXCEPTION)
                                            .execute2result(() -> (AnnotationFuncs.getAnnotation(refClass1, ReferenceSyncOrder.class)).order_num(), Integer.valueOf("10000"))).<Integer>getObject();

                                    final Integer order_num2 = (NullSafe.create(OBJECT_NULL, NullSafe.DONT_THROW_EXCEPTION)
                                            .execute2result(() -> (AnnotationFuncs.getAnnotation(refClass2, ReferenceSyncOrder.class)).order_num(), Integer.valueOf("10000"))).<Integer>getObject();

                                    return order_num1.compareTo(order_num2);
                                })
                                .subscribe(new Subscriber<Class>() {
                                    @Override
                                    public void onSubscribe(Subscription s) {
                                        s.request(Long.MAX_VALUE);
                                    }

                                    @Override
                                    public void onNext(Class clazz) {
                                        log.debug("processing {}", clazz.getCanonicalName());
                                        if (refSynchronize) {
                                            NullSafe.create(findRegisterMethod(servFinalClass, String.format("get%sCollection", clazz.getSimpleName())))
                                                    .safeExecute(ns_method -> {
                                                        synchronized (clazz) {

                                                            log.debug("Register '{}'", clazz.getCanonicalName());
                                                            // коллекция записей справочника

                                                            referenceRecordsManager.registerReference(
                                                                    (Collection) ((Method) ns_method).invoke(null),
                                                                    clazz,
                                                                    this.getClass().getSimpleName());
                                                        }
                                                    }).throwException();
                                        }
                                        referenceRecordsManager.refreshCacheReference(clazz);
                                    }

                                    @Override
                                    public void onComplete() {
                                        log.debug("completed processing {}", servFinalClass.getCanonicalName());
                                    }

                                    @Override
                                    public void onError(Throwable t) {
                                        log.error("onError {}", t.getMessage());
                                    }
                                });
                    });
            clAss = (Class<? extends AbstractReferencesService>) clAss.getSuperclass();
        }

        log.info("{}: {} system reference(s) loaded",
                this.getClass().getSimpleName(),
                AbstractRefRecord.REF_CACHE.size()
        );

        //       });
    }

    //==========================================================================
    private Method findRegisterMethod(
            Class<? extends AbstractReferencesService> serviceClass,
            String methodName) {

        final String key = String.format("%s.%s",
                serviceClass.getCanonicalName(),
                methodName);

        log.debug("Lookup for '{}' method", key);

        return (NullSafe.create()
                .execute2result(() -> serviceClass.getMethod(methodName))
                .catchMsgException(errMsg
                        -> log.error("method not found ('{}', '{}')",
                        methodName,
                        key,
                        errMsg)
                ))
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
