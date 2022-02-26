package org.dbs24.tik.assist.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.dbs24.tik.assist.constant.RequestQueryParam;
import org.dbs24.tik.assist.entity.dto.plan.template.PlanTemplateActionDto;
import org.dbs24.tik.assist.entity.dto.plan.template.PlanTemplateDto;
import org.dbs24.tik.assist.entity.dto.plan.template.PlanTemplateListDto;
import org.dbs24.tik.assist.service.hierarchy.PlanTemplateService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "tik-assist")
public class PlanTemplateRest extends ReactiveRestProcessor {

    final PlanTemplateService planTemplateService;

    public PlanTemplateRest(PlanTemplateService planTemplateService) {

        this.planTemplateService = planTemplateService;
    }

    @ResponseStatus
    public Mono<ServerResponse> createOrUpdatePlanTemplate(ServerRequest request) {

        return this.<PlanTemplateDto, PlanTemplateActionDto>createResponse(
                request,
                PlanTemplateDto.class,
                PlanTemplateActionDto.class,
                (planTemplateDto) -> planTemplateService.createOrUpdatePlanTemplate(
                        planTemplateDto,
                        getStringFromParam(
                                request,
                                RequestQueryParam.QP_STAFF_PASS
                        )
                ));
    }

    @ResponseStatus
    public Mono<ServerResponse> getPlanTemplateByName(ServerRequest request) {

        return this.<PlanTemplateDto>createResponse(
                request,
                PlanTemplateDto.class,
                () -> planTemplateService.getActivePlanTemplateByName(
                        getStringFromParam(
                                request,
                                RequestQueryParam.QP_PLAN_TEMPLATE_NAME
                        )
                ));
    }

    public Mono<ServerResponse> getAllPlanTemplates(ServerRequest request) {

        return this.<PlanTemplateListDto>createResponse(
                request,
                PlanTemplateListDto.class,
                planTemplateService::getAllActivePlanTemplates);
    }

    @ResponseStatus
    public Mono<ServerResponse> invalidatePlanTemplate(ServerRequest request) {

        return this.<PlanTemplateDto>createResponse(
                request,
                PlanTemplateDto.class,
                () -> planTemplateService.invalidatePlanTemplate(
                        getIntegerFromParam(
                                request,
                                RequestQueryParam.QP_PLAN_TEMPLATE_ID
                        ),
                        getStringFromParam(
                                request,
                                RequestQueryParam.QP_STAFF_PASS
                        )
                ));
    }
}
