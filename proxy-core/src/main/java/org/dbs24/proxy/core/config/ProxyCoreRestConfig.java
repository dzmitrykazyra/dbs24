/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.dbs24.config.AbstractWebSecurityConfig;
import org.dbs24.proxy.core.entity.dto.request.*;
import org.dbs24.proxy.core.entity.dto.response.*;
import org.dbs24.proxy.core.rest.*;
import org.slf4j.IMarkerFactory;
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

import static org.dbs24.consts.RestHttpConsts.HTTP_200_STRING;
import static org.dbs24.consts.RestHttpConsts.URI_API;
import static org.dbs24.proxy.core.rest.api.consts.ProxyHttpConsts.RestQueryParams.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Log4j2
@Configuration
@EnableCaching
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "proxy-core")
public class ProxyCoreRestConfig extends AbstractWebSecurityConfig {

    final ProxyRest proxyRest;
    final UpdateProxyRest updateProxyRest;
    final RequestRest requestRest;
    final ApplicationRest applicationRest;
    final ApplicationNetworkRest applicationNetworkRest;
    final ProxyUsageErrorRest proxyUsageErrorRest;
    final ProxyUsageBanRest proxyUsageBanRest;
    final ReferenceRest referenceRest;
    final ProxyUsageRest proxyUsageRest;

    public static final String URI_CREATE_OR_UPDATE_APPLICATION = URI_API + "/createOrUpdateApplication";
    public static final String URI_BOOK = URI_API + "/book";
    public static final String URI_BOOK_BY_PROXY_ID = URI_API + "/bookById";
    public static final String URI_UPDATE_BOOKED_PROXY = URI_BOOK + "/update";
    public static final String URI_CREATE_OR_UPDATE_APPLICATION_NETWORK = URI_API + "/createOrUpdateApplicationNetwork";
    public static final String URI_CREATE_USAGE_ERROR = URI_API + "/createUsageError";
    public static final String URI_CREATE_USAGE_BAN = URI_API + "/createUsageBan";

    public static final String URI_GET_AMOUNT_ACTUAL_PROXIES = URI_API + "/getAmountActualProxies";

    public static final String URI_FINALIZE_USAGES_BY_REQUEST_ID = URI_API + "/finalizeUsagesByRequestId";
    public static final String URI_FINALIZE_USAGES_BY_APPLICATION_ID = URI_API + "/finalizeUsagesByApplicationId";

    public static final String URI_GET_PROXY_TYPES = URI_API + "/getProxyTypes";
    public static final String URI_GET_PROXY_PROVIDERS = URI_API + "/getProxyProviders";
    public static final String URI_GET_COUNTRIES = URI_API + "/getCountries";
    public static final String URI_GET_NETWORKS = URI_API + "/getNetworks";
    public static final String URI_GET_APPLICATIONS = URI_API + "/getApplications";
    public static final String URI_GET_ALGORITHM_SELECTIONS = URI_API + "/getAlgorithmSelections";

    public ProxyCoreRestConfig(ProxyRest proxyRest,
                               UpdateProxyRest updateProxyRest,
                               RequestRest requestRest,
                               ApplicationRest applicationRest,
                               ApplicationNetworkRest applicationNetworkRest,
                               ProxyUsageErrorRest proxyUsageErrorRest,
                               ProxyUsageBanRest proxyUsageBanRest,
                               ReferenceRest referenceRest,
                               ProxyUsageRest proxyUsageRest) {
        this.proxyRest = proxyRest;
        this.updateProxyRest = updateProxyRest;
        this.requestRest = requestRest;
        this.applicationRest = applicationRest;
        this.applicationNetworkRest = applicationNetworkRest;
        this.proxyUsageErrorRest = proxyUsageErrorRest;
        this.proxyUsageBanRest = proxyUsageBanRest;
        this.referenceRest = referenceRest;
        this.proxyUsageRest = proxyUsageRest;
    }

    @RouterOperations({
            @RouterOperation(
                    path = URI_CREATE_OR_UPDATE_APPLICATION,
                    method = POST,
                    operation = @Operation(
                            operationId = URI_CREATE_OR_UPDATE_APPLICATION,
                            requestBody = @RequestBody(
                                    description =
                                            "Request consisting of entity action info and application DTO (name, description and application network) " +
                                                    "to create application bounded with specific network",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreateApplicationRequest.class
                                            )
                                    )
                            ),
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Metadata and created application DTO response / error DTO response",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreateApplicationResponse.class
                                            )))
                    )),
            @RouterOperation(
                    path = URI_BOOK_BY_PROXY_ID,
                    method = POST,
                    operation = @Operation(
                            operationId = URI_BOOK_BY_PROXY_ID,
                            requestBody = @RequestBody(
                                    description =
                                            "Request consisting of entity action info and book single proxy server parameters DTO.",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = BookProxyRequest.class
                                            )
                                    )
                            ),
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description =
                                            "Response contains booked proxy entity",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = BookedProxyResponse.class
                                            )))
                    )),
            @RouterOperation(
                    path = URI_BOOK,
                    method = POST,
                    operation = @Operation(
                            operationId = URI_BOOK,
                            requestBody = @RequestBody(
                                    description =
                                            "Request consisting of entity action info and book proxy server parameters DTO." +
                                                    "Fields, which can be blank, may contain values like 'any', " +
                                                    "which means that these parameters can take any values during searching proxies list",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = BookProxiesRequest.class
                                            )
                                    )
                            ),
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description =
                                            "Response DTO, which contains target booked by required parameters proxies list " +
                                                    "with required size or less (if there is no available free proxies now)",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = ProxyListResponse.class
                                            )))
                    )),
            @RouterOperation(
                    path = URI_UPDATE_BOOKED_PROXY,
                    method = POST,
                    operation = @Operation(
                            operationId = URI_UPDATE_BOOKED_PROXY,
                            requestBody = @RequestBody(
                                    description = "Request consisting of action info and update request DTO.",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UpdateBookProxiesRequest.class
                                            )
                                    )
                            ),
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Response DTO, which contains all actual proxies from old request_id + newest proxies",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = ProxyListResponse.class
                                            )
                                    )
                            )
                    )
            ),
            @RouterOperation(
                    path = URI_GET_AMOUNT_ACTUAL_PROXIES,
                    method = GET,
                    operation = @Operation(
                            operationId = URI_GET_AMOUNT_ACTUAL_PROXIES,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Response DTO, which contains all proxies from request and amount of actual proxies",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CheckProxyRelevanceResponse.class
                                            )
                                    )
                            ),
                            parameters = @Parameter(
                                    name = QP_REQUEST_ID,
                                    in = ParameterIn.QUERY,
                                    schema = @Schema(type = "Integer"),
                                    description = "Request id to find proxies"
                            )
                    )

            ),
            @RouterOperation(
                    path = URI_CREATE_OR_UPDATE_APPLICATION_NETWORK,
                    method = POST,
                    operation = @Operation(
                            operationId = URI_CREATE_OR_UPDATE_APPLICATION_NETWORK,
                            requestBody = @RequestBody(
                                    description = "Request consisting of entity action info and application DTO (application network name)",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreateApplicationNetworkRequest.class
                                            )
                                    )
                            ),
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Metadata and created application network DTO response / error DTO response",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreateApplicationNetworkResponse.class
                                            )))
                    )),
            @RouterOperation(
                    path = URI_CREATE_USAGE_ERROR,
                    method = POST,
                    operation = @Operation(
                            operationId = URI_CREATE_USAGE_ERROR,
                            requestBody = @RequestBody(
                                    description =
                                            "Request consisting of entity action info and DTO to сreate occurred during proxy usage error entity",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreateProxyUsageErrorRequest.class
                                            )
                                    )
                            ),
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Response DTO, containing message about creating error record result",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreateProxyUsageErrorResponse.class
                                            )))
                    )),
            @RouterOperation(
                    path = URI_CREATE_USAGE_BAN,
                    method = POST,
                    operation = @Operation(
                            operationId = URI_CREATE_USAGE_BAN,
                            requestBody = @RequestBody(
                                    description =
                                            "Request consisting of entity action info and DTO to сreate occurred during proxy usage ban entity",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreateProxyUsageBanRequest.class
                                            )
                                    )
                            ),
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Response DTO, containing message about creating proxy usage ban record result",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreateProxyUsageBanResponse.class
                                            )))
                    )),
            @RouterOperation(
                    path = URI_FINALIZE_USAGES_BY_REQUEST_ID,
                    method = GET,
                    operation = @Operation(
                            operationId = URI_FINALIZE_USAGES_BY_REQUEST_ID,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Finalize proxies usages(make proxies 'free') by request id",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = FinalizeUsagesResponse.class
                                            ))),
                            parameters = {
                                    @Parameter(
                                            name = QP_REQUEST_ID,
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "integer"),
                                            description = "Usage request id",
                                            example = "25"
                                    )
                            })),
            @RouterOperation(
                    path = URI_FINALIZE_USAGES_BY_APPLICATION_ID,
                    method = GET,
                    operation = @Operation(
                            operationId = URI_FINALIZE_USAGES_BY_APPLICATION_ID,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Finalize proxies usages(make proxies 'free') by application id",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = FinalizeUsagesResponse.class
                                            ))),
                            parameters = {
                                    @Parameter(
                                            name = QP_APPLICATION_ID,
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "integer"),
                                            description = "Application id",
                                            example = "1"
                                    )
                            })),

            @RouterOperation(
                    path = URI_GET_PROXY_TYPES,
                    method = GET,
                    operation = @Operation(
                            operationId = URI_GET_PROXY_TYPES,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Get all proxy types names",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = SingleValuesDtoResponse.class
                                            ))))
            ),
            @RouterOperation(
                    path = URI_GET_PROXY_PROVIDERS,
                    method = GET,
                    operation = @Operation(
                            operationId = URI_GET_PROXY_PROVIDERS,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Get all proxy providers names",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = SingleValuesDtoResponse.class
                                            ))))
            ),
            @RouterOperation(
                    path = URI_GET_COUNTRIES,
                    method = GET,
                    operation = @Operation(
                            operationId = URI_GET_COUNTRIES,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Get all countries names",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = SingleValuesDtoResponse.class
                                            ))))
            ),
            @RouterOperation(
                    path = URI_GET_ALGORITHM_SELECTIONS,
                    method = GET,
                    operation = @Operation(
                            operationId = URI_GET_ALGORITHM_SELECTIONS,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Get all algorithm selections names",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = SingleValuesDtoResponse.class
                                            ))))
            ),
            @RouterOperation(
                    path = URI_GET_NETWORKS,
                    method = GET,
                    operation = @Operation(
                            operationId = URI_GET_NETWORKS,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Get all applications networks names",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = SingleValuesDtoResponse.class
                                            ))))
            ),
            @RouterOperation(
                    path = URI_GET_APPLICATIONS,
                    method = GET,
                    operation = @Operation(
                            operationId = URI_GET_APPLICATIONS,
                            responses = @ApiResponse(
                                    responseCode = HTTP_200_STRING,
                                    description = "Get all applications/ all applications bounded with specific network",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = SingleValuesDtoResponse.class
                                            ))),
                            parameters = {
                                    @Parameter(
                                            name = QP_APPLICATION_NETWORK_NAME,
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "string"),
                                            description = "Application network to find matching applications name optional parameter",
                                            example = "tiktok")
                            })
            )
    })

    @Bean
    public RouterFunction<ServerResponse> routerProxyCoreRest() {

        return addCommonRoutes()
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_APPLICATION), applicationRest::createOrUpdateApplication)
                .andRoute(postRoute(URI_BOOK_BY_PROXY_ID), proxyRest::bookProxy)
                .andRoute(postRoute(URI_BOOK), proxyRest::bookProxies)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_APPLICATION_NETWORK), applicationNetworkRest::createOrUpdateApplicationNetwork)
                .andRoute(postRoute(URI_CREATE_USAGE_ERROR), proxyUsageErrorRest::createProxyUsageError)
                .andRoute(postRoute(URI_CREATE_USAGE_BAN), proxyUsageBanRest::createProxyUsageBan)

                .andRoute(postRoute(URI_UPDATE_BOOKED_PROXY), updateProxyRest::updateBookedProxies)
                .andRoute(getRoute(URI_GET_AMOUNT_ACTUAL_PROXIES), updateProxyRest::checkProxiesRelevance)

                .andRoute(getRoute(URI_FINALIZE_USAGES_BY_REQUEST_ID), proxyUsageRest::finalizeByRequestId)
                .andRoute(getRoute(URI_FINALIZE_USAGES_BY_APPLICATION_ID), proxyUsageRest::finalizeByApplicationId)

                .andRoute(getRoute(URI_GET_PROXY_TYPES), referenceRest::getAllProxyTypes)
                .andRoute(getRoute(URI_GET_PROXY_PROVIDERS), referenceRest::getAllProxyProviders)
                .andRoute(getRoute(URI_GET_COUNTRIES), referenceRest::getAllCountries)
                .andRoute(getRoute(URI_GET_ALGORITHM_SELECTIONS), referenceRest::getAllAlgorithmSelections)
                .andRoute(getRoute(URI_GET_NETWORKS), applicationNetworkRest::getAllApplicationNetworks)
                .andRoute(getRoute(URI_GET_APPLICATIONS), applicationRest::getAllApplications);
    }
}