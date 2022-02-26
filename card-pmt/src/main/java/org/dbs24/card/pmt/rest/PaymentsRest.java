/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.rest;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.card.pmt.component.PaymentsService;
import org.dbs24.card.pmt.component.WaPaymentsService;
import org.dbs24.card.pmt.rest.payment.api.CreatePaymentRequest;
import org.dbs24.card.pmt.rest.payment.wa.api.CreateWaPaymentRequest;
import org.dbs24.card.pmt.rest.payment.wa.api.cancel.CreateCancelWaPaymentRequest;
import org.dbs24.rest.api.NewReactiveRestProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@EqualsAndHashCode(callSuper = true)
public class PaymentsRest extends NewReactiveRestProcessor {

    final PaymentsService paymentsService;
    final WaPaymentsService waPaymentsService;

    public PaymentsRest(PaymentsService paymentsService, WaPaymentsService waPaymentService) {
        this.paymentsService = paymentsService;
        this.waPaymentsService = waPaymentService;
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdatePayment(ServerRequest request) {

        return buildPostRequest(request, CreatePaymentRequest.class, paymentsService::createOrUpdatePayment);
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdateWaPayment(ServerRequest request) {

        return buildPostRequest(request, CreateWaPaymentRequest.class, waPaymentsService::createOrUpdateWaPayment);
    }

    //==========================================================================
    public Mono<ServerResponse> cancelWaPayment(ServerRequest request) {

        return buildPostRequest(request, CreateCancelWaPaymentRequest.class, waPaymentsService::cancelWaPayment);
    }

}
