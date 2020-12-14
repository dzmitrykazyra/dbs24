/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest;

import java.time.Duration;
import java.time.LocalDateTime;
import org.dbs24.application.core.nullsafe.NullSafe;
import static org.dbs24.consts.SysConst.*;
import static org.dbs24.consts.RetailLoanContractConst.*;
import org.dbs24.entity.RetailLoanContract;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.dbs24.service.RetailLoanContractActionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import org.springframework.util.MultiValueMap;
import org.springframework.util.Assert;
import lombok.extern.log4j.Log4j2;
import org.dbs24.service.*;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Flux;

@Component
@Log4j2
public class RetailLoanContractRest extends ReactiveRestProcessor {

    final RetailLoanContractActionsService retailLoanContractActionsService;

    @Autowired
    public RetailLoanContractRest(RetailLoanContractActionsService retailLoanContractActionsService) {
        this.retailLoanContractActionsService = retailLoanContractActionsService;
    }
    //==========================================================================

    public Mono<ServerResponse> findRetailLoanContract(ServerRequest request) {

        // поднимаем id сущности из параметра запроса
        final Long entityId = Long.valueOf(request.queryParam("entityId")
                .orElseThrow(() -> new RuntimeException("Entity ID is not defined in request")));

        return this.<Void, RetailLoanContract>processServerRequest(request, VOID_CLASS,
                noJon -> {

                    // поднимаем из репозитория по ИД
                    final RetailLoanContract existEntity = retailLoanContractActionsService
                            .getPersistenceEntityManager()
                            .getEntityManager()
                            .find(RETAIL_LOAN_CONTRACT_CLASS, entityId);

                    Assert.notNull(existEntity, String.format("%s is not found (entityId=%d)",
                            RETAIL_LOAN_CONTRACT_CLASS.getSimpleName(),
                            entityId));

                    return existEntity;
                }, httpOk);
    }

    //==========================================================================
    // создание кредитного договора
    public Mono<ServerResponse> createRetailLoanContract(ServerRequest request) {

        return this.<RetailLoanContract, RetailLoanContract>processServerRequest(request, RETAIL_LOAN_CONTRACT_CLASS,
                entity -> {
                    // обработка сущности из запроса
                    this.retailLoanContractActionsService.executeAction(
                            entity,
                            MODIFY_INDIVIDUAL_LOAN_CONTRACT,
                            null);

                    return entity;
                }, httpOk);
    }

    //==========================================================================
    // выполнение действия над договором
    public Mono<ServerResponse> executeAction(ServerRequest request) {

        final Integer actionId = Integer.valueOf(request.queryParam("actionId")
                .orElseThrow(() -> new RuntimeException("Action code is not defined")));

        return this.<RetailLoanContract>processServerRequest(request, RETAIL_LOAN_CONTRACT_CLASS,
                entity -> {

                    final RetailLoanContract actionEntity;

                    // тело entity отсутствует
                    if (NullSafe.isNull(entity)) {
                        // поднимаем id сущности из параметра запроса
                        final Long entityId = Long.valueOf(request.queryParam("entityId")
                                .orElseThrow(() -> new RuntimeException("Entity ID is not defined in request")));

                        // поднимаем из репозитория по ИД
                        actionEntity = retailLoanContractActionsService
                                .getPersistenceEntityManager()
                                .getEntityManager()
                                .find(RETAIL_LOAN_CONTRACT_CLASS, entityId);

                    } else {
                        actionEntity = entity;
                    }

                    final MultiValueMap mvm = request.queryParams();

                    // выполнение действия над сущностью из запроса
                    this.retailLoanContractActionsService.executeAction(
                            actionEntity,
                            actionId,
                            null);
                }, httpOk);
    }

    //==========================================================================
    // test ws
    //==========================================================================
    final WebSocketHandler echoHandler1 = session -> {

        session.receive();

        Mono<WebSocketMessage> outMessage = Mono.just(String.format("Created message %s", LocalDateTime.now().toString()))
                .log()
                .doOnSubscribe(s -> {
                    s.request(Long.MAX_VALUE);
                    log.debug("{}: start messaging", session.getId());
                })
                .doOnNext(x -> log.info("{}: prepare to send '{}'", session.getId(), x))
                .doOnError(e -> log.error("{}: Error processing '{}'", session.getId(), e.getMessage()))
                .map(session::textMessage);

        log.debug("{}: Ураааа!!! - 1", session.getId());

        return session.send(outMessage);
    };

    @Autowired
    RetailLoanContractWebSocketService webSocketService;

    public Mono<ServerResponse> runWS(ServerRequest request) {

        return this.<Void>processServerRequest(request, VOID_CLASS,
                entity -> {

                    Mono<Void> resp = webSocketService
                            .getWebSocketClient()
                            //.execute(webSocketService.buildUri("ws://104.197.253.120:%d%s", 7001), this.getTestHandler1());
                            //                    .execute(webSocketService.buildUri("ws://127.0.0.1", this.getPort()), session
                            //                            -> session.receive()
                            //                            .doOnNext(s -> log.debug("answer from server: {}", s.getPayloadAsText()))
                            //                            .then());
                            //.execute(webSocketService.buildUri("ws://127.0.0.1", this.getPort()), session
                            .execute(webSocketService.buildUri("ws://104.197.253.120", 7001), session
                                    -> {

                                Mono<Void> inMsg = session.receive()
                                        .doOnNext(s -> log.debug("process answer from server: '{}'", s.getPayloadAsText()))
                                        .then();

                                Flux<WebSocketMessage> outMessages = Flux.just(
                                        String.format("Client message 1 - %s", LocalDateTime.now().toString()),
                                        String.format("Client message 2 - %s", LocalDateTime.now().toString()),
                                        String.format("Client message 3 - %s", LocalDateTime.now().toString()))
                                        .map(session::textMessage)
                                        .doOnNext(s -> log.debug("sending to server '{}'", s.getPayloadAsText()));

                                return inMsg
                                        .and(session.send(outMessages));
                            }).then();

                    //log.debug("{}: receive response  {}", testName, response.getClass().getCanonicalName());
                    //this.disposable = response.subscribe();
                    resp.block(Duration.ofSeconds(1L));
                }, httpOk);
    }
}
