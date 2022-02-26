/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.rest.AppPromotionRest;
import org.dbs24.app.promo.rest.dto.action.CreateOrderActionRequest;
import org.dbs24.app.promo.rest.dto.action.CreatedOrderActionResponse;
import org.dbs24.app.promo.rest.dto.apppackage.CreatePackageRequest;
import org.dbs24.app.promo.rest.dto.apppackage.CreatedPackageResponse;
import org.dbs24.app.promo.rest.dto.batchsetup.CreateBatchSetupRequest;
import org.dbs24.app.promo.rest.dto.batchsetup.CreatedBatchSetupResponse;
import org.dbs24.app.promo.rest.dto.batchtemplate.CreateBatchTemplateRequest;
import org.dbs24.app.promo.rest.dto.batchtemplate.CreatedBatchTemplateResponse;
import org.dbs24.app.promo.rest.dto.bot.CreateBotRequest;
import org.dbs24.app.promo.rest.dto.bot.CreatedBotResponse;
import org.dbs24.app.promo.rest.dto.comment.CreateCommentRequest;
import org.dbs24.app.promo.rest.dto.comment.CreatedCommentResponse;
import org.dbs24.app.promo.rest.dto.order.CreateOrderRequest;
import org.dbs24.app.promo.rest.dto.order.CreatedOrderResponse;
import org.dbs24.config.AbstractWebSecurityConfig;
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

import static org.dbs24.app.promo.consts.AppPromoutionConsts.UriConsts.*;
import static org.dbs24.consts.RestHttpConsts.HTTP_200_STRING;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Log4j2
@Configuration
@EnableCaching
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "app-promo")
public class AppPromoutionRestConfig extends AbstractWebSecurityConfig {

    public AppPromoutionRestConfig() {
    }

    @RouterOperations({
            @RouterOperation(path = URI_CREATE_OR_UPDATE_BOT, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_BOT, requestBody = @RequestBody(description = "Bot details", content = @Content(schema = @Schema(implementation = CreateBotRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create/update bot", content = @Content(schema = @Schema(implementation = CreatedBotResponse.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_COMMENT, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_COMMENT, requestBody = @RequestBody(description = "Comment details", content = @Content(schema = @Schema(implementation = CreateCommentRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create/update comment", content = @Content(schema = @Schema(implementation = CreatedCommentResponse.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_PACKAGE, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_PACKAGE, requestBody = @RequestBody(description = "Package details", content = @Content(schema = @Schema(implementation = CreatePackageRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create/update package", content = @Content(schema = @Schema(implementation = CreatedPackageResponse.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_BATCH_SETUP, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_BATCH_SETUP, requestBody = @RequestBody(description = "Batch setup details", content = @Content(schema = @Schema(implementation = CreateBatchSetupRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create/update batchSetup", content = @Content(schema = @Schema(implementation = CreatedBatchSetupResponse.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_ORDER, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_ORDER, requestBody = @RequestBody(description = "Order details", content = @Content(schema = @Schema(implementation = CreateOrderRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create/update order", content = @Content(schema = @Schema(implementation = CreatedOrderResponse.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_ORDER_ACTON, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_ORDER_ACTON, requestBody = @RequestBody(description = "action details", content = @Content(schema = @Schema(implementation = CreateOrderActionRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create/update order action", content = @Content(schema = @Schema(implementation = CreatedOrderActionResponse.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_BATCH_TEMPLATE, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_BATCH_TEMPLATE, requestBody = @RequestBody(description = "batch template", content = @Content(schema = @Schema(implementation = CreateBatchTemplateRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create/update batch template", content = @Content(schema = @Schema(implementation = CreatedBatchTemplateResponse.class))))),
            @RouterOperation
    })

    @Bean
    public RouterFunction<ServerResponse> routerProxyCoreRest(AppPromotionRest appPromotionRest) {

        return addCommonRoutes()
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_BOT), appPromotionRest::createOrUpdateBot)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_COMMENT), appPromotionRest::createOrUpdateComment)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_PACKAGE), appPromotionRest::createOrUpdatePackage)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_BATCH_SETUP), appPromotionRest::createOrUpdateBatchSetup)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_ORDER), appPromotionRest::createOrUpdateOrder)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_ORDER_ACTON), appPromotionRest::createOrUpdateOrderAction)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_BATCH_TEMPLATE), appPromotionRest::createOrUpdateBatchTemplate);
    }
}
