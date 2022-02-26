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
import org.dbs24.tik.dev.dao.TariffLimitDao;
import org.dbs24.tik.dev.entity.TariffLimit;
import org.dbs24.tik.dev.entity.TariffLimitHist;
import org.dbs24.tik.dev.rest.dto.tariff.limit.CreateTariffLimitRequest;
import org.dbs24.tik.dev.rest.dto.tariff.limit.CreatedTariffLimit;
import org.dbs24.tik.dev.rest.dto.tariff.limit.CreatedTariffLimitResponse;
import org.dbs24.tik.dev.rest.dto.tariff.limit.TariffLimitInfo;
import org.dbs24.tik.dev.rest.dto.tariff.limit.validator.TariffLimitInfoValidator;
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
public class TariffLimitService extends AbstractRestApplicationService {

    final TariffLimitDao tariffLimitDao;
    final RefsService refsService;
    final DevelopersService developersService;
    final TariffLimitInfoValidator tariffLimitInfoValidator;

    public TariffLimitService(RefsService refsService, TariffLimitDao tariffLimitDao, TariffLimitInfoValidator tariffLimitInfoValidator, DevelopersService developersService) {

        this.refsService = refsService;
        this.tariffLimitDao = tariffLimitDao;
        this.tariffLimitInfoValidator = tariffLimitInfoValidator;
        this.developersService = developersService;
    }

    @FunctionalInterface
    interface TariffLimitsHistBuilder {
        void buildTariffLimitsHist(TariffLimit tariffLimit);
    }

    final Supplier<TariffLimit> createNewTariffLimit = () -> create(TariffLimit.class, tariffLimit -> {
        tariffLimit.setActualDate(now());
    });


    final BiFunction<TariffLimitInfo, TariffLimit, TariffLimit> assignDto = (tariffLimitInfo, tariffLimit) -> {

        tariffLimit.setActualDate(long2LocalDateTime(tariffLimitInfo.getActualDate()));
        ifNull(tariffLimit.getActualDate(), () -> tariffLimit.setActualDate(now()));

        tariffLimit.setBandwidthMbLimit(tariffLimitInfo.getBandwidthMbLimit());
        tariffLimit.setDailyEndpointsLimit(tariffLimitInfo.getDailyEndpointsLimit());
        tariffLimit.setUsePremiumPoints(tariffLimitInfo.getUsePremiumPoints());
        tariffLimit.setOauthUsersLimit(tariffLimitInfo.getOauthUsersLimit());

        return tariffLimit;
    };

    final BiFunction<TariffLimitInfo, TariffLimitsHistBuilder, TariffLimit> assignTariffLimitsInfo = (tariffLimitInfo, tariffLimitsHistBuilder) -> {

        final TariffLimit tariffLimit = ofNullable(tariffLimitInfo.getTariffLimitId())
                .map(this::findTariffLimit)
                .orElseGet(createNewTariffLimit);

        // store history
        ofNullable(tariffLimit.getTariffLimitId()).ifPresent(borId -> tariffLimitsHistBuilder.buildTariffLimitsHist(tariffLimit));

        assignDto.apply(tariffLimitInfo, tariffLimit);

        return tariffLimit;
    };

    //==========================================================================
    @Transactional
    public CreatedTariffLimitResponse createOrUpdateTariffLimit(Mono<CreateTariffLimitRequest> monoRequest) {

        return this.<CreatedTariffLimit, CreatedTariffLimitResponse>createAnswer(CreatedTariffLimitResponse.class,
                (responseBody, createdTariffLimit) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(tariffLimitInfoValidator.validateConditional(request.getEntityInfo(), tariffLimitInfo
                        -> {

                    final SimpleActionInfo simpleActionInfo = request.getEntityAction();

                    //log.info("simpleActionInfo =  {}", simpleActionInfo);

                    log.debug("create/update tariffLimit: {}", tariffLimitInfo);

                    //StmtProcessor.assertNotNull(String.class, tariffLimitInfo.getPackageName(), "packageName name is not defined");

                    final TariffLimit tariffLimit = findOrCreateTariffLimits(tariffLimitInfo, tariffLimitHist -> saveTariffLimitHist(createTariffLimitHist(tariffLimitHist)));

                    final Boolean isNewSetting = isNull(tariffLimit.getTariffLimitId());

                    getTariffLimitDao().saveTariffLimit(tariffLimit);

                    final String finalMessage = String.format("TariffLimit is %s (TariffLimitId=%d)",
                            isNewSetting ? "created" : "updated",
                            tariffLimit.getTariffLimitId());

                    createdTariffLimit.setCreatedLimitId(tariffLimit.getTariffLimitId());
                    responseBody.complete();

                    log.debug(finalMessage);

                    responseBody.setCode(OC_OK);
                    responseBody.setMessage(finalMessage);
                }, errorInfos -> {
                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                }))));
    }

    public TariffLimit findOrCreateTariffLimits(TariffLimitInfo tariffLimitInfo, TariffLimitService.TariffLimitsHistBuilder tariffLimitsHistBuilder) {
        return assignTariffLimitsInfo.apply(tariffLimitInfo, tariffLimitsHistBuilder);
    }

    private TariffLimitHist createTariffLimitHist(TariffLimit tariffLimit) {
        return create(TariffLimitHist.class, tariffLimitHist -> {
            tariffLimitHist.setTariffLimitId(tariffLimit.getTariffLimitId());
            tariffLimitHist.setActualDate(tariffLimit.getActualDate());
            tariffLimitHist.setBandwidthMbLimit(tariffLimit.getBandwidthMbLimit());
            tariffLimitHist.setOauthUsersLimit(tariffLimit.getOauthUsersLimit());
            tariffLimitHist.setUsePremiumPoints(tariffLimit.getUsePremiumPoints());
            tariffLimitHist.setDailyEndpointsLimit(tariffLimit.getDailyEndpointsLimit());
        });
    }

    private void saveTariffLimitHist(TariffLimitHist tariffLimitHist) {
        getTariffLimitDao().saveTariffLimitHist(tariffLimitHist);
    }

    public TariffLimit findTariffLimit(Long tariffLimitId) {
        return getTariffLimitDao().findTariffLimit(tariffLimitId);
    }
}
