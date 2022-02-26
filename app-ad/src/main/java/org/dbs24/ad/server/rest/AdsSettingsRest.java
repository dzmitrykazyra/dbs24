/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.ad.server.rest;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.ad.server.component.AdsSettingsService;
import org.dbs24.ad.server.entity.dto.AdsSettingsDto;
import org.dbs24.ad.server.entity.dto.CreatedAdsSettings;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.dbs24.ad.server.consts.AdConsts.RestQueryParams.QP_ACTUAL_DATE;
import static org.dbs24.ad.server.consts.AdConsts.RestQueryParams.QP_PACKAGE_NAME;

@Log4j2
@Component
@EqualsAndHashCode(callSuper = true)
public class AdsSettingsRest extends ReactiveRestProcessor {

    final AdsSettingsService adsSettingsService;

    public AdsSettingsRest(AdsSettingsService adsSettingsService) {
        this.adsSettingsService = adsSettingsService;
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdateAdsSettings(ServerRequest request) {

        return processServerRequest(
                request,
                AdsSettingsDto.class,
                CreatedAdsSettings.class,
                adsSettingsService::createOrUpdateAdsSettings);
    }

    //==========================================================================
    public Mono<ServerResponse> getAdsSettings(ServerRequest request) {

        final var actualDate = getOptionalLongFromParam(request, QP_ACTUAL_DATE);
        final var appPackage = getOptionalStringFromParam(request, QP_PACKAGE_NAME);

        return createResponse(request, AdsSettingsDto.class, () -> adsSettingsService.getAdsSettings(actualDate, appPackage));

    }
}
