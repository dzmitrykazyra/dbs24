package org.dbs24.tik.assist.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.dbs24.tik.assist.entity.dto.constraint.CustomPlanConstraintsDto;
import org.dbs24.tik.assist.entity.dto.proportion.*;
import org.dbs24.tik.assist.service.hierarchy.ProportionService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "tik-assist")
public class ProportionRest extends ReactiveRestProcessor {

    final ProportionService proportionService;


    public ProportionRest(ProportionService proportionService) {

        this.proportionService = proportionService;
    }

    public Mono<ServerResponse> createOrUpdateActionsProportion(ServerRequest request) {

        return this.<ActionsProportionDto, CreatedActionsProportionDto>createResponse(
                request,
                ActionsProportionDto.class,
                CreatedActionsProportionDto.class,
                proportionService::createOrUpdateActionsProportion
        );
    }

    public Mono<ServerResponse> createOrUpdateAccountsProportion(ServerRequest request) {

        return this.<AccountsProportionDto, CreatedAccountsProportionDto>createResponse(
                request,
                AccountsProportionDto.class,
                CreatedAccountsProportionDto.class,
                proportionService::createOrUpdateAccountsProportion
        );
    }

    public Mono<ServerResponse> getAllActionsProportions(ServerRequest request) {

        return this.<ActionsProportionDtoList>createResponse(
                request,
                ActionsProportionDtoList.class,
                proportionService::getAllActionsProportions
        );
    }

    public Mono<ServerResponse> getAllAccountsProportions(ServerRequest request) {

        return this.<AccountsProportionDtoList>createResponse(
                request,
                AccountsProportionDtoList.class,
                proportionService::getAllAccountsProportions
        );
    }

    public Mono<ServerResponse> getCustomPlanMaxConstraints(ServerRequest request) {

        return this.<CustomPlanConstraintsDto>createResponse(
                request,
                CustomPlanConstraintsDto.class,
                proportionService::getCustomPlanMaxConstraints
        );
    }
}
