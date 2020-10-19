/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest;

/**
 *
 * @author Козыро Дмитрий
 */
import org.dbs24.application.core.log.LogService;
import org.dbs24.application.core.nullsafe.NullSafe;
import static org.dbs24.application.core.sysconst.SysConst.*;
import org.dbs24.entity.RetailLoanContract;
import org.dbs24.consts.RetailLoanContractConst;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.dbs24.service.RetailLoanContractActionsService;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import org.springframework.util.MultiValueMap;
import org.springframework.util.Assert;

@Component
public class RetailLoanContractRest<T extends RetailLoanContract> extends ReactiveRestProcessor {

    @Autowired
    private RetailLoanContractActionsService retailLoanContractActionsService;
    //==========================================================================
    final Class<T> rlcClass = (Class<T>) RetailLoanContractConst.RETAIL_LOAN_CONTRACT_CLASS;

    public Mono<ServerResponse> findRetailLoanContract(ServerRequest request) {

        // поднимаем id сущности из параметра запроса
        final Long entityId = Long.valueOf(request.queryParam("entityId")
                .orElseThrow(() -> new RuntimeException("Entity ID is not defined in request")));

        // поднимаем из репозитория по ИД
        final T existEntity = (T) retailLoanContractActionsService
                .getPersistenceEntityManager()
                .getEntityManager()
                .<T>find(rlcClass, entityId);

        Assert.notNull(existEntity, String.format("%s is not found (entityId=%d)",
                rlcClass.getSimpleName(),
                entityId));

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
    // создание кредитного договора
    public Mono<ServerResponse> createRetailLoanContract(final ServerRequest request) {

        return this.<T>processServerRequest(request, rlcClass,
                entity -> {
                    // обработка сущности из запроса
                    this.retailLoanContractActionsService.executeAction(
                            entity,
                            RetailLoanContractConst.MODIFY_INDIVIDUAL_LOAN_CONTRACT,
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
    // выполнение действия над договором
    public Mono<ServerResponse> executeAction(final ServerRequest request) {

        //final Long entityId = Long.valueOf(request.queryParam("entityId").orElse("0"));
        final Integer actionId = Integer.valueOf(request.queryParam("actionId")
                .orElseThrow(() -> new RuntimeException("Action code is not defined")));

        //Assert.notNull(entity, "Entity must not be null!");
        return this.<T>processServerRequest(request, rlcClass,
                entity -> {

                    final T actionEntity;

                    // тело entity отсутствует
                    if (NullSafe.isNull(entity)) {
                        // поднимаем id сущности из параметра запроса
                        final Long entityId = Long.valueOf(request.queryParam("entityId")
                                .orElseThrow(() -> new RuntimeException("Entity ID is not defined in request")));

                        // поднимаем из репозитория по ИД
                        actionEntity = (T) retailLoanContractActionsService
                                .getPersistenceEntityManager()
                                .getEntityManager()
                                .<T>find(rlcClass, entityId);

                    } else {
                        actionEntity = entity;
                    }

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
                    this.retailLoanContractActionsService.executeAction(
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

/*

@RestController
@RequestMapping("/helloWorld")
public class ApplicationController {
 
  @GetMapping("/welcome")
  private Mono<String> getMessage() {
    return Mono.just("Hello World!!");
  }   
}

 */
