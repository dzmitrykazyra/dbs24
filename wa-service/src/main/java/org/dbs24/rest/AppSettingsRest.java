/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.component.AppSettingsService;
import org.dbs24.entity.dto.AppSettingsDto;
import org.dbs24.rest.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.dbs24.consts.WaConsts.RestQueryParams.QP_DEVICE_TYPE;
import static org.dbs24.consts.WaConsts.RestQueryParams.QP_PACKAGE_NAME;
import static org.dbs24.stmt.StmtProcessor.create;
import static org.dbs24.stmt.StmtProcessor.ifNull;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
public class AppSettingsRest extends ReactiveRestProcessor {

    @Value("${config.settings.contact-telegram:watracker_support}")
    private String contactTelegram;

    @Value("${config.settings.secondary-payment-app-packageName:com.tinyapp.smartpurchase}")
    private String secondaryPaymentAppPackageName;

    @Value("${config.settings.should-download-proxy:false}")
    private Boolean shouldDownloadProxy;

    @Value("${config.settings.should-download-ads:false}")
    private Boolean shouldDownloadAds;

    @Value("${config.settings.proxy-actual-version:dev}")
    private String proxyActualVersion;

    //--------------------------------------------------------------------------
    final AppSettingsService appSettingsService;

    public AppSettingsRest(AppSettingsService appSettingsService) {
        this.appSettingsService = appSettingsService;
    }

    //==========================================================================
    public Mono<ServerResponse> getSettings(ServerRequest request) {

        return this.<AppSettingsInfo>createResponse(request, AppSettingsInfo.class,
                () -> create(AppSettingsInfo.class, appSettingsInfo -> {

                    final String packageName = getOptionalNullStringFromParam(request, QP_PACKAGE_NAME);

                    log.debug("getting app settings for package '{}'", packageName);

                    ifNull(packageName, () -> appSettingsInfo.setPrimaryPaymentAppPackageName("package variable not defined"), () ->

                            appSettingsService.findAppSettings(packageName).ifPresentOrElse(appSettings -> {

                                appSettingsInfo.setContactEmail(appSettings.getEmail());
                                appSettingsInfo.setContactTelegram(contactTelegram);
                                appSettingsInfo.setContactWhatsApp(appSettings.getWhatsappId());
                                appSettingsInfo.setRequiredAppVersion(appSettings.getMinVersion());
                                appSettingsInfo.setRequiredVersionCode(appSettings.getMinVersionCode());
                                appSettingsInfo.setServerTimestampMillis(NLS.localDateTime2long(LocalDateTime.now()));
                                appSettingsInfo.setPrimaryPaymentAppPackageName(appSettings.getPackageName());
                                appSettingsInfo.setSecondaryPaymentAppPackageName(secondaryPaymentAppPackageName);
                                appSettingsInfo.setShouldDownloadProxy(shouldDownloadProxy);
                                appSettingsInfo.setShouldDownloadAds(shouldDownloadAds);
                                appSettingsInfo.setProxyActualVersion(proxyActualVersion);
                                appSettingsInfo.setCompanyName(appSettings.getCompanyName());
                                appSettingsInfo.setAppName(appSettings.getAppName());
                                appSettingsInfo.setSiteUrl(appSettings.getSiteUrl());
                                appSettingsInfo.setNote(appSettings.getNote());
                            }, () -> appSettingsInfo.setPrimaryPaymentAppPackageName(String.format("Unknown package - '%s'", packageName))));

                    log.debug("return settings: {}", appSettingsInfo);

                }));
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

        return this.<AppSettingsDto, CreatedAppSetting>createResponse(request, AppSettingsDto.class, CreatedAppSetting.class, appSettingsService::createOrUpdateAppSettings);
    }
}
