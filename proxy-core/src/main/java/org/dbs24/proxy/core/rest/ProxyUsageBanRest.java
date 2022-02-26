package org.dbs24.proxy.core.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.component.ProxyUsageBanService;
import org.dbs24.proxy.core.entity.dto.request.CreateProxyUsageBanRequest;
import org.dbs24.rest.api.NewReactiveRestProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "proxy-core")
public class ProxyUsageBanRest extends NewReactiveRestProcessor {

    final ProxyUsageBanService proxyUsageBanService;

    public ProxyUsageBanRest(ProxyUsageBanService proxyUsageBanService) {

        this.proxyUsageBanService = proxyUsageBanService;
    }

    public Mono<ServerResponse> createProxyUsageBan(ServerRequest request) {

        return buildPostRequest(request, CreateProxyUsageBanRequest.class, proxyUsageBanService::createProxyUsageBan);
    }
}
