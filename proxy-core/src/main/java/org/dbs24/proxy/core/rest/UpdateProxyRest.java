package org.dbs24.proxy.core.rest;

import org.dbs24.proxy.core.component.UpdateProxyService;
import org.dbs24.proxy.core.entity.dto.request.UpdateBookProxiesRequest;
import org.dbs24.rest.api.NewReactiveRestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.dbs24.proxy.core.rest.api.consts.ProxyHttpConsts.RestQueryParams.QP_REQUEST_ID;

@Component
public class UpdateProxyRest extends NewReactiveRestProcessor {

    private final UpdateProxyService updateProxyService;

    @Autowired
    public UpdateProxyRest(UpdateProxyService updateProxyService) {
        this.updateProxyService = updateProxyService;
    }

    public Mono<ServerResponse> updateBookedProxies(ServerRequest request) {

        return buildPostRequest(request, UpdateBookProxiesRequest.class, updateProxyService::updateBookedProxies);
    }

    public Mono<ServerResponse> checkProxiesRelevance(ServerRequest request) {

        return buildGetRequest(request, () -> updateProxyService.checkProxiesRelevance(getIntegerFromParam(request, QP_REQUEST_ID)));
    }

}