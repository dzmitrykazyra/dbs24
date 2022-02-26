/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.dbs24.config.AbstractWebSecurityConfig;
import org.dbs24.tik.dev.rest.TikDevApiRest;
import org.dbs24.tik.dev.rest.dto.contract.CreateContractRequest;
import org.dbs24.tik.dev.rest.dto.contract.CreatedContractResponse;
import org.dbs24.tik.dev.rest.dto.developer.CreateDeveloperRequest;
import org.dbs24.tik.dev.rest.dto.developer.CreatedDeveloperResponse;
import org.dbs24.tik.dev.rest.dto.device.CreateDeviceRequest;
import org.dbs24.tik.dev.rest.dto.device.CreatedDeviceResponse;
import org.dbs24.tik.dev.rest.dto.endpoint.CreateEndpointActionRequest;
import org.dbs24.tik.dev.rest.dto.endpoint.CreatedEndpointActionResponse;
import org.dbs24.tik.dev.rest.dto.tariff.limit.CreateTariffLimitRequest;
import org.dbs24.tik.dev.rest.dto.tariff.limit.CreatedTariffLimitResponse;
import org.dbs24.tik.dev.rest.dto.tariff.plan.CreateTariffPlanRequest;
import org.dbs24.tik.dev.rest.dto.tariff.plan.CreatedTariffPlanResponse;
import org.dbs24.tik.dev.rest.dto.tariff.price.CreateTariffPlanPriceRequest;
import org.dbs24.tik.dev.rest.dto.tik.account.CreateTikAccountRequest;
import org.dbs24.tik.dev.rest.dto.tik.account.CreatedTikAccountResponse;
import org.dbs24.tik.dev.rest.dto.tik.account.scope.CreateTikAccountScopeRequest;
import org.dbs24.tik.dev.rest.dto.tik.account.scope.CreatedTikAccountScopeResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.dbs24.consts.RestHttpConsts.HTTP_200_STRING;
import static org.dbs24.tik.dev.consts.TikDevApiConsts.UriConsts.*;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Log4j2
@Configuration
@EnableCaching
public class TikDevRestConfig extends AbstractWebSecurityConfig {

    @RouterOperations({
            @RouterOperation(path = URI_CREATE_OR_UPDATE_DEVELOPER, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_DEVELOPER, requestBody = @RequestBody(description = "Developer details", content = @Content(schema = @Schema(implementation = CreateDeveloperRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create/update developer", content = @Content(schema = @Schema(implementation = CreatedDeveloperResponse.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_TIK_ACCOUNT, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_TIK_ACCOUNT, requestBody = @RequestBody(description = "Account details", content = @Content(schema = @Schema(implementation = CreateTikAccountRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create/update tik account", content = @Content(schema = @Schema(implementation = CreatedTikAccountResponse.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_TIK_ACCOUNT_SCOPE, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_TIK_ACCOUNT_SCOPE, requestBody = @RequestBody(description = "Account Scope details", content = @Content(schema = @Schema(implementation = CreateTikAccountScopeRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create/update tik account scope", content = @Content(schema = @Schema(implementation = CreatedTikAccountScopeResponse.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_TARIFF_LIMIT, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_TARIFF_LIMIT, requestBody = @RequestBody(description = "Tariff limit details", content = @Content(schema = @Schema(implementation = CreateTariffLimitRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create/update tariff limit", content = @Content(schema = @Schema(implementation = CreatedTariffLimitResponse.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_TARIFF_PLAN, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_TARIFF_PLAN, requestBody = @RequestBody(description = "Tariff plan details", content = @Content(schema = @Schema(implementation = CreateTariffPlanRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create/update tariff plan", content = @Content(schema = @Schema(implementation = CreatedTariffPlanResponse.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_CONTRACT, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_CONTRACT, requestBody = @RequestBody(description = "Contract details", content = @Content(schema = @Schema(implementation = CreateContractRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create/update contract", content = @Content(schema = @Schema(implementation = CreatedContractResponse.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_TARIFF_PLAN_PRICE, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_TARIFF_PLAN_PRICE, requestBody = @RequestBody(description = "Tariff price details", content = @Content(schema = @Schema(implementation = CreateTariffPlanPriceRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create/update tariff price", content = @Content(schema = @Schema(implementation = CreatedTariffPlanResponse.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_DEVICE, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_DEVICE, requestBody = @RequestBody(description = "Device details", content = @Content(schema = @Schema(implementation = CreateDeviceRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create/update device", content = @Content(schema = @Schema(implementation = CreatedDeviceResponse.class))))),
            @RouterOperation(path = URI_CREATE_ENDPOINT_ACTION, method = POST, operation = @Operation(operationId = URI_CREATE_ENDPOINT_ACTION, requestBody = @RequestBody(description = "Enpoint action details", content = @Content(schema = @Schema(implementation = CreateEndpointActionRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create endpoint action", content = @Content(schema = @Schema(implementation = CreatedEndpointActionResponse.class)))))
    })
    @Bean
    public RouterFunction<ServerResponse> routerTikDevApiRest(TikDevApiRest tikDevApiRest) {

        return addCommonRoutes()
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_DEVELOPER), tikDevApiRest::createOrUpdateDeveloper)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_TIK_ACCOUNT), tikDevApiRest::createOrUpdateTikAccount)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_TIK_ACCOUNT_SCOPE), tikDevApiRest::createOrUpdateTikAccountScope)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_TARIFF_LIMIT), tikDevApiRest::createOrUpdateTariffLimit)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_TARIFF_PLAN), tikDevApiRest::createOrUpdateTariffPlan)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_CONTRACT), tikDevApiRest::createOrUpdateContract)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_TARIFF_PLAN_PRICE), tikDevApiRest::createOrUpdateTariffPlanPrice)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_DEVICE), tikDevApiRest::createOrUpdateDevice)
                .andRoute(postRoute(URI_CREATE_ENDPOINT_ACTION), tikDevApiRest::createEndpointAction);
    }
}
