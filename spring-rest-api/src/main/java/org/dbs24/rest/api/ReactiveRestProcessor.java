/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import org.dbs24.application.core.log.LogService;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.application.core.sysconst.SysConst;
import org.dbs24.spring.core.api.ApplicationService;
import lombok.Data;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
public abstract class ReactiveRestProcessor extends RestProcessor implements ApplicationService {

//    @Value("${reactive.rest.debug:false}")
//    private Boolean restDebug = SysConst.BOOLEAN_FALSE;
    private StopWatcher stopWatcher;

    @Override
    protected void initRestService() {

    }
    //==========================================================================
//    protected Mono<ServerResponse> executeRestAction(final ServerRequest request, final Class<?> clazz) {
//        
//    }

    //==========================================================================
    protected <T> Mono<ServerResponse> processServerRequest(final ServerRequest request,
            final Class<T> clazz,
            final EntityProcessor<T> reactiveProcessor) {

        final Mono<T> requestEntity = request.bodyToMono(clazz);
        if (this.getRestDebug()) {
            LogService.LogInfo(clazz, () -> String.format("%s%s: START",
                    request.methodName(),
                    request.path()));
        }
        return ServerResponse
                .ok()
                .body(this.<T>log(requestEntity)
                        .doOnError(e -> LogService.LogErr(e.getClass(), () -> e.getMessage()))
                        //.subscribe()
                        //.map( x -> x)
                        .doOnSuccess(entity -> {
                            if (this.getRestDebug()) {
                                stopWatcher = StopWatcher.create(SysConst.EMPTY_STRING);
                            }

                            // выполнение лямды, обработка конкретной сущности
                            final T updatedEntity = reactiveProcessor.processEntity(entity);

                            if (this.getRestDebug()) {
                                LogService.LogInfo(updatedEntity.getClass(), () -> String.format("%s%s: %s",
                                        request.methodName(),
                                        request.path(),
                                        stopWatcher.getStringExecutionTime()));
                            }
                        })
                        .doFinally(s -> {

                            if (this.getRestDebug()) {
                                LogService.LogInfo(s.getClass(), () -> String.format("%s: FINISH REQUEST %s::%s%s",
                                        s.name(),
                                        request.getClass(),
                                        request.methodName(),
                                        request.path()));
                            }
                        }), clazz);
    }

    //==========================================================================
    protected <T> Mono<ServerResponse> processServerRequest(final ServerRequest request,
            final T returnedEntity,
            final EntityProcessor<T> reactiveProcessor) {

        final Class<T> clazz = (Class<T>) returnedEntity.getClass();

        return ServerResponse.ok()
                .body(this.<T>log(Mono.just(returnedEntity))
                        .doOnError(e -> LogService.LogErr(e.getClass(), () -> e.getMessage()))
                        //.subscribe()
                        //.map( x -> x)
                        .doOnSuccess(entity -> {

                            if (this.getRestDebug()) {
                                this.stopWatcher = StopWatcher.create(SysConst.EMPTY_STRING);
                            }

                            // выполнение лямды, обработка конкретной сущности
                            final T updatedEntity = reactiveProcessor.processEntity(entity);

                            if (this.getRestDebug()) {
                                LogService.LogInfo(updatedEntity.getClass(), () -> String.format("%s%s: %s",
                                        request.methodName(),
                                        request.path(),
                                        stopWatcher.getStringExecutionTime()));
                            }
                        })
                        .doFinally(s -> {

                            if (this.getRestDebug()) {
                                LogService.LogInfo(s.getClass(), () -> String.format("%s: FINISH REQUEST %s::%s%s",
                                        s.name(),
                                        request.getClass(),
                                        request.methodName(),
                                        request.path()));
                            }
                        }), clazz);
    }

    //==========================================================================
    protected <T> Mono<T> log(final Mono<T> mono) {
        return (this.getRestDebug() ? mono.log() : mono);
    }
}
