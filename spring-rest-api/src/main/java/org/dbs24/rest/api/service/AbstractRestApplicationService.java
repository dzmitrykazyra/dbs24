/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api.service;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.application.core.service.funcs.GenericFuncs;
import org.dbs24.rest.api.EntityInfoBuilder;
import org.dbs24.rest.api.EntityInfoProcessor;
import org.dbs24.rest.api.ResponseBody;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.spring.core.api.EntityInfo;
import org.dbs24.spring.core.api.RequestBody;
import org.dbs24.spring.core.api.RequestProcessor;
import org.dbs24.stmt.StmtProcessor;
import reactor.core.publisher.Mono;

import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;
import static org.dbs24.stmt.StmtProcessor.*;

@Getter
@Log4j2
public abstract class AbstractRestApplicationService extends AbstractApplicationService {

    //==========================================================================

    protected <E extends EntityInfo, R extends ResponseBody<E>> R createAnswer(Class<R> classResp, EntityInfoProcessor<E> rp) {

        return create(classResp,
                responseBody -> {
                    responseBody.setCreatedEntity(create((Class<E>) GenericFuncs.getTypeParameterClass(classResp),
                            entity -> rp.build(responseBody, entity)));
                    waitForComplected(responseBody);
                    log.debug("{}: {}", classResp.getSimpleName(), responseBody);
                }
        );
    }
    //==========================================================================

    protected <E extends EntityInfo, R extends ResponseBody<E>, T extends RequestBody> void processRequest(Mono<T> monoRequest, R responseBody, RequestProcessor<T> requestProcessor) {

        monoRequest.flatMap(request -> {

                    log.debug("process entityInfo ({}): {}", getClass().getCanonicalName(), request);

                    requestProcessor.<T>processRequest(request);
                    //responseBody.complete();
                    ifNull(responseBody.getCode(), () -> responseBody.setCode(OC_OK));
                    ifNull(responseBody.getMessage(), () -> responseBody.setMessage(responseBody.getCode().getValue()));

                    return Mono.justOrEmpty(responseBody);
                })
                .doOnError(throwable -> {

                    responseBody.complete();
                    responseBody.assignException(throwable);

                    ifNotNull(throwable, exception -> {
                        log.error("### processRequest Exception: '{}, {} \n {}'", responseBody.getCode(), responseBody.getError(), exception.getMessage());
                        exception.printStackTrace();
                    }, () -> {
                        responseBody.complete();
                        log.error("### throwable object is invalid");
                        log.error("### processRequest Exception: '{}, {} ", responseBody.getCode(), responseBody.getError());
                    });
                }).subscribe();
    }

    protected <E extends EntityInfo, R extends ResponseBody<E>> void processRequest(R responseBody, EntityInfoBuilder entityInfoBuilder) {

        try {

            log.debug("process entityInfo ({})", getClass().getCanonicalName());

            entityInfoBuilder.buildEntityInfo();

            responseBody.complete();
            ifNull(responseBody.getCode(), () -> responseBody.setCode(OC_OK));
            ifNull(responseBody.getMessage(), () -> responseBody.setMessage(responseBody.getCode().getValue()));

        } catch (Throwable throwable) {
            processException(responseBody, throwable);
        }
    }

    private <E extends EntityInfo, R extends ResponseBody<E>> void processException(R responseBody, Throwable throwable) {
        responseBody.complete();
        responseBody.assignException(throwable);

        ifNotNull(throwable, exception -> {
            log.error("### processRequest Exception: '{}, {} \n {}'", responseBody.getCode(), responseBody.getError(), exception.getMessage());
            exception.printStackTrace();
        }, () -> {
            log.error("### throwable object is invalid");
            log.error("### processRequest Exception: '{}, {} ", responseBody.getCode(), responseBody.getError());
        });
    }

    @Deprecated
    protected <E extends EntityInfo, R extends ResponseBody<E>, T extends RequestBody> void waitForComplected(R responseBody) {

        final StopWatcher stopWatcher = StopWatcher.create();

        while ((!responseBody.isCompleted()) && (stopWatcher.getExecutionTime() < 5000)) {
            StmtProcessor.sleep(50);
            Thread.yield();
        }
        log.warn("{}: wait for completed ({}) ", responseBody.getClass().getCanonicalName(), stopWatcher.getExecutionTime());
    }

    //==========================================================================
    protected String createEntityServiceMsg(Class<?> clazz, Boolean isNew, Long entityId) {
        return String.format("%s is %s (%d)", clazz.getSimpleName(),
                isNew ? "created" : "updated",
                entityId);
    }
}
