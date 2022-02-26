/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.entity.dto.BookProxyByIdDto;
import org.dbs24.proxy.core.entity.dto.request.BookProxiesRequest;
import org.dbs24.proxy.core.entity.dto.request.BookProxyRequest;
import org.dbs24.proxy.core.entity.dto.request.UpdateBookProxiesRequest;
import org.dbs24.rest.api.NewReactiveRestProcessor;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.dbs24.proxy.core.component.ProxyService;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.dbs24.proxy.core.rest.api.consts.ProxyHttpConsts.RestQueryParams.*;
import static org.dbs24.rest.api.action.RestAction.MODIFY_ENTITY;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "proxy-core")
public class ProxyRest extends NewReactiveRestProcessor {

    final ProxyService proxyService;

    public ProxyRest(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    public Mono<ServerResponse> bookProxy(ServerRequest request) {

        return buildPostRequest(request, BookProxyRequest.class, proxyService::bookProxy);
    }


    public Mono<ServerResponse> bookProxies(ServerRequest request) {

        return buildPostRequest(request, BookProxiesRequest.class, proxyService::bookProxies);
    }

}
