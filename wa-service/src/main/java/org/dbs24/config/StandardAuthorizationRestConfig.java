/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.component.AgentsService;
import org.dbs24.component.RefsService;
import org.dbs24.entity.dto.AgentPayloadInfo;
import org.dbs24.entity.dto.AppSettingsDto;
import org.dbs24.entity.dto.RebalanceAgentsResult;
import org.dbs24.rest.AppSettingsRest;
import org.dbs24.rest.FireBaseRest;
import org.dbs24.rest.TariffPlanRest;
import org.dbs24.rest.api.*;
import org.dbs24.rest.dto.serviceperiod.CreateServicePeriodRequest;
import org.dbs24.rest.dto.serviceperiod.CreatedServicePeriod;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Arrays;
import java.util.Collection;

import static org.dbs24.consts.RestHttpConsts.HTTP_200_STRING;
import static org.dbs24.consts.ServicePeriodsConsts.Routes.ROUTE_OUT_OF_SERVICE_GET;
import static org.dbs24.consts.ServicePeriodsConsts.Routes.ROUTE_OUT_OF_SERVICE_SET;
import static org.dbs24.consts.WaConsts.RestQueryParams.*;
import static org.dbs24.consts.WaConsts.Uri.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Log4j2
@Configuration
@EnableCaching
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-service")
public class StandardAuthorizationRestConfig extends AbstractWebSecurityConfig {

    final RefsService refsService;
    final AgentsService agentsService;
    final AppSettingsRest appSettingsRest;
    final ServicesPeriodsRestConfig servicesPeriodsRestConfig;

    public StandardAuthorizationRestConfig(RefsService refsService, AgentsService agentsService, AppSettingsRest miscRest, ServicesPeriodsRestConfig servicesPeriodsRestConfig) {
        this.refsService = refsService;
        this.agentsService = agentsService;
        this.appSettingsRest = miscRest;
        this.servicesPeriodsRestConfig = servicesPeriodsRestConfig;
    }

    //@SecurityScheme - use 4 authorization bearer

    @RouterOperations({
            // tariffs
            @RouterOperation(path = URI_CREATE_OR_UPDATE_TARIFF_PLAN, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_TARIFF_PLAN, requestBody = @RequestBody(description = "Tariff plan info", content = @Content(schema = @Schema(implementation = TariffPlanInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create new contract from payment", content = @Content(schema = @Schema(implementation = CreatedTariffPlan.class))))),
            @RouterOperation(path = URI_GET_TARIFF_PLANS, method = GET, operation = @Operation(operationId = URI_GET_TARIFF_PLANS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Get tariff plan list", content = @Content(schema = @Schema(implementation = AllTariffPlans.class))),
                    parameters = {@Parameter(name = QP_TARIFF_PLAN_STATUS, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "allowed tariff plan statuses = {1 - Active, 10 - Closed}", example = "1"),
                            @Parameter(name = QP_DEVICE_TYPE, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "allowed devices = {0 - Android, 1 - Ios}", example = "0")
                    })),
            // agents
            //@RouterOperation(path = URI_CREATE_AGENT, method = POST, operation = @Operation(operationId = URI_CREATE_AGENT, requestBody = @RequestBody(description = "Agent info", content = @Content(schema = @Schema(implementation = AgentInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create new agent", content = @Content(schema = @Schema(implementation = CreatedAgent.class))))),
            @RouterOperation(path = URI_GET_AGENTS_LIST, method = GET, operation = @Operation(operationId = URI_GET_AGENTS_LIST, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Get agents list", content = @Content(schema = @Schema(implementation = AgentInfoCollection.class))), parameters = @Parameter(name = QP_AGENT_STATUS, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "allowed agent statuses = {1, 2, 3, 5}", example = "2"))),
            @RouterOperation(path = URI_GET_AGENTS_LIST_BY_ACTUAL_DATE, method = GET, operation = @Operation(operationId = URI_GET_AGENTS_LIST_BY_ACTUAL_DATE, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Agents list by actual date", content = @Content(schema = @Schema(implementation = AgentInfoCollection.class))), parameters = @Parameter(name = QP_ACTUAL_DATE, in = ParameterIn.QUERY, schema = @Schema(type = "long"), description = "actual date in millis", example = "12322233344"))),
            @RouterOperation(path = URI_GET_AGENT_HISTORY, method = GET, operation = @Operation(operationId = URI_GET_AGENT_HISTORY, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Agent attrs history", content = @Content(schema = @Schema(implementation = AgentInfoCollection.class))), parameters = @Parameter(name = QP_AGENT_ID, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "agent id", example = "1"))),
            @RouterOperation(path = URI_UPDATE_AGENT_STATUS, method = GET, operation = @Operation(operationId = URI_UPDATE_AGENT_STATUS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Update agent status", content = @Content(schema = @Schema(implementation = AgentPayloadInfo.class))), parameters = {
                    @Parameter(name = QP_AGENT_PHONE, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "agent phone", example = "77755963436"),
                    @Parameter(name = QP_AGENT_STATUS, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "allowed agent statuses = {1, 2, 3, 5}", example = "2")})),
            @RouterOperation(path = URI_REBALANCE_AGENTS, method = POST, operation = @Operation(operationId = URI_REBALANCE_AGENTS, requestBody = @RequestBody(description = "rebalance agents"), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "rebalance agents", content = @Content(schema = @Schema(implementation = RebalanceAgentsResult.class))))),
            // subscriptions
            // activities
            @RouterOperation(path = URI_CREATE_OR_UPDATE_FIREBASE_APPLICATION, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_FIREBASE_APPLICATION, requestBody = @RequestBody(description = "create firebase application", content = @Content(schema = @Schema(implementation = FireBaseApplicationInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Created firebase application", content = @Content(schema = @Schema(implementation = CreatedFireBaseApplication.class))))),
            @RouterOperation(path = URI_ALL_FIREBASE_APPLICATION, method = GET, operation = @Operation(operationId = URI_ALL_FIREBASE_APPLICATION, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "get all firebase applications", content = @Content(schema = @Schema(implementation = AllFirebaseApplications.class))))),
            @RouterOperation(path = URI_GET_SETTINGS, method = GET, operation = @Operation(operationId = URI_GET_SETTINGS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "get app settings", content = @Content(schema = @Schema(implementation = AppSettingsInfo.class))), parameters = {
                    @Parameter(name = QP_PACKAGE_NAME, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "package name", example = "com.zeta.io")})),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_APP_SETTING, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_APP_SETTING, requestBody = @RequestBody(description = "create app setting info", content = @Content(schema = @Schema(implementation = AppSettingsDto.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Created/updated setting name", content = @Content(schema = @Schema(implementation = CreatedAppSetting.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_PACKAGE_DETAILS, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_PACKAGE_DETAILS, requestBody = @RequestBody(description = "create package details info", content = @Content(schema = @Schema(implementation = PackageDetailsInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Created/updated package name", content = @Content(schema = @Schema(implementation = CreatedPackageDetails.class))))),
            @RouterOperation(path = URI_GET_LICENSE_AGREEMENT, method = GET, operation = @Operation(operationId = URI_GET_LICENSE_AGREEMENT, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "get license agreement for package name", content = @Content(schema = @Schema(implementation = LicenseAgreementInfo.class))), parameters = {
                    @Parameter(name = QP_PACKAGE_NAME, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "package name", example = "pkgName"),
                    @Parameter(name = QP_DEVICE_TYPE, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "deviceTypeId [0 - android, 1 - ios]", example = "0")})),
            @RouterOperation(path = URI_ALL_PACKAGE_DETAILS, method = GET, operation = @Operation(operationId = URI_ALL_PACKAGE_DETAILS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "get all license agreement for all packages", content = @Content(schema = @Schema(implementation = AllPackageDetailsInfo.class))))),
            @RouterOperation(path = ROUTE_OUT_OF_SERVICE_SET, method = POST, operation = @Operation(operationId = ROUTE_OUT_OF_SERVICE_SET, requestBody = @RequestBody(description = "set service period", content = @Content(schema = @Schema(implementation = CreateServicePeriodRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Created/updated package name", content = @Content(schema = @Schema(implementation = CreatedServicePeriod.class))))),
            @RouterOperation(path = ROUTE_OUT_OF_SERVICE_GET, method = GET, operation = @Operation(operationId = ROUTE_OUT_OF_SERVICE_GET, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "get service period", content = @Content(schema = @Schema(implementation = CreatedServicePeriod.class))))),
            @RouterOperation
    })

    @Bean
    public RouterFunction<ServerResponse> routerAuthorizationRest(
            FireBaseRest fireBaseRest, TariffPlanRest tariffPlanRest, ServicesPeriodsRestConfig servicesPeriodsRestConfig) {
        return addCommonRoutes()
                // agents
//                .andRoute(postRoute(URI_CREATE_AGENT), waMonitoringRest::createOrUpdateAgent)
//                .andRoute(getRoute(URI_GET_AGENTS_LIST), waMonitoringRest::getAgentsList)
//                .andRoute(getRoute(URI_GET_AGENTS_LIST_BY_ACTUAL_DATE), waMonitoringRest::getAgentsListByActualDate)
//                .andRoute(getRoute(URI_GET_AGENT_HISTORY), waMonitoringRest::getAgentHistory)
//                .andRoute(getRoute(URI_UPDATE_AGENT_STATUS), userSubscriptionsRest::updateAgentStatus)
//                .andRoute(postRoute(URI_REBALANCE_AGENTS), userSubscriptionsRest::rebalanceAgents)
                // tariffs
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_TARIFF_PLAN), tariffPlanRest::createOrUpdateTariffPlan)
                .andRoute(getRoute(URI_GET_TARIFF_PLANS), tariffPlanRest::getTariffPlans)
                // fireBase
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_FIREBASE_APPLICATION), fireBaseRest::createOrUpdateFireBaseApplication)
                .andRoute(getRoute(URI_ALL_FIREBASE_APPLICATION), fireBaseRest::getAllFirebaseApplications)
                // setting
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_PACKAGE_DETAILS), appSettingsRest::createOrUpdatePackageDetails)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_APP_SETTING), appSettingsRest::createOrUpdateAppSettings)
                .andRoute(getRoute(URI_GET_LICENSE_AGREEMENT), appSettingsRest::getLicenseAgreement)
                .andRoute(getRoute(URI_ALL_PACKAGE_DETAILS), appSettingsRest::getAllPackageDetails)
                .andRoute(getRoute(URI_GET_SETTINGS), appSettingsRest::getSettings)
                // testing
                //.andRoute(postRoute(URI_CREATE_TEST_SUBSCRIPTIONS), userSubscriptionsRest::createUserSubscriptions4test)
                // services periods
                .and(servicesPeriodsRestConfig.servicesPeriodRoutes());
    }

    @Override
    protected String[] addWhiteListAuthUrl(String[] existsUrl) {

        final Collection<String> whiteUrls = ServiceFuncs.createCollection();

        whiteUrls.addAll(Arrays.asList(existsUrl));
        whiteUrls.add(URI_GET_SETTINGS);
        whiteUrls.add(URI_GET_LICENSE_AGREEMENT);
        whiteUrls.add(URI_CREATE_AGENT);
        whiteUrls.add(URI_GET_AGENTS_LIST);
        whiteUrls.add(URI_GET_SUBSCRIPTION);
        whiteUrls.add(URI_UPDATE_SUBSCRIPTION_STATUS);
        whiteUrls.add(URI_GET_MODIFIED_SUBSCRIPTIONS);
        whiteUrls.add(URI_UPDATE_AGENT_STATUS);
        whiteUrls.add(URI_GET_MODIFIED_DEVICES);
        whiteUrls.add(URI_CREATE_ACTIVITIES);
        whiteUrls.add(URI_GET_ALL_USER_DEVICES);
        whiteUrls.add(URI_GET_AGENT_HISTORY);
        whiteUrls.add(URI_GET_AGENTS_LIST_BY_ACTUAL_DATE);
        whiteUrls.add(URI_GET_ALL_SUBSCRIPTIONS);
        whiteUrls.add(URI_GET_AGENT_SUBSCRIPTIONS);
        whiteUrls.add(URI_UPDATE_DEVICE_ATTRS);
        whiteUrls.add(URI_CREATE_OR_UPDATE_AVATAR);
        whiteUrls.add(URI_CREATE_OR_UPDATE_FIREBASE_APPLICATION);
        whiteUrls.add(URI_ALL_FIREBASE_APPLICATION);
        whiteUrls.add(URI_GET_INVALID_ACTIVITY_SUBSCRIPTIONS);
        whiteUrls.add(URI_GENERATE_TEST_USERS);
        whiteUrls.add(URI_REMOVE_DEVICE_BY_ID);
        whiteUrls.add(URI_GET_CREATE_CONTRACT_FROM_PAYMENT);
        whiteUrls.add(URI_GET_TARIFF_PLANS);
        whiteUrls.add(URI_MODIFY_CONTRACT_BY_SUPPORT);

        return whiteUrls.toArray(new String[]{});
    }

    @Override
    public void initialize() {

        super.initialize();

        refsService.synchronizeRefs();
    }
}
