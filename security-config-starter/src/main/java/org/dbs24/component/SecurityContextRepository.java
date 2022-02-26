/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.security.jwt.AuthorizationHeaderPayload;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.dbs24.consts.SysConst.EMPTY_STRING;
import static reactor.core.publisher.Mono.justOrEmpty;

@Log4j2
@Component
@EqualsAndHashCode(callSuper = true)
public class SecurityContextRepository extends AbstractApplicationService implements ServerSecurityContextRepository {

    final AuthenticationManager authenticationManager;
    final JwtSecurityService jwtSecurityService;

    public SecurityContextRepository(AuthenticationManager authenticationManager, JwtSecurityService jwtSecurityService) {
        this.authenticationManager = authenticationManager;
        this.jwtSecurityService = jwtSecurityService;
    }

    private static final String BEARER = "Bearer ";
    private static final Predicate<String> matchBearerLength = authValue -> authValue.length() > BEARER.length();
    private static final Function<String, Mono<String>> isolateBearerValue = authValue -> justOrEmpty(authValue.substring(BEARER.length()));

    @Override
    public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static String getRequestDetails(ServerHttpRequest serverHttpRequest) {
        return String.format("=== %s %s; [%s -> %s, %s]",
                serverHttpRequest.getMethod(),
                serverHttpRequest.getURI(),
                serverHttpRequest.getRemoteAddress().toString().replaceAll("/", EMPTY_STRING),
                serverHttpRequest.getLocalAddress().toString().replaceAll("/", EMPTY_STRING),
                serverHttpRequest.getId());
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {

        log.debug(getRequestDetails(serverWebExchange.getRequest()));

        return justOrEmpty(serverWebExchange)
                .flatMap(AuthorizationHeaderPayload::extract)
                .filter(matchBearerLength)
                .flatMap(isolateBearerValue)
                .flatMap(jwtSecurityService::checkToken)
                .flatMap(authenticationManager::createSecurityContext)
                .log();

        //log.info("{}: response = {}", serverWebExchange.getRequest().getURI(), serverWebExchange.getResponse().getStatusCode());
    }
}
