/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.rest;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.rest.api.NewReactiveRestProcessor;
import org.dbs24.tik.dev.rest.dto.contract.CreateContractRequest;
import org.dbs24.tik.dev.rest.dto.developer.CreateDeveloperRequest;
import org.dbs24.tik.dev.rest.dto.device.CreateDeviceRequest;
import org.dbs24.tik.dev.rest.dto.endpoint.CreateEndpointActionRequest;
import org.dbs24.tik.dev.rest.dto.tariff.limit.CreateTariffLimitRequest;
import org.dbs24.tik.dev.rest.dto.tariff.plan.CreateTariffPlanRequest;
import org.dbs24.tik.dev.rest.dto.tariff.price.CreateTariffPlanPriceRequest;
import org.dbs24.tik.dev.rest.dto.tik.account.CreateTikAccountRequest;
import org.dbs24.tik.dev.rest.dto.tik.account.scope.CreateTikAccountScopeRequest;
import org.dbs24.tik.dev.service.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@EqualsAndHashCode
public class TikDevApiRest extends NewReactiveRestProcessor {

    final DevelopersService developersService;
    final TikAccountService tikAccountService;
    final TikAccountScopeService tikAccountScopeService;
    final TariffLimitService tariffLimitService;
    final TariffPlanService tariffPlanService;
    final TariffPlanPriceService tariffPlanPriceService;
    final ContractsService contractsService;
    final DevicesService devicesService;
    final EndpointActionsService endpointActionsService;

    public TikDevApiRest(DevelopersService developersService, TikAccountService tikAccountService, TikAccountScopeService tikAccountScopeService, TariffLimitService tariffLimitService, TariffPlanService tariffPlanService, ContractsService contractsService, TariffPlanPriceService tariffPlanPriceService, DevicesService devicesService, EndpointActionsService endpointActionsService) {

        this.developersService = developersService;
        this.tikAccountService = tikAccountService;
        this.tikAccountScopeService = tikAccountScopeService;
        this.tariffLimitService = tariffLimitService;
        this.tariffPlanService = tariffPlanService;
        this.contractsService = contractsService;
        this.tariffPlanPriceService = tariffPlanPriceService;
        this.devicesService = devicesService;
        this.endpointActionsService = endpointActionsService;

    }

    public Mono<ServerResponse> createOrUpdateDeveloper(ServerRequest serverRequest) {

        return buildPostRequest(serverRequest, CreateDeveloperRequest.class, developersService::createOrUpdateDeveloper);
    }

    public Mono<ServerResponse> createOrUpdateTikAccount(ServerRequest serverRequest) {

        return buildPostRequest(serverRequest, CreateTikAccountRequest.class, tikAccountService::createOrUpdateTikAccount);
    }

    public Mono<ServerResponse> createOrUpdateTikAccountScope(ServerRequest serverRequest) {

        return buildPostRequest(serverRequest, CreateTikAccountScopeRequest.class, tikAccountScopeService::createOrUpdateTikAccountScope);
    }

    public Mono<ServerResponse> createOrUpdateTariffLimit(ServerRequest serverRequest) {

        return buildPostRequest(serverRequest, CreateTariffLimitRequest.class, tariffLimitService::createOrUpdateTariffLimit);
    }

    public Mono<ServerResponse> createOrUpdateTariffPlan(ServerRequest serverRequest) {

        return buildPostRequest(serverRequest, CreateTariffPlanRequest.class, tariffPlanService::createOrUpdateTariffPlan);
    }

    public Mono<ServerResponse> createOrUpdateContract(ServerRequest serverRequest) {

        return buildPostRequest(serverRequest, CreateContractRequest.class, contractsService::createOrUpdateContract);
    }

    public Mono<ServerResponse> createOrUpdateTariffPlanPrice(ServerRequest serverRequest) {

        return buildPostRequest(serverRequest, CreateTariffPlanPriceRequest.class, tariffPlanPriceService::createOrUpdateTariffPlanPrice);
    }

    public Mono<ServerResponse> createOrUpdateDevice(ServerRequest serverRequest) {

        return buildPostRequest(serverRequest, CreateDeviceRequest.class, devicesService::createOrUpdateDevice);
    }

    public Mono<ServerResponse> createEndpointAction(ServerRequest serverRequest) {

        return buildPostRequest(serverRequest, CreateEndpointActionRequest.class, endpointActionsService::createEndpointAction);
    }

}
