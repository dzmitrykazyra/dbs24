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
import org.dbs24.tik.dev.dao.TariffPlanPriceDao;
import org.dbs24.tik.dev.entity.TariffPlanPrice;
import org.dbs24.tik.dev.entity.TariffPlanPriceHist;
import org.dbs24.tik.dev.rest.dto.tariff.price.CreateTariffPlanPriceRequest;
import org.dbs24.tik.dev.rest.dto.tariff.price.CreatedTariffPlanPrice;
import org.dbs24.tik.dev.rest.dto.tariff.price.CreatedTariffPlanPriceResponse;
import org.dbs24.tik.dev.rest.dto.tariff.price.TariffPlanPriceInfo;
import org.dbs24.tik.dev.rest.dto.tariff.price.validator.TariffPlanPriceInfoValidator;
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
public class TariffPlanPriceService extends AbstractRestApplicationService {

    final TariffPlanPriceDao tariffPlanPriceDao;
    final RefsService refsService;
    final DevelopersService developersService;
    final TariffLimitService tariffLimitService;
    final TariffPlanPriceInfoValidator tariffPlanPriceInfoValidator;

    public TariffPlanPriceService(RefsService refsService, TariffPlanPriceDao tariffPlanPriceDao, TariffPlanPriceInfoValidator tariffPlanPriceInfoValidator, DevelopersService developersService, TariffLimitService tariffLimitService) {

        this.refsService = refsService;
        this.tariffPlanPriceDao = tariffPlanPriceDao;
        this.tariffPlanPriceInfoValidator = tariffPlanPriceInfoValidator;
        this.developersService = developersService;
        this.tariffLimitService = tariffLimitService;
    }

    @FunctionalInterface
    interface TariffPlanPricesHistBuilder {
        void buildTariffPlanPricesHist(TariffPlanPrice tariffPlanPrice);
    }

    final Supplier<TariffPlanPrice> createNewTariffPlanPrice = () -> create(TariffPlanPrice.class, tariffPlanPrice -> tariffPlanPrice.setActualDate(now()));


    final BiFunction<TariffPlanPriceInfo, TariffPlanPrice, TariffPlanPrice> assignDto = (tariffPlanPriceInfo, tariffPlanPrice) -> {

        tariffPlanPrice.setActualDate(long2LocalDateTime(tariffPlanPriceInfo.getActualDate()));
        ifNull(tariffPlanPrice.getActualDate(), () -> tariffPlanPrice.setActualDate(now()));
        tariffPlanPrice.setTariffPlanType(getRefsService().findTariffPlanType(tariffPlanPriceInfo.getTariffPlanTypeId()));
        tariffPlanPrice.setTariffBeginDate(long2LocalDateTime(tariffPlanPriceInfo.getTariffBeginDate()));
        tariffPlanPrice.setCountryCode(tariffPlanPriceInfo.getCountryCode());
        tariffPlanPrice.setCurrencyIso(tariffPlanPriceInfo.getCurrencyIso());
        tariffPlanPrice.setSumm(tariffPlanPriceInfo.getSumm());

        return tariffPlanPrice;
    };

    final BiFunction<TariffPlanPriceInfo, TariffPlanPricesHistBuilder, TariffPlanPrice> assignTariffPlanPricesInfo = (tariffPlanPriceInfo, tariffPlanPricesHistBuilder) -> {

        final TariffPlanPrice tariffPlanPrice = ofNullable(tariffPlanPriceInfo.getTariffPriceId())
                .map(this::findTariffPlanPrice)
                .orElseGet(createNewTariffPlanPrice);

        // store history
        ofNullable(tariffPlanPrice.getTariffPriceId()).ifPresent(borId -> tariffPlanPricesHistBuilder.buildTariffPlanPricesHist(tariffPlanPrice));

        assignDto.apply(tariffPlanPriceInfo, tariffPlanPrice);

        return tariffPlanPrice;
    };

    //==========================================================================
    @Transactional
    public CreatedTariffPlanPriceResponse createOrUpdateTariffPlanPrice(Mono<CreateTariffPlanPriceRequest> monoRequest) {

        return this.<CreatedTariffPlanPrice, CreatedTariffPlanPriceResponse>createAnswer(CreatedTariffPlanPriceResponse.class,
                (responseBody, createdTariffPlanPrice) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(tariffPlanPriceInfoValidator.validateConditional(request.getEntityInfo(), tariffPlanPriceInfo
                        -> {

                    final SimpleActionInfo simpleActionInfo = request.getEntityAction();

                    //log.info("simpleActionInfo =  {}", simpleActionInfo);

                    log.debug("create/update tariffPlanPrice: {}", tariffPlanPriceInfo);

                    //StmtProcessor.assertNotNull(String.class, tariffPlanPriceInfo.getPackageName(), "packageName name is not defined");

                    final TariffPlanPrice tariffPlanPrice = findOrCreateTariffPlanPrices(tariffPlanPriceInfo, tariffPlanPriceHist -> saveTariffPlanPriceHist(createTariffPlanPriceHist(tariffPlanPriceHist)));

                    final Boolean isNewSetting = isNull(tariffPlanPrice.getTariffPriceId());

                    getTariffPlanPriceDao().saveTariffPlanPrice(tariffPlanPrice);

                    final String finalMessage = String.format("TariffPlanPrice is %s (TariffPlanPriceId=%d)",
                            isNewSetting ? "created" : "updated",
                            tariffPlanPrice.getTariffPriceId());

                    createdTariffPlanPrice.setCreatedTariffPlanPriceId(tariffPlanPrice.getTariffPriceId());
                    responseBody.complete();

                    log.debug(finalMessage);

                    responseBody.setCode(OC_OK);
                    responseBody.setMessage(finalMessage);
                }, errorInfos -> {
                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                }))));
    }

    public TariffPlanPrice findOrCreateTariffPlanPrices(TariffPlanPriceInfo tariffPlanPriceInfo, TariffPlanPriceService.TariffPlanPricesHistBuilder tariffPlanPricesHistBuilder) {
        return assignTariffPlanPricesInfo.apply(tariffPlanPriceInfo, tariffPlanPricesHistBuilder);
    }

    private TariffPlanPriceHist createTariffPlanPriceHist(TariffPlanPrice tariffPlanPrice) {
        return create(TariffPlanPriceHist.class, tariffPlanPriceHist -> {
            tariffPlanPriceHist.setTariffPriceId(tariffPlanPrice.getTariffPriceId());
            tariffPlanPriceHist.setActualDate(tariffPlanPrice.getActualDate());
            tariffPlanPriceHist.setTariffPlanType(tariffPlanPrice.getTariffPlanType());
            tariffPlanPriceHist.setTariffBeginDate(tariffPlanPrice.getTariffBeginDate());
            tariffPlanPriceHist.setCountryCode(tariffPlanPrice.getCountryCode());
            tariffPlanPriceHist.setCurrencyIso(tariffPlanPrice.getCurrencyIso());
            tariffPlanPriceHist.setSumm(tariffPlanPrice.getSumm());
        });
    }

    private void saveTariffPlanPriceHist(TariffPlanPriceHist tariffPlanPriceHist) {
        getTariffPlanPriceDao().saveTariffPlanPriceHist(tariffPlanPriceHist);
    }

    public TariffPlanPrice findTariffPlanPrice(Long tariffPlanPriceId) {
        return getTariffPlanPriceDao().findTariffPlanPrice(tariffPlanPriceId);
    }
}
