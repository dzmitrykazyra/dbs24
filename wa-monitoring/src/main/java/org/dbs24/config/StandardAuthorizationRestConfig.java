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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.component.AgentsService;
import org.dbs24.component.RefsService;
import org.dbs24.component.UserContractsService;
import org.dbs24.component.UserSubscriptionsService;
import org.dbs24.entity.dto.*;
import org.dbs24.rest.*;
import org.dbs24.rest.api.*;
import org.dbs24.rest.dto.serviceperiod.CreateServicePeriodRequest;
import org.dbs24.rest.dto.serviceperiod.CreatedServicePeriod;
import org.dbs24.rest.dto.serviceperiod.ServicePeriodInfoResponse;
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
import static org.dbs24.consts.WaConsts.SwaggerTags.*;
import static org.dbs24.consts.WaConsts.Uri.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Log4j2
@Configuration
@EnableCaching
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
@EqualsAndHashCode(callSuper = true)
public class StandardAuthorizationRestConfig extends AbstractWebSecurityConfig {

    final RefsService refsService;
    final AgentsService agentsService;
    final UserContractsService userContractsService;
    final UserSubscriptionsService userSubscriptionsService;
    final AppSettingsRest appSettingsRest;
    final UserContractsRest userContractsRest;
    final ServicesPeriodsRestConfig servicesPeriodsRestConfig;

    public StandardAuthorizationRestConfig(RefsService refsService, AgentsService agentsService, UserContractsService userContractsService, UserSubscriptionsService userSubscriptionsService, AppSettingsRest miscRest, UserContractsRest userContractsRest, ServicesPeriodsRestConfig servicesPeriodsRestConfig) {
        this.refsService = refsService;
        this.agentsService = agentsService;
        this.userContractsService = userContractsService;
        this.userSubscriptionsService = userSubscriptionsService;
        this.appSettingsRest = miscRest;
        this.userContractsRest = userContractsRest;
        this.servicesPeriodsRestConfig = servicesPeriodsRestConfig;
    }

    //@SecurityScheme - use 4 authorization bearer

    @RouterOperations({
            // users
            @RouterOperation(path = URI_GENERATE_TEST_USERS, method = GET, operation = @Operation(tags = TAG_USERS, operationId = URI_GENERATE_TEST_USERS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Generate test users/subscriptions/activities", content = @Content(schema = @Schema(implementation = UsersAmount.class))), parameters =
                    {@Parameter(name = QP_AMOUNT, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "amount of users ", example = "10"),
                            @Parameter(name = QP_PHONE_MASK, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "phone mask of exist subscription ", example = "9"),
                            @Parameter(name = QP_ACTIVITY_LIMIT, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "limit of subscription activities ", example = "8")})),
            @RouterOperation(path = URI_CHECK_TOKEN_VALIDITY, method = GET, operation = @Operation(tags = TAG_USERS, operationId = URI_CHECK_TOKEN_VALIDITY, security = @SecurityRequirement(name = AUTHORIZATION), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Check token validity", content = @Content(schema = @Schema(implementation = LoginTokenInfo.class))), parameters = {@Parameter(name = QP_LOGIN_TOKEN, in = ParameterIn.QUERY, schema = @Schema(type = "string")), @Parameter(name = AUTHORIZATION, in = ParameterIn.HEADER, schema = @Schema(type = "bearer"), description = "The authorization token ")})),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_MP_USER, method = POST, operation = @Operation(tags = TAG_USERS, operationId = URI_CREATE_OR_UPDATE_MP_USER, requestBody = @RequestBody(description = "User info", content = @Content(schema = @Schema(implementation = UserAttrsInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create new user", content = @Content(schema = @Schema(implementation = CreatedUser.class))))),
            // contracts
            @RouterOperation(path = URI_CREATE_CONTRACT, method = POST, operation = @Operation(tags = TAG_CONTRACTS, operationId = URI_CREATE_CONTRACT, requestBody = @RequestBody(description = "Contract info", content = @Content(schema = @Schema(implementation = UserContractInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create new contract", content = @Content(schema = @Schema(implementation = CreatedUserContract.class))))),
            @RouterOperation(path = URI_GET_CREATE_CONTRACT_FROM_PAYMENT, method = POST, operation = @Operation(tags = TAG_CONTRACTS, operationId = URI_GET_CREATE_CONTRACT_FROM_PAYMENT, requestBody = @RequestBody(description = "Contract info", content = @Content(schema = @Schema(implementation = UserContractFromPaymentInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create new contract from payment", content = @Content(schema = @Schema(implementation = CreatedUserContract.class))))),
            @RouterOperation(path = URI_MODIFY_CONTRACT, method = POST, operation = @Operation(tags = TAG_CONTRACTS, operationId = URI_MODIFY_CONTRACT, requestBody = @RequestBody(description = "New contract info", content = @Content(schema = @Schema(implementation = ModifyContractInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Manual update contract info", content = @Content(schema = @Schema(implementation = CreatedUserContract.class))))),
            @RouterOperation(path = URI_MODIFY_CONTRACT_END_DATE, method = POST, operation = @Operation(tags = TAG_CONTRACTS, operationId = URI_MODIFY_CONTRACT_END_DATE, requestBody = @RequestBody(description = "New contract endDate info", content = @Content(schema = @Schema(implementation = ModifyContractEndDateInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Manual update contract endDate info", content = @Content(schema = @Schema(implementation = CreatedUserContract.class))))),
            @RouterOperation(path = URI_MODIFY_CONTRACT_BY_SUPPORT, method = POST, operation = @Operation(tags = TAG_CONTRACTS, operationId = URI_MODIFY_CONTRACT_BY_SUPPORT, requestBody = @RequestBody(description = "Update contract by support info", content = @Content(schema = @Schema(implementation = ModifyContractBySupportInfo.class))), parameters = {
                    @Parameter(name = QP_LOGIN_TOKEN, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "user loginToken", example = "4NPOF6A99Z1OU0AECT3VRQGYJS7R4Q")
            }, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Manual update contract endDate info", content = @Content(schema = @Schema(implementation = CreatedUserContract.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_FUTUTE_TRIAL_CONTRACT, method = POST, operation = @Operation(tags = TAG_CONTRACTS, operationId = URI_CREATE_OR_UPDATE_FUTUTE_TRIAL_CONTRACT, requestBody = @RequestBody(description = "Trial Contract info", content = @Content(schema = @Schema(implementation = FutureTrialUserContractsInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create new future trial contract ", content = @Content(schema = @Schema(implementation = CreatedUserContract.class))))),
            @RouterOperation(path = URI_GET_USER_CONTRACTS, method = GET, operation = @Operation(tags = TAG_CONTRACTS, operationId = URI_GET_USER_CONTRACTS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Get user contracts", content = @Content(schema = @Schema(implementation = UserContractInfoCollection.class))), parameters = {
                    @Parameter(name = QP_LOGIN_TOKEN, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "user loginToken", example = "4NPOF6A99Z1OU0AECT3VRQGYJS7R4Q"),
                    @Parameter(name = QP_CONTRACT_STATUS, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "allowed contract statuses = {when is empty - ALL contracts statuses; 0 - Actual; 1 - Closed; 9 - Trial; -1 - Cancelled }", example = "0")})),
            @RouterOperation(path = URI_GET_ACTUAL_USER_CONTRACTS, method = GET, operation = @Operation(tags = TAG_CONTRACTS, operationId = URI_GET_ACTUAL_USER_CONTRACTS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Get all actual user contracts", content = @Content(schema = @Schema(implementation = UserContractInfoCollection.class))), parameters = {
                    @Parameter(name = QP_LOGIN_TOKEN, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "user loginToken", example = "4NPOF6A99Z1OU0AECT3VRQGYJS7R4Q")})),
            @RouterOperation(path = URI_GET_CHECK_USER_CONTRACTS_VALIDITY, method = GET, operation = @Operation(tags = TAG_CONTRACTS, operationId = URI_GET_CHECK_USER_CONTRACTS_VALIDITY, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Actual contract info", content = @Content(schema = @Schema(implementation = ContractExpiryInfo.class))), parameters = {
                    @Parameter(name = QP_LOGIN_TOKEN, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "user loginToken", example = "4NPOF6A99Z1OU0AECT3VRQGYJS7R4Q")})),
            @RouterOperation(path = URI_GET_CHECK_ALL_USER_CONTRACTS_VALIDITY, method = GET, operation = @Operation(tags = TAG_CONTRACTS, operationId = URI_GET_CHECK_ALL_USER_CONTRACTS_VALIDITY, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Actual contract info", content = @Content(schema = @Schema(implementation = ShortUserContractInfoCollection.class))), parameters = {
                    @Parameter(name = QP_LOGIN_TOKEN, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "user loginToken", example = "4NPOF6A99Z1OU0AECT3VRQGYJS7R4Q")})),
            // tariffs
            @RouterOperation(path = URI_CREATE_OR_UPDATE_TARIFF_PLAN, method = POST, operation = @Operation(tags = TAG_TARIFFS, operationId = URI_CREATE_OR_UPDATE_TARIFF_PLAN, requestBody = @RequestBody(description = "Tariff plan info", content = @Content(schema = @Schema(implementation = TariffPlanInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create new contract from payment", content = @Content(schema = @Schema(implementation = CreatedTariffPlan.class))))),
            @RouterOperation(path = URI_GET_TARIFF_PLANS, method = GET, operation = @Operation(tags = TAG_TARIFFS, operationId = URI_GET_TARIFF_PLANS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Get tariff plan list", content = @Content(schema = @Schema(implementation = AllTariffPlans.class))),
                    parameters = {@Parameter(name = QP_TARIFF_PLAN_STATUS, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "allowed tariff plan statuses = {1 - Active, 10 - Closed}", example = "1"),
                            @Parameter(name = QP_DEVICE_TYPE, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "allowed devices = {0 - Android, 1 - Ios}", example = "0")
                    })),
            // agents
            @RouterOperation(path = URI_CREATE_AGENT, method = POST, operation = @Operation(tags = TAG_AGENTS, operationId = URI_CREATE_AGENT, requestBody = @RequestBody(description = "Agent info", content = @Content(schema = @Schema(implementation = AgentInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create new agent", content = @Content(schema = @Schema(implementation = CreatedAgent.class))))),
            @RouterOperation(path = URI_GET_AGENTS_LIST, method = GET, operation = @Operation(tags = TAG_AGENTS, operationId = URI_GET_AGENTS_LIST, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Get agents list", content = @Content(schema = @Schema(implementation = AgentInfoCollection.class))), parameters = @Parameter(name = QP_AGENT_STATUS, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "allowed agent statuses = {1, 2, 3, 5}", example = "2"))),
            @RouterOperation(path = URI_GET_AGENTS_LIST_BY_ACTUAL_DATE, method = GET, operation = @Operation(tags = TAG_AGENTS, operationId = URI_GET_AGENTS_LIST_BY_ACTUAL_DATE, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Agents list by actual date", content = @Content(schema = @Schema(implementation = AgentInfoCollection.class))), parameters = @Parameter(name = QP_ACTUAL_DATE, in = ParameterIn.QUERY, schema = @Schema(type = "long"), description = "actual date in millis", example = "12322233344"))),
            @RouterOperation(path = URI_GET_AGENT_HISTORY, method = GET, operation = @Operation(tags = TAG_AGENTS, operationId = URI_GET_AGENT_HISTORY, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Agent attrs history", content = @Content(schema = @Schema(implementation = AgentInfoCollection.class))), parameters = @Parameter(name = QP_AGENT_ID, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "agent id", example = "1"))),
            @RouterOperation(path = URI_UPDATE_AGENT_STATUS, method = GET, operation = @Operation(tags = TAG_AGENTS, operationId = URI_UPDATE_AGENT_STATUS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Update agent status", content = @Content(schema = @Schema(implementation = AgentPayloadInfo.class))), parameters = {
                    @Parameter(name = QP_AGENT_PHONE, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "agent phone", example = "77755963436"),
                    @Parameter(name = QP_AGENT_STATUS, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "allowed agent statuses = {1, 2, 3, 5}", example = "2"),
                    @Parameter(name = QP_AGENT_NOTE, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "agent note", example = "some note")})),
            @RouterOperation(path = URI_REBALANCE_AGENTS, method = POST, operation = @Operation(tags = TAG_AGENTS, operationId = URI_REBALANCE_AGENTS, requestBody = @RequestBody(description = "rebalance agents"), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "rebalance agents", content = @Content(schema = @Schema(implementation = RebalanceAgentsResult.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_AGENT_MESSAGE, method = POST, operation = @Operation(tags = TAG_AGENTS, operationId = URI_CREATE_OR_UPDATE_AGENT_MESSAGE, requestBody = @RequestBody(description = "create/update agent message", content = @Content(schema = @Schema(implementation = AgentMessageDto.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "create/update agent message", content = @Content(schema = @Schema(implementation = CreatedAgentMessage.class))))),
            @RouterOperation(path = URI_GET_AGENT_MESSAGE, method = GET, operation = @Operation(tags = TAG_AGENTS, operationId = URI_GET_AGENT_MESSAGE, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "get agents message", content = @Content(schema = @Schema(implementation = AgentMessageDtoResponse.class))),
                    parameters = {@Parameter(name = QP_AGENT_PHONE, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "agent phone num", example = "79096155782"),
                            @Parameter(name = QP_PHONE, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "subscription phone num", example = "79096155783")})),
            @RouterOperation(path = URI_GET_ACTUAL_MESSAGES_COUNT, method = GET, operation = @Operation(tags = TAG_AGENTS, operationId = URI_GET_ACTUAL_MESSAGES_COUNT, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "get agents message", content = @Content(schema = @Schema(implementation = ActualMessagesCountDtoResponse.class))),
                    parameters = { @Parameter(name = QP_PHONE, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "subscription phone num", example = "79096155783")})),
            @RouterOperation(path = URI_GET_MESSAGING_SUBSCRIPTION, method = GET, operation = @Operation(tags = TAG_AGENTS, operationId = URI_GET_MESSAGING_SUBSCRIPTION, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "get messages subscriptions", content = @Content(schema = @Schema(implementation = MessagesPhoneNumsDtoResponse.class))),
                    parameters = { @Parameter(name = QP_AGENT_PHONE, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "agent phone num", example = "79096155782")})),
            @RouterOperation(path = URI_GET_MESSAGING_LAST_CASHE, method = GET, operation = @Operation(tags = TAG_AGENTS, operationId = URI_GET_MESSAGING_LAST_CASHE, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "get messaging last messagecache", content = @Content(schema = @Schema(implementation = AgentLatestMessagesDtoResponse.class))))),
            // subscriptions
            @RouterOperation(path = URI_CREATE_OR_UPDATE_SUBSCRIPTION, method = POST, operation = @Operation(tags = TAG_SUBSCRIPTIONS, operationId = URI_CREATE_OR_UPDATE_SUBSCRIPTION, requestBody = @RequestBody(description = "Create or update subscription", content = @Content(schema = @Schema(implementation = UserSubscriptionInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create new subscription", content = @Content(schema = @Schema(implementation = CreatedUserSubscription.class))), parameters = {@Parameter(name = QP_LOGIN_TOKEN, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "login token", example = "4NPOF6A99Z1OU0AECT3VRQGYJS7R4Q")})),
            @RouterOperation(path = URI_UPDATE_SUBSCRIPTION_STATUS, method = POST, operation = @Operation(tags = TAG_SUBSCRIPTIONS, operationId = URI_UPDATE_SUBSCRIPTION_STATUS, requestBody = @RequestBody(description = "update subscription status"), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "update subscription status", content = @Content(schema = @Schema(implementation = CreatedUserSubscription.class))), parameters = {@Parameter(name = QP_SUBSCRIPTION_ID, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "subscription id", example = "1233"), @Parameter(name = QP_SUBSCRIPTION_STATUS, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "subscription status", example = "1")})),
            @RouterOperation(path = URI_GET_ALL_SUBSCRIPTIONS, method = GET, operation = @Operation(tags = TAG_SUBSCRIPTIONS, operationId = URI_GET_ALL_SUBSCRIPTIONS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Get all user subscription", content = @Content(schema = @Schema(implementation = UserSubscriptionInfoCollection.class))), parameters = {
                    @Parameter(name = QP_LOGIN_TOKEN, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "user loginToken", example = "4NPOF6A99Z1OU0AECT3VRQGYJS7R4Q")})),
            @RouterOperation(path = URI_GET_SUBSCRIPTION, method = GET, operation = @Operation(tags = TAG_SUBSCRIPTIONS, operationId = URI_GET_SUBSCRIPTION, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Get user subscription", content = @Content(schema = @Schema(implementation = UserSubscriptionInfo.class))), parameters = {
                    @Parameter(name = QP_LOGIN_TOKEN, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "user loginToken", example = "4NPOF6A99Z1OU0AECT3VRQGYJS7R4Q"),
                    @Parameter(name = QP_PHONE, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "subscription phone", example = "375442320202")})),
            @RouterOperation(path = URI_GET_AGENT_SUBSCRIPTIONS, method = GET, operation = @Operation(tags = TAG_SUBSCRIPTIONS, operationId = URI_GET_AGENT_SUBSCRIPTIONS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "List of agent subscriptions", content = @Content(schema = @Schema(implementation = UserSubscriptionInfoCollection.class))), parameters = {
                    @Parameter(name = QP_AGENT_PHONE, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "agent phone", example = "77081549574"),
                    @Parameter(name = QP_ACTUAL_ONLY, in = ParameterIn.QUERY, schema = @Schema(type = "boolean"), description = "filter actualOnly subscriptions", example = "true")})),
            @RouterOperation(path = URI_GET_SUBSCRIPTIONS_NOTIFY_STATUS, method = GET, operation = @Operation(tags = TAG_SUBSCRIPTIONS, operationId = URI_GET_SUBSCRIPTIONS_NOTIFY_STATUS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "subscriptions list", content = @Content(schema = @Schema(implementation = UserSubscriptionShortInfoCollection.class))), parameters = {
                    @Parameter(name = QP_SUBSCRIPTION_ID, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "subscriiptionId", example = "23689")})),
            @RouterOperation(path = URI_GET_ALL_USER_SUBSCRIPTIONS_NOTIFY_STATUS, method = GET, operation = @Operation(tags = TAG_SUBSCRIPTIONS, operationId = URI_GET_ALL_USER_SUBSCRIPTIONS_NOTIFY_STATUS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Get all user subscriptions", content = @Content(schema = @Schema(implementation = UserSubscriptionNotifyStatusInfo.class))), parameters = {
                    @Parameter(name = QP_USER_ID, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "internal user id", example = "10717")})),
            @RouterOperation(path = URI_GET_INVALID_SUBSCRIPTIONS, method = GET, operation = @Operation(tags = TAG_SUBSCRIPTIONS, operationId = URI_GET_INVALID_SUBSCRIPTIONS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Status of subscriptions", content = @Content(schema = @Schema(implementation = UserSubscriptionAgentInfoCollection.class))))),
            @RouterOperation(path = URI_UPDATE_INVALID_SUBSCRIPTIONS, method = GET, operation = @Operation(tags = TAG_SUBSCRIPTIONS, operationId = URI_UPDATE_INVALID_SUBSCRIPTIONS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Update invalid subscriptions", content = @Content(schema = @Schema(implementation = UserSubscriptionAgentInfoCollection.class))))),
            @RouterOperation(path = URI_GET_INVALID_ACTIVITY_SUBSCRIPTIONS, method = GET, operation = @Operation(tags = TAG_SUBSCRIPTIONS, operationId = URI_GET_INVALID_ACTIVITY_SUBSCRIPTIONS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "invalid subscriptions list with empty activities list", content = @Content(schema = @Schema(implementation = UserSubscriptionShortInfoCollection.class))), parameters = {
                    @Parameter(name = QP_HOURS, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "hours of inactivity", example = "24")})),
            @RouterOperation(path = URI_PREPARE_ACTUAL_SUBSCRIPTIONS, method = GET, operation = @Operation(tags = TAG_SUBSCRIPTIONS, operationId = URI_PREPARE_ACTUAL_SUBSCRIPTIONS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "prepare actual subscriptions", content = @Content(schema = @Schema(implementation = PrepareSubscriptionAnswer.class))), parameters = {
                    @Parameter(name = QP_PAGE_SIZE, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "page size", example = "100")})),
            // avatar
            @RouterOperation(path = URI_CREATE_OR_UPDATE_AVATAR, method = POST, operation = @Operation(tags = TAG_AVATARS, operationId = URI_CREATE_OR_UPDATE_AVATAR, requestBody = @RequestBody(description = "Avatar info", content = @Content(schema = @Schema(implementation = AvatarInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create new avatar", content = @Content(schema = @Schema(implementation = CreatedAvatar.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_CUSTOM_AVATAR, method = POST, operation = @Operation(tags = TAG_AVATARS, operationId = URI_CREATE_OR_UPDATE_CUSTOM_AVATAR, requestBody = @RequestBody(description = "Custom Avatar info", content = @Content(schema = @Schema(implementation = AvatarInfoBySubscriptionPhone.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create new avatar", content = @Content(schema = @Schema(implementation = CreatedAvatar.class))), parameters = {
                    @Parameter(name = QP_LOGIN_TOKEN, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "login Token", example = "77081549574"),
                    @Parameter(name = QP_PHONE, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "subscription phone", example = "77081549574")})),
            @RouterOperation(path = URI_GET_AVATAR, method = GET, operation = @Operation(tags = TAG_AVATARS, operationId = URI_GET_AVATAR, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Get avatar", content = @Content(schema = @Schema(implementation = AvatarInfo.class))), parameters = {
                    @Parameter(name = QP_SUBSCRIPTION_ID, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "subscription id", example = "77081549574")})),
            @RouterOperation(path = URI_GET_CUSTOM_AVATAR, method = GET, operation = @Operation(tags = TAG_AVATARS, operationId = URI_GET_CUSTOM_AVATAR, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Get custom avatar", content = @Content(schema = @Schema(implementation = AvatarInfoBySubscriptionPhone.class))), parameters = {
                    @Parameter(name = QP_LOGIN_TOKEN, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "login Token", example = "4NPOF6A99Z1OU0AECT3VRQGYJS7R4Q"),
                    @Parameter(name = QP_PHONE, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "subscription phone", example = "77081549574")})),
            // devices
            @RouterOperation(path = URI_GET_ALL_USER_DEVICES, method = GET, operation = @Operation(tags = TAG_DEVICES, operationId = URI_GET_ALL_USER_DEVICES, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Get all user devices", content = @Content(schema = @Schema(implementation = UserDevicesInfoCollection.class))), parameters = {
                    @Parameter(name = QP_USER_ID, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "internal user id", example = "10458"),
                    @Parameter(name = QP_LOGIN_TOKEN, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "user loginToken id", example = "4NPOF6A99Z1OU0AECT3VRQGYJS7R4Q")})),
            @RouterOperation(path = URI_GET_EXISTS_DEVICE, method = GET, operation = @Operation(tags = TAG_DEVICES, operationId = URI_GET_EXISTS_DEVICE, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Is exists device", content = @Content(schema = @Schema(implementation = ExistsDeviceInfo.class))), parameters = {
                    @Parameter(name = QP_DEVICE_TYPE, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "internl device type id", example = "1"),
                    @Parameter(name = QP_DEVICE_UID, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "gsfId|identifierForVendor", example = "1")})),
            @RouterOperation(path = URI_REMOVE_DEVICE_BY_ID, method = GET, operation = @Operation(tags = TAG_DEVICES, operationId = URI_REMOVE_DEVICE_BY_ID, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Remove device by gsfId|identifierForVendor", content = @Content(schema = @Schema(implementation = RemoveDeviceResult.class))), parameters = {
                    @Parameter(name = QP_DEVICE_UID, in = ParameterIn.QUERY, schema = @Schema(type = "String"), description = "gsfId|identifierForVendor", example = "3a5cbcbd69b4d2f4")})),
            // activities
            @RouterOperation(path = URI_GET_ACTIVITIES, method = GET, operation = @Operation(tags = TAG_ACTIVITIES, operationId = URI_GET_ACTIVITIES, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "get activities list", content = @Content(schema = @Schema(implementation = ActivityInfo.class))), parameters = {
                    @Parameter(name = QP_D1, in = ParameterIn.QUERY, schema = @Schema(type = "long"), description = "date from in millis", example = "1234567890123"),
                    @Parameter(name = QP_D2, in = ParameterIn.QUERY, schema = @Schema(type = "long"), description = "date to in millis", example = "1234567890123"),
                    @Parameter(name = QP_PHONE, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "subscription phone", example = "1234567890123"),
                    @Parameter(name = QP_LOGIN_TOKEN, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "login token", example = "4NPOF6A99Z1OU0AECT3VRQGYJS7R4Q")})),
            @RouterOperation(path = URI_GET_LATEST_ACTIVITIES, method = GET, operation = @Operation(tags = TAG_ACTIVITIES, operationId = URI_GET_LATEST_ACTIVITIES, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "get latest activities list", content = @Content(schema = @Schema(implementation = LastSessionInfo.class))), parameters = {
                    @Parameter(name = QP_PHONE, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "subscription phone", example = "1234567890123"),
                    @Parameter(name = QP_LOGIN_TOKEN, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "login token", example = "4NPOF6A99Z1OU0AECT3VRQGYJS7R4Q")})),
            @RouterOperation(path = URI_GET_SUBSCRIPTIONS_LATEST_ACTIVITIES, method = GET, operation = @Operation(tags = TAG_ACTIVITIES, operationId = URI_GET_SUBSCRIPTIONS_LATEST_ACTIVITIES, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "get latest activities list", content = @Content(schema = @Schema(implementation = SubscriptionsSessions.class))), parameters = {
                    @Parameter(name = QP_LOGIN_TOKEN, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "login token", example = "4NPOF6A99Z1OU0AECT3VRQGYJS7R4Q")})),
            // settings
            @RouterOperation(path = URI_CREATE_OR_UPDATE_FIREBASE_APPLICATION, method = POST, operation = @Operation(tags = TAG_SETTINGS, operationId = URI_CREATE_OR_UPDATE_FIREBASE_APPLICATION, requestBody = @RequestBody(description = "create firebase application", content = @Content(schema = @Schema(implementation = FireBaseApplicationInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Created firebase application", content = @Content(schema = @Schema(implementation = CreatedFireBaseApplication.class))))),
            @RouterOperation(path = URI_ALL_FIREBASE_APPLICATION, method = GET, operation = @Operation(tags = TAG_SETTINGS, operationId = URI_ALL_FIREBASE_APPLICATION, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "get all firebase applications", content = @Content(schema = @Schema(implementation = AllFirebaseApplications.class))))),
            @RouterOperation(path = URI_GET_SETTINGS, method = GET, operation = @Operation(tags = TAG_SETTINGS, operationId = URI_GET_SETTINGS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "get app settings", content = @Content(schema = @Schema(implementation = AppSettingsInfo.class))), parameters = {
                    @Parameter(name = QP_PACKAGE_NAME, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "package name", example = "com.zeta.io")})),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_APP_SETTING, method = POST, operation = @Operation(tags = TAG_SETTINGS, operationId = URI_CREATE_OR_UPDATE_APP_SETTING, requestBody = @RequestBody(description = "create app setting info", content = @Content(schema = @Schema(implementation = AppSettingsDto.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Created/updated setting name", content = @Content(schema = @Schema(implementation = CreatedAppSetting.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_PACKAGE_DETAILS, method = POST, operation = @Operation(tags = TAG_SETTINGS, operationId = URI_CREATE_OR_UPDATE_PACKAGE_DETAILS, requestBody = @RequestBody(description = "create package details info", content = @Content(schema = @Schema(implementation = PackageDetailsInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Created/updated package name", content = @Content(schema = @Schema(implementation = CreatedPackageDetails.class))))),
            @RouterOperation(path = URI_GET_LICENSE_AGREEMENT, method = GET, operation = @Operation(tags = TAG_SETTINGS, operationId = URI_GET_LICENSE_AGREEMENT, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "get license agreement for package name", content = @Content(schema = @Schema(implementation = LicenseAgreementInfo.class))), parameters = {
                    @Parameter(name = QP_PACKAGE_NAME, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "package name", example = "pkgName"),
                    @Parameter(name = QP_DEVICE_TYPE, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "deviceTypeId [0 - android, 1 - ios]", example = "0")})),
            @RouterOperation(path = URI_ALL_PACKAGE_DETAILS, method = GET, operation = @Operation(tags = TAG_SETTINGS, operationId = URI_ALL_PACKAGE_DETAILS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "get all license agreement for all packages", content = @Content(schema = @Schema(implementation = AllPackageDetailsInfo.class))))),
            @RouterOperation(path = ROUTE_OUT_OF_SERVICE_SET, method = POST, operation = @Operation(tags = TAG_SETTINGS, operationId = ROUTE_OUT_OF_SERVICE_SET, requestBody = @RequestBody(description = "set service period", content = @Content(schema = @Schema(implementation = CreateServicePeriodRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Created/updated service period", content = @Content(schema = @Schema(implementation = CreatedServicePeriod.class))))),
            @RouterOperation(path = ROUTE_OUT_OF_SERVICE_GET, method = GET, operation = @Operation(tags = TAG_SETTINGS, operationId = ROUTE_OUT_OF_SERVICE_GET, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "get service period", content = @Content(schema = @Schema(implementation = ServicePeriodInfoResponse.class)))))
    })

    @Bean
    public RouterFunction<ServerResponse> routerAuthorizationRest(
            WaMonitoringRest waMonitoringRest, UsersRest usersRest, UserSubscriptionsRest userSubscriptionsRest, ActivitiesRest activitiesRest, UserDevicesRest devicesRest, FireBaseRest fireBaseRest, TariffPlanRest tariffPlanRest) {
        return addCommonRoutes()
                // users
                .andRoute(getRoute(URI_CHECK_TOKEN_VALIDITY), usersRest::checkTokenValidity)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_MP_USER), usersRest::createOrUpdateMultiUser)
                .andRoute(getRoute(URI_GENERATE_TEST_USERS), usersRest::generateTestUsers)
                // agents
                .andRoute(postRoute(URI_CREATE_AGENT), waMonitoringRest::createOrUpdateAgent)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_AGENT_MESSAGE), waMonitoringRest::createOrUpdateAgentMessage)
                .andRoute(getRoute(URI_GET_AGENT_MESSAGE), waMonitoringRest::getAgentMessage)
                .andRoute(getRoute(URI_GET_ACTUAL_MESSAGES_COUNT), waMonitoringRest::getActualMessagesCount)
                .andRoute(getRoute(URI_GET_MESSAGING_SUBSCRIPTION), waMonitoringRest::getMessagingSubscriptions)
                .andRoute(getRoute(URI_GET_MESSAGING_LAST_CASHE), waMonitoringRest::getMessagingLastMessageCache)
                .andRoute(getRoute(URI_GET_AGENTS_LIST), waMonitoringRest::getAgentsList)
                .andRoute(getRoute(URI_GET_AGENTS_LIST_BY_ACTUAL_DATE), waMonitoringRest::getAgentsListByActualDate)
                .andRoute(getRoute(URI_GET_AGENT_HISTORY), waMonitoringRest::getAgentHistory)
                .andRoute(getRoute(URI_UPDATE_AGENT_STATUS), userSubscriptionsRest::updateAgentStatus)
                .andRoute(postRoute(URI_REBALANCE_AGENTS), userSubscriptionsRest::rebalanceAgents)
                // contracts
                .andRoute(postRoute(URI_CREATE_CONTRACT), userContractsRest::createOrUpdateContract)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_FUTUTE_TRIAL_CONTRACT), userContractsRest::createOrUpdateFutureTrialContract)
                .andRoute(getRoute(URI_GET_USER_CONTRACTS), userContractsRest::getUserContracts)
                .andRoute(getRoute(URI_GET_ACTUAL_USER_CONTRACTS), userContractsRest::getActualUserContracts)
                .andRoute(getRoute(URI_GET_CHECK_USER_CONTRACTS_VALIDITY), userContractsRest::checkUserContractValidity)
                .andRoute(getRoute(URI_GET_CHECK_ALL_USER_CONTRACTS_VALIDITY), userContractsRest::checkAllUserContractValidity)
                .andRoute(postRoute(URI_GET_CREATE_CONTRACT_FROM_PAYMENT), userContractsRest::createContractFromPayment)
                .andRoute(postRoute(URI_MODIFY_CONTRACT), userContractsRest::modifyContract)
                .andRoute(postRoute(URI_MODIFY_CONTRACT_BY_SUPPORT), userContractsRest::modifyContractBySupport)
                .andRoute(postRoute(URI_MODIFY_CONTRACT_END_DATE), userContractsRest::modifyContractEndDate)
                // tariffs
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_TARIFF_PLAN), tariffPlanRest::createOrUpdateTariffPlan)
                .andRoute(getRoute(URI_GET_TARIFF_PLANS), tariffPlanRest::getTariffPlans)
                // subscriptions
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_SUBSCRIPTION), userSubscriptionsRest::createOrUpdateSubscription)
                .andRoute(postRoute(URI_UPDATE_SUBSCRIPTION_STATUS), userSubscriptionsRest::updateSubscriptionStatus)
                .andRoute(getRoute(URI_GET_ALL_SUBSCRIPTIONS), userSubscriptionsRest::getAllActualSubscriptions)
                .andRoute(getRoute(URI_GET_SUBSCRIPTION), userSubscriptionsRest::getSubscription)
                .andRoute(getRoute(URI_GET_AGENT_SUBSCRIPTIONS), userSubscriptionsRest::getAgentSubscriptions)
                .andRoute(getRoute(URI_GET_SUBSCRIPTIONS_NOTIFY_STATUS), userSubscriptionsRest::getSubscriptionNotifyStatus)
                .andRoute(getRoute(URI_GET_ALL_USER_SUBSCRIPTIONS_NOTIFY_STATUS), userSubscriptionsRest::getAllUserSubscriptionNotifyStatus)
                .andRoute(getRoute(URI_GET_INVALID_SUBSCRIPTIONS), userSubscriptionsRest::getInvalidSubscriptions)
                .andRoute(getRoute(URI_GET_INVALID_ACTIVITY_SUBSCRIPTIONS), userSubscriptionsRest::getInvalidActivitySubscriptions)
                .andRoute(getRoute(URI_UPDATE_INVALID_SUBSCRIPTIONS), userSubscriptionsRest::updateInvalidSubscriptions)
                .andRoute(getRoute(URI_PREPARE_ACTUAL_SUBSCRIPTIONS), userSubscriptionsRest::prepareActualSubscriptions)
                // avatars
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_AVATAR), userSubscriptionsRest::createOrUpdateAvatar)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_CUSTOM_AVATAR), userSubscriptionsRest::createOrUpdateCustomAvatar)
                .andRoute(getRoute(URI_GET_AVATAR), userSubscriptionsRest::getAvatar)
                .andRoute(getRoute(URI_GET_CUSTOM_AVATAR), userSubscriptionsRest::getCustomAvatar)
                // devices
                .andRoute(postRoute(URI_CREATE_DEVICE), waMonitoringRest::createOrUpdateDevice)
                .andRoute(getRoute(URI_REMOVE_DEVICE_BY_ID), devicesRest::removeDevicebyId)
                //.andRoute(postRoute(URI_CREATE_DEVICE_SESSION), waMonitoringRest::createDeviceSession)
                //.andRoute(postRoute(URI_CREATE_TOKEN), waMonitoringRest::createUserToken)
                .andRoute(postRoute(URI_UPDATE_DEVICE_ATTRS), devicesRest::updateDeviceAttrs)
                .andRoute(getRoute(URI_GET_ALL_USER_DEVICES), userSubscriptionsRest::getAllUserDevices)
                .andRoute(getRoute(URI_GET_EXISTS_DEVICE), usersRest::existsDevice)
                // activities
                .andRoute(postRoute(URI_CREATE_ACTIVITIES), activitiesRest::createActivities)
                .andRoute(getRoute(URI_GET_ACTIVITIES), activitiesRest::getActivitiesD1D2)
                .andRoute(getRoute(URI_GET_LATEST_ACTIVITIES), activitiesRest::getLatestActivities)
                .andRoute(getRoute(URI_GET_SUBSCRIPTIONS_LATEST_ACTIVITIES), activitiesRest::getSubscriptionsLatestActivities)
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
        whiteUrls.add(URI_GET_AGENT_MESSAGE);
        whiteUrls.add(URI_CREATE_OR_UPDATE_AGENT_MESSAGE);
        whiteUrls.add(URI_GET_ACTUAL_MESSAGES_COUNT);
        whiteUrls.add(URI_GET_MESSAGING_SUBSCRIPTION);
        whiteUrls.add(URI_GET_MESSAGING_LAST_CASHE);

        return whiteUrls.toArray(new String[]{});
    }

    @Override
    public void initialize() {

        super.initialize();

        refsService.synchronizeRefs();
    }
}
