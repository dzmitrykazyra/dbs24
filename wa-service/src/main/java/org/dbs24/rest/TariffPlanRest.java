package org.dbs24.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.component.RefsService;
import org.dbs24.component.TariffPlansService;
import org.dbs24.rest.api.AllTariffPlans;
import org.dbs24.rest.api.CreatedTariffPlan;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.dbs24.rest.api.TariffPlanInfo;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.dbs24.consts.SysConst.*;
import static org.dbs24.consts.WaConsts.RestQueryParams.QP_DEVICE_TYPE;
import static org.dbs24.consts.WaConsts.RestQueryParams.QP_TARIFF_PLAN_STATUS;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
public class TariffPlanRest extends ReactiveRestProcessor {

    final RefsService refsService;
    final TariffPlansService tariffPlansService;

    public TariffPlanRest(RefsService refsService, TariffPlansService tariffPlansService) {
        this.refsService = refsService;
        this.tariffPlansService = tariffPlansService;
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdateTariffPlan(ServerRequest request) {

        return this.<TariffPlanInfo, CreatedTariffPlan>createResponse(request, TariffPlanInfo.class, CreatedTariffPlan.class,
                tariffPlanInfo -> tariffPlansService.createOrUpdateTariffPlan(tariffPlanInfo));
    }

    public Mono<ServerResponse> getTariffPlans(ServerRequest request) {

        return this.<AllTariffPlans>createResponse(request, AllTariffPlans.class, () -> {

            final Integer deviceTypeId = getOptionalIntegerFromParam(request, QP_DEVICE_TYPE);
            final Boolean allDevices = Optional.ofNullable(deviceTypeId).map(status -> BOOLEAN_FALSE).orElse(BOOLEAN_TRUE);

            final Integer tariffStatusId = getOptionalIntegerFromParam(request, QP_TARIFF_PLAN_STATUS);
            final Boolean allPlanStatus = Optional.ofNullable(tariffStatusId).map(status -> BOOLEAN_FALSE).orElse(BOOLEAN_TRUE);

            return tariffPlansService.getTariffPlans(
                    allDevices, StmtProcessor.nvl(deviceTypeId, INTEGER_ZERO),
                    allPlanStatus, StmtProcessor.nvl(tariffStatusId, INTEGER_ZERO));
        });
    }
}
