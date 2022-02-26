/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.ad.server.config;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.ad.server.entity.dto.AdsSettingsDto;
import org.dbs24.ad.server.entity.dto.CreatedAdsSettings;
import org.dbs24.ad.server.rest.AdsSettingsRest;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.config.AbstractWebSecurityConfig;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Arrays;
import java.util.Collection;

import static org.dbs24.ad.server.consts.AdConsts.RestQueryParams.QP_ACTUAL_DATE;
import static org.dbs24.ad.server.consts.AdConsts.RestQueryParams.QP_PACKAGE_NAME;
import static org.dbs24.ad.server.consts.AdConsts.Uri.URI_CREATE_OR_UPDATE_ADS_SETTINGS;
import static org.dbs24.ad.server.consts.AdConsts.Uri.URI_GET_ADS_SETTINGS;
import static org.dbs24.ad.server.wa.WaAdConsts.*;
import static org.dbs24.consts.RestHttpConsts.HTTP_200_STRING;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Log4j2
@Configuration
@EnableCaching
@EqualsAndHashCode(callSuper = true)
public class WaAdRestConfig extends AbstractWebSecurityConfig {

    final AdsSettingsRest adsSettingsRest;

    @Override
    public String getAppUriPrefix() {
        return URI_WA_CONST;
    }

    public WaAdRestConfig(AdsSettingsRest adsSettingsRest) {
        this.adsSettingsRest = adsSettingsRest;
    }

    @RouterOperations({
            @RouterOperation(path = URI_WA_CREATE_OR_UPDATE_ADS_SETTINGS, method = POST, operation = @Operation(tags = APP_TAG_NAME, operationId = URI_WA_CREATE_OR_UPDATE_ADS_SETTINGS, requestBody = @RequestBody(description = "message details", content = @Content(schema = @Schema(implementation = AdsSettingsDto.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create/update adSettings", content = @Content(schema = @Schema(implementation = CreatedAdsSettings.class))))),
            @RouterOperation(path = URI_WA_GET_ADS_SETTINGS, method = GET, operation = @Operation(tags = APP_TAG_NAME, operationId = URI_WA_GET_ADS_SETTINGS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "get messages", content = @Content(schema = @Schema(implementation = AdsSettingsDto.class))), parameters = {
                    @Parameter(name = QP_ACTUAL_DATE, in = ParameterIn.QUERY, schema = @Schema(type = "actual date"), description = "ad settings actual date", example = "1234567890"),
                    @Parameter(name = QP_PACKAGE_NAME, in = ParameterIn.QUERY, schema = @Schema(type = "package name"), description = "ad packages settings", example = "com.onlinetracker.whatschat")})),
    })
    @Bean
    public RouterFunction<ServerResponse> routerWaNotifierRest() {
        return addCommonRoutes()
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_ADS_SETTINGS), adsSettingsRest::createOrUpdateAdsSettings)
                .andRoute(getRoute(URI_GET_ADS_SETTINGS), adsSettingsRest::getAdsSettings);
    }

    @Override
    protected String[] addWhiteListAuthUrl(String[] existsUrl) {

        final Collection<String> whiteUrls = ServiceFuncs.createCollection();

        whiteUrls.addAll(Arrays.asList(existsUrl));
        whiteUrls.add(URI_GET_ADS_SETTINGS);

        return whiteUrls.toArray(new String[]{});
    }
}
