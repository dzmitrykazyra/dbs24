package org.dbs24.proxy.core.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.component.ReferenceService;
import org.dbs24.rest.api.NewReactiveRestProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "proxy-core")
public class ReferenceRest extends NewReactiveRestProcessor {

    final ReferenceService referenceService;

    public ReferenceRest(ReferenceService referenceService) {

        this.referenceService = referenceService;
    }

    public Mono<ServerResponse> getAllProxyTypes(ServerRequest request) {

        return buildGetRequest(request, referenceService::getProxyTypes);
    }

    public Mono<ServerResponse> getAllProxyProviders(ServerRequest request) {

        return buildGetRequest(request, referenceService::getProxyProviders);
    }

    public Mono<ServerResponse> getAllCountries(ServerRequest request) {

        return buildGetRequest(request, referenceService::getCountries);
    }

    public Mono<ServerResponse> getAllAlgorithmSelections(ServerRequest request) {

        return buildGetRequest(request, referenceService::getAlgorithmSelections);
    }
}
