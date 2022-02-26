/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.exception.api.QueryParamNotExists;
import org.dbs24.consts.SysConst;
import org.dbs24.rest.api.service.HttpBalancerService;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.time.LocalDateTime.now;
import static org.dbs24.application.core.locale.NLS.localDateTime2long;
import static org.dbs24.application.core.locale.NLS.long2LocalDateTime;
import static org.dbs24.consts.SysConst.*;
import static org.dbs24.stmt.StmtProcessor.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.justOrEmpty;

@Getter
@Log4j2
@EqualsAndHashCode(callSuper = true)
public abstract class ReactiveRestProcessor extends AbstractApplicationService {

    public static final HttpStatusProcessor httpOk = () -> OK;
    final String expriedExcClass = "io.jsonwebtoken.ExpiredJwtException";

    @Value("${config.reactor.rest.debug:true}")
    private Boolean restDebug = BOOLEAN_FALSE;

    @Value("${eureka.balancer.enabled:false}")
    private Boolean enableBalancer;

    private HttpBalancerService httpBalancerService;

    @Autowired(required = false)
    private void initHttpBalancerService(HttpBalancerService httpBalancerService) {
        this.httpBalancerService = httpBalancerService;
    }

    @Value("${config.restfull.put-ts:true}")
    private Boolean putTimeStamp = BOOLEAN_TRUE;

    final QueryMetrics queryMetrics = create(QueryMetrics.class);

    protected void printServerRequest(ServerRequest request) {

        log.debug("{}: principal {}, ServerRequest {}",
                request.path(),
                request.principal(),
                request.headers());
    }

    //==========================================================================
    final Predicate<Class<?>> isVoidClass = clazz -> clazz.equals(VOID_CLASS);

    final Consumer<Throwable> throwableConsumer = throwable -> {
        log.error("{}: {}", throwable.getCause(), throwable.getMessage());
    };

    private <V> Mono<V> getBody(ServerRequest serverRequest, Class<V> clazz) {
        return isVoidClass.test(clazz) ? Mono.just((V) EMPTY_STRING) : serverRequest.bodyToMono(clazz);
    }

    private <T, V> Mono<V> getEntity(T entity, EntityProcessor<T, V> entityProcessor) {
        return Mono.just(entityProcessor.<T, V>processEntity(entity.equals(EMPTY_STRING) ? null : entity));
    }

    private <T, V> Mono<V> getCloudEntity(ServerRequest serverRequest, T entity, Class<V> classV) {

        return httpBalancerService.getCloudEntity(serverRequest, entity, classV);
    }

    private <T, V> Mono<V> processEntity(T entity, NoBodyEntityProcessor<T> noBodyEntityProcessor) {

        noBodyEntityProcessor.processEntity(entity);

        return Mono.just((V) EMPTY_STRING);
    }

    private <T> Mono<ServerResponse> getResponse(T response) {

        return ServerResponse
                .status(OK)
                .contentType(APPLICATION_JSON)
                .headers(this::putHeaderServerTimeStamp)
                .body(Mono.just(response), response.getClass());
    }

    private <T> Mono<ServerResponse> emptyResponse(T response) {

        return ServerResponse
                .status(OK)
                .contentType(APPLICATION_JSON)
                .headers(this::putHeaderServerTimeStamp)
                .body(EMPTY_STRING, STRING_CLASS);
    }
    //==================================================================================================================

    protected <T, V> Mono<ServerResponse> createResponse(
            ServerRequest serverRequest,
            Class<T> clazzT,
            Class<V> clazzV,
            EntityProcessor<T, V> entityProcessor) {

        final Integer qryNum = queryMetrics.registryQuery(serverRequest.methodName(), serverRequest.path());

        ifTrue(enableBalancer, () -> assertNotNull(HttpBalancerService.class, httpBalancerService, "HttpBalancer is not ready "));

        return justOrEmpty(serverRequest)
                .flatMap(sr -> getBody(sr, clazzT))
                .flatMap(body -> enableBalancer ? getCloudEntity(serverRequest, body, clazzV) : getEntity(body, entityProcessor))
                .flatMap(this::getResponse)
                .doOnError(throwableConsumer)
                .doFinally(df -> queryMetrics.finishQuery(qryNum, df.toString()))
                .log();
    }

    //==========================================================================
    protected <T> Mono<ServerResponse> createResponse(
            ServerRequest serverRequest,
            Class<T> classT,
            NoEntityProcessor<T> noEntityProcessor) {

        final Integer qryNum = queryMetrics.registryQuery(serverRequest.methodName(), serverRequest.path());

        return ServerResponse
                .status(httpOk.processHttpStatus())
                .contentType(APPLICATION_JSON)
                .headers(this::putHeaderServerTimeStamp)
                .body(enableBalancer ? getCloudEntity(serverRequest, null, classT) : just(noEntityProcessor.<T>processEntity()), classT)
                .doFinally(df -> queryMetrics.finishQuery(qryNum, df.toString()))
                .log("processServerRequest");
    }

    //=========================================================================
    protected <T, V> Mono<ServerResponse> processServerRequest(
            ServerRequest serverRequest,
            Class<T> classT,
            Class<V> classV,
            Function<T, Mono<V>> funkResponse) {

        final Integer qryNum = queryMetrics.registryQuery(serverRequest.methodName(), serverRequest.path());

        return ServerResponse
                .status(OK)
                .contentType(APPLICATION_JSON)
                .headers(this::putHeaderServerTimeStamp)
                .body(serverRequest
                        .bodyToMono(classT)
                        .flatMap(body -> enableBalancer ? getCloudEntity(serverRequest, body, classV) : funkResponse.apply(body)), classV)
                .doFinally(df -> queryMetrics.finishQuery(qryNum, df.toString()))
                .log();

    }

    protected <V> Mono<ServerResponse> processServerRequest(
            ServerRequest serverRequest,
            Class<V> classV,
            Function<ServerRequest, Mono<V>> funkResponse) {

        final Integer qryNum = queryMetrics.registryQuery(serverRequest.methodName(), serverRequest.path());

        return ServerResponse
                .status(OK)
                .headers(this::putHeaderServerTimeStamp)
                .contentType(APPLICATION_JSON)
                .body(just(serverRequest)
                                .flatMap(body -> enableBalancer ? getCloudEntity(serverRequest, body, classV) : funkResponse.apply(body))
                                .doOnError(throwableConsumer)
                                .log()
                        , classV)
                .doOnError(throwableConsumer)
                .doFinally(df -> queryMetrics.finishQuery(qryNum, df.toString()))
                .log();
    }

    public void putHeaderServerTimeStamp(HttpHeaders httpHeaders) {
        ifTrue(putTimeStamp, () -> httpHeaders.add("Server-Time-Stamp", localDateTime2long(now()).toString()));
    }

    //==========================================================================
    protected <T> Mono<ServerResponse> processServerRequest(
            ServerRequest serverRequest,
            Class<T> clazzT,
            NoBodyEntityProcessor<T> noBodyEntityProcessor) {

        final Integer qryNum = queryMetrics.registryQuery(serverRequest.methodName(), serverRequest.path());

        return justOrEmpty(serverRequest)
                .flatMap(sr -> getBody(sr, clazzT))
                .flatMap(body -> enableBalancer ? getCloudEntity(serverRequest, body, VOID_CLASS) : processEntity(body, noBodyEntityProcessor))
                .flatMap(this::emptyResponse)
                .doOnError(throwableConsumer)
                .doFinally(df -> queryMetrics.finishQuery(qryNum, df.toString()))
                .log();
    }

    //==========================================================================
    protected <T> Mono<T> log(Mono<T> mono) {
        return restDebug ? mono.log() : mono;
    }

    //==========================================================================
    public static LocalDateTime getLocalDateTimeFromLongParam(ServerRequest request, String paramName) {

        return long2LocalDateTime(Long.valueOf(request.queryParam(paramName)
                .orElseThrow(() -> new QueryParamNotExists(
                        String.format("%s: parameter '%s' not defined", request.path(), paramName)))));
    }

    //==========================================================================
    public static String getStringFromParam(ServerRequest request, String paramName) {

        return validateParameter(request.queryParam(paramName)
                .orElseThrow(() -> new QueryParamNotExists(
                        String.format("%s: parameter '%s' not defined", request.path(), paramName))), paramName);
    }

    //==========================================================================
    public static String validateParameter(String param, String paramName) {

        if (param.isEmpty()) {
            throw new QueryParamNotExists(String.format("Parameter '%s' is empty", paramName));
        }
        return param;
    }

    //==========================================================================
    public static String getOptionalStringFromParam(ServerRequest request, String paramName) {

        return request.queryParam(paramName)
                .orElseGet(() -> SysConst.EMPTY_STRING);

    }

    //==========================================================================
    public static String getOptionalNullStringFromParam(ServerRequest request, String paramName) {

        return request.queryParam(paramName)
                .orElse(STRING_NULL);

    }

    public static Integer getIntegerFromParam(ServerRequest request, String paramName) {

        return Integer.valueOf(request.queryParam(paramName)
                .orElseThrow(() -> new QueryParamNotExists(
                        String.format("%s: parameter '%s' not defined", request.path(), paramName))));
    }

    //==========================================================================
    public static Integer getOptionalIntegerFromParam(ServerRequest request, String paramName) {

        return request.queryParam(paramName)
                .map(Integer::valueOf)
                .orElse(INTEGER_NULL);

    }

    //==========================================================================
    public static Long getOptionalLongFromParam(ServerRequest request, String paramName) {

        return request.queryParam(paramName)
                .map(Long::valueOf)
                .orElse(LONG_NULL);

    }


    public static Boolean getBooleanFromParam(ServerRequest request, String paramName) {

        return Boolean.valueOf(request.queryParam(paramName)
                .orElseThrow(() -> new QueryParamNotExists(
                        String.format("%s: parameter '%s' not defined", request.path(), paramName))));
    }

    public static Boolean getOptionalBooleanFromParam(ServerRequest request, String paramName) {

        return request.queryParam(paramName)
                .map(m -> m.toLowerCase().equals(STRING_TRUE))
                .orElseGet(() -> Boolean.FALSE);
    }

    public static Long getLongFromParam(ServerRequest request, String paramName) {

        return Long.valueOf(request.queryParam(paramName)
                .orElseThrow(() -> new QueryParamNotExists(
                        String.format("%s: parameter '%s' not defined", request.path(), paramName))));
    }

    //==========================================================================
    public static Byte getByteFromParam(ServerRequest request, String paramName) {

        return Byte.valueOf(request.queryParam(paramName)
                .orElseThrow(() -> new QueryParamNotExists(
                        String.format("%s: parameter '%s' not defined", request.path(), paramName))));
    }
}
