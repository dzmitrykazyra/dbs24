/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.reactor;

import static org.dbs24.service.AbstractActionExecutionService.CLASS_INT2ACTION;
import static org.dbs24.service.AbstractActionExecutionService.CLASS_ENT2ACTION;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.exception.EntityMetadataIsNotDefined;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.component.PersistenceEntityManager;
import org.dbs24.entity.action.ActionCode;
import org.dbs24.entity.core.AbstractAction;
import org.dbs24.entity.core.AbstractActionEntity;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.mime.MimeTypes;
import org.dbs24.exception.EntityReactorException;
import org.dbs24.exception.IllegalActionForEntity;
import org.dbs24.exception.UnknownFuckingActionCode;
import org.dbs24.service.EntityReferencesService;
import org.dbs24.service.ReferenceRecordsManager;
import java.util.Map;
import java.util.Collection;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Value;
import org.dbs24.service.MonitoringRSocketService;
import static org.dbs24.rsocket.api.MessageType.*;
import org.dbs24.reactor.SimpleSubscriber;
import org.dbs24.spring.core.api.ApplicationService;

@Log4j2
public abstract class AbstractEntityReactor<T extends Collection<? extends AbstractActionEntity>>
        extends SimpleSubscriber<T> implements ApplicationService {

    final AtomicInteger actionsCount = new AtomicInteger();

    @Autowired
    GenericApplicationContext genericApplicationContext;

    @Autowired
    PersistenceEntityManager persistenceEntityManager;

    @Autowired
    EntityReferencesService entityReferencesService;

    @Autowired
    ReferenceRecordsManager referenceRecordsManager;

    @Autowired
    MonitoringRSocketService monitoringRSocketService;

    @Value("${messages.limit:10}")
    Integer limit = 10;

    //--------------------------------------------------------------------------
    protected <V> V getMetaDataValue(Map<String, Object> metaData, MimeTypes key) {

        return (V) Optional.ofNullable((Optional.ofNullable(metaData)
                .orElseThrow(() -> new EntityMetadataIsNotDefined("entity metaData is not defined"))).get(key.toString()))
                .orElseThrow(() -> new EntityMetadataIsNotDefined(String.format("mimeType is not defined {%s}", key.getValue())));
    }

    //==========================================================================
    final RunnableAction internalAction = entity -> StmtProcessor.execute(
            () -> {

                final Integer actionId = Integer.valueOf(this.getMetaDataValue(entity.getMetaData(), MimeTypes.ENTITY_ACTION_ID));
                final Long userId = Long.valueOf(this.getMetaDataValue(entity.getMetaData(), MimeTypes.ENTITY_USER_ID));

                log.debug("{}: execute action {}, entId={}", entity.getClass().getSimpleName(), actionId, StmtProcessor.nvl(entity.getEntity_id(), "new entity"));

                //monitoringRSocketService.echo("execute action");
                this.executeAction(entity, actionId, userId);
                actionsCount.incrementAndGet();
            });

    @Override
    public void onNext(T t) {

        StmtProcessor.execute(() -> {

            final Boolean mtMode = Boolean.FALSE;

            t.stream().forEach(entity -> StmtProcessor.execute(() -> {
                if (mtMode) {
                    StmtProcessor.runNewThread(() -> internalAction.run(entity));
                } else {
                    this.internalAction.run(entity);
                }
            }));
        });
    }

    @Override
    public void onError(Throwable t) {

        final String errMsg = StmtProcessor.getErrorMessage(t);

        log.error("onError: {} ", errMsg);
        t.printStackTrace();

        monitoringRSocketService.send(MONITORING_ABSTRACT_ERROR, errMsg);
        throw new EntityReactorException(errMsg);
    }

    // выполнение действия над сущностью
    //==========================================================================
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    private void executeAction(
            final AbstractActionEntity entity,
            final Integer actCode,
            final Long userId) {
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
        action.setUserId(userId);
        // дополнительные параметры из запроса
        //action.setMvm(mvm);

        // дополнительная инициализация действия
        NullSafe.create()
                .execute(() -> action.execute())
                .catchException(e -> action.registerActionFail(e))
                .throwException();
    }
}
