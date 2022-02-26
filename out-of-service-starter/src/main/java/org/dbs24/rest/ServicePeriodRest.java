package org.dbs24.rest;

import org.dbs24.component.ServicePeriodsService;
import org.dbs24.rest.api.NewReactiveRestProcessor;
import org.dbs24.rest.dto.serviceperiod.CreateServicePeriodRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ServicePeriodRest extends NewReactiveRestProcessor {

    //==================================================================================================================
    final ServicePeriodsService servicePeriodsService;

    public ServicePeriodRest(ServicePeriodsService servicePeriodsService) {
        this.servicePeriodsService = servicePeriodsService;
    }
    //==================================================================================================================

    public Mono<ServerResponse> createOrUpdateServicePeriod(ServerRequest request) {
        return buildPostRequest(request, CreateServicePeriodRequest.class, servicePeriodsService::createOrUpdateServicePeriod);
    }

    public Mono<ServerResponse> getPeriodsService(ServerRequest request) {

        return buildGetRequest(request, servicePeriodsService::getServicePeriod);
    }

}
