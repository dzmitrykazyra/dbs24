/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.component.AppSettingsService;
import org.dbs24.entity.dto.AppSettingsDto;
import org.dbs24.rest.api.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.dbs24.consts.WaConsts.RestQueryParams.QP_DEVICE_TYPE;
import static org.dbs24.consts.WaConsts.RestQueryParams.QP_PACKAGE_NAME;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AppSettingsRest extends ReactiveRestProcessor {

    final AppSettingsService appSettingsService;

    public AppSettingsRest(AppSettingsService appSettingsService) {
        this.appSettingsService = appSettingsService;
    }

    //==========================================================================
    public Mono<ServerResponse> getSettings(ServerRequest request) {

        return this.processServerRequest(request, AppSettingsInfo.class, appSettingsService::getSettings);
    }

    //==========================================================================
    @Deprecated
    public Mono<ServerResponse> createOrUpdatePackageDetails(ServerRequest request) {

        return this.<PackageDetailsInfo, CreatedPackageDetails>createResponse(request, PackageDetailsInfo.class, CreatedPackageDetails.class,
                appSettingsService::createOrUpdatePackageDetails);
    }

    //==========================================================================
    public Mono<ServerResponse> getAllPackageDetails(ServerRequest request) {

        return this.<AllPackageDetailsInfo>createResponse(request, AllPackageDetailsInfo.class, appSettingsService::getAllPackageDetails);

    }

    //==========================================================================
    public Mono<ServerResponse> getLicenseAgreement(ServerRequest request) {

        final String pkgName = getStringFromParam(request, QP_PACKAGE_NAME);
        final Integer deviceTypeId = getOptionalIntegerFromParam(request, QP_DEVICE_TYPE);

        return this.<LicenseAgreementInfo>createResponse(request, LicenseAgreementInfo.class, () -> appSettingsService.getLicenseAgreementInfo(pkgName, deviceTypeId));
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdateAppSettings(ServerRequest request) {

        return this.<AppSettingsDto, CreatedAppSetting>createResponse(request, AppSettingsDto.class, CreatedAppSetting.class,
                appSettingsService::createOrUpdateAppSettings);
    }
}
