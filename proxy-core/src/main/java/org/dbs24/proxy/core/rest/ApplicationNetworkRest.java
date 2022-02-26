package org.dbs24.proxy.core.rest;


import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.component.ApplicationNetworkService;
import org.dbs24.proxy.core.entity.dto.request.CreateApplicationNetworkRequest;
import org.dbs24.rest.api.NewReactiveRestProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "proxy-core")
public class ApplicationNetworkRest extends NewReactiveRestProcessor {

    final ApplicationNetworkService applicationNetworkService;

    public ApplicationNetworkRest(ApplicationNetworkService applicationNetworkService) {

        this.applicationNetworkService = applicationNetworkService;
    }

    public Mono<ServerResponse> createOrUpdateApplicationNetwork(ServerRequest request) {

        return buildPostRequest(request, CreateApplicationNetworkRequest.class, applicationNetworkService::createOrUpdateApplicationNetwork);
    }

    public Mono<ServerResponse> getAllApplicationNetworks(ServerRequest request) {

        return buildGetRequest(request, applicationNetworkService::getApplicationNetworks);
    }
}
