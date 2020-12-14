package org.dbs24.entity.core;

import org.dbs24.application.core.exception.api.InternalAppException;
import org.dbs24.entity.core.api.ActionEntity;
import static org.dbs24.consts.SysConst.*;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.application.core.service.funcs.AnnotationFuncs;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.persistence.api.PersistenceEntity;
import org.dbs24.component.PersistenceEntityManager;
import java.util.Collection;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.dbs24.application.core.nullsafe.StopWatcher;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import javax.persistence.EntityManager;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.entity.core.api.RefreshEntity;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;
import org.springframework.beans.factory.annotation.Qualifier;
import reactor.core.publisher.Mono;

@Data
@Log4j2
@Qualifier("abstractAction")
public abstract class AbstractAction<T extends ActionEntity>
        extends AbstractPersistenceAction {
    
    @Value("${entity.core.debug:false}")
    private Boolean entityCoreDebug = BOOLEAN_FALSE;
    
    @Autowired
    private PersistenceEntityManager persistenceEntityManager;

    //==========================================================================
    private final Collection<PersistenceEntity> persistenceEntities
            = ServiceFuncs.<PersistenceEntity>createCollection();
    
    private StopWatcher stopWatcher = StopWatcher.create();
    private String errMsg;
    private AbstractPersistenceAction persistAction;
    @Deprecated
    private MultiValueMap mvm;
    private EntityManager entityManager;

    //==========================================================================
    @Override
    public AbstractPersistenceEntity getEntity() {
        return (AbstractPersistenceEntity) super.getEntity();
    }

    //==========================================================================
    @Transactional
    public void execute() {
        log.debug("{} - begin execute", this.getClass().getSimpleName());
        
        Mono.just(this)
                .log()
                .subscribe(new Subscriber() {
                    
                    private StopWatcher stopWatcher;
                    
                    @Override
                    public void onSubscribe(Subscription s) {
                        if (isValid()) {
                            s.request(Long.MAX_VALUE);
                            this.stopWatcher = StopWatcher.create();
                        }
                    }
                    
                    @Override
                    public void onNext(Object action) {
                        
                        beforeUpdate();

//                        getPersistenceEntityManager()
//                                .getEntityManager()
//                                .unwrap(Session.class)
//                                .setJdbcBatchSize(getJdbcBatchSize());
                        getPersistenceEntityManager()
                                .executeTransaction(getEntityManager(), em -> {
                                    
                                    setEntityManager(em); /// todo - переделать цивильно
                                    
                                    log.debug("em = {} ", em);

                                    // создание базовой сущности
                                    createMainEntity();

                                    // создание действия
                                    createPersistenceAction();

                                    // модификация данных
                                    doUpdate();
                                    
                                    // обновление действия
                                    updatePersistenceAction();
                                    updateMainEntity();
                                    afterCommit();
                                });
                    }
                    
                    @Override
                    public void onError(Throwable t) {
                        
                        final String errMsg4User = String.format("%s: %s", getClass().getSimpleName(), StmtProcessor.getErrorMessage(t));
                        
                        log.error(errMsg4User);
                        throw new ActionExecutionException(errMsg4User);
                    }
                    
                    @Override
                    public void onComplete() {
                        
                        log.debug("{}: finish execute ({} ms)",
                                AbstractAction.this.getClass().getSimpleName(),
                                stopWatcher.getExecutionTime());
                    }
                });
        //refreshModifiedEntities();
        afterExecute();
    }
//==========================================================================

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void registerActionFail(Throwable th) {
        getPersistenceEntityManager()
                .executeTransaction(getEntityManager(), em -> {
                    
                    this.setErrMsg(String.format("%s: \n %s",
                            StmtProcessor.getErrorMessage(th),
                            StmtProcessor.getStackTraceRaw(th)));
                    this.updatePersistenceAction();
                });
    }

    //==========================================================================
    private void createMainEntity() {
        
        if (this.getEntity().justCreated()) {
            
            this.getEntity().setCreationDate(LocalDateTime.now());
            
            entityManager.persist(getEntity());
        }
    }

    //==========================================================================
    private void updateMainEntity() {
        final AbstractPersistenceEntity obj = (AbstractPersistenceEntity) this.getEntity();
        
        obj.setLastModify(this.getPersistAction().getExecuteDate());
        
        entityManager.persist(obj);
        
    }

    //==========================================================================
    private void createPersistenceAction() {
        
        final AbstractPersistenceAction apa = StmtProcessor.create(AbstractPersistenceAction.class, action -> {
            action.setEntity(this.getEntity());
            action.setActionCode(this.getActionCode());
            action.setActionDuration(LocalTime.MIN);
        });
        
        entityManager.persist(apa);
        
        this.setPersistAction(apa);
        
    }

    //==========================================================================
    private void updatePersistenceAction() {
        this.getPersistAction().setActionDuration(LocalTime.MIN.plus(this.stopWatcher.getExecutionTime(), ChronoUnit.MILLIS));
        this.getPersistAction().setErrMsg(this.getErrMsg());
        entityManager.merge(this.getPersistAction());
    }

    //==========================================================================
    protected void addPersistenceEntity(PersistenceEntity persistenceEntity) {
        this.persistenceEntities.add(persistenceEntity);
    }

    //==========================================================================
    protected void doUpdate() {
        
    }

    //==========================================================================
    protected void afterCommit() {
        
    }
    
    protected void afterExecute() {
    }

    //==========================================================================
    protected Integer getJdbcBatchSize() {
        return (this.getPersistenceEntityManager().getDefaultJdbcBatchSize());
    }

    //==========================================================================
    public void refreshModifiedEntities() {
        
        Boolean needRefresh = BOOLEAN_FALSE;
        
        if (AnnotationFuncs.isAnnotated(this.getClass(), RefreshEntity.class
        )) {
            needRefresh = AnnotationFuncs.<RefreshEntity>getAnnotation(this.getClass(), RefreshEntity.class).refresh();
        }
        
        if (this.entityCoreDebug) {
            final String msg = String.format("(Refresh after '%s' = %b)",
                    this.getClass().getCanonicalName(),
                    needRefresh);
            
            log.debug(msg);
        }
        
        if (needRefresh) {
            
            if (this.entityCoreDebug) {
                
                log.debug("refresh entity ({}, {})",
                        this.getEntity().entityId(),
                        this.getEntity().getClass().getSimpleName());
            }
            // обновление кэша
            getPersistenceEntityManager()
                    .getFactory()
                    .getCache()
                    .evict(this.getEntity().getClass(),
                            this.getEntity().entityId());

            // обновление главной сущности
            entityManager.refresh(this.getEntity());
            
            if (this.entityCoreDebug) {
                log.debug("refresh entity is finished ({}, {})",
                        this.getEntity().entityId(),
                        this.getEntity().getClass().getSimpleName());
            }
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
    
    protected void insertErrMsg(String errMsg) {
    }
    
    public static Boolean isAllowed(AbstractActionEntity entity, Integer allowedStatus) {
        
        return (Boolean) StmtProcessor.createSilent(() -> (entity.getEntityStatus().getEntityStatusId().equals(allowedStatus)))
                .whenIsNull(() -> IS_NOT_ALLOWED_ACTION)
                .get();
        
    }
    //==========================================================================
}

class ActionExecutionException extends InternalAppException {
    
    public ActionExecutionException(String message) {
        super(message);
    }
}
