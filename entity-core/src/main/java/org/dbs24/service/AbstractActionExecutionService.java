/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import static org.dbs24.consts.SysConst.*;
import org.dbs24.application.core.service.funcs.AnnotationFuncs;
import lombok.Data;
import lombok.AllArgsConstructor;
import org.dbs24.entity.core.AbstractPersistenceEntity;
import org.dbs24.component.PersistenceEntityManager;
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
import org.dbs24.entity.core.api.EntityKindsRef;
import java.util.Arrays;
import java.util.Map;
import java.util.Collection;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.dbs24.entity.core.AbstractActionEntity;
import org.dbs24.entity.reactor.GenericEntityReactor;
import org.dbs24.entity.action.ActionCode;
import org.dbs24.entity.core.api.EntityKindId;
import org.dbs24.entity.core.api.EntityStatusesRef;
import org.dbs24.entity.status.EntityStatus;
import org.dbs24.entity.kind.EntityKind;
import org.dbs24.entity.marks.Mark;
import org.dbs24.entity.marks.MarkValue;
import org.dbs24.entity.status.EntityStatusId;
import org.dbs24.references.core.CachedReferencesClasses;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.dbs24.entity.type.EntityType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.util.Assert;
import org.dbs24.persistence.core.PersisntanceEntityCreator;
import org.dbs24.persistence.api.PersistenceEntity;
import org.dbs24.exception.*;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import reactor.core.publisher.Mono;

@Data
@Log4j2
@CachedReferencesClasses(classes = {EntityStatus.class, EntityKind.class, ActionCode.class, Mark.class, MarkValue.class})
public abstract class AbstractActionExecutionService extends AbstractApplicationService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value("${entity.core.ref.synchronize:true}")
    private Boolean refSynchronize = BOOLEAN_TRUE;

    @Autowired
    GenericApplicationContext genericApplicationContext;

    @Autowired
    PersistenceEntityManager persistenceEntityManager;

    @Autowired
    EntityReferencesService entityReferencesService;

    @Autowired
    ReferenceRecordsManager referenceRecordsManager;
    
    @Autowired
    GenericEntityReactor genericEntityReactor;

    final Collection<EntityType> entityTypesCollection = ServiceFuncs.<EntityType>getOrCreateCollection(ServiceFuncs.COLLECTION_NULL);
    final Collection<EntityStatus> entityStatusCollection = ServiceFuncs.<EntityStatus>getOrCreateCollection(ServiceFuncs.COLLECTION_NULL);
    final Collection<EntityKind> entityKindsCollection = ServiceFuncs.<EntityKind>getOrCreateCollection(ServiceFuncs.COLLECTION_NULL);
    final Collection<ActionCode> actionCodesCollection = ServiceFuncs.<ActionCode>getOrCreateCollection(ServiceFuncs.COLLECTION_NULL);

    //==========================================================================
    public static final Collection<Pair> CLASS_ENT2ACTION
            = ServiceFuncs.createCollection();

    final Collection<? extends AbstractActionEntity> enityCollection
            = ServiceFuncs.createCollection();

    // действие - номер действия
    public static final Map<Integer, Class<AbstractAction>> CLASS_INT2ACTION
            = ServiceFuncs.getOrCreateMap_Safe(ServiceFuncs.MAP_NULL);

    // сущность - статус сущности
    public static final Map<Class<AbstractActionEntity>, Integer> CLASS_ENT2STATUS
            = ServiceFuncs.getOrCreateMap_Safe(ServiceFuncs.MAP_NULL);

    // выполнение действия над сущностью
    @Deprecated
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void executeAction(
            final AbstractActionEntity entity,
            final Integer actCode,
            final EntityManager entityManager) {

        final Class actClass = ServiceFuncs.getMapValue(CLASS_INT2ACTION,
                mapEntry -> mapEntry.getKey().equals(actCode))
                .orElseThrow(() -> new UnknownFuckingActionCode(String.format("Unknown fucking action_code (%d)", actCode)));

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
                actClass.getSimpleName());

        final AbstractAction action = genericApplicationContext.<AbstractAction>getBean(actClass);

        final ActionCode actionCode = entityReferencesService.findActionCode(actCode);

        action.setEntity(entity);
        action.setActionCode(actionCode);
        action.setEntityManager(entityManager);
        // дополнительные параметры из запроса
//        action.setMvm(mvm);

        // дополнительная инициализация действия
        NullSafe.create()
                .execute(() -> action.execute())
                .catchException(e -> action.registerActionFail(e))
                .throwException();
    }

    //==========================================================================
    @PostConstruct
    public void postActionExecutionService() {

//        StmtProcessor.runNewThread(() -> {
        final StopWatcher stopWatcher = StopWatcher.create(this.getClass().getSimpleName());

        final Class<? extends AbstractActionExecutionService> thisClass = this.getClass();

        // классы сущностей
        Arrays.stream(
                Optional.ofNullable(AnnotationFuncs.<EntityClassesPackages>getAnnotation(thisClass, EntityClassesPackages.class))
                        .orElseThrow(() -> new NoActionClassesPackagesDefined(
                        String.format("No EntityClassesPackages annotations defined for '%s' ",
                                thisClass.getCanonicalName()))).pkgList())
                .forEach(entPkg -> {

                    final Class<AbstractActionEntity> clazz = (AbstractActionEntity.class);

                    // @EntityTypeId
                    ReflectionFuncs.processPkgClassesCollection(entPkg, clazz, EntityTypeId.class,
                            entClazz -> {

                                Arrays.stream(
                                        Optional.ofNullable(AnnotationFuncs.<ActionClassesPackages>getAnnotation(entClazz, ActionClassesPackages.class))
                                                .orElseThrow(() -> new NoActionClassesPackagesDefined(
                                                String.format("No ActionClassesPackagesDefined annotations defined for '%s' ", entClazz.getCanonicalName())))
                                                .pkgList())
                                        .forEach(actPkg -> {

                                            final Class<AbstractAction> clazzAct = (AbstractAction.class);
                                            final Class<ActionCodeId> annActClazz = ActionCodeId.class;

                                            // значения для справочника берутся из аннотаций классов
                                            ReflectionFuncs.processPkgClassesCollection(actPkg, clazzAct, annActClazz,
                                                    actClazz -> this.registerEntityClass(entClazz,
                                                            actClazz,
                                                            AnnotationFuncs.getAnnotation(actClazz, annActClazz).action_code())
                                            );
                                        });

                                Arrays.stream(
                                        Optional.ofNullable(AnnotationFuncs.<EntityStatusesRef>getAnnotation(entClazz, EntityStatusesRef.class))
                                                .orElseThrow(() -> new NoEntityStatusesDefined(
                                                String.format("No EntityStatuses defined annotations defined for ", entClazz.getCanonicalName()))).entiy_status())
                                        .forEach(entityStatus -> this.registerEntStatus(entClazz, entityStatus));
                            });

                    // @EntityKindsRef                    
                    ReflectionFuncs.processPkgClassesCollection(entPkg, clazz, EntityKindsRef.class,
                            entClazz -> Optional.ofNullable(AnnotationFuncs.<EntityKindsRef>getAnnotation(entClazz, EntityKindsRef.class))
                                    .ifPresent(items
                                            -> Arrays.stream(items
                                            .kind_id())
                                            .forEach(kind
                                                    -> entityKindsCollection.add((EntityKind) NullSafe.<EntityKind>createObject(EntityKind.class,
                                                    entityKind -> {

                                                        //log.debug("add entityKind '{}'", kind.entity_kind_id());
                                                        entityKind.setEntityKindId(kind.entity_kind_id());
                                                        entityKind.setEntityTypeId(kind.entity_type_id());
                                                        entityKind.setEntityKindName(kind.entity_kind_name());
                                                    }))))
                    );

                    // @EntityKindId
                    ReflectionFuncs.processPkgClassesCollection(entPkg, clazz, EntityKindId.class,
                            entClazz -> registerEntityKind(entClazz));
                });

        CLASS_ENT2ACTION
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoActionClassesDefined(String.format("Action classes is empty for '%s' ",
                thisClass.getCanonicalName())));

//        log.debug("There [{}] pair(s) (entities/action): '{}' ",
//                CLASS_ENT2ACTION.size(),
//                thisClass.getCanonicalName());
        final Class<?> logClass = log.getClass();
        final String logClassName = logClass.getCanonicalName();

        Optional.ofNullable(this.getGenericApplicationContext().containsBean(logClassName)
                ? OBJECT_NULL : BOOLEAN_TRUE)
                .ifPresent(bean -> this.getGenericApplicationContext().registerBean(logClass,
                bd -> {
                    bd.setBeanClassName(logClassName);
                    bd.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
                    bd.setAutowireCandidate(true);
                }));

        // register system references
        final String serviceName = thisClass.getSimpleName();

        referenceRecordsManager.registerReferenceAndRefresh(entityTypesCollection, EntityType.class, serviceName);
        referenceRecordsManager.registerReferenceAndRefresh(entityStatusCollection, EntityStatus.class, serviceName);
        referenceRecordsManager.registerReferenceAndRefresh(entityKindsCollection, EntityKind.class, serviceName);
        referenceRecordsManager.registerReferenceAndRefresh(actionCodesCollection, ActionCode.class, serviceName);

        log.info("{}: entity/action reference(s) is loaded, {} ",
                this.getClass().getSimpleName(), stopWatcher.getStringExecutionTime());

//        });
    }

    //==========================================================================
    private void registerEntityKind(Class<AbstractActionEntity> entClass) {

        log.debug("process {} for EntityKind ", entClass.getSimpleName());

        Optional.ofNullable(AnnotationFuncs.<EntityKindId>getAnnotation(entClass, EntityKindId.class))
                .ifPresent(entityKindId
                        -> entityKindsCollection.add((EntityKind) NullSafe.<EntityKind>createObject(EntityKind.class,
                        entityKind -> {

                            //log.debug("add entityKind '{}'", entityKindId.entity_kind_id());
                            entityKind.setEntityKindId(entityKindId.entity_kind_id());
                            entityKind.setEntityTypeId(entityKindId.entity_type_id());
                            entityKind.setEntityKindName(entityKindId.entity_kind_name());
                        })));
    }

    //==========================================================================
    private void registerEntityClass(Class<AbstractActionEntity> entClass, Class<AbstractAction> actClass, Integer actCode) {

//        log.debug(String.format("registry class/action: %s/%s",
//                entClass.getSimpleName(),
//                actClass.getSimpleName()));
        CLASS_ENT2ACTION.add(new Pair(entClass, actClass));
        CLASS_INT2ACTION.put(actCode, actClass);

        final String actionClassName = actClass.getCanonicalName();

        // регистрируем действие как bean
        Optional.ofNullable(genericApplicationContext.containsBean(actionClassName)
                ? OBJECT_NULL : BOOLEAN_TRUE)
                .ifPresent(bean -> {
                    log.debug(String.format("registry action as bean: '%s'",
                            actClass.getCanonicalName()));

                    genericApplicationContext.registerBean(actClass,
                            bd -> {
                                bd.setBeanClassName(actionClassName);
                                bd.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
                                bd.setAutowireCandidate(true);
                            });
                });
        //======================================================================
        Optional.ofNullable(AnnotationFuncs
                .<EntityTypeId>getAnnotation(entClass, EntityTypeId.class))
                .ifPresent(entityTypeId
                        -> // create EntityTypes Collection
                        entityTypesCollection.add((EntityType) NullSafe.<EntityType>createObject(EntityType.class,
                        entityType -> {
                            entityType.setEntityTypeId(entityTypeId.entity_type_id());
                            entityType.setEntityTypeName(entityTypeId.entity_type_name());
                            entityType.setEntityAppName(getModuleName(entClass.getProtectionDomain().getCodeSource().getLocation().getFile()));
                        })));
        //======================================================================
        Optional.ofNullable(AnnotationFuncs.<ActionCodeId>getAnnotation(actClass, ActionCodeId.class))
                .ifPresent(actionCodeId
                        -> actionCodesCollection.add((ActionCode) NullSafe.<ActionCode>createObject(ActionCode.class,
                        actionCode -> {
                            actionCode.setActionCode(actionCodeId.action_code());
                            actionCode.setActionName(actionCodeId.action_name());
                            actionCode.setAppName(getModuleName(entClass.getProtectionDomain().getCodeSource().getLocation().getFile()));
                            actionCode.setIsClosed(actionCodeId.is_closed());
                        })));
    }

    //==========================================================================
    private void registerEntStatus(Class<AbstractActionEntity> entClass, EntityStatusId entityStatusId) {
        CLASS_ENT2STATUS.put(entClass, entityStatusId.entity_status_id());

        entityStatusCollection.add((EntityStatus) NullSafe.<EntityStatus>createObject(EntityStatus.class,
                entityStatus -> {
                    entityStatus.setEntityStatusId(entityStatusId.entity_status_id());
                    entityStatus.setEntityTypeId(entityStatusId.entity_type_id());
                    entityStatus.setEntityStatusName(entityStatusId.entity_status_name());
                }));
    }

    //==========================================================================    
    protected static String getModuleName(String classUrl) {

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
    public <T extends AbstractActionEntity> T createActionEntity(Class<T> entClass,
            final ActionEntityCreator<T> aec) {

        final T actionEntity = NullSafe.<T>createObject(entClass);

        aec.createEntity(actionEntity);

        return actionEntity;
    }

    //==========================================================================
    public <T extends PersistenceEntity> T createPersistenceEntity(Class<T> entClass,
            final PersisntanceEntityCreator<T> pec) {

        final T persistenceEntity = NullSafe.<T>createObject(entClass);

        pec.create(persistenceEntity);

        return persistenceEntity;
    }

    //==========================================================================
    public <T extends AbstractPersistenceEntity> T reloadCreatedEntity(Class<T> entClass,
            final Long entityId) {

        log.debug("try 2 reload created entity ({})", entityId);

        final StopWatcher stopWatcher = StopWatcher.create();

        // поиск сущности
        final T entity = this.getPersistenceEntityManager()
                .getEntityManager()
                .<T>find(entClass, entityId);
        log.debug("Refresh entity is finished (%d, %d ms)",
                entity.entityId(),
                stopWatcher.getExecutionTime());
        return entity;

    }

    //==========================================================================
    protected <E extends AbstractActionEntity> E createEntity(E entity, MetadataCreator metadataCreator) {

        //final GenericEntityReactor<E> aer = StmtProcessor.create(GenericEntityReactor.class);

        final Collection<E> collection = ServiceFuncs.<E>createCollection();

        final Map<String, Object> metadata = ServiceFuncs.createMap();

        metadataCreator.fillMataData(metadata);

        entity.setMetaData(metadata);

        collection.add(entity);

        final Mono<Collection<E>> mono = Mono.just(collection);

        mono.subscribe(genericEntityReactor);

        return entity;
    }
}
//==============================================================================

//@AllArgsConstructor
//@Data
//final class Pair {
//
//    private Class<ENT> entClass;
//    private Class<ACT> actClass;
//}
//
//class NoActionCodeDefined extends InternalAppException {
//
//    public NoActionCodeDefined(String message) {
//        super(message);
//    }
//}
//
//class ENT extends AbstractActionEntity {
//
//}
//
//class ACT extends AbstractAction {
//
//}
