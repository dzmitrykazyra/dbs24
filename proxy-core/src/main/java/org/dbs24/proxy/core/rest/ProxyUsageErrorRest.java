package org.dbs24.proxy.core.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.component.ProxyUsageErrorService;
import org.dbs24.proxy.core.entity.dto.request.CreateProxyUsageErrorRequest;
import org.dbs24.rest.api.NewReactiveRestProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "proxy-core")
public class ProxyUsageErrorRest extends NewReactiveRestProcessor {

    final ProxyUsageErrorService proxyUsageErrorService;

    public ProxyUsageErrorRest(ProxyUsageErrorService proxyUsageErrorService) {

        this.proxyUsageErrorService = proxyUsageErrorService;
    }

    public Mono<ServerResponse> createProxyUsageError(ServerRequest request) {

        return buildPostRequest(request, CreateProxyUsageErrorRequest.class, proxyUsageErrorService::createProxyUsageError);
    }
}
