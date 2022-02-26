/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.component;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.card.pmt.dao.PaymentDao;
import org.dbs24.card.pmt.entity.ApplePayment;
import org.dbs24.card.pmt.entity.GooglePayment;
import org.dbs24.card.pmt.rest.payment.api.CreatePaymentRequest;
import org.dbs24.card.pmt.rest.payment.api.CreatedPayment;
import org.dbs24.card.pmt.rest.payment.api.CreatedPaymentResponse;
import org.dbs24.card.pmt.rest.payment.validator.PaymentInfoValidator;
import org.dbs24.card.pmt.rest.payment.validator.WaPaymentInfoValidator;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_INVALID_ENTITY_ATTRS;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "card-pmt")
public class PaymentsService extends AbstractRestApplicationService {

    final PaymentDao paymentDao;
    final RefsService refsService;
    final PaymentInfoValidator paymentInfoValidator;
    final WaPaymentInfoValidator waPaymentInfoValidator;

    public PaymentsService(RefsService refsService, PaymentDao paymentDao, PaymentInfoValidator paymentInfoValidator, WaPaymentInfoValidator waPaymentInfoValidator) {
        this.refsService = refsService;
        this.paymentDao = paymentDao;
        this.paymentInfoValidator = paymentInfoValidator;
        this.waPaymentInfoValidator = waPaymentInfoValidator;
    }

    //==========================================================================
    @Transactional
    public CreatedPaymentResponse createOrUpdatePayment(Mono<CreatePaymentRequest> monoRequest) {

        return this.<CreatedPayment, CreatedPaymentResponse>createAnswer(CreatedPaymentResponse.class,
                (responseBody, createdPayment) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(paymentInfoValidator.validateConditional(request.getEntityInfo(), paymentInfo
                        -> {

                    final SimpleActionInfo simpleActionInfo = request.getEntityAction();

                    log.info("simpleActionInfo =  {}", simpleActionInfo);

//                    createdPayment.setCreatedPaymentId(payment.getPaymentId());

                    responseBody.setCode(OC_OK);
                    //responseBody.setMessage(String.format("Payment is %s (%d)", isNewPayment ? "created" : "updated", payment.getPaymentId()));
                }, errorInfos -> {
                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                }))));
    }

    //==========================================================================
    public GooglePayment createGooglePayment() {
        return StmtProcessor.create(GooglePayment.class, a -> {
            a.setPaymentDate(LocalDateTime.now());
        });
    }

    public ApplePayment createApplePayment() {
        return StmtProcessor.create(ApplePayment.class, a -> {
            a.setPaymentDate(LocalDateTime.now());
        });
    }        
    
    public GooglePayment findGooglePayment(Integer paymentId) {
        return paymentDao.findGooglePayment(paymentId);
    }    

    public ApplePayment findApplePayment(Integer paymentId) {
        return paymentDao.findApplePayment(paymentId);
    }    
    
    public GooglePayment findOrCreateGooglePayment(Integer paymentId) {
        return (Optional.ofNullable(paymentId)
                .orElseGet(() -> 0) > 0)
                ? findGooglePayment(paymentId)
                : createGooglePayment();
    }
    
    public ApplePayment findOrCreateApplePayment(Integer paymentId) {
        return (Optional.ofNullable(paymentId)
                .orElseGet(() -> 0) > 0)
                ? findApplePayment(paymentId)
                : createApplePayment();
    }    
}
