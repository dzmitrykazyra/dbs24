/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.dbs24.config.AbstractWebSecurityConfig;
import static org.dbs24.consts.RestHttpConsts.HTTP_200_STRING;

import org.dbs24.tik.assist.constant.RequestQueryParam;
import org.dbs24.tik.assist.entity.domain.UserSubscription;
import org.dbs24.tik.assist.entity.dto.bot.BotIdList;
import org.dbs24.tik.assist.entity.dto.bot.response.AvailableBotsByAwemeIdResponse;
import org.dbs24.tik.assist.entity.dto.bot.BotDto;
import org.dbs24.tik.assist.entity.dto.bot.CreatedBotDto;
import org.dbs24.tik.assist.entity.dto.cart.CartOrderDto;
import org.dbs24.tik.assist.entity.dto.cart.CreatedCartOrderDto;
import org.dbs24.tik.assist.entity.dto.constraint.CustomPlanConstraintsDto;
import org.dbs24.tik.assist.entity.dto.order.*;
import org.dbs24.tik.assist.entity.dto.payment.IncreaseUserDepositDto;
import org.dbs24.tik.assist.entity.dto.payment.MonthlySubscriptionPaymentDto;
import org.dbs24.tik.assist.entity.dto.payment.UserDepositBalanceDto;
import org.dbs24.tik.assist.entity.dto.phone.*;
import org.dbs24.tik.assist.entity.dto.plan.CustomPlanConstraint;
import org.dbs24.tik.assist.entity.dto.plan.CustomUserPlanDto;
import org.dbs24.tik.assist.entity.dto.plan.response.CreatedCustomUserPlanDto;
import org.dbs24.tik.assist.entity.dto.plan.template.PlanTemplateActionDto;
import org.dbs24.tik.assist.entity.dto.plan.template.PlanTemplateDto;
import org.dbs24.tik.assist.entity.dto.plan.template.PlanTemplateListDto;
import org.dbs24.tik.assist.entity.dto.proportion.*;
import org.dbs24.tik.assist.entity.dto.statistics.OrderStatisticsDtoList;
import org.dbs24.tik.assist.entity.dto.subscription.*;
import org.dbs24.tik.assist.entity.dto.subscription.UserSubscriptionIdDto;
;
import org.dbs24.tik.assist.entity.dto.tiktok.*;
import org.dbs24.tik.assist.entity.dto.tiktok.response.AccountActionResponseDto;
import org.dbs24.tik.assist.entity.dto.tiktok.response.AccountAddResponseDto;
import org.dbs24.tik.assist.entity.dto.tiktok.response.AccountDeleteResponseDto;
import org.dbs24.tik.assist.entity.dto.user.*;
import org.dbs24.tik.assist.entity.dto.user.AuthDto;
import org.dbs24.tik.assist.entity.dto.user.UserIdDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.dbs24.tik.assist.rest.*;
import static org.dbs24.tik.assist.constant.ApiPath.*;
import static org.dbs24.tik.assist.constant.ApiPath.EmuTest.*;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Value;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Log4j2
@Configuration
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "tik-assist")
public class TikAssistRestConfig extends AbstractWebSecurityConfig {

    private static final String SUBSCRIPTIONS_SWAGGER_TAG = "Subscriptions";
    private static final String PLANS_SWAGGER_TAG = "Plans";
    private static final String BOTS_SWAGGER_TAG = "Bots";
    private static final String PHONES_SWAGGER_TAG = "Phones";
    private static final String PROFILES_SWAGGER_TAG = "Profiles";
    private static final String PLAN_TEMPLATES_SWAGGER_TAG = "Plan templates";
    private static final String USER_ACTIONS_SWAGGER_TAG = "User actions";
    private static final String ORDERS_SWAGGER_TAG = "Orders";
    private static final String PROPORTIONS_SWAGGER_TAG = "Proportions";
    private static final String USER_STATISTICS_SWAGGER_TAG = "User statistics";
    private static final String PAYMENTS_SWAGGER_TAG = "Payments";
    private static final String CARTS_SWAGGER_TAG = "Carts";
    private static final String PROMOCODES_TAG = "Promocodes";

    public static final String AUTHORIZATION_HEADER_NAME = "Authorization1"; /*todo HttpHeaders.AUTHORIZATION*/

    @Value("${config.tik-connector.emu.enabled:false}")
    private Boolean addEmuRoutes;

    private RouterFunction<ServerResponse> addEmuRoutes(RouterFunction<ServerResponse> mainRoutes, TikConnectorEmuRest tikConnectorEmuRest) {

        return addEmuRoutes ? mainRoutes
                .andRoute(postRoute(URI_CREATE_TASK_4_TIK_CONNECTOR), tikConnectorEmuRest::createTikAction)
                .andRoute(getRoute(URI_GET_TASK_STATUS_4_TIK_CONNECTOR), tikConnectorEmuRest::getTikActionStatus)
                .andRoute(postRoute(URI_CREATE_AGENT_TASK_4_TIK_CONNECTOR), tikConnectorEmuRest::createRepairTaskAction)
                .andRoute(getRoute(URI_GET_AGENT_STATUS_4_TIK_CONNECTOR), tikConnectorEmuRest::getAgentRepairActionStatus)
                : mainRoutes;
    }

    @RouterOperations({
            //subscriptions
            @RouterOperation(
                    path = URI_CREATE_USER_SUBSCRIPTION_BY_TEMPLATE,
                    method = POST,
                    operation = @Operation(
                            tags = SUBSCRIPTIONS_SWAGGER_TAG,
                            operationId = URI_CREATE_USER_SUBSCRIPTION_BY_TEMPLATE,
                            requestBody = @RequestBody(
                                    description = "User subscription info",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = ByTemplateUserSubscriptionDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = HTTP_200_STRING,
                                            description = "Created user subscription info",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = UserSubscriptionIdDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "This tiktokAccount is not linked to user"
                                    ),
                                    @ApiResponse(
                                            responseCode = "403",
                                            description = "Tiktok account also have subscription"
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_CREATE_USER_SUBSCRIPTION_CUSTOM,
                    method = POST,
                    operation = @Operation(
                            tags = SUBSCRIPTIONS_SWAGGER_TAG,
                            operationId = URI_CREATE_USER_SUBSCRIPTION_CUSTOM,
                            requestBody = @RequestBody(
                                    description = "User subscription info",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CustomUserSubscriptionDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = HTTP_200_STRING,
                                            description = "Created user subscription info",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = UserSubscriptionIdDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "This tiktokAccount is not linked to user"
                                    ),
                                    @ApiResponse(
                                            responseCode = "403",
                                            description = "Tiktok account also have subscription"
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_UPDATE_USER_SUBSCRIPTION_BY_TEMPLATE,
                    method = PUT,
                    operation = @Operation(
                            tags = SUBSCRIPTIONS_SWAGGER_TAG,
                            operationId = URI_UPDATE_USER_SUBSCRIPTION_BY_TEMPLATE,
                            requestBody = @RequestBody(
                                    description = "Dto with user subscription field values to update",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UpdateUserSubscriptionByTemplateDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Dto with updated user subscription field values",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = UserSubscriptionIdDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "User has no active subscription"
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_UPDATE_USER_SUBSCRIPTION_CUSTOM,
                    method = PUT,
                    operation = @Operation(
                            tags = SUBSCRIPTIONS_SWAGGER_TAG,
                            operationId = URI_UPDATE_USER_SUBSCRIPTION_CUSTOM,
                            requestBody = @RequestBody(
                                    description = "Dto with user subscription field values to update",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CustomPlanConstraint.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Dto with updated user subscription field values",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = UserSubscriptionIdDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "User has no active subscription"
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_CALCULATE_SUBSCRIPTION_SUM,
                    method = PUT,
                    operation = @Operation(
                            tags = SUBSCRIPTIONS_SWAGGER_TAG,
                            operationId = URI_CALCULATE_SUBSCRIPTION_SUM,
                            requestBody = @RequestBody(
                                    description = "Subscription/plan to calculate sum data",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CalculateSubscriptionCostDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "User subscription/plan cost",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = SubscriptionCostDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid subscription/plan data"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = URI_UNDO_SUBSCRIPTION,
                    method = DELETE,
                    operation = @Operation(
                            tags = SUBSCRIPTIONS_SWAGGER_TAG,
                            operationId = URI_UNDO_SUBSCRIPTION,
                            requestBody = @RequestBody(
                                    description = "User subscription id to undo",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UserSubscriptionIdDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = HTTP_200_STRING,
                                            description = "Cancelled user subscription id DTO",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = TiktokPlanDtoList.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "JWT is invalid"
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            //plans
            @RouterOperation(
                    path = URI_CREATE_CUSTOM_PLAN,
                    method = POST,
                    operation = @Operation(
                            tags = PLANS_SWAGGER_TAG,
                            operationId = URI_CREATE_CUSTOM_PLAN,
                            requestBody = @RequestBody(
                                    description = "Dto with custom user plan field values to create",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CustomUserPlanDto.class
                                            )
                                    )
                            ),
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Dto with created custom user plan field values",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreatedCustomUserPlanDto.class
                                            )
                                    )
                            ),
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            //bots
            @RouterOperation(
                    path = URI_CREATE_OR_UPDATE_BOT,
                    method = POST,
                    operation = @Operation(
                            tags = BOTS_SWAGGER_TAG,
                            operationId = URI_CREATE_OR_UPDATE_BOT,
                            requestBody = @RequestBody(
                                    description = "Bot info",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = BotDto.class
                                            )
                                    )
                            ),
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Create new bot",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreatedBotDto.class
                                            )
                                    )
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_GET_BOT_BY_ID,
                    method = GET,
                    operation = @Operation(
                            tags = BOTS_SWAGGER_TAG,
                            operationId = URI_GET_BOT_BY_ID,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Agent details",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = BotDto.class
                                            )
                                    )
                            ),
                            parameters = @Parameter(
                                    name = RequestQueryParam.QP_BOT_ID,
                                    description = "internal agentId",
                                    in = ParameterIn.QUERY,
                                    schema = @Schema(type = "integer")
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_GET_AVAILABLE_BOTS_BY_AWEME_ID,
                    method = GET,
                    operation = @Operation(
                            tags = BOTS_SWAGGER_TAG,
                            operationId = URI_GET_AVAILABLE_BOTS_BY_AWEME_ID,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Get available agents",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = AvailableBotsByAwemeIdResponse.class
                                            )
                                    )
                            ),
                            parameters = @Parameter(
                                    name = RequestQueryParam.QP_AWEME_ID,
                                    description = "internal aweme id",
                                    in = ParameterIn.QUERY,
                                    schema = @Schema(type = "string")
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_GET_AVAILABLE_BOTS_BY_SEC_USER_ID,
                    method = GET,
                    operation = @Operation(
                            tags = BOTS_SWAGGER_TAG,
                            operationId = URI_GET_AVAILABLE_BOTS_BY_SEC_USER_ID,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Get available agents by secUserId",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = AvailableBotsByAwemeIdResponse.class
                                            )
                                    )
                            ),
                            parameters = @Parameter(
                                    name = RequestQueryParam.QP_SEC_USER_ID,
                                    description = "secUserId",
                                    in = ParameterIn.QUERY,
                                    schema = @Schema(type = "string")
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_GET_ACTIVE_BOTS_IDS,
                    method = GET,
                    operation = @Operation(
                            tags = BOTS_SWAGGER_TAG,
                            operationId = URI_GET_ACTIVE_BOTS_IDS,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Id of bot with status active",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = BotIdList.class
                                            )
                                    )
                            )
                    )
            ),
            // phones
            @RouterOperation(
                    path = URI_CREATE_OR_UPDATE_PHONE,
                    method = POST,
                    operation = @Operation(
                            tags = PHONES_SWAGGER_TAG,
                            operationId = URI_CREATE_OR_UPDATE_PHONE,
                            requestBody = @RequestBody(
                                    description = "Phone info",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = PhoneDto.class
                                            )
                                    )
                            ),
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Create or update phone",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreatedPhoneDto.class
                                            )
                                    )
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_CREATE_OR_UPDATE_PHONE_USAGE,
                    method = POST,
                    operation = @Operation(
                            tags = PHONES_SWAGGER_TAG,
                            operationId = URI_CREATE_OR_UPDATE_PHONE_USAGE,
                            requestBody = @RequestBody(
                                    description = "Phone usage info",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = PhoneUsageDto.class
                                            )
                                    )
                            ),
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Create or update phone usage",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreatedPhoneUsageDto.class
                                            )
                                    )
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_GET_PHONE,
                    method = GET,
                    operation = @Operation(
                            tags = PHONES_SWAGGER_TAG,
                            operationId = URI_GET_PHONE,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING, description = "" +
                                    "Get phone info",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = PhoneDto.class
                                            )
                                    )
                            ),
                            parameters = @Parameter(
                                    name = RequestQueryParam.QP_PHONE_ID,
                                    description = "phone id",
                                    in = ParameterIn.QUERY,
                                    schema = @Schema(
                                            type = "string")
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_GET_AVAILABLE_PHONE,
                    method = GET,
                    operation = @Operation(
                            tags = PHONES_SWAGGER_TAG,
                            operationId = URI_GET_AVAILABLE_PHONE,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING, description = "Get available phone info",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = PhoneDto.class
                                            )
                                    )
                            )
                    )
            ),
            //profile
            @RouterOperation(
                    path = URI_GET_USER_BOUNDED_ACCOUNTS,
                    method = GET,
                    operation = @Operation(
                            tags = PROFILES_SWAGGER_TAG,
                            operationId = URI_GET_USER_BOUNDED_ACCOUNTS,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING, description = "Get bounded with application user tiktok accounts",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = TiktokPlanDtoList.class
                                            )
                                    )
                            ),
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_GET_LAST_SELECTED_USER_BOUNDED_ACCOUNT,
                    method = POST,
                    operation = @Operation(
                            tags = PROFILES_SWAGGER_TAG,    
                            operationId = URI_GET_LAST_SELECTED_USER_BOUNDED_ACCOUNT,
                            requestBody = @RequestBody(
                                    description = "DTO with optional last selected tiktok account by required user",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = LastSelectedAccountDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Tiktok account info to display profile information",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = TiktokAccountInfoDto.class

                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "204",
                                            description = "User has no any bounded tiktok account"
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Tiktok account with required username does not exist"
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_ADD_TIKTOK_ACCOUNT_TO_PROFILE,
                    method = POST,
                    operation = @Operation(
                            tags = PROFILES_SWAGGER_TAG,
                            operationId = URI_ADD_TIKTOK_ACCOUNT_TO_PROFILE,
                            requestBody = @RequestBody(
                                    description = "Tiktok account info",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = TiktokAccountDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Created account-user bound record info",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = AccountAddResponseDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Tiktok account by required username not found"
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_REMOVE_TIKTOK_ACCOUNT_FROM_PROFILE,
                    method = DELETE,
                    operation = @Operation(
                            tags = PROFILES_SWAGGER_TAG,
                            operationId = URI_REMOVE_TIKTOK_ACCOUNT_FROM_PROFILE,
                            requestBody = @RequestBody(
                                    description = "Tiktok account info",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = TiktokAccountDto.class
                                            )
                                    )
                            ),
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Loosed account-user bound record info",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = AccountDeleteResponseDto.class
                                            )
                                    )
                            ),
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_FIND_PLAN_TIKTOK_ACCOUNT,
                    method = POST,
                    operation = @Operation(
                            tags = PROFILES_SWAGGER_TAG,
                            operationId = URI_FIND_PLAN_TIKTOK_ACCOUNT,
                            requestBody = @RequestBody(
                                    description = "Tiktok account info",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = TiktokAccountDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = HTTP_200_STRING,
                                            description = "Plan of tiktok-account",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = TiktokPlanDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "JWT is invalid"
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Tiktok account is not linked to this user"
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )

                    )
            ),
            @RouterOperation(
                    path = URI_GET_USER_ACCOUNTS_SUBSCRIPTION,
                    method = GET,
                    operation = @Operation(
                            tags = PROFILES_SWAGGER_TAG,
                            operationId = URI_GET_USER_ACCOUNTS_SUBSCRIPTION,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING, description = "Get subscriptions tiktok accounts",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = TiktokExistSubscriptionDtoList.class
                                            )
                                    )
                            ),
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            //Plan templates
            @RouterOperation(
                    path = URI_CREATE_OR_UPDATE_PLAN_TEMPLATE,
                    method = POST,
                    operation = @Operation(
                            tags = PLAN_TEMPLATES_SWAGGER_TAG,
                            operationId = URI_CREATE_OR_UPDATE_PLAN_TEMPLATE,
                            parameters = @Parameter(
                                    name = RequestQueryParam.QP_STAFF_PASS,
                                    description = "Staff to plan template change access",
                                    in = ParameterIn.QUERY,
                                    schema = @Schema(
                                            type = "string"
                                    )
                            ),
                            requestBody = @RequestBody(
                                    description = "Plan template info",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = PlanTemplateDto.class
                                            )
                                    )
                            ),
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Created plan template dto",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = PlanTemplateActionDto.class
                                            )
                                    )
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_GET_PLAN_TEMPLATE_BY_NAME,
                    method = GET,
                    operation = @Operation(
                            tags = PLAN_TEMPLATES_SWAGGER_TAG,
                            operationId = URI_GET_PLAN_TEMPLATE_BY_NAME,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Plan template info by name",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = PlanTemplateDto.class
                                            )
                                    )
                            ),
                            parameters = @Parameter(
                                    name = RequestQueryParam.QP_PLAN_TEMPLATE_NAME,
                                    description = "Plan template name",
                                    in = ParameterIn.QUERY,
                                    schema = @Schema(
                                            type = "string")
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_GET_ALL_PLAN_TEMPLATES,
                    method = GET,
                    operation = @Operation(
                            tags = PLAN_TEMPLATES_SWAGGER_TAG,
                            operationId = URI_GET_ALL_PLAN_TEMPLATES,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Dto containing list of plan template and information about customPlan. ratio is price for max value of metric." +
                                            " For example: ratio: 8.33 for 10000 likes. It is means 1 like costs 0.000833",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = PlanTemplateListDto.class
                                            )
                                    )
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_CHANGE_PLAN_TEMPLATE_TO_NOT_ACTIVE,
                    method = DELETE,
                    operation = @Operation(
                            tags = PLAN_TEMPLATES_SWAGGER_TAG,
                            operationId = URI_CHANGE_PLAN_TEMPLATE_TO_NOT_ACTIVE,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Invalidated plan template dto",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = PlanTemplateDto.class
                                            )
                                    )
                            ),
                            parameters = {
                                    @Parameter(
                                            name = RequestQueryParam.QP_PLAN_TEMPLATE_ID,
                                            description = "Plat template id to invalidate",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(
                                                    type = "integer"
                                            )),
                                    @Parameter(
                                            name = RequestQueryParam.QP_STAFF_PASS,
                                            description = "Staff to plan template change access",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(
                                                    type = "string"
                                            ))
                            }
                    )
            ),
            // Users
            @RouterOperation(
                    path = URI_REGISTER,
                    method = POST,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = URI_REGISTER,
                            requestBody = @RequestBody(
                                    description = "Default user registration",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UserDto.class
                                            ))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Pre-registered user activation info",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = UserActivationDto.class

                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "409",
                                            description = "User was already registered response"
                                    ),
                                    @ApiResponse(
                                            responseCode = "422",
                                            description = "Error during email sending (maybe, email does not exist)"
                                    )}
                    )
            ),
            @RouterOperation(
                    path = URI_RESEND_ACTIVATION_EMAIL,
                    method = PUT,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = URI_RESEND_ACTIVATION_EMAIL,
                            requestBody = @RequestBody(
                                    description = "User email address ot resend activation email",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UserEmailDto.class
                                            ))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Pre-registered user activation info",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = UserActivationDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "User is not registered to activate required email"
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "User is already activated"
                                    ),
                                    @ApiResponse(
                                            responseCode = "422",
                                            description = "Error during email sending (maybe, email does not exist)"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = URI_AUTH,
                    method = GET,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = URI_AUTH,
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Refreshed user token DTO",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = AuthDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "JWT is invalid"
                                    ),
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_ACTIVATE_USER,
                    method = PUT,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = URI_ACTIVATE_USER,
                            requestBody = @RequestBody(
                                    description = "Pre-registered user activation info(activation key to verify)",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UserActivationDto.class
                                            ))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Activated user response(brand new JWT)",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = AuthDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "No such activation key in store (maybe resend email)"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = URI_CHANGE_PASSWORD,
                    method = PUT,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = URI_CHANGE_PASSWORD,
                            requestBody = @RequestBody(
                                    description = "Old and new password DTO",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = ChangePasswordDto.class
                                            ))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "DTO containing JWT",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = AuthDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Password mismatch"
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_FORGOT_PASSWORD,
                    method = POST,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = URI_FORGOT_PASSWORD,
                            requestBody = @RequestBody(
                                    description = "DTO containing user email to send email",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = ForgotPasswordDto.class
                                            ))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "DTO containing user id",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = UserIdDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "User is not registered"
                                    ),
                                    @ApiResponse(
                                            responseCode = "422",
                                            description = "Error during email sending (maybe, email does not exist)"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = URI_FORGOT_PASSWORD_AUTHENTICATED,
                    method = GET,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = URI_FORGOT_PASSWORD_AUTHENTICATED,
                            requestBody = @RequestBody(
                                    description = "Endpoint for authenticated user forgotten password to get new password creating email"
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "DTO containing user id",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = UserIdDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "422",
                                            description = "Error during email sending (maybe, email does not exist)"
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_CHANGE_FORGOTTEN_PASSWORD,
                    method = PUT,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = URI_CHANGE_FORGOTTEN_PASSWORD,
                            requestBody = @RequestBody(
                                    description = "Link from email is something like .../userKey/expirationKey; This DTO consist of extracted from email link: userKey, expirationKey, new user password",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = ChangeForgottenPasswordDto.class
                                            ))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "DTO containing JWT",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = AuthDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "DTO is invalid(maybe key is expired)"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = URI_LOGIN,
                    method = POST,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = URI_LOGIN,
                            requestBody = @RequestBody(
                                    description = "User to login credentials",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UserDto.class
                                            ))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Verified user JWT",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = AuthDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "422",
                                            description = "User with such email not found"
                                    ),
                                    @ApiResponse(
                                            responseCode = "403",
                                            description = "Incorrect password"
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "User was not activated"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = URI_LOGIN_WITH_FACEBOOK,
                    method = POST,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = URI_LOGIN_WITH_FACEBOOK,
                            requestBody = @RequestBody(
                                    description = "Got from facebook OAuth 2.0 API data (email and phone number are optional)",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = FacebookLoginUserDto.class
                                            ))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = HTTP_200_STRING,
                                            description = "Verified by facebook API user JWT",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = AuthDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "403",
                                            description = "Facebook account is not verified"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = URI_LOGIN_WITH_GOOGLE,
                    method = POST,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = URI_LOGIN_WITH_GOOGLE,
                            requestBody = @RequestBody(
                                    description = "Got from google OAuth 2.0 API data",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = GoogleLoginUserDto.class
                                            ))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = HTTP_200_STRING,
                                            description = "Verified by google API user JWT",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = AuthDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "403",
                                            description = "Google account is not verified"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = URI_VERIFY_KEY_SET,
                    method = POST,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = URI_VERIFY_KEY_SET,
                            requestBody = @RequestBody(
                                    description = "DTO containing userKey and expirationKey got from change password email",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = PasswordKeySetDto.class
                                            ))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Key set is valid",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = Boolean.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Key set is invalid"
                                    )}
                    )
            ),
            @RouterOperation(
                    path = URI_LOGOUT,
                    method = GET,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = URI_LOGOUT,
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "Logout user id dto",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UserIdDto.class
                                            )
                                    )
                            ),
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_CREATE_LIKES_ORDER,
                    method = POST,
                    operation = @Operation(
                            tags = ORDERS_SWAGGER_TAG,
                            operationId = URI_CREATE_LIKES_ORDER,
                            requestBody = @RequestBody(
                                    description = "Create likes order",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreateVideoOrderDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Created likes order id DTO",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = CreatedUserOrderDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Required actions quantity out of possible constraints"
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_CREATE_FOLLOWERS_ORDER,
                    method = POST,
                    operation = @Operation(
                            tags = ORDERS_SWAGGER_TAG,
                            operationId = URI_CREATE_FOLLOWERS_ORDER,
                            requestBody = @RequestBody(
                                    description = "Create followers order",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreateFollowersOrderDto.class
                                            )
                                    )),
                            responses = {
                                    @ApiResponse(
                                            responseCode = HTTP_200_STRING,
                                            description = "Created followers order id DTO",
                                            content = @Content(
                                                    schema = @Schema(

                                                            implementation = CreatedUserOrderDto.class

                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Required actions quantity out of possible constraints"
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_CREATE_VIEWS_ORDER,
                    method = POST,
                    operation = @Operation(
                            tags = ORDERS_SWAGGER_TAG,
                            operationId = URI_CREATE_VIEWS_ORDER,
                            requestBody = @RequestBody(
                                    description = "Create views order",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreateVideoOrderDto.class
                                            )
                                    )),
                            responses = {
                                    @ApiResponse(
                                            responseCode = HTTP_200_STRING,
                                            description = "Created views order id DTO",
                                            content = @Content(
                                                    schema = @Schema(

                                                            implementation = CreatedUserOrderDto.class

                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Required actions quantity out of possible constraints"
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_VERIFY_LIKES_ORDER_DATA,
                    method = PUT,
                    operation = @Operation(
                            tags = ORDERS_SWAGGER_TAG,
                            operationId = URI_VERIFY_LIKES_ORDER_DATA,
                            requestBody = @RequestBody(
                                    description = "Verify likes order to create data",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreateVideoOrderDto.class
                                            )
                                    )),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Order validity DTO",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = OrderValidityDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Order is invalid",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = OrderValidityDto.class
                                                    )
                                            )
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_VERIFY_FOLLOWERS_ORDER_DATA,
                    method = PUT,
                    operation = @Operation(
                            tags = ORDERS_SWAGGER_TAG,
                            operationId = URI_VERIFY_FOLLOWERS_ORDER_DATA,
                            requestBody = @RequestBody(
                                    description = "Verify followers order to create data",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreateFollowersOrderDto.class
                                            )
                                    )),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Order validity DTO",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = OrderValidityDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Order is invalid",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = OrderValidityDto.class
                                                    )
                                            )
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_VERIFY_VIEWS_ORDER_DATA,
                    method = PUT,
                    operation = @Operation(
                            tags = ORDERS_SWAGGER_TAG,
                            operationId = URI_VERIFY_VIEWS_ORDER_DATA,
                            requestBody = @RequestBody(
                                    description = "Verify likes order to create data",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreateVideoOrderDto.class
                                            )
                                    )),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Order validity DTO",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = OrderValidityDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Order is invalid",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = OrderValidityDto.class
                                                    )
                                            )
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            //Proportions
            @RouterOperation(
                    path = URI_CREATE_OR_UPDATE_ACTIONS_PROPORTION,
                    method = POST,
                    operation = @Operation(
                            tags = PROPORTIONS_SWAGGER_TAG,
                            operationId = URI_CREATE_OR_UPDATE_ACTIONS_PROPORTION,
                            requestBody = @RequestBody(
                                    description = "Create or update sum to action type by actions quantity proportion record",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = ActionsProportionDto.class
                                            )
                                    )),
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Created/updated action type proportion data",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreatedActionsProportionDto.class
                                            )
                                    )
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_CREATE_OR_UPDATE_ACCOUNTS_PROPORTION,
                    method = POST,
                    operation = @Operation(
                            tags = PROPORTIONS_SWAGGER_TAG,
                            operationId = URI_CREATE_OR_UPDATE_ACCOUNTS_PROPORTION,
                            requestBody = @RequestBody(
                                    description = "Create or update sum to accounts quantity by actions quantity proportion record",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = AccountsProportionDto.class
                                            )
                                    )),
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Created/updated accounts quantity data",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreatedAccountsProportionDto.class
                                            )
                                    )
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_GET_ALL_ACTIONS_PROPORTIONS,
                    method = GET,
                    operation = @Operation(
                            tags = PROPORTIONS_SWAGGER_TAG,
                            operationId = URI_GET_ALL_ACTIONS_PROPORTIONS,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Get all action-depending proportions",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = ActionsProportionDtoList.class
                                            )
                                    )
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_GET_ALL_ACCOUNTS_PROPORTIONS,
                    method = GET,
                    operation = @Operation(
                            tags = PROPORTIONS_SWAGGER_TAG,
                            operationId = URI_GET_ALL_ACCOUNTS_PROPORTIONS,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Get all account-quantity-depending proportions",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = AccountsProportionDtoList.class
                                            )
                                    )
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_GET_CUSTOM_PLAN_MAX_CONSTRAINTS,
                    method = GET,
                    operation = @Operation(
                            tags = PROPORTIONS_SWAGGER_TAG,
                            operationId = URI_GET_ALL_ACCOUNTS_PROPORTIONS,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Get all custom plan max value constraints",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CustomPlanConstraintsDto.class
                                            )
                                    )
                            )
                    )
            ),
            //User statistics
            @RouterOperation(
                    path = URI_GET_ACTIVE_USER_SUBSCRIPTION,
                    method = GET,
                    operation = @Operation(
                            tags = USER_STATISTICS_SWAGGER_TAG,
                            operationId = URI_GET_ACTIVE_USER_SUBSCRIPTION,
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Get active user's subscription plans",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = UserSubscriptionPlanStatisticsDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "204",
                                            description = "User has no any active subscription"
                                    )
                            },
                            parameters = {
                                    @Parameter(
                                            name = AUTHORIZATION_HEADER_NAME,
                                            in = ParameterIn.HEADER,
                                            required = true,
                                            description = "JWT header"
                                    ),
                                    @Parameter(
                                            name = RequestQueryParam.QP_TIKTOK_USERNAME,
                                            description = "Tiktok username to perform disparate profile actions",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = URI_GET_ACTIVE_USER_ORDERS_PROGRESSES,
                    method = GET,
                    operation = @Operation(
                            tags = USER_STATISTICS_SWAGGER_TAG,
                            operationId = URI_GET_ACTIVE_USER_ORDERS_PROGRESSES,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Get active orders list with data and order progess in %",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = OrderStatisticsDtoList.class
                                            )
                                    )
                            ),
                            parameters = {
                                    @Parameter(
                                            name = AUTHORIZATION_HEADER_NAME,
                                            in = ParameterIn.HEADER,
                                            required = true,
                                            description = "JWT header"
                                    ),
                                    @Parameter(
                                            name = RequestQueryParam.QP_TIKTOK_USERNAME,
                                            description = "Tiktok username to perform disparate profile actions",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = URI_GET_USER_ORDERS_HISTORY_PAGES_QUANTITY,
                    method = GET,
                    operation = @Operation(
                            tags = USER_STATISTICS_SWAGGER_TAG,
                            operationId = URI_GET_USER_ORDERS_HISTORY_PAGES_QUANTITY,
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Total user closed orders quantity by tiktok account",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = OrderPagesQuantityDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Account does not exist"
                                    )
                            },
                            parameters = {
                                    @Parameter(
                                            name = AUTHORIZATION_HEADER_NAME,
                                            in = ParameterIn.HEADER,
                                            required = true,
                                            description = "JWT header"
                                    ),
                                    @Parameter(
                                            name = RequestQueryParam.QP_TIKTOK_USERNAME,
                                            description = "Tiktok username to perform disparate profile actions",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = URI_GET_USER_ORDERS_HISTORY,
                    method = GET,
                    operation = @Operation(
                            tags = USER_STATISTICS_SWAGGER_TAG,
                            operationId = URI_GET_USER_ORDERS_HISTORY,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Get finished orders list",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = OrderStatisticsDtoList.class
                                            )
                                    )
                            ),
                            parameters = {
                                    @Parameter(
                                            name = AUTHORIZATION_HEADER_NAME,
                                            in = ParameterIn.HEADER,
                                            required = true,
                                            description = "JWT header"
                                    ),
                                    @Parameter(
                                            name = RequestQueryParam.QP_PAGE_NUMBER,
                                            description = "User history display page number",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "integer")
                                    ),
                                    @Parameter(
                                            name = RequestQueryParam.QP_TIKTOK_USERNAME,
                                            description = "Tiktok username to perform disparate profile actions",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = URI_CLEAR_USER_ORDERS_HISTORY,
                    method = DELETE,
                    operation = @Operation(
                            tags = USER_STATISTICS_SWAGGER_TAG,
                            operationId = URI_CLEAR_USER_ORDERS_HISTORY,
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "Clear finished user's orders",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = OrderStatisticsDtoList.class
                                            )
                                    )
                            ),
                            parameters = {
                                    @Parameter(
                                            name = AUTHORIZATION_HEADER_NAME,
                                            in = ParameterIn.HEADER,
                                            required = true,
                                            description = "JWT header"
                                    ),
                                    @Parameter(
                                            name = RequestQueryParam.QP_TIKTOK_USERNAME,
                                            description = "Tiktok username to perform disparate profile actions",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = URI_CLEAR_USER_ORDER_HISTORY_BY_ID,
                    method = DELETE,
                    operation = @Operation(
                            tags = USER_STATISTICS_SWAGGER_TAG,
                            operationId = URI_CLEAR_USER_ORDER_HISTORY_BY_ID,
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Clear finished user's orders",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = OrderStatisticsDtoList.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Order record with required id not found"
                                    ),
                            },
                            parameters = {
                                    @Parameter(
                                            name = AUTHORIZATION_HEADER_NAME,
                                            in = ParameterIn.HEADER,
                                            required = true,
                                            description = "JWT header"
                                    ),
                                    @Parameter(
                                            name = RequestQueryParam.QP_ORDER_ID,
                                            description = "Order to data return id",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "integer")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = URI_GET_VIDEO_ORDER_DETAILS_BY_ID,
                    method = GET,
                    operation = @Operation(
                            tags = USER_STATISTICS_SWAGGER_TAG,
                            operationId = URI_GET_VIDEO_ORDER_DETAILS_BY_ID,
                            responses = {
                                    @ApiResponse(
                                            responseCode = HTTP_200_STRING,
                                            description = "Finished user video order (get likes/views/shares/comments actions) data",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = UserVideoOrderDetailsDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Order action types mismatch"
                                    )
                            },
                            parameters = {
                                    @Parameter(
                                            name = AUTHORIZATION_HEADER_NAME,
                                            in = ParameterIn.HEADER,
                                            required = true,
                                            description = "JWT header"
                                    ),
                                    @Parameter(
                                            name = RequestQueryParam.QP_ORDER_ID,
                                            description = "Order to data return id",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "integer")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = URI_GET_ACCOUNT_ORDER_DETAILS_BY_ID,
                    method = GET,
                    operation = @Operation(
                            tags = USER_STATISTICS_SWAGGER_TAG,
                            operationId = URI_GET_ACCOUNT_ORDER_DETAILS_BY_ID,
                            responses = {
                                    @ApiResponse(
                                            responseCode = HTTP_200_STRING,
                                            description = "Finished user account order (get followers actions) data",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = UserAccountOrderDetailsDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Order action types mismatch"
                                    )
                            },
                            parameters = {
                                    @Parameter(
                                            name = AUTHORIZATION_HEADER_NAME,
                                            in = ParameterIn.HEADER,
                                            required = true,
                                            description = "JWT header"
                                    ),
                                    @Parameter(
                                            name = RequestQueryParam.QP_ORDER_ID,
                                            description = "Order to data return id",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "integer")
                                    )
                            }
                    )
            ),
            //Deposits
            @RouterOperation(
                    path = URI_INCREASE_DEPOSIT,
                    method = PUT,
                    operation = @Operation(
                            tags = PAYMENTS_SWAGGER_TAG,
                            operationId = URI_INCREASE_DEPOSIT,
                            requestBody = @RequestBody(
                                    description = "Dto with increase account deposit sum",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = IncreaseUserDepositDto.class
                                            )
                                    )),
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "Actual user deposit balance",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UserDepositBalanceDto.class
                                            )
                                    )
                            ),
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_PAY_FOR_SUBSCRIPTION,
                    method = POST,
                    operation = @Operation(
                            tags = PAYMENTS_SWAGGER_TAG,
                            operationId = URI_PAY_FOR_SUBSCRIPTION,
                            requestBody = @RequestBody(
                                    description = "DTO with user subscription info to monthly extend",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = MonthlySubscriptionPaymentDto.class
                                            )
                                    )),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "User extended subscription id DTO",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = UserIdDto.class

                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "204",
                                            description = "User has no anu active subscription"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = URI_GET_DEPOSIT_BALANCE,
                    method = GET,
                    operation = @Operation(
                            tags = PAYMENTS_SWAGGER_TAG,
                            operationId = URI_GET_DEPOSIT_BALANCE,
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "Actual user deposit balance",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UserDepositBalanceDto.class
                                            )
                                    )
                            ),
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            //Cart
            @RouterOperation(
                    path = URI_PAY_FOR_CART,
                    method = POST,
                    operation = @Operation(
                            tags = CARTS_SWAGGER_TAG,
                            operationId = URI_PAY_FOR_CART,
                            requestBody = @RequestBody(
                                    description = "User cart data to create",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CartOrderDto.class
                                            )
                                    )),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Created user cart data",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = CreatedCartOrderDto.class

                                                    )
                                            )
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            ),
            //Promocodes
            @RouterOperation(
                    path = URI_SEND_MAILING_PROMOCODE,
                    method = POST,
                    operation = @Operation(
                            tags = PROMOCODES_TAG,
                            operationId = URI_SEND_MAILING_PROMOCODE,
                            requestBody = @RequestBody(
                                    description = "User email to create mailing DTO",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UserEmailDto.class
                                            )
                                    )),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Mailing status (is promocode sent) DTO",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = UserMailingDto.class

                                                    )
                                            )
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            )
                    )
            )
    })
    @Bean
    public RouterFunction<ServerResponse> routerInstaRegRest(
            UserSubscriptionRest userSubscriptionRest,
            UserPlanRest userPlanRest,
            BotRest botRest,
            PhoneRest phoneRest,
            UserRest userRest,
            OrderRest orderRest,
            TikConnectorEmuRest tikConnectorEmuRest,
            PaymentRest paymentRest,
            TiktokAccountRest tiktokAccountRest,
            PlanTemplateRest planTemplateRest,
            ProportionRest proportionRest,
            UserStatisticsRest userStatisticsRest,
            CartRest cartRest,
            PromocodeRest promocodeRest
    ) {

        final RouterFunction<ServerResponse> mainRoutes = addCommonRoutes()
                .andRoute(RequestPredicates.POST(URI_CREATE_USER_SUBSCRIPTION_BY_TEMPLATE).and(accept(MediaType.APPLICATION_JSON)), userSubscriptionRest::createUserSubscriptionByTemplate)
                .andRoute(RequestPredicates.POST(URI_CREATE_USER_SUBSCRIPTION_CUSTOM).and(accept(MediaType.APPLICATION_JSON)), userSubscriptionRest::createUserSubscriptionCustom)
                .andRoute(RequestPredicates.PUT(URI_UPDATE_USER_SUBSCRIPTION_BY_TEMPLATE).and(accept(MediaType.APPLICATION_JSON)), userSubscriptionRest::updateUserSubscriptionByTemplate)
                .andRoute(RequestPredicates.PUT(URI_UPDATE_USER_SUBSCRIPTION_CUSTOM).and(accept(MediaType.APPLICATION_JSON)), userSubscriptionRest::updateUserSubscriptionCustom)
                .andRoute(RequestPredicates.POST(URI_CALCULATE_SUBSCRIPTION_SUM).and(accept(MediaType.APPLICATION_JSON)), userSubscriptionRest::calculateUserSubscriptionSum)
                .andRoute(RequestPredicates.DELETE(URI_UNDO_SUBSCRIPTION).and(accept(MediaType.APPLICATION_JSON)), userSubscriptionRest::undoUserSubscription)

                .andRoute(RequestPredicates.POST(URI_CREATE_CUSTOM_PLAN).and(accept(MediaType.APPLICATION_JSON)), userPlanRest::createCustomUserPlan)

                .andRoute(RequestPredicates.POST(URI_CREATE_OR_UPDATE_BOT).and(accept(MediaType.APPLICATION_JSON)), botRest::createOrUpdateBot)
                .andRoute(RequestPredicates.GET(URI_GET_BOT_BY_ID).and(accept(MediaType.APPLICATION_JSON)), botRest::getBotById)
                .andRoute(RequestPredicates.GET(URI_GET_AVAILABLE_BOTS_BY_AWEME_ID).and(accept(MediaType.APPLICATION_JSON)), botRest::getAvailableBotsAmountByAwemeId)
                .andRoute(RequestPredicates.GET(URI_GET_AVAILABLE_BOTS_BY_SEC_USER_ID).and(accept(MediaType.APPLICATION_JSON)), botRest::getAvailableBotsAmountBySecUserId)
                .andRoute(RequestPredicates.GET(URI_GET_ACTIVE_BOTS_IDS).and(accept(MediaType.APPLICATION_JSON)), botRest::getActiveBotsIds)

                .andRoute(RequestPredicates.POST(URI_CREATE_OR_UPDATE_PHONE).and(accept(MediaType.APPLICATION_JSON)), phoneRest::createOrUpdatePhone)
                .andRoute(RequestPredicates.POST(URI_CREATE_OR_UPDATE_PHONE_USAGE).and(accept(MediaType.APPLICATION_JSON)), phoneRest::createOrUpdatePhoneUsage)
                .andRoute(RequestPredicates.GET(URI_GET_PHONE).and(accept(MediaType.APPLICATION_JSON)), phoneRest::getPhone)
                .andRoute(RequestPredicates.GET(URI_GET_AVAILABLE_PHONE).and(accept(MediaType.APPLICATION_JSON)), phoneRest::getLongestNotUsedPhone)

                .andRoute(RequestPredicates.GET(URI_GET_USER_BOUNDED_ACCOUNTS).and(accept(MediaType.APPLICATION_JSON)), tiktokAccountRest::getAllAccountProfiles)
                .andRoute(RequestPredicates.POST(URI_GET_LAST_SELECTED_USER_BOUNDED_ACCOUNT).and(accept(MediaType.APPLICATION_JSON)), tiktokAccountRest::getLastSelectedAccountInfo)
                .andRoute(RequestPredicates.POST(URI_ADD_TIKTOK_ACCOUNT_TO_PROFILE).and(accept(MediaType.APPLICATION_JSON)), tiktokAccountRest::addAccountToProfile)
                .andRoute(RequestPredicates.DELETE(URI_REMOVE_TIKTOK_ACCOUNT_FROM_PROFILE).and(accept(MediaType.APPLICATION_JSON)), tiktokAccountRest::removeAccountFromProfile)
                .andRoute(RequestPredicates.POST(URI_FIND_PLAN_TIKTOK_ACCOUNT).and(accept(MediaType.APPLICATION_JSON)), tiktokAccountRest::getPlanByTiktokAccount)
                .andRoute(RequestPredicates.GET(URI_GET_USER_ACCOUNTS_SUBSCRIPTION).and(accept(MediaType.APPLICATION_JSON)), tiktokAccountRest::getAllSubscriptionsByUser)

                .andRoute(RequestPredicates.POST(URI_CREATE_OR_UPDATE_PLAN_TEMPLATE).and(accept(MediaType.APPLICATION_JSON)), planTemplateRest::createOrUpdatePlanTemplate)
                .andRoute(RequestPredicates.GET(URI_GET_PLAN_TEMPLATE_BY_NAME).and(accept(MediaType.APPLICATION_JSON)), planTemplateRest::getPlanTemplateByName)
                .andRoute(RequestPredicates.GET(URI_GET_ALL_PLAN_TEMPLATES).and(accept(MediaType.APPLICATION_JSON)), planTemplateRest::getAllPlanTemplates)
                .andRoute(RequestPredicates.DELETE(URI_CHANGE_PLAN_TEMPLATE_TO_NOT_ACTIVE).and(accept(MediaType.APPLICATION_JSON)), planTemplateRest::invalidatePlanTemplate)

                .andRoute(RequestPredicates.POST(URI_REGISTER).and(accept(MediaType.APPLICATION_JSON)), userRest::register)
                .andRoute(RequestPredicates.PUT(URI_RESEND_ACTIVATION_EMAIL).and(accept(MediaType.APPLICATION_JSON)), userRest::resendActivationEmail)
                .andRoute(RequestPredicates.GET(URI_AUTH).and(accept(MediaType.APPLICATION_JSON)), userRest::refreshToken)
                .andRoute(RequestPredicates.POST(URI_LOGIN).and(accept(MediaType.APPLICATION_JSON)), userRest::login)
                .andRoute(RequestPredicates.POST(URI_LOGIN_WITH_FACEBOOK).and(accept(MediaType.APPLICATION_JSON)), userRest::loginWithFacebook)
                .andRoute(RequestPredicates.POST(URI_LOGIN_WITH_GOOGLE).and(accept(MediaType.APPLICATION_JSON)), userRest::loginWithGoogle)
                .andRoute(RequestPredicates.PUT(URI_ACTIVATE_USER).and(accept(MediaType.APPLICATION_JSON)), userRest::activateUserByKey)
                .andRoute(RequestPredicates.PUT(URI_CHANGE_PASSWORD).and(accept(MediaType.APPLICATION_JSON)), userRest::changePassword)
                .andRoute(RequestPredicates.POST(URI_FORGOT_PASSWORD).and(accept(MediaType.APPLICATION_JSON)), userRest::forgotPassword)
                .andRoute(RequestPredicates.GET(URI_FORGOT_PASSWORD_AUTHENTICATED).and(accept(MediaType.APPLICATION_JSON)), userRest::forgotPasswordAuthenticated)
                .andRoute(RequestPredicates.PUT(URI_CHANGE_FORGOTTEN_PASSWORD).and(accept(MediaType.APPLICATION_JSON)), userRest::changeForgottenPassword)
                .andRoute(RequestPredicates.POST(URI_VERIFY_KEY_SET).and(accept(MediaType.APPLICATION_JSON)), userRest::isKeySetValid)
                .andRoute(RequestPredicates.GET(URI_LOGOUT).and(accept(MediaType.APPLICATION_JSON)), userRest::logout)

                .andRoute(RequestPredicates.POST(URI_CREATE_OR_UPDATE_ORDER).and(accept(MediaType.APPLICATION_JSON)), orderRest::createOrUpdateOrder)
                .andRoute(RequestPredicates.POST(URI_CREATE_LIKES_ORDER).and(accept(MediaType.APPLICATION_JSON)), orderRest::createLikesOrder)
                .andRoute(RequestPredicates.POST(URI_CREATE_FOLLOWERS_ORDER).and(accept(MediaType.APPLICATION_JSON)), orderRest::createFollowersOrder)
                .andRoute(RequestPredicates.POST(URI_CREATE_VIEWS_ORDER).and(accept(MediaType.APPLICATION_JSON)), orderRest::createViewsOrder)
                .andRoute(RequestPredicates.PUT(URI_VERIFY_LIKES_ORDER_DATA).and(accept(MediaType.APPLICATION_JSON)), orderRest::verifyLikesOrder)
                .andRoute(RequestPredicates.PUT(URI_VERIFY_FOLLOWERS_ORDER_DATA).and(accept(MediaType.APPLICATION_JSON)), orderRest::verifyFollowersOrder)
                .andRoute(RequestPredicates.PUT(URI_VERIFY_VIEWS_ORDER_DATA).and(accept(MediaType.APPLICATION_JSON)), orderRest::verifyViewsOrder)

                .andRoute(RequestPredicates.POST(URI_CREATE_OR_UPDATE_ACTIONS_PROPORTION).and(accept(MediaType.APPLICATION_JSON)), proportionRest::createOrUpdateActionsProportion)
                .andRoute(RequestPredicates.POST(URI_CREATE_OR_UPDATE_ACCOUNTS_PROPORTION).and(accept(MediaType.APPLICATION_JSON)), proportionRest::createOrUpdateAccountsProportion)
                .andRoute(RequestPredicates.GET(URI_GET_ALL_ACTIONS_PROPORTIONS).and(accept(MediaType.APPLICATION_JSON)), proportionRest::getAllActionsProportions)
                .andRoute(RequestPredicates.GET(URI_GET_ALL_ACCOUNTS_PROPORTIONS).and(accept(MediaType.APPLICATION_JSON)), proportionRest::getAllAccountsProportions)
                .andRoute(RequestPredicates.GET(URI_GET_CUSTOM_PLAN_MAX_CONSTRAINTS).and(accept(MediaType.APPLICATION_JSON)), proportionRest::getCustomPlanMaxConstraints)

                .andRoute(RequestPredicates.GET(URI_GET_ACTIVE_USER_ORDERS_PROGRESSES).and(accept(MediaType.APPLICATION_JSON)), userStatisticsRest::getActiveOrderProgresses)
                .andRoute(RequestPredicates.GET(URI_GET_USER_ORDERS_HISTORY_PAGES_QUANTITY).and(accept(MediaType.APPLICATION_JSON)), userStatisticsRest::getOrdersHistoryPagesQuantity)
                .andRoute(RequestPredicates.GET(URI_GET_USER_ORDERS_HISTORY).and(accept(MediaType.APPLICATION_JSON)), userStatisticsRest::getOrdersHistory)
                .andRoute(RequestPredicates.DELETE(URI_CLEAR_USER_ORDERS_HISTORY).and(accept(MediaType.APPLICATION_JSON)), userStatisticsRest::clearOrdersHistory)
                .andRoute(RequestPredicates.DELETE(URI_CLEAR_USER_ORDER_HISTORY_BY_ID).and(accept(MediaType.APPLICATION_JSON)), userStatisticsRest::clearOrderHistoryById)
                .andRoute(RequestPredicates.GET(URI_GET_VIDEO_ORDER_DETAILS_BY_ID).and(accept(MediaType.APPLICATION_JSON)), userStatisticsRest::getVideoOrderDetails)
                .andRoute(RequestPredicates.GET(URI_GET_ACCOUNT_ORDER_DETAILS_BY_ID).and(accept(MediaType.APPLICATION_JSON)), userStatisticsRest::getAccountOrderDetails)
                .andRoute(RequestPredicates.GET(URI_GET_ACTIVE_USER_SUBSCRIPTION).and(accept(MediaType.APPLICATION_JSON)), userSubscriptionRest::getActiveSubscription)

                .andRoute(RequestPredicates.PUT(URI_INCREASE_DEPOSIT).and(accept(MediaType.APPLICATION_JSON)), paymentRest::increaseDeposit)
                .andRoute(RequestPredicates.POST(URI_PAY_FOR_SUBSCRIPTION).and(accept(MediaType.APPLICATION_JSON)), paymentRest::payForSubscription)
                .andRoute(RequestPredicates.GET(URI_GET_DEPOSIT_BALANCE).and(accept(MediaType.APPLICATION_JSON)), paymentRest::getDepositBalance)

                .andRoute(RequestPredicates.POST(URI_PAY_FOR_CART).and(accept(MediaType.APPLICATION_JSON)), cartRest::createAllHierarchyEntities)

                .andRoute(RequestPredicates.POST(URI_SEND_MAILING_PROMOCODE).and(accept(MediaType.APPLICATION_JSON)), promocodeRest::createMailingPromocode);
        // Emu
        return addEmuRoutes(mainRoutes, tikConnectorEmuRest);
    }
}
