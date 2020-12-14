/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest;

import org.dbs24.rest.api.ReactiveRestProcessor;
import org.springframework.stereotype.Component;
import org.dbs24.entity.*;

import static org.dbs24.consts.SysConst.*;
import static org.dbs24.consts.WorldChessConst.*;
import org.dbs24.entity.classic.ClassicGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import org.dbs24.service.ChessActionExecutionService;

@Component
public class ChessRestApi extends ReactiveRestProcessor {

    final ChessActionExecutionService chessActionExecutionService;

    @Autowired
    public ChessRestApi(ChessActionExecutionService chessActionExecutionService) {
        this.chessActionExecutionService = chessActionExecutionService;
    }

    //==========================================================================
    public Mono<ServerResponse> findChessPlayer(ServerRequest request) {

        // поднимаем id сущности из параметра запроса
        final Long entityId = Long.valueOf(request.queryParam("entityId")
                .orElseThrow(() -> new RuntimeException("Entity ID is not defined in request")));

        return this.<Void, AbstractPlayer>processServerRequest(request, VOID_CLASS,
                noEntity -> {

                    // поднимаем из репозитория по ИД
                    final AbstractPlayer existEntity = chessActionExecutionService
                            .getPersistenceEntityManager()
                            .getEntityManager()
                            .find(CHESS_PLAYER_CLASS, entityId);

                    Assert.notNull(existEntity, String.format("Entity is not found (entityId=%d, %s)",
                            entityId,
                            CHESS_PLAYER_CLASS.getSimpleName()));
                    return existEntity;
                }, httpOk);
    }

    //==========================================================================
    // создание игрока
    public Mono<ServerResponse> createChessPlayer(ServerRequest request) {

        return this.<AbstractPlayer>processServerRequest(request, CHESS_PLAYER_CLASS,
                player -> chessActionExecutionService.executeAction(
                        player,
                        ACT_CREATE_OR_MODIFY_PLAYER,
                        null), httpOk);
    }

    //==========================================================================
    // создание партии
    public Mono<ServerResponse> createClassisChessGame(ServerRequest request) {

        return this.<ClassicGame>processServerRequest(request, CHESS_GAME_CLASS,
                game
                -> // обработка сущности из запроса
                this.chessActionExecutionService.executeAction(
                        game,
                        ACT_CREATE_OR_MODIFY_GAME,
                        null), httpOk);
    }
}
