package org.dbs24.entity.core;

import org.dbs24.application.core.exception.api.InternalAppException;
import org.dbs24.application.core.log.LogService;
import org.dbs24.entity.core.api.ActionEntity;
import static org.dbs24.application.core.sysconst.SysConst.*;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.AnnotationFuncs;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.entity.core.api.ActionClassesPackages;
import org.dbs24.persistence.api.PersistenceEntity;
import org.dbs24.persistence.core.PersistanceEntityManager;
import java.util.Collection;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.dbs24.application.core.nullsafe.StopWatcher;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import lombok.Data;
import org.dbs24.entity.core.api.RefreshEntity;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author kazyra_d
 */
@Data
@Qualifier("abstractAction")
public abstract class AbstractAction<T extends ActionEntity>
        extends AbstractPersistenceAction {

    @Value("${entity.core.debug:false}")
    private Boolean entityCoreDebug = BOOLEAN_FALSE;
    
    @Autowired
    private PersistanceEntityManager persistanceEntityManager;    

    //==========================================================================
    private final Collection<PersistenceEntity> persistenceEntities
            = ServiceFuncs.<PersistenceEntity>createCollection();

    private StopWatcher stopWatcher;
    private String errMsg;
    private AbstractPersistenceAction persistAction;
    private MultiValueMap mvm;

    //==========================================================================
    @Override
    public AbstractPersistenceEntity getEntity() {
        return (AbstractPersistenceEntity) super.getEntity();
    }

    //==========================================================================
    @Transactional
    public void execute() {

        if (this.entityCoreDebug) {
            final String msg = String.format("%s: START EXECUTION",
                    this.getClass().getSimpleName());

            LogService.LogInfo(this.getClass(), () -> msg);
        }

        this.initialize();
        this.doCalculation();
        this.afterCalculation();

        if (this.isValid()) {
            this.stopWatcher = StopWatcher.create();
            this.beforeUpdate();

            this.getPersistanceEntityManager()
                    .getEntityManager()
                    .unwrap(Session.class)
                    .setJdbcBatchSize(this.getJdbcBatchSize());//this.getPersistanceEntityManager().getDefaultJdbcBatchSize());

            // наполнение в предках объектов для персистенса
            //this.doUpdate();
            //final AbstractPersistenceAction<T> ent2persist = NullSafe.createObject(AbstractPersistenceAction.class); 
            // сохранили объекты
            getPersistanceEntityManager()
                    .executeTransaction(em -> {
                        //.executeUserTransaction(em -> {

                        NullSafe.create()
                                .execute(() -> {
                                    // создание базовой сущности
                                    this.createMainEntity();

                                    // создание действия
                                    this.createPersistenceAction();

                                    // модификация данных
                                    this.doUpdate();

                                    persistenceEntities
                                            .forEach((obj) -> {

                                                if (NullSafe.isNull(this.getErrMsg())) {

                                                    if (obj instanceof AbstractPersistenceEntity) {
                                                        final AbstractPersistenceEntity apeObj = (AbstractPersistenceEntity) (obj);

                                                        apeObj.setLastModify(this.getExecuteDate());
                                                    }

                                                    if (em.contains(obj)) {
                                                        em.merge(obj);
                                                    } else {
                                                        em.persist(obj);
                                                    }
                                                }
                                            });

                                }).catchException(e -> this.setErrMsg(String.format("%s: %s", this.getClass().getSimpleName(), NullSafe.getErrorMessage(e))))
                                .finallyBlock(() -> {
                                    // обновление действия
                                    this.updatePersistenceAction();
                                    this.updateMainEntity();
                                });
                        this.getPersistanceEntityManager()
                                .getEntityManager()
                                .flush();
                    });

            if (this.entityCoreDebug) {
                final String msg = String.format("%s: FINISH EXECUTION ( %s ms)",
                        this.getClass().getSimpleName(),
                        stopWatcher.getExecutionTime());

                LogService.LogInfo(this.getClass(), () -> msg);
            }

            if (NullSafe.notNull(this.getErrMsg())) {

                throw new ActionExecutionException(this.getErrMsg());
            }

            this.afterCommit();
            this.refreshModifiedEntities();
            this.afterExecute();
        }
    }

    //==========================================================================
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void registerActionFail(final Throwable th) {
        getPersistanceEntityManager()
                .executeTransaction(em -> {

                    this.setErrMsg(String.format("%s: \n %s",
                            NullSafe.getErrorMessage(th),
                            NullSafe.getStackTraceRaw(th)));
                    this.updatePersistenceAction();
                });
    }

    //==========================================================================
    private void createMainEntity() {

        if (this.getEntity().justCreated()) {

            this.getEntity().setCreation_date(LocalDateTime.now());

            getPersistanceEntityManager()
                    .getEntityManager()
                    .persist(this.getEntity());
        }
    }

    //==========================================================================
    private void updateMainEntity() {
        final AbstractPersistenceEntity obj = (AbstractPersistenceEntity) this.getEntity();

        obj.setLastModify(this.getPersistAction().getExecuteDate());

        getPersistanceEntityManager().getEntityManager().persist(obj);

    }

    //==========================================================================
    private void createPersistenceAction() {
        this.setPersistAction(getPersistanceEntityManager().<AbstractPersistenceAction>createPersistenceEntity(
                AbstractPersistenceAction.class,
                (action) -> {
                    action.setEntity(this.getEntity());
                    action.setActionCode(this.getActionCode());
                    action.setActionDuration(LocalTime.MIN);
//                    action.setNotes(ServiceFuncs.getJsonFromObject(this.getEntity()));
//                    action.setErrMsg(this.getErrMsg());
                }));
    }

    //==========================================================================
    private void updatePersistenceAction() {
        this.getPersistAction()
                .setActionDuration(LocalTime.MIN.plus(this.stopWatcher.getExecutionTime(), ChronoUnit.MILLIS));
//        this.getPersistAction().setNotes(ServiceFuncs.getJsonFromObject(this.getEntity()));
        this.getPersistAction()
                .setErrMsg(this.getErrMsg());
        this.getPersistanceEntityManager()
                .getEntityManager()
                .merge(this.getPersistAction());
    }

    //==========================================================================
    protected void addPersistenceEntity(final PersistenceEntity persistenceEntity) {
        this.persistenceEntities.add(persistenceEntity);
    }

    //==========================================================================
    protected void doUpdate() {

    }

    //==========================================================================
    protected void afterCommit() {

    }

    protected void afterExecute() {
//        this.getPersistanceEntityManager()
//                .getEntityManager()
//                .clear();
    }

    //==========================================================================
    protected Integer getJdbcBatchSize() {
        return (this.getPersistanceEntityManager().getDefaultJdbcBatchSize());
    }

    //==========================================================================
    public void refreshModifiedEntities() {

        Boolean needRefresh = BOOLEAN_FALSE;

        if (AnnotationFuncs.isAnnotated(this.getClass(), RefreshEntity.class)) {
            needRefresh = AnnotationFuncs.<RefreshEntity>getAnnotation(this.getClass(), RefreshEntity.class).refresh();
        }

        if (this.entityCoreDebug) {
            final String msg = String.format("(Refresh after '%s' = %b)",
                    this.getClass().getCanonicalName(),
                    needRefresh);

            LogService.LogInfo(this.getClass(), () -> msg);
        }

        if (needRefresh) {

            if (this.entityCoreDebug) {

                LogService.LogInfo(this.getClass(), () -> String.format("refresh entity (%d, %s)",
                        this.getEntity().entityId(),
                        this.getEntity().getClass().getSimpleName())
                        .toUpperCase());
            }
            // обновление кэша
            getPersistanceEntityManager()
                    .getFactory()
                    .getCache()
                    .evict(this.getEntity().getClass(),
                            this.getEntity().entityId());

            // поиск сущности
//            final ActionEntity entity = persistanceEntityManager
//                    .getEntityManager()
//                    .find(this.getEntity().getClass(), this.getEntity().entityId());
            // обновление главной сущности
            getPersistanceEntityManager()
                    .getEntityManager()
                    .refresh(this.getEntity());

            if (this.entityCoreDebug) {
                LogService.LogInfo(this.getClass(), () -> String.format("refresh entity is finished (%d, %s)",
                        this.getEntity().entityId(),
                        this.getEntity().getClass().getSimpleName())
                        .toUpperCase());
            }

            // обновление подчиненных сущности
//            persistenceEntities
//                    .forEach((obj) -> {
//
//                        LogService.LogInfo(this.getClass(), () -> String.format("refresh persistene entity (%s)",
//                                obj.getClass().getSimpleName()).toUpperCase());
//
//                        persistanceEntityManager
//                                .getEntityManager()
//                                .refresh(obj);
//                    });
        }
    }

    protected void doCalculation() {

    }

    protected void afterCalculation() {

    }

    protected void beforeUpdate() {

    }

    protected boolean isValid() {

        return BOOLEAN_TRUE;
    }

    public void initialize() {

    }

    protected void finallyExecute() {

    }

    protected void strongExecute() {

    }

    protected void insertErrMsg(final String errMsg) {
//        getDbService()
//                .createCallQuery("{call core_insert_errmsg(:AC, :ERR)}")
//                .setParamByName("AC", getActionId())
//                .setParamByName("ERR", errMsg)
//                .execCallStmt();
    }

    public static Boolean isAllowed(final AbstractActionEntity entity, final Integer allowedStatus) {

        return (NullSafe.create()
                .execute2result(() -> (entity.getEntityStatus().getEntityStatusId().equals(allowedStatus))))
                .catchException2result((e) -> IS_NOT_ALLOWED_ACTION)
                .<Boolean>getObject();

//        return (entity.getEntityStatus().getEntity_status_id().equals(allowedStatus));
    }
    //==========================================================================

}

class ActionExecutionException extends InternalAppException {

    public ActionExecutionException(final String message) {
        super(message);
    }
}
