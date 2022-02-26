/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.card.pmt.rest.PaymentsRest;
import org.dbs24.card.pmt.rest.payment.api.CreatePaymentRequest;
import org.dbs24.card.pmt.rest.payment.api.CreatedPaymentResponse;
import org.dbs24.card.pmt.rest.payment.wa.api.CreateWaPaymentRequest;
import org.dbs24.card.pmt.rest.payment.wa.api.CreatedWaPaymentResponse;
import org.dbs24.card.pmt.rest.payment.wa.api.cancel.CreateCancelWaPaymentRequest;
import org.dbs24.card.pmt.rest.payment.wa.api.cancel.CreatedCancelWaPaymentResponse;
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

import static org.dbs24.card.pmt.consts.CardPaymentConsts.UriConsts.*;
import static org.dbs24.consts.RestHttpConsts.HTTP_200_STRING;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Log4j2
@Configuration
@EnableCaching
@EqualsAndHashCode(callSuper = true)
public class CardPaymentRestConfig extends AbstractWebSecurityConfig {

    @RouterOperations({
            @RouterOperation(path = URI_CREATE_OR_UPDATE_PAYMENT, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_PAYMENT, requestBody = @RequestBody(description = "Payment details", content = @Content(schema = @Schema(implementation = CreatePaymentRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create new payment", content = @Content(schema = @Schema(implementation = CreatedPaymentResponse.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_WA_PAYMENT, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_WA_PAYMENT, requestBody = @RequestBody(description = "WA Payment details", content = @Content(schema = @Schema(implementation = CreateWaPaymentRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create new WA payment", content = @Content(schema = @Schema(implementation = CreatedWaPaymentResponse.class))))),
            @RouterOperation(path = URI_CANCEL_WA_PAYMENT, method = POST, operation = @Operation(operationId = URI_CANCEL_WA_PAYMENT, requestBody = @RequestBody(description = "WA Payment details", content = @Content(schema = @Schema(implementation = CreateCancelWaPaymentRequest.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "cancel WA payment", content = @Content(schema = @Schema(implementation = CreatedCancelWaPaymentResponse.class)))))
    })

    @Bean
    public RouterFunction<ServerResponse> routerProxyCoreRest(PaymentsRest paymentsRest) {

        return addCommonRoutes()
                // Payment
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_PAYMENT), paymentsRest::createOrUpdatePayment)
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_WA_PAYMENT), paymentsRest::createOrUpdateWaPayment)
                .andRoute(postRoute(URI_CANCEL_WA_PAYMENT), paymentsRest::cancelWaPayment);
    }
}
