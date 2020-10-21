/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import static org.dbs24.application.core.sysconst.SysConst.*;
import org.dbs24.application.core.service.funcs.AnnotationFuncs;
import lombok.Data;
import lombok.AllArgsConstructor;
import org.dbs24.entity.core.AbstractPersistenceEntity;
import org.dbs24.persistence.core.PersistenceEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import org.dbs24.entity.core.api.EntityClassesPackages;
import org.dbs24.entity.core.api.ActionClassesPackages;
import org.dbs24.application.core.exception.api.InternalAppException;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.application.core.service.funcs.ReflectionFuncs;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.entity.core.AbstractAction;
import org.dbs24.entity.core.api.ActionCodeId;
import org.dbs24.entity.core.api.EntityTypeId;
import java.util.Arrays;
import java.util.Map;
import java.util.Collection;
import org.dbs24.entity.core.AbstractActionEntity;
import org.dbs24.entity.action.ActionCode;
import org.dbs24.entity.core.api.EntityKindId;
import org.dbs24.entity.core.api.EntityStatusesRef;
import org.dbs24.entity.status.EntityStatus;
import org.dbs24.entity.kind.EntityKind;
import org.dbs24.entity.marks.Mark;
import org.dbs24.entity.marks.MarkValue;
import org.dbs24.entity.status.EntityStatusId;
import org.dbs24.references.api.AbstractRefRecord;
import java.lang.annotation.Annotation;
import org.dbs24.references.core.CachedReferencesClasses;
import org.dbs24.references.api.ReferenceSyncOrder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.util.Assert;
import org.dbs24.persistence.core.PersisntanceEntityCreator;
import org.dbs24.persistence.api.PersistenceEntity;

@Data
@Slf4j
@CachedReferencesClasses(classes = {EntityStatus.class, EntityKind.class, ActionCode.class, Mark.class, MarkValue.class})
public abstract class AbstractActionExecutionService extends AbstractApplicationService {

    @Value("${entity.core.ref.synchronize:true}")
    private Boolean refSynchronize = BOOLEAN_TRUE;

    @Autowired
    GenericApplicationContext genericApplicationContext;

    @Autowired
    PersistenceEntityManager persistenceEntityManager;

    @Autowired
    EntityReferencesService entityReferencesService;

    //==========================================================================
    final Collection<Pair> CLASS_ENT2ACTION
            = ServiceFuncs.createCollection();

    final Collection<? extends AbstractActionEntity> enityCollection
            = ServiceFuncs.createCollection();

    // сущность - действие
//    private final Map<Class<ENT>, Class<ACT>> CLASS_ENT2ACTION
//            = ServiceFuncs.getOrCreateMap_Safe(ServiceFuncs.MAP_NULL);
    // действие - номер действия
    final Map<Integer, Class<ACT>> CLASS_INT2ACTION
            = ServiceFuncs.getOrCreateMap_Safe(ServiceFuncs.MAP_NULL);

    // сущность - статус сущности
    final Map<Class<ENT>, Integer> CLASS_ENT2STATUS
            = ServiceFuncs.getOrCreateMap_Safe(ServiceFuncs.MAP_NULL);

    //==========================================================================
    class IllegalActionForEntity extends InternalAppException {

        public IllegalActionForEntity(final String message) {
            super(message);
        }
    }

    class UnknownActionCode extends InternalAppException {

        public UnknownActionCode(final String message) {
            super(message);
        }
    }

    // выполнение действия над сущностью
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public <T extends AbstractAction> void executeAction(
            final AbstractActionEntity entity,
            final Integer action_code,
            //final ActionInitializer<T> actionInitializer) {
            final MultiValueMap mvm) {
        final Class actClass = ServiceFuncs.getMapValue(
                CLASS_INT2ACTION,
                mapEntry -> mapEntry.getKey().equals(action_code))
                .orElseThrow(() -> new UnknownActionCode(String.format("Unknown fucking action_code (%d)", action_code)));

        Assert.notNull(entity, String.format("%s: fucking entity is null!",
                actClass.getCanonicalName()));

        // находим класс среди допустимых
        CLASS_ENT2ACTION
                .stream()
                .filter(pair -> pair.getEntClass().equals(entity.getClass()))
                .filter(pair -> pair.getActClass().equals(actClass))
                .findFirst()
                .orElseThrow(() -> new IllegalActionForEntity(String.format("Action '%s' is not allowed for entity (%s)",
                entity, actClass)));

        log.debug("Execute action ({}, {}): '{}'",
                entity.getClass().getSimpleName(),
                entity.entityId(),
                actClass.getCanonicalName());

        final AbstractAction action = genericApplicationContext.<AbstractAction>getBean(actClass);

        final ActionCode actionCode = ActionCode.findActionCode(action_code);

        action.setEntity(entity);
        action.setActionCode(actionCode);
        // дополнительные параметры из запроса
        action.setMvm(mvm);

        // дополнительная инициализация действия
        NullSafe.create()
                .execute(() -> action.execute())
                .catchException(e -> action.registerActionFail(e))
                .throwException();
    }

    //==========================================================================
    @PostConstruct
    public void postActionExecutionService() {

        if (!AnnotationFuncs.isAnnotated(this.getClass(), EntityClassesPackages.class)) {
            class NoEntityClassesPackagesDefined extends InternalAppException {

                public NoEntityClassesPackagesDefined(final String message) {
                    super(message);
                }
            }
            throw new NoEntityClassesPackagesDefined(String.format("No EntityClassesPackages annotations defined for '%s' ",
                    this.getClass().getCanonicalName()));
        }

        final String[] entityClassesPackages = AnnotationFuncs.<EntityClassesPackages>getAnnotation(this.getClass(), EntityClassesPackages.class).pkgList();

        // классы сущностей
        Arrays.stream(entityClassesPackages)
                .forEach(entPkg -> {

                    final Class<AbstractActionEntity> clazz = (AbstractActionEntity.class);
                    final Class<EntityTypeId> annClazz = EntityTypeId.class;

                    // значения для справочника берутся из аннотаций классов
                    ReflectionFuncs.processPkgClassesCollection(entPkg, clazz, annClazz,
                            (entClazz) -> {

                                // действия на сущности
                                if (!AnnotationFuncs.isAnnotated(entClazz, ActionClassesPackages.class)) {
                                    class NoActionClassesPackagesDefined extends InternalAppException {

                                        public NoActionClassesPackagesDefined(final String message) {
                                            super(message);
                                        }
                                    }
                                    throw new NoActionClassesPackagesDefined(String.format("No ActionClassesPackagesDefined annotations defined for '%s' ",
                                            entClazz.getCanonicalName()));
                                }

                                final String[] actionsClassesPackages = AnnotationFuncs.<ActionClassesPackages>getAnnotation(entClazz, ActionClassesPackages.class).pkgList();
                                //this.RegisterEntityClass((Class< T>) clazz);
                                Arrays.stream(actionsClassesPackages)
                                        .forEach(actPkg -> {

                                            final Class<AbstractAction> clazzAct = (AbstractAction.class);
                                            final Class<ActionCodeId> annActClazz = ActionCodeId.class;

                                            // значения для справочника берутся из аннотаций классов
                                            ReflectionFuncs.processPkgClassesCollection(actPkg, clazzAct, annActClazz,
                                                    (actClazz) -> {
                                                        this.registerEntClass(entClazz,
                                                                actClazz,
                                                                AnnotationFuncs.getAnnotation(actClazz, annActClazz).action_code());
                                                    });
                                        });

                                // статусы сущностей
                                if (!AnnotationFuncs.isAnnotated(entClazz, EntityStatusesRef.class)) {
                                    class NoEntityStatusesDefined extends InternalAppException {

                                        public NoEntityStatusesDefined(final String message) {
                                            super(message);
                                        }
                                    }
                                    throw new NoEntityStatusesDefined(String.format("No EntityStatuses defined annotations defined for '%s' ",
                                            entClazz.getCanonicalName()));
                                }

                                final EntityStatusId[] entityStatuses = AnnotationFuncs.<EntityStatusesRef>getAnnotation(entClazz, EntityStatusesRef.class).entiy_status();

                                Arrays.stream(entityStatuses)
                                        .forEach(entityStatus -> {
                                            this.registerEntStatus(entClazz, entityStatus);
                                        });
                            });
                });

        if (this.CLASS_ENT2ACTION.isEmpty()) {
            class NoActionClassesDefined extends InternalAppException {

                public NoActionClassesDefined(final String message) {
                    super(message);
                }
            }
            throw new NoActionClassesDefined(String.format("Action classes is empty for '%s' ",
                    this.getClass().getCanonicalName()));
        }

        log.debug("There [{}] pair(s) (entities/action): '{}' ",
                this.CLASS_ENT2ACTION.size(),
                this.getClass().getCanonicalName());

        //
        final Class<?> logClass = log.getClass();
        final String logClassName = logClass.getCanonicalName();

        if (!this.getGenericApplicationContext().containsBean(logClassName)) {
            this.getGenericApplicationContext().registerBean(logClass, bd -> {
                bd.setBeanClassName(logClassName);
                bd.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
                bd.setAutowireCandidate(true);
            });
        }

        // печать списка зарегистрированных бинов
        final String[] beansDefs = genericApplicationContext
                .getBeanDefinitionNames();

        log.debug(Arrays.stream(beansDefs)
                .sorted((s1, s2) -> s1.compareTo(s2))
                .reduce(String.format("Spring beans list (%d): \n",
                        beansDefs.length),
                        (x, y) -> x.concat("\n").concat(y)));

    }
    //==========================================================================

    private void registerEntClass(final Class entClass, final Class actClass, final Integer action_code) {

        log.debug(String.format("Add entity class/action: %s->%s",
                entClass.getCanonicalName(),
                actClass.getCanonicalName()));

        //this.CLASS_ENT2ACTION.put(entClass, actClass);
        this.CLASS_ENT2ACTION.add(new Pair(entClass, actClass));
        this.CLASS_INT2ACTION.put(action_code, actClass);

        final String actionClassName = actClass.getCanonicalName();

        // регистрируем действие как bean
        if (!genericApplicationContext.containsBean(actionClassName)) {

            log.debug(String.format("Registry action bean: '%s'",
                    actClass.getCanonicalName()));

            genericApplicationContext.registerBean(actClass, bd -> {
                bd.setBeanClassName(actionClassName);
                bd.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
                bd.setAutowireCandidate(true);
            });
        }

        // обновляем справочники БД
        //======================================================================
        final EntityTypeId entityTypeId = AnnotationFuncs
                .<EntityTypeId>getAnnotation(entClass, EntityTypeId.class);

        if (NullSafe.notNull(entityTypeId)) {

            entityReferencesService.createNewEntityType(entityTypeId.entity_type_id(),
                    entityTypeId.entity_type_name(),
                    getModuleName(entClass.getProtectionDomain().getCodeSource().getLocation().getFile()));

        }
        //======================================================================
        final EntityKindId entityKindId = AnnotationFuncs.<EntityKindId>getAnnotation(entClass, EntityKindId.class
        );

        if (NullSafe.notNull(entityKindId)) {

            entityReferencesService.createNewEntityKind(entityKindId.entity_kind_id(),
                    entityKindId.entity_type_id(),
                    entityKindId.entity_kind_name());

        }
        //======================================================================
        final ActionCodeId actionCodeId = AnnotationFuncs.<ActionCodeId>getAnnotation(actClass, ActionCodeId.class
        );
        if (NullSafe.notNull(actionCodeId)) {

            entityReferencesService.createNewActionCode(actionCodeId.action_code(),
                    actionCodeId.action_name(),
                    getModuleName(entClass.getProtectionDomain().getCodeSource().getLocation().getFile()),
                    Boolean.FALSE);
        }
    }

    //==========================================================================
    private void registerEntStatus(final Class entClass, final EntityStatusId entityStatusId) {
        CLASS_ENT2STATUS.put(entClass, entityStatusId.entity_status_id());

        entityReferencesService.createNewEntityStatus(entityStatusId.entity_status_id(),
                entityStatusId.entity_type_id(),
                entityStatusId.entity_status_name());
    }

    //==========================================================================    
    protected static String getModuleName(final String classUrl) {

        String moduleName = EMPTY_STRING;
        String url = classUrl;

        if ((url.lastIndexOf("/target/") < 0)) {
            while (moduleName.lastIndexOf(".") < 0) {

                if (!moduleName.isEmpty()) {
                    url = url.substring(0, url.lastIndexOf("/WEB-INF"));
                }

                moduleName = url.substring(url.lastIndexOf("/") + 1);
            }
        }

        return moduleName;
    }

    //==========================================================================
    public <T extends AbstractActionEntity> T createActionEntity(final Class<T> entClass,
            final ActionEntityCreator<T> aec) {

        final T actionEntity = NullSafe.<T>createObject(entClass);

        aec.createEntity(actionEntity);

        return actionEntity;
    }

    //==========================================================================
    public <T extends PersistenceEntity> T createPersistenceEntity(final Class<T> entClass,
            final PersisntanceEntityCreator<T> pec) {

        final T persistenceEntity = NullSafe.<T>createObject(entClass);

        pec.create(persistenceEntity);

        return persistenceEntity;
    }

    //==========================================================================
    @PostConstruct
    public void loadSysReferences() {

        Class<? extends AbstractActionExecutionService> clAss = this.getClass();

        while (NullSafe.notNull(clAss)) {

            final Class<? extends AbstractActionExecutionService> servFinalClass = clAss;
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
                                    NullSafe.create(this.findRegisterMethod(servFinalClass, clazz, "getActualRefRecords"))
                                            .safeExecute(ns_method -> {
                                                synchronized (clazz) {

                                                    log.info("Register reference '{}'", clazz.getCanonicalName());
                                                    // коллекция записей справочника
                                                    final Collection collection = (Collection) ((Method) ns_method).invoke(clazz);
                                                    // сохранение в бд
                                                    getPersistenceEntityManager()
                                                            .executeTransaction(em -> collection
                                                            .stream()
                                                            .forEach(record -> em.merge(record)
                                                            ));
                                                }
                                            }).throwException();
                                }

                                // перечитываем справочники
                                NullSafe.create()
                                        .execute((stmt) -> {
                                            final Collection<? extends AbstractRefRecord> collection = this.getPersistenceEntityManager()
                                                    .getEntityManager()
                                                    .createQuery("Select t from " + clazz.getSimpleName() + " t")
                                                    .getResultList();

                                            AbstractRefRecord.REF_CACHE.put(clazz, collection);
                                            log.debug(String.format("Reference '%s' is loaded ",
                                                    clazz.getCanonicalName()));

                                        })
                                        .catchException(e
                                                -> log.error("Can't load refernce '{}' ({}) ", clazz.getCanonicalName(), e.getMessage()))
                                        .throwException();
                            }
                        });
            }
            clAss = (Class<? extends AbstractActionExecutionService>) clAss.getSuperclass();
        }

        log.info("There are [{}] system reference(s) loaded '{}' ",
                AbstractRefRecord.REF_CACHE.size(),
                this.getClass().getCanonicalName());
    }

    //--------------------------------------------------------------------------
    private Method findRegisterMethod(
            Class<? extends AbstractActionExecutionService> serviceClass,
            Class<?> clazz,
            String methodName) {

        final String key = String.format("%s.%s(%s)",
                serviceClass.getCanonicalName(),
                methodName,
                clazz.getCanonicalName());

        log.debug("Lookup for '{}' method", key);

        return (NullSafe.create()
                .execute2result(() -> serviceClass.getMethod(methodName, clazz))
                .catchMsgException(errMsg -> {
                    log.error("methodName not found ('{}', signature='{}') ({})",
                            methodName,
                            key,
                            errMsg);
                }))
                .<Method>getObject();
    }

    //==========================================================================
    public <T extends AbstractPersistenceEntity> T reloadCreatedEntity(final Class<T> entClass,
            final Long entityId) {

        log.debug("try 2 reload created entity ({})", entityId);

        final StopWatcher stopWatcher = StopWatcher.create();

        // поиск сущности
        final T entity = this.getPersistenceEntityManager()
                .getEntityManager()
                .<T>find(entClass, entityId);

//        this.getPersistenceEntityManager()
//                .getEntityManager()
//                .refresh(entity);
        log.debug("Refresh entity is finished (%d, %d ms)",
                entity.entityId(),
                stopWatcher.getExecutionTime());

        return entity;

    }
}
//==============================================================================

@AllArgsConstructor
@Data
final class Pair {

    private Class<ENT> entClass;
    private Class<ACT> actClass;
}

class NoActionCodeDefined extends InternalAppException {

    public NoActionCodeDefined(final String message) {
        super(message);
    }
}

class ENT extends AbstractActionEntity {

}

class ACT extends AbstractAction {

}
