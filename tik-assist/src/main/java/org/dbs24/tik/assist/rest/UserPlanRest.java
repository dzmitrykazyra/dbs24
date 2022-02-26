package org.dbs24.tik.assist.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.dbs24.tik.assist.entity.dto.plan.CustomUserPlanDto;
import org.dbs24.tik.assist.entity.dto.plan.response.CreatedCustomUserPlanDto;
import org.dbs24.tik.assist.service.hierarchy.PlanService;
import org.dbs24.tik.assist.service.security.AuthenticationService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class UserPlanRest extends ReactiveRestProcessor {

    private final PlanService planService;
    private final AuthenticationService authenticationService;

    public UserPlanRest(PlanService planService, AuthenticationService authenticationService) {

        this.planService = planService;
        this.authenticationService = authenticationService;
    }

    public Mono<ServerResponse> createCustomUserPlan(ServerRequest request) {

        return this.<CustomUserPlanDto, CreatedCustomUserPlanDto>createResponse(
                request,
                CustomUserPlanDto.class,
                CreatedCustomUserPlanDto.class,
                customUserPlanDto -> planService.createCustomPlan(
                        authenticationService.extractUserIdFromServerRequest(request),
                        customUserPlanDto
                ));
    }
}
