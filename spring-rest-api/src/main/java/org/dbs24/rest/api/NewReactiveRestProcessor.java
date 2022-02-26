/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.spring.core.api.EntityInfo;
import org.dbs24.spring.core.api.PostRequestBody;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_UNKNOWN_ERROR;
import static org.dbs24.stmt.StmtProcessor.create;
import static org.dbs24.stmt.StmtProcessor.notNull;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Getter
@Log4j2
public abstract class NewReactiveRestProcessor extends ReactiveRestProcessor {

    protected <E extends EntityInfo, T extends PostRequestBody, V extends ResponseBody<E>> Mono<ServerResponse> buildPostRequest(
            ServerRequest serverRequest,
            Class<T> requestBodyClass,
            BodyProcessor<E, T, V> bodyProcessor) {

        return processRequest(serverRequest, () -> bodyProcessor.<E, T, V>processBody(serverRequest.bodyToMono(requestBodyClass)));
    }

    //==========================================================================
    protected <E extends EntityInfo, V extends ResponseBody<E>> Mono<ServerResponse> buildGetRequest(
            ServerRequest serverRequest,
            NewNoEntityProcessor<E, V> noEntityProcessor) {

        return processRequest(serverRequest, noEntityProcessor::processEntity);
    }

    //==========================================================================
    protected final ThrowableProcessorReq tpr = (request, throwable, epr) -> {

        final String errMsg = String.format("### %s://%s: %s -> '%s'",
                request.methodName(), request.path(), throwable.getClass().getCanonicalName(), throwable.getMessage());

        epr.setCode(OC_UNKNOWN_ERROR);
        epr.setError(getRestDebug() ? errMsg : OC_UNKNOWN_ERROR.getValue());
        epr.setMessage(getRestDebug() ? throwable.toString() : OC_UNKNOWN_ERROR.getValue());
        log.error(errMsg);

        throwable.printStackTrace();

        return errMsg;

    };

    //==========================================================================
    protected <E extends EntityInfo, V extends ResponseBody<E>> Mono<ServerResponse> processRequest(
            ServerRequest serverRequest,
            NewRequestProcessor<E, V> requestProcessor) {
        printServerRequest(serverRequest);

        final StopWatcher stopWatcher = getRestDebug()
                ? StopWatcher.create(serverRequest.methodName().concat(serverRequest.path())) : null;

        // create empty answer
        final ResponseBody<E> responseBody = create(ResponseBody.class);

        try {

            responseBody.assign(requestProcessor.processRequest());

            StmtProcessor.ifNull(responseBody.getCode(), () -> {
                responseBody.setCode(OC_UNKNOWN_ERROR);
                responseBody.setMessage(OC_UNKNOWN_ERROR.getValue());
                responseBody.setError(OC_UNKNOWN_ERROR.getValue());

            });

        } catch (Throwable throwable) {
            tpr.processThrowable(serverRequest, throwable, responseBody);
        }

        return ServerResponse.status(OK)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(responseBody), responseBody.getClass())
                .doOnCancel(() -> log.warn("{}: cancel ", serverRequest.path()))
                .doOnError(throwable -> {

                    final String errMsg = tpr.processThrowable(serverRequest, throwable, responseBody);

                    final ResponseBody<E> errorResponse
                            = create(ResponseBody.class,
                            result -> {
                                result.setCode(OC_UNKNOWN_ERROR);
                                result.setMessage(getRestDebug() ? errMsg : OC_UNKNOWN_ERROR.getValue());
                                result.setCreatedEntity(new EntityInfo() {
                                });
                            });

                    ServerResponse.ok()
                            .contentType(APPLICATION_JSON)
                            .body(Mono.just(errorResponse), ResponseBody.class);

                })
                .doFinally(df -> {
                    if (notNull(stopWatcher)) {
                        log.debug("{}: {}", df.name(), stopWatcher.getStringExecutionTime());
                    }
                });
    }
}
