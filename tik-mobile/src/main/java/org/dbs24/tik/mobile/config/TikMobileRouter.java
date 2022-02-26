package org.dbs24.tik.mobile.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.dbs24.config.AbstractWebSecurityConfig;
import org.dbs24.tik.mobile.constant.ApiPath;
import org.dbs24.tik.mobile.constant.RequestQueryParam;
import org.dbs24.tik.mobile.entity.dto.action.ExecutedActionResponseDto;
import org.dbs24.tik.mobile.entity.dto.action.OrderActionDto;
import org.dbs24.tik.mobile.entity.dto.action.OrderActionTypeDtoList;
import org.dbs24.tik.mobile.entity.dto.device.DeviceIdResponseDto;
import org.dbs24.tik.mobile.entity.dto.device.UpdateDeviceAttributesRequestDto;
import org.dbs24.tik.mobile.entity.dto.device.UserDeviceListDto;
import org.dbs24.tik.mobile.entity.dto.download.DownloadVideoDtoList;
import org.dbs24.tik.mobile.entity.dto.download.DownloadVideoIdDto;
import org.dbs24.tik.mobile.entity.dto.firebase.FireBaseApplicationIdDto;
import org.dbs24.tik.mobile.entity.dto.firebase.FireBaseApplicationListDto;
import org.dbs24.tik.mobile.entity.dto.firebase.FireBaseApplicationRequestDto;
import org.dbs24.tik.mobile.entity.dto.order.OrderDto;
import org.dbs24.tik.mobile.entity.dto.order.OrderIdDto;
import org.dbs24.tik.mobile.entity.dto.order.actual.ActualOrderDtoList;
import org.dbs24.tik.mobile.entity.dto.order.details.OrderDetailsDto;
import org.dbs24.tik.mobile.entity.dto.order.details.OrderDetailsDtoList;
import org.dbs24.tik.mobile.entity.dto.order.statistic.StatisticOrderHistDtoList;
import org.dbs24.tik.mobile.entity.dto.payment.UserDepositDto;
import org.dbs24.tik.mobile.entity.dto.payment.UserDepositIncreaseDto;
import org.dbs24.tik.mobile.entity.dto.proportion.heart.HeartPriceDto;
import org.dbs24.tik.mobile.entity.dto.proportion.heart.HeartPriceListDto;
import org.dbs24.tik.mobile.entity.dto.proportion.order.OrderPriceDto;
import org.dbs24.tik.mobile.entity.dto.proportion.order.OrderPriceListDto;
import org.dbs24.tik.mobile.entity.dto.settings.AppSettingsRequestDto;
import org.dbs24.tik.mobile.entity.dto.settings.AppSettingsResponseDto;
import org.dbs24.tik.mobile.entity.dto.settings.PackageNameDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.TiktokPostIdentifierListDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.TiktokUserProfileDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.SearchSinglePostDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.TiktokPostLinkDto;
import org.dbs24.tik.mobile.entity.dto.user.*;
import org.dbs24.tik.mobile.rest.*;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.dbs24.tik.mobile.constant.ApiPath.*;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Log4j2
@Configuration
public class TikMobileRouter extends AbstractWebSecurityConfig {

    private static final String USER_ACTIONS_SWAGGER_TAG = "User actions";
    private static final String DEPOSIT_ACTIONS_SWAGGER_TAG = "Deposit actions";
    private static final String ORDERS_SWAGGER_TAG = "Orders";
    private static final String VIDEO_DOWNLOAD_SWAGGER_TAG = "Download actions";
    private static final String ORDER_STATISTICS_SWAGGER_TAG = "Order statistics actions";
    private static final String PROPORTIONS_SWAGGER_TAG = "Proportions actions";
    private static final String USER_STATISTICS_SWAGGER_TAG = "User statistics actions";
    private static final String TIKTOK_ACCOUNT_ACTIONS_SWAGGER_TAG = "Tiktok account actions";
    private static final String APP_SETTINGS_SWAGGER_TAG = "Application settings actions";
    private static final String FIREBASE_APPS_SWAGGER_TAG = "FireBase application actions";
    private static final String DEVICE_SWAGGER_TAG = "Device actions";

    public static final String AUTHORIZATION_HEADER_NAME = "Authorization1"; /*todo HttpHeaders.AUTHORIZATION*/

    @RouterOperations({
            /**
             * ORDERS
             */
            @RouterOperation(
                    path = ApiPath.ORDERS_GET_TO_EXECUTE,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = ORDERS_SWAGGER_TAG,
                            operationId = ApiPath.ORDERS_GET_TO_EXECUTE,
                            description = "Get actual orders for user",
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "All available orders",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = ActualOrderDtoList.class
                                            )
                                    )
                            ),
                            parameters = {
                                    @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            ),@Parameter(
                                    name = RequestQueryParam.QP_ACTION_TYPE_ID,
                                    description = "Action type id",
                                    in = ParameterIn.QUERY,
                                    schema = @Schema(type = "integer")
                            )
                            }
                    )
            ),
            @RouterOperation(
                    path = ApiPath.ORDERS_EXECUTION_COMPLETE,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = ORDERS_SWAGGER_TAG,
                            operationId = ApiPath.ORDERS_EXECUTION_COMPLETE,
                            description = "Complete order action",
                            requestBody = @RequestBody(
                                    description = "Info about order action",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = OrderActionDto.class
                                            )
                                    )
                            ),
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "order ID",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = ExecutedActionResponseDto.class
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
                    path = ApiPath.ORDERS_CREATE,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = ORDERS_SWAGGER_TAG,
                            operationId = ApiPath.ORDERS_CREATE,
                            description = "Create new order",
                            requestBody = @RequestBody(
                                    description = "DTO with order info",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = OrderDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Created order ID",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = OrderIdDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Link to tiktok uncorrected"
                                    ),
                                    @ApiResponse(
                                            responseCode = "402",
                                            description = "Not enough hearts to pay"
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
                    path = ApiPath.ORDERS_SKIP,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = ORDERS_SWAGGER_TAG,
                            operationId = ApiPath.ORDERS_SKIP,
                            description = "Skip order",
                            requestBody = @RequestBody(
                                    description = "DTO with order id",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = OrderIdDto.class
                                            )
                                    )
                            ),
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "Skipped order id",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = OrderIdDto.class
                                            )
                                    )
                            )
                    )
            ),
            @RouterOperation(
                    path = ApiPath.ORDERS_GET_ALL_ACTION_TYPES,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = ORDERS_SWAGGER_TAG,
                            operationId = ApiPath.ORDERS_GET_ALL_ACTION_TYPES,
                            description = "Get all action types in database",
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "All action types with id and name",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = OrderActionTypeDtoList.class
                                            )
                                    )
                            )
                    )
            ),
            /**
             * STATISTICS
             */
            @RouterOperation(
                    path = ApiPath.STATISTICS_GET_SINGLE_ORDER_DETAILS,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = ORDER_STATISTICS_SWAGGER_TAG,
                            operationId = ApiPath.STATISTICS_GET_SINGLE_ORDER_DETAILS,
                            description = "Get info about order",
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "Order info by id",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = OrderDetailsDto.class
                                            )
                                    )
                            ),
                            parameters = @Parameter(
                                    name = RequestQueryParam.QP_ORDER_ID,
                                    description = "order id in database",
                                    in = ParameterIn.QUERY,
                                    schema = @Schema(type = "integer")
                            )
                    )
            ),
            @RouterOperation(
                    path = ApiPath.STATISTICS_CLEAR_SINGLE_ORDER_HISTORY,
                    method = RequestMethod.DELETE,
                    operation = @Operation(
                            tags = ORDER_STATISTICS_SWAGGER_TAG,
                            operationId = ApiPath.STATISTICS_CLEAR_SINGLE_ORDER_HISTORY,
                            description = "Get info about order",
                            parameters = @Parameter(
                                    name = RequestQueryParam.QP_ORDER_ID,
                                    description = "order id in database",
                                    in = ParameterIn.QUERY,
                                    schema = @Schema(type = "integer")
                            )
                    )
            ),
            @RouterOperation(
                    path = ApiPath.STATISTICS_GET_ACTIVE_ORDERS_PROGRESSES,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = ORDER_STATISTICS_SWAGGER_TAG,
                            operationId = ApiPath.STATISTICS_GET_ACTIVE_ORDERS_PROGRESSES,
                            description = "Get active user orders, [OPTIONAL]action type id to filter orders",
                            responses =
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "All active user orders",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = OrderDetailsDtoList.class
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
                                            name = RequestQueryParam.QP_ACTION_TYPE_ID,
                                            description = "Action type id",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "integer")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = ApiPath.STATISTICS_INVALIDATE_ACTIVE_ORDER,
                    method = RequestMethod.DELETE,
                    operation = @Operation(
                            tags = ORDER_STATISTICS_SWAGGER_TAG,
                            operationId = ApiPath.STATISTICS_INVALIDATE_ACTIVE_ORDER,
                            description = "Invalidate user order in 'user post was deleted' case",
                            responses =
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "All active user orders",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = OrderIdDto.class
                                            )
                                    )
                            ),
                            parameters = {
                                    @Parameter(
                                            name = RequestQueryParam.QP_ORDER_ID,
                                            description = "Order id to invalidate",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "integer")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = ApiPath.STATISTICS_GET_ALL_DONE_ORDER_ACTIONS_PAGES_QUANTITY,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = ORDER_STATISTICS_SWAGGER_TAG,
                            operationId = ApiPath.STATISTICS_GET_ALL_DONE_ORDER_ACTIONS_PAGES_QUANTITY,
                            description = "Get completed orders history by page, [OPTIONAL]action type id to filter orders",
                            responses =
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Completed user orders by page",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = StatisticOrderHistDtoList.class
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
                                            name = RequestQueryParam.QP_PAGE_NUM,
                                            description = "Page number",
                                            in = ParameterIn.QUERY,
                                            required = true,
                                            schema = @Schema(type = "integer")
                                    ),
                                    @Parameter(
                                            name = RequestQueryParam.QP_ACTION_TYPE_ID,
                                            description = "Action type id",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "integer")
                                    )
                            }
                    )
            ),
            /**
             * USERS
             */
            @RouterOperation(
                    path = ApiPath.USER_REGISTER,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = ApiPath.USER_REGISTER,
                            requestBody = @RequestBody(
                                    description = "User to register info",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UserDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "User was successfully registered",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = TokenDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "No tiktok user with such username"
                                    ),
                                    @ApiResponse(
                                            responseCode = "409",
                                            description = "Tiktok username is busy"
                                    )
                            })
            ),
            @RouterOperation(
                    path = ApiPath.USER_LOGIN,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = ApiPath.USER_LOGIN,
                            requestBody = @RequestBody(
                                    description = "User to login info",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UserDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "User jwt DTO",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = TokenDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "403",
                                            description = "Username/password mismatch"
                                    )
                            })
            ),
            @RouterOperation(
                    path = ApiPath.USER_LOGOUT,
                    method = RequestMethod.DELETE,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = ApiPath.USER_LOGOUT,
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Logout success"
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            ))
            ),
            @RouterOperation(
                    path = ApiPath.USER_IS_EMAIL_BOUNDED,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = ApiPath.USER_IS_EMAIL_BOUNDED,
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "User has bounded email address",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = UserEmailDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "204",
                                            description = "User has no bounded email address"
                                    )
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            ))
            ),
            @RouterOperation(
                    path = ApiPath.USER_BOUND_EMAIL,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = ApiPath.USER_BOUND_EMAIL,
                            requestBody = @RequestBody(
                                    description = "DTO to send email to required address to email bounding",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UserEmailBoundingDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Email was successfully sent to required address"
                                    ),
                                    @ApiResponse(
                                            responseCode = "409",
                                            description = "Email address is busy"
                                    )
                            })
            ),
            @RouterOperation(
                    path = ApiPath.USER_CONFIRM_EMAIL_BY_KEY,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = ApiPath.USER_CONFIRM_EMAIL_BY_KEY,
                            requestBody = @RequestBody(
                                    description = "DTO with keys extracted from email redirect link",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UserEmailBoundingKeysetDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Email was successfully bounded"
                                    )
                            })
            ),
            @RouterOperation(
                    path = ApiPath.USER_FORGOT_PASSWORD,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = ApiPath.USER_FORGOT_PASSWORD,
                            requestBody = @RequestBody(
                                    description = "DTO with email to send forgot password request",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UserEmailDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Email was successfully sent to required address"
                                    )
                            })
            ),
            @RouterOperation(
                    path = ApiPath.USER_CHANGE_FORGOTTEN_PASSWORD,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = ApiPath.USER_CHANGE_FORGOTTEN_PASSWORD,
                            requestBody = @RequestBody(
                                    description = "DTO with keys extracted from email redirect link",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UserForgottenPasswordKeysetDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Email was successfully sent to required address",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = TokenDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Account with such email does not exist"
                                    ),
                                    @ApiResponse(
                                            responseCode = "403",
                                            description = "Keyset is invalid/expired"
                                    )
                            })
            ),
            @RouterOperation(
                    path = ApiPath.USER_AUTH,
                    method = RequestMethod.PUT,
                    operation = @Operation(
                            tags = USER_ACTIONS_SWAGGER_TAG,
                            operationId = ApiPath.USER_AUTH,
                            requestBody = @RequestBody(
                                    description = "DTO with token to refresh",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = TokenDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "DTO with fresh token",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = TokenDto.class
                                                    )
                                            )
                                    )
                            })
            ),
            /**
             * PROPORTIONS
             */
            @RouterOperation(
                    path = ApiPath.HURTS_TO_CURRENCY_PROPORTIONS_CREATE,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = PROPORTIONS_SWAGGER_TAG,
                            operationId = ApiPath.HURTS_TO_CURRENCY_PROPORTIONS_CREATE,
                            requestBody = @RequestBody(
                                    description = "Heart price with upper limit hearts amount DTO",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = HeartPriceDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Created heart price record DTO",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = HeartPriceDto.class
                                                    )
                                            )
                                    )
                            })
            ),
            @RouterOperation(
                    path = ApiPath.HURTS_TO_CURRENCY_PROPORTIONS_GET,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = PROPORTIONS_SWAGGER_TAG,
                            operationId = ApiPath.HURTS_TO_CURRENCY_PROPORTIONS_GET,
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "DTO with heart price upper limit constraints",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = HeartPriceListDto.class
                                                    )
                                            )
                                    )
                            })
            ),
            @RouterOperation(
                    path = ApiPath.HURTS_COST_BY_HEARTS_QUANTITY,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = PROPORTIONS_SWAGGER_TAG,
                            operationId = ApiPath.HURTS_COST_BY_HEARTS_QUANTITY,
                            parameters = @Parameter(
                                    name = RequestQueryParam.QP_HEARTS_AMOUNT,
                                    description = "Hearts amount to cost research",
                                    in = ParameterIn.QUERY,
                                    schema = @Schema(type = "integer")
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "DTO with hearts cost by required quantity",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = HeartPriceDto.class
                                                    )
                                            )
                                    )
                            })
            ),
            @RouterOperation(
                    path = ApiPath.HURTS_TO_ACTION_TYPE_PROPORTIONS_GET,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = PROPORTIONS_SWAGGER_TAG,
                            operationId = ApiPath.HURTS_TO_ACTION_TYPE_PROPORTIONS_GET,
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "All action types order till required hearts amount quantity",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = OrderPriceListDto.class
                                                    )
                                            )
                                    )
                            })
            ),
            @RouterOperation(
                    path = ApiPath.HURTS_TO_ACTION_TYPE_PROPORTIONS_GET_BY_TYPE_AND_QUANTITY,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = PROPORTIONS_SWAGGER_TAG,
                            operationId = ApiPath.HURTS_TO_ACTION_TYPE_PROPORTIONS_GET_BY_TYPE_AND_QUANTITY,
                            parameters = {
                                    @Parameter(
                                            name = RequestQueryParam.QP_ACTION_TYPE_ID,
                                            description = "Action type id to perform order",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "integer")
                                    ),
                                    @Parameter(
                                            name = RequestQueryParam.QP_ACTIONS_AMOUNT,
                                            description = "Required order actions amount",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "integer")
                                    ),
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "DTO with hearts quantity to \"pay\" by required quantity and action type",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = OrderPriceDto.class
                                                    )
                                            )
                                    )
                            })
            ),
            @RouterOperation(
                    path = ApiPath.HURTS_TO_ACTION_TYPE_PROPORTIONS_CREATE,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = PROPORTIONS_SWAGGER_TAG,
                            operationId = ApiPath.HURTS_TO_ACTION_TYPE_PROPORTIONS_CREATE,
                            requestBody = @RequestBody(
                                    description = "Heart price with upper limit hearts amount DTO",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = OrderPriceDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "DTO with created constraint",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = OrderPriceDto.class
                                                    )
                                            )
                                    )
                            })
            ),
            /**
             * TIKTOK
             */
            @RouterOperation(
                    path = ApiPath.TIKTOK_ACCOUNT_GET_LAST_VIDEOS,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = TIKTOK_ACCOUNT_ACTIONS_SWAGGER_TAG,
                            operationId = ApiPath.TIKTOK_ACCOUNT_GET_LAST_VIDEOS,
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "User latest N(configured in properties) posts",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = TiktokPostIdentifierListDto.class
                                                    )
                                            )
                                    )/*,
                                    @ApiResponse(
                                            responseCode = "204",
                                            description = "User has no any post"
                                    )*/
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            ))
            ),
            @RouterOperation(
                    path = ApiPath.TIKTOK_ACCOUNT_GET_DETAILS,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = TIKTOK_ACCOUNT_ACTIONS_SWAGGER_TAG,
                            operationId = ApiPath.TIKTOK_ACCOUNT_GET_DETAILS,
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "User's tiktok account data",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = TiktokUserProfileDto.class
                                                    )
                                            )
                                    )/*,
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "User's account is private"
                                    )*/
                            },
                            parameters = @Parameter(
                                    name = AUTHORIZATION_HEADER_NAME,
                                    in = ParameterIn.HEADER,
                                    required = true,
                                    description = "JWT header"
                            ))
            ),
            /**
             * VIDEO DOWNLOAD
             */
            @RouterOperation(
                    path = ApiPath.DOWNLOADS_FIND_VIDEO_BY_LINK,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = VIDEO_DOWNLOAD_SWAGGER_TAG,
                            operationId = ApiPath.DOWNLOADS_FIND_VIDEO_BY_LINK,
                            description = "Find post by link for download",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Tiktok post info",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = SearchSinglePostDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Link to tiktok uncorrected"
                                    )
                            },
                            parameters = @Parameter(
                                    name = RequestQueryParam.QP_TIKTOK_POST_LINK,
                                    in = ParameterIn.QUERY,
                                    required = true,
                                    description = "Link to tiktok post"
                            )
                    )
            ),
            @RouterOperation(
                    path = ApiPath.DOWNLOADS_GET_ALL_VIDEO_HISTORY,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = VIDEO_DOWNLOAD_SWAGGER_TAG,
                            operationId = ApiPath.DOWNLOADS_GET_ALL_VIDEO_HISTORY,
                            description = "Find all downloaded videos by year",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Videos info",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = DownloadVideoDtoList.class
                                                    )
                                            )
                                    )
                            },
                            parameters = {
                                    @Parameter(
                                            name = RequestQueryParam.QP_YEAR,
                                            in = ParameterIn.QUERY,
                                            required = true,
                                            description = "Year for sort videos"
                                    ),
                                    @Parameter(
                                            name = AUTHORIZATION_HEADER_NAME,
                                            in = ParameterIn.HEADER,
                                            required = true,
                                            description = "JWT header"
                                    )
                            }

                    )
            ),
            @RouterOperation(
                    path = ApiPath.DOWNLOADS_VIDEO_DOWNLOAD,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = VIDEO_DOWNLOAD_SWAGGER_TAG,
                            operationId = ApiPath.DOWNLOADS_VIDEO_DOWNLOAD,
                            description = "Save download history",
                            requestBody = @RequestBody(
                                    description = "Any [web,share,short] link to post",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = TiktokPostLinkDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Saved download history id",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = DownloadVideoIdDto.class
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
            /**
             * DEPOSITS
             */
            @RouterOperation(
                    path = ApiPath.DEPOSITS_GET_CURRENT_BALANCE,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = DEPOSIT_ACTIONS_SWAGGER_TAG,
                            operationId = ApiPath.DEPOSITS_GET_CURRENT_BALANCE,
                            description = "Get actual user balance",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Get user balance",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = UserDepositDto.class
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
                    path = ApiPath.DEPOSITS_INCREASE,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = DEPOSIT_ACTIONS_SWAGGER_TAG,
                            operationId = ApiPath.DEPOSITS_INCREASE,
                            requestBody = @RequestBody(
                                    description = "Amount hearts to increase user balance",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UserDepositIncreaseDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Updated user balance",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = UserDepositDto.class
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
            /**
             * APP SETTINGS
             */
            @RouterOperation(
                    path = SETTINGS_GET_BY_PACKAGE_NAME,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = APP_SETTINGS_SWAGGER_TAG,
                            operationId = SETTINGS_GET_BY_PACKAGE_NAME,
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "App settings info",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = AppSettingsResponseDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Cannot find settings with requested package name"
                                    )
                            },
                            parameters = @Parameter(
                                    name = RequestQueryParam.QP_PACKAGE_NAME,
                                    in = ParameterIn.QUERY,
                                    required = true,
                                    description = "App package name",
                                    example = "com.zeta.io"
                            )
                    )
            ),
            @RouterOperation(
                    path = SETTINGS_CREATE_OR_UPDATE_APP_SETTINGS,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = APP_SETTINGS_SWAGGER_TAG,
                            operationId = SETTINGS_CREATE_OR_UPDATE_APP_SETTINGS,
                            requestBody = @RequestBody(
                                    description = "Application settings info",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = AppSettingsRequestDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Created/Updated package name",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = PackageNameDto.class
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            /**
             * FIREBASE APPLICATIONS
             */
            @RouterOperation(
                    path = FIREBASE_GET_ALL_FIREBASE_APPS,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = FIREBASE_APPS_SWAGGER_TAG,
                            operationId = FIREBASE_GET_ALL_FIREBASE_APPS,
                            description = "Get all fireBase applications",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "All fireBase applications info",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = FireBaseApplicationListDto.class
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = FIREBASE_CREATE_OR_UPDATE_APP_FIREBASE,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = FIREBASE_APPS_SWAGGER_TAG,
                            operationId = FIREBASE_CREATE_OR_UPDATE_APP_FIREBASE,
                            description = "Create/Update fireBase application",
                            requestBody = @RequestBody(
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = FireBaseApplicationRequestDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Created/Updated app id",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = FireBaseApplicationIdDto.class
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            /**
             * DEVICES
             */
            @RouterOperation(
                    path = DEVICE_GET_ALL_BY_USER,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = DEVICE_SWAGGER_TAG,
                            operationId = DEVICE_GET_ALL_BY_USER,
                            description = "Get all user devices",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "All user devices info",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = UserDeviceListDto.class
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
                    path = DEVICE_REMOVE_BY_ID,
                    method = RequestMethod.DELETE,
                    operation = @Operation(
                            tags = DEVICE_SWAGGER_TAG,
                            operationId = DEVICE_REMOVE_BY_ID,
                            description = "Remove device by gsf_id or identifier_for_vendor",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Removed device id",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = DeviceIdResponseDto.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Cannot find device with requested param"
                                    )
                            },
                            parameters = @Parameter(
                                    name = RequestQueryParam.QP_DEVICE_UID,
                                    in = ParameterIn.QUERY,
                                    required = true,
                                    description = "Gsf_id or identifier_for_vendor"
                            )
                    )
            ),
            @RouterOperation(
                    path = DEVICE_ATTRIBUTES_CREATE_OR_UPDATE,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = DEVICE_SWAGGER_TAG,
                            operationId = DEVICE_ATTRIBUTES_CREATE_OR_UPDATE,
                            description = "Create/Update device android|ios attributes",
                            requestBody = @RequestBody(
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UpdateDeviceAttributesRequestDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Created/Updated device id",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = DeviceIdResponseDto.class
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
    public RouterFunction<ServerResponse> registerRoutes(UserRest userRest,
                                                         OrderRest orderRest,
                                                         ProportionRest proportionRest,
                                                         UserTiktokAccountRest userTiktokAccountRest,
                                                         OrderStatisticRest orderStatisticRest,
                                                         PaymentRest paymentRest,
                                                         VideoDownloadRest videoDownloadRest,
                                                         AppSettingsRest appSettingsRest,
                                                         FireBaseApplicationRest fireBaseApplicationRest,
                                                         UserDeviceRest userDeviceRest) {

        return RouterFunctions.route(RequestPredicates.POST(ApiPath.USER_REGISTER).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userRest::register)
                .andRoute(RequestPredicates.POST(ApiPath.USER_LOGIN).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userRest::login)
                .andRoute(RequestPredicates.DELETE(ApiPath.USER_LOGOUT).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userRest::logout)
                .andRoute(RequestPredicates.GET(ApiPath.USER_IS_EMAIL_BOUNDED).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userRest::isUserEmailBounded)
                .andRoute(RequestPredicates.POST(ApiPath.USER_BOUND_EMAIL).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userRest::boundEmailRequest)
                .andRoute(RequestPredicates.POST(ApiPath.USER_CONFIRM_EMAIL_BY_KEY).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userRest::boundEmail)
                .andRoute(RequestPredicates.POST(ApiPath.USER_FORGOT_PASSWORD).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userRest::forgotPassword)
                .andRoute(RequestPredicates.POST(ApiPath.USER_CHANGE_FORGOTTEN_PASSWORD).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userRest::changeForgottenPassword)
                .andRoute(RequestPredicates.PUT(ApiPath.USER_AUTH).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userRest::refreshToken)

                //HEARTS
                .andRoute(RequestPredicates.POST(ApiPath.HURTS_TO_CURRENCY_PROPORTIONS_CREATE).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), proportionRest::createHeartPriceRecord)
                .andRoute(RequestPredicates.GET(ApiPath.HURTS_TO_CURRENCY_PROPORTIONS_GET).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), proportionRest::getAllHeartPrices)
                .andRoute(RequestPredicates.GET(ApiPath.HURTS_COST_BY_HEARTS_QUANTITY).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), proportionRest::getHeartPriceByHeartsAmount)
                .andRoute(RequestPredicates.POST(ApiPath.HURTS_TO_ACTION_TYPE_PROPORTIONS_CREATE).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), proportionRest::createOrderRecord)
                .andRoute(RequestPredicates.GET(ApiPath.HURTS_TO_ACTION_TYPE_PROPORTIONS_GET).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), proportionRest::getAllOrderPrices)
                .andRoute(RequestPredicates.GET(ApiPath.HURTS_TO_ACTION_TYPE_PROPORTIONS_GET_BY_TYPE_AND_QUANTITY).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), proportionRest::getOrderPrice)

                //ORDERS
                .andRoute(RequestPredicates.GET(ApiPath.ORDERS_GET_TO_EXECUTE).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), orderRest::getAllAvailableOrders)
                .andRoute(RequestPredicates.POST(ApiPath.ORDERS_EXECUTION_COMPLETE).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), orderRest::completeOrderAction)
                .andRoute(RequestPredicates.POST(ApiPath.ORDERS_CREATE).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), orderRest::createOrder)
                .andRoute(RequestPredicates.POST(ApiPath.ORDERS_SKIP).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), orderRest::skipOrder)
                .andRoute(RequestPredicates.GET(ApiPath.ORDERS_GET_ALL_ACTION_TYPES).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), orderRest::getAllActionType)

                //TIKTOK INFO
                .andRoute(RequestPredicates.GET(ApiPath.TIKTOK_ACCOUNT_GET_LAST_VIDEOS).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userTiktokAccountRest::getLatestTiktokUserVideos)
                .andRoute(RequestPredicates.GET(ApiPath.TIKTOK_ACCOUNT_GET_DETAILS).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userTiktokAccountRest::getUserTiktokProfileInfo)

                //DOWNLOAD VIDEO
                .andRoute(RequestPredicates.GET(ApiPath.DOWNLOADS_FIND_VIDEO_BY_LINK).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), videoDownloadRest::findVideoForDownload)
                .andRoute(RequestPredicates.GET(ApiPath.DOWNLOADS_GET_ALL_VIDEO_HISTORY).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), videoDownloadRest::findAllDownloadsVideoByYear)
                .andRoute(RequestPredicates.POST(ApiPath.DOWNLOADS_VIDEO_DOWNLOAD).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), videoDownloadRest::downloadVideo)

                //STATISTICS
                .andRoute(RequestPredicates.GET(ApiPath.STATISTICS_GET_SINGLE_ORDER_DETAILS).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), orderStatisticRest::getOderInfo)
                .andRoute(RequestPredicates.DELETE(ApiPath.STATISTICS_CLEAR_SINGLE_ORDER_HISTORY).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), orderStatisticRest::clearOrderHistory)
                .andRoute(RequestPredicates.GET(ApiPath.STATISTICS_GET_ACTIVE_ORDERS_PROGRESSES).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), orderStatisticRest::getActiveOrdersByUser)
                .andRoute(RequestPredicates.DELETE(ApiPath.STATISTICS_INVALIDATE_ACTIVE_ORDER).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), orderStatisticRest::invalidateUserOrder)
                .andRoute(RequestPredicates.GET(ApiPath.STATISTICS_GET_ALL_DONE_ORDER_ACTIONS_PAGES_QUANTITY).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), orderStatisticRest::getOrdersHistory)

                //DEPOSITS
                .andRoute(RequestPredicates.GET(ApiPath.DEPOSITS_GET_CURRENT_BALANCE).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), paymentRest::getActualUserBalance)
                .andRoute(RequestPredicates.POST(ApiPath.DEPOSITS_INCREASE).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), paymentRest::increaseUserBalance)

                //APP SETTINGS
                .andRoute(RequestPredicates.GET(SETTINGS_GET_BY_PACKAGE_NAME).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), appSettingsRest::getSettingsByPackageName)
                .andRoute(RequestPredicates.POST(SETTINGS_CREATE_OR_UPDATE_APP_SETTINGS).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), appSettingsRest::createOrUpdateAppSettings)

                //FIREBASE APPS
                .andRoute(RequestPredicates.GET(FIREBASE_GET_ALL_FIREBASE_APPS).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), fireBaseApplicationRest::getAllFireBaseApplications)
                .andRoute(RequestPredicates.POST(FIREBASE_CREATE_OR_UPDATE_APP_FIREBASE).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), fireBaseApplicationRest::createOrUpdateFireBaseApplications)

                //USER DEVICES
                .andRoute(RequestPredicates.GET(DEVICE_GET_ALL_BY_USER).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userDeviceRest::findAllUserDevices)
                .andRoute(RequestPredicates.DELETE(DEVICE_REMOVE_BY_ID).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userDeviceRest::removeDeviceById)
                .andRoute(RequestPredicates.POST(DEVICE_ATTRIBUTES_CREATE_OR_UPDATE).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userDeviceRest::createOrUpdateDeviceAttributes);
    }
}