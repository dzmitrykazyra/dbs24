/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.component.RequestService;
import org.dbs24.rest.api.NewReactiveRestProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "proxy-core")
public class RequestRest extends NewReactiveRestProcessor {

    final RequestService requestService;
    
    public RequestRest(RequestService requestService) {
        this.requestService = requestService;
    }
    
    //==========================================================================
/*    public Mono<ServerResponse> createOrUpdateProxyRequest(ServerRequest request) {

        return buildPostRequest(request, CreateProxyRequest.class, requestsServices::createOrUpdateProxyRequest);
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdateProxyUsage(ServerRequest request) {

        return buildPostRequest(request, CreatedUsageRequest.class, requestsServices::createOrUpdateProxyUsage);
    }   */
}
