/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest;

import org.dbs24.application.core.log.LogService;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.springframework.stereotype.Component;
import org.dbs24.entity.*;

import org.dbs24.consts.WorldChessConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import org.dbs24.service.WorldChessActionExecutionService;
import org.dbs24.entity.core.AbstractActionEntity;

@Component
public class ChessRestApi<T extends AbstractActionEntity> extends ReactiveRestProcessor {

    @Autowired
    private WorldChessActionExecutionService worldChessActionExecutionService;
    //==========================================================================
    final Class<T> playerClass = (Class<T>) AbstractPlayer.class;
    final Class<T> gameClass = (Class<T>) AbstractGame.class;

    public Mono<ServerResponse> findChessPlayer(ServerRequest request) {

        // поднимаем id сущности из параметра запроса
        final Long entityId = Long.valueOf(request.queryParam("entityId")
                .orElseThrow(() -> new RuntimeException("Entity ID is not defined in request")));

        // поднимаем из репозитория по ИД
        final T existEntity = (T) worldChessActionExecutionService
                .getPersistenceEntityManager()
                .getEntityManager()
                .<T>find(playerClass, entityId);

        Assert.notNull(existEntity, String.format("Entity is not found (entityId=%d, %s)",
                entityId,
                playerClass.getSimpleName()));

        return this.<T>processServerRequest(request, existEntity,
                entity -> {

                    if (this.getRestDebug()) {
                        LogService.LogInfo(entity.getClass(), () -> String.format("%s%s (find entityid=%d)",
                        request.methodName(),
                        request.path(),
                        entity.entityId()));
                    }

                    return entity;
                }
        );
    }

    //==========================================================================
    // создание игрока
    public Mono<ServerResponse> createChessPlayer(ServerRequest request) {

        return this.<T>processServerRequest(request, playerClass,
                entity -> {
                    // обработка сущности из запроса
                    this.worldChessActionExecutionService.executeAction(
                            entity,
                            WorldChessConst.ACT_CREATE_OR_MODIFY_PLAYER,
                            request.queryParams());

                    if (this.getRestDebug()) {
                        LogService.LogInfo(entity.getClass(), () -> String.format("%s%s (create entityid=%d)",
                        request.methodName(),
                        request.path(),
                        entity.entityId()));
                    }

                    return entity;
                }
        );
    }

    //==========================================================================
    // создание партии
    public Mono<ServerResponse> createClassisChessGame(final ServerRequest request) {

        return this.<T>processServerRequest(request, gameClass,
                entity -> {
                    // обработка сущности из запроса
                    this.worldChessActionExecutionService.executeAction(
                            entity,
                            WorldChessConst.ACT_CREATE_OR_MODIFY_GAME,
                            request.queryParams());

                    if (this.getRestDebug()) {
                        LogService.LogInfo(entity.getClass(), () -> String.format("%s%s (create entityid=%d)",
                        request.methodName(),
                        request.path(),
                        entity.entityId()));
                    }

                    return entity;
                }
        );
    }

    //==========================================================================
    // выполнение действия над обобщенной сущностью
    public Mono<ServerResponse> executeAction(final ServerRequest request) {

//        final String strClass = request.queryParam("entClass")
//                .orElseThrow(() -> new RuntimeException("Entity class is not defined"));
        final Class<T> entClass = NullSafe.create()
                .execute2result(()
                        -> (Class<T>) Class.forName(request.queryParam("entClass")
                        .orElseThrow(() -> new RuntimeException("Entity class is not defined in query param")))
                ).<Class<T>>getObject();

        return this.<T>processServerRequest(request, entClass,
                entity -> {

                    final Integer actionId = Integer.valueOf(request.queryParam("actionId")
                            .orElseThrow(() -> new RuntimeException("Action code is not defined")));

                    final T actionEntity;

                    // тело entity отсутствует
                    if (NullSafe.isNull(entity)) {
                        // поднимаем id сущности из параметра запроса
                        final Long entityId = Long.valueOf(request.queryParam("entityId")
                                .orElseThrow(() -> new RuntimeException("Entity ID is not defined in request")));

                        // поднимаем из репозитория по ИД
                        actionEntity = worldChessActionExecutionService
                                .getPersistenceEntityManager()
                                .getEntityManager()
                                .<T>find(entClass, entityId);

                    } else {
                        actionEntity = entity;
                    }

                    Assert.notNull(actionEntity, String.format("%s: fucking entity is null!",
                            LogService.getCurrentObjProcName(this)));

                    final MultiValueMap mvm = request.queryParams();

                    if (this.getRestDebug()) {
                        LogService.LogInfo(actionEntity.getClass(), () -> String.format("%s%s: execute action  (entityId=%d, actionCode=%d)",
                        request.methodName(),
                        request.path(),
                        actionEntity.entityId(),
                        actionId));
                    }

                    //request.queryParams();
                    // выполнение действия над сущностью из запроса
                    this.worldChessActionExecutionService.executeAction(
                            actionEntity,
                            actionId,
                            mvm);

                    if (this.getRestDebug()) {
                        LogService.LogInfo(actionEntity.getClass(), () -> String.format("%s%s: (execute action code=%d)",
                        request.methodName(),
                        request.path(),
                        actionEntity.entityId()));
                    }

                    return actionEntity;
                }
        );
    }
}
