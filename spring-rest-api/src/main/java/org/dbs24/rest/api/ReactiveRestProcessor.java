/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.nullsafe.StopWatcher;
import static org.dbs24.consts.SysConst.*;
import org.dbs24.spring.core.api.ApplicationService;
import org.springframework.beans.factory.annotation.Value;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

@Data
@Log4j2
public abstract class ReactiveRestProcessor implements ApplicationService {

    public static final HttpStatusProcessor httpOk = () -> OK;

    @Value("${reactive.rest.debug:true}")
    private Boolean restDebug = BOOLEAN_FALSE;

    //==========================================================================
    protected <T, V> Mono<ServerResponse> processServerRequest(
            ServerRequest request,
            Class<T> clazz,
            EntityProcessor<T, V> entityProcessor,
            HttpStatusProcessor httpStatusProcessor) {

        final StopWatcher stopWatcher = restDebug
                ? StopWatcher.create(request.methodName().concat(request.path())) : null;

        final T entity = request.bodyToMono(clazz).block();
        final V response = entityProcessor.processEntity(entity);

        Assert.notNull(response, String.format("%s: response can't be null", request.methodName()));

        return ServerResponse
                .status(httpStatusProcessor.processHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(response), response.getClass())
                .doOnCancel(() -> log.error("cancel {}", request.path()))
                .doOnError(throwable -> {
                    log.error("{}: {}",
                            request.path(),
                            throwable.getMessage());
                    ServerResponse
                            .badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(Mono.just(throwable.getMessage()), String.class);
                })
                .doFinally(df -> {
                    if (NullSafe.notNull(stopWatcher)) {
                        log.debug("{}: {}",
                                df.name(),
                                stopWatcher.getStringExecutionTime());
                    }
                });
    }

    //==========================================================================
    protected <T> Mono<ServerResponse> processServerRequest(
            ServerRequest request,
            Class<T> clazz,
            NoBodyEntityProcessor<T> noBodyEntityProcessor,
            HttpStatusProcessor httpStatusProcessor) {

        final StopWatcher stopWatcher = restDebug
                ? StopWatcher.create(request.methodName().concat(request.path())) : null;

        final Mono<ServerResponse> serverResponse = NullSafe.create()
                .execute2result(() -> {
                    final T entity = request.bodyToMono(clazz).block();
                    noBodyEntityProcessor.processEntity(entity);

                    return log(
                            ServerResponse
                                    .status(httpStatusProcessor.processHttpStatus())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .build());
                })
                .catchException2result(throwable -> {
                    throwable.printStackTrace();
                    return ServerResponse
                            .badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(Mono.just(throwable.getLocalizedMessage()), String.class);
                })
                .getObject();
        if (NullSafe.notNull(stopWatcher)) {
            log.debug(stopWatcher.getStringExecutionTime());
        }
        return serverResponse;
    }

    //==========================================================================
    protected <T> Mono<T> log(Mono<T> mono) {
        return restDebug ? mono.log() : mono;
    }
}
