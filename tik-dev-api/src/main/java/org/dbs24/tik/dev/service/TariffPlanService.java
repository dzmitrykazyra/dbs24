/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.service;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.dbs24.tik.dev.dao.TariffPlanDao;
import org.dbs24.tik.dev.entity.TariffPlan;
import org.dbs24.tik.dev.entity.TariffPlanHist;
import org.dbs24.tik.dev.rest.dto.tariff.plan.CreateTariffPlanRequest;
import org.dbs24.tik.dev.rest.dto.tariff.plan.CreatedTariffPlan;
import org.dbs24.tik.dev.rest.dto.tariff.plan.CreatedTariffPlanResponse;
import org.dbs24.tik.dev.rest.dto.tariff.plan.TariffPlanInfo;
import org.dbs24.tik.dev.rest.dto.tariff.plan.validator.TariffPlanInfoValidator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.dbs24.application.core.locale.NLS.long2LocalDateTime;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_INVALID_ENTITY_ATTRS;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;
import static org.dbs24.stmt.StmtProcessor.*;

@Getter
@Log4j2
@Component
@EqualsAndHashCode
public class TariffPlanService extends AbstractRestApplicationService {

    final TariffPlanDao tariffPlanDao;
    final RefsService refsService;
    final DevelopersService developersService;
    final TariffLimitService tariffLimitService;
    final TariffPlanInfoValidator tariffPlanInfoValidator;

    public TariffPlanService(RefsService refsService, TariffPlanDao tariffPlanDao, TariffPlanInfoValidator tariffPlanInfoValidator, DevelopersService developersService, TariffLimitService tariffLimitService) {

        this.refsService = refsService;
        this.tariffPlanDao = tariffPlanDao;
        this.tariffPlanInfoValidator = tariffPlanInfoValidator;
        this.developersService = developersService;
        this.tariffLimitService = tariffLimitService;
    }

    @FunctionalInterface
    interface TariffPlansHistBuilder {
        void buildTariffPlansHist(TariffPlan tariffPlan);
    }

    final Supplier<TariffPlan> createNewTariffPlan = () -> create(TariffPlan.class, tariffPlan -> tariffPlan.setActualDate(now()));


    final BiFunction<TariffPlanInfo, TariffPlan, TariffPlan> assignDto = (tariffPlanInfo, tariffPlan) -> {

        tariffPlan.setActualDate(long2LocalDateTime(tariffPlanInfo.getActualDate()));
        ifNull(tariffPlan.getActualDate(), () -> tariffPlan.setActualDate(now()));

        tariffPlan.setTariffPlanType(getRefsService().getReferencesDao().findTariffPlanType(tariffPlanInfo.getTariffPlanTypeId()));
        tariffPlan.setTariffPlanStatus(getRefsService().getReferencesDao().findTariffPlanStatus(tariffPlanInfo.getTariffPlanStatusId()));
        tariffPlan.setTariffLimit(getTariffLimitService().findTariffLimit(tariffPlanInfo.getTariffLimitId()));
        tariffPlan.setTpName(tariffPlanInfo.getTpName());
        tariffPlan.setTpNote(tariffPlanInfo.getTpNote());

        return tariffPlan;
    };

    final BiFunction<TariffPlanInfo, TariffPlansHistBuilder, TariffPlan> assignTariffPlansInfo = (tariffPlanInfo, tariffPlansHistBuilder) -> {

        final TariffPlan tariffPlan = ofNullable(tariffPlanInfo.getTariffPlanId())
                .map(this::findTariffPlan)
                .orElseGet(createNewTariffPlan);

        // store history
        ofNullable(tariffPlan.getTariffPlanId()).ifPresent(borId -> tariffPlansHistBuilder.buildTariffPlansHist(tariffPlan));

        assignDto.apply(tariffPlanInfo, tariffPlan);

        return tariffPlan;
    };

    //==========================================================================
    @Transactional
    public CreatedTariffPlanResponse createOrUpdateTariffPlan(Mono<CreateTariffPlanRequest> monoRequest) {

        return this.<CreatedTariffPlan, CreatedTariffPlanResponse>createAnswer(CreatedTariffPlanResponse.class,
                (responseBody, createdTariffPlan) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(tariffPlanInfoValidator.validateConditional(request.getEntityInfo(), tariffPlanInfo
                        -> {

                    final SimpleActionInfo simpleActionInfo = request.getEntityAction();

                    //log.info("simpleActionInfo =  {}", simpleActionInfo);

                    log.debug("create/update tariffPlan: {}", tariffPlanInfo);

                    //StmtProcessor.assertNotNull(String.class, tariffPlanInfo.getPackageName(), "packageName name is not defined");

                    final TariffPlan tariffPlan = findOrCreateTariffPlans(tariffPlanInfo, tariffPlanHist -> saveTariffPlanHist(createTariffPlanHist(tariffPlanHist)));

                    final Boolean isNewSetting = isNull(tariffPlan.getTariffPlanId());

                    getTariffPlanDao().saveTariffPlan(tariffPlan);

                    final String finalMessage = String.format("TariffPlan is %s (TariffPlanId=%d)",
                            isNewSetting ? "created" : "updated",
                            tariffPlan.getTariffPlanId());

                    createdTariffPlan.setCreatedTariffPlanId(tariffPlan.getTariffPlanId());
                    responseBody.complete();

                    log.debug(finalMessage);

                    responseBody.setCode(OC_OK);
                    responseBody.setMessage(finalMessage);
                }, errorInfos -> {
                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                }))));
    }

    public TariffPlan findOrCreateTariffPlans(TariffPlanInfo tariffPlanInfo, TariffPlanService.TariffPlansHistBuilder tariffPlansHistBuilder) {
        return assignTariffPlansInfo.apply(tariffPlanInfo, tariffPlansHistBuilder);
    }

    private TariffPlanHist createTariffPlanHist(TariffPlan tariffPlan) {
        return create(TariffPlanHist.class, tariffPlanHist -> {
            tariffPlanHist.setTariffPlanId(tariffPlan.getTariffPlanId());
            tariffPlanHist.setActualDate(tariffPlan.getActualDate());
            tariffPlanHist.setTariffPlanType(tariffPlan.getTariffPlanType());
            tariffPlanHist.setTariffLimit(tariffPlan.getTariffLimit());
            tariffPlanHist.setTariffPlanStatus(tariffPlan.getTariffPlanStatus());
            tariffPlanHist.setTpName(tariffPlan.getTpName());
            tariffPlanHist.setTpNote(tariffPlan.getTpNote());
        });
    }

    private void saveTariffPlanHist(TariffPlanHist tariffPlanHist) {
        getTariffPlanDao().saveTariffPlanHist(tariffPlanHist);
    }

    public TariffPlan findTariffPlan(Long tariffPlanId) {
        return getTariffPlanDao().findTariffPlan(tariffPlanId);
    }
}
