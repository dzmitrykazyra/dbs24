/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.dao;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.card.pmt.entity.*;
import org.dbs24.card.pmt.repo.*;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Data
@Log4j2
@Component
public class PaymentDao extends DaoAbstractApplicationService {

    final ApplePaymentRepo applePaymentRepo;
    final GooglePaymentRepo googlePaymentRepo;
    final GooglePaymentHistRepo googlePaymentHistRepo;
    final ApplyPaymentHistRepo applePaymentHistRepo;
    final PaymentHistRepo paymentHistRepo;
    final PayerRepo payerRepo;

    public PaymentDao(ApplePaymentRepo applePaymentRepo, GooglePaymentRepo googlePaymentRepo, PayerRepo payerRepo, GooglePaymentHistRepo googlePaymentHistRepo, ApplyPaymentHistRepo applePaymentHistRepo, PaymentHistRepo paymentHistRepo) {
        this.applePaymentRepo = applePaymentRepo;
        this.googlePaymentRepo = googlePaymentRepo;
        this.payerRepo = payerRepo;
        this.googlePaymentHistRepo = googlePaymentHistRepo;
        this.applePaymentHistRepo = applePaymentHistRepo;
        this.paymentHistRepo = paymentHistRepo;
    }

    public ApplePayment findApplePayment(Integer paymentId) {

        return applePaymentRepo
                .findById(paymentId)
                .orElseThrow(() -> new RuntimeException(String.format("Apple paymentId not found (%d)", paymentId)));
    }

    public GooglePayment findGooglePayment(Integer paymentId) {

        return googlePaymentRepo
                .findById(paymentId)
                .orElseThrow(() -> new RuntimeException(String.format("Google paymentId not found (%d)", paymentId)));
    }

    public Optional<GooglePayment> findGooglePaymentByGoogleOrderId(String googleOrderId) {

        return googlePaymentRepo
                .findByGoogleOrderId(googleOrderId)
                .stream()
                .findFirst();
    }

    public Optional<ApplePayment> findApplePaymentByAppleTransactionId(String appleTransactionId) {

        return applePaymentRepo
                .findByAppleTransactionId(appleTransactionId)
                .stream()
                .findFirst();
    }

    public Optional<Payer> findPayer(String loginToken) {

        return payerRepo.findByLoginToken(loginToken);
    }

    public Optional<Payer> findPayerApplePayment(String appleOriginalTransactionId) {

        return applePaymentRepo.findByAppleOriginalTransactionId(appleOriginalTransactionId)
                .stream()
                .findFirst()
                .map(AbstractPayment::getPayer);

    }

    public Optional<Payer> findPayerGooglePayment(String googlePurchaseToken) {

        return googlePaymentRepo.findByGooglePurchaseToken(googlePurchaseToken)
                .stream()
                .findFirst()
                .map(AbstractPayment::getPayer);
    }

    public Optional<ApplePayment> findAppleOriginalTransactionIdPayment(String appleOriginalTransactionId) {

        return applePaymentRepo.findByAppleOriginalTransactionId(appleOriginalTransactionId)
                .stream()
                .findFirst();
    }

    public Optional<ApplePayment> findAppleTransactionIdPayment(String appleTransactionId) {

        return applePaymentRepo.findByAppleTransactionId(appleTransactionId)
                .stream()
                .findFirst();
    }

    public Optional<GooglePayment> findGooglePurchaseTokenPayment(String googlePurchaseToken) {

        return googlePaymentRepo.findByGooglePurchaseToken(googlePurchaseToken)
                .stream()
                .findFirst();
    }

    public Optional<GooglePayment> findGoogleOrderIdPayment(String googleOrderId) {

        return googlePaymentRepo.findByGoogleOrderId(googleOrderId)
                .stream()
                .findFirst();
    }

    public Optional<ApplePayment> findApplePaymentById(Integer paymentId) {

        return applePaymentRepo.findById(paymentId);
    }

    public Optional<GooglePayment> findGooglePaymentById(Integer paymentId) {

        return googlePaymentRepo.findById(paymentId);
    }


    public synchronized void saveApplePayment(ApplePayment applePayment) {
        applePaymentRepo.save(applePayment);
    }

    public synchronized void saveGooglePayment(GooglePayment googlePayment) {
        googlePaymentRepo.save(googlePayment);
    }

    //==================================================================================================================
    public synchronized void saveAbstractPaymentHist(AbstractPayment abstractPayment) {
        paymentHistRepo.save(StmtProcessor.create(AbstractPaymentHist.class,
                abstractPaymentHist -> {

                    abstractPaymentHist.setPaymentId(abstractPayment.getPaymentId());
                    abstractPaymentHist.setActualDate(abstractPayment.getActualDate());
                    abstractPaymentHist.setApplication(abstractPayment.getApplication());
                    abstractPaymentHist.setAppPackage(abstractPayment.getAppPackage());
                    //abstractPaymentHist.setCountry(abstractPayment.getCountry());
                    abstractPaymentHist.setCurrency(abstractPayment.getCurrency());
                    abstractPaymentHist.setPayer(abstractPayment.getPayer());
                    abstractPaymentHist.setPaymentDate(abstractPayment.getPaymentDate());
                    abstractPaymentHist.setPaymentService(abstractPayment.getPaymentService());
                    abstractPaymentHist.setPaymentStatus(abstractPayment.getPaymentStatus());
                    abstractPaymentHist.setPaymentSumm(abstractPayment.getPaymentSumm());
                    abstractPaymentHist.setPaymentSummMicros(abstractPayment.getPaymentSummMicros());
                    abstractPaymentHist.setPmtNote(abstractPayment.getPmtNote());
                    abstractPaymentHist.setPlatform(abstractPayment.getPlatform());
                }));
    }

    //==================================================================================================================
    public synchronized void saveGooglePaymentHist(GooglePayment googlePayment) {
        googlePaymentHistRepo.save(StmtProcessor.create(GooglePaymentHist.class,
                googlePaymentHist -> {

                    googlePaymentHist.setGoogleOrderId(googlePayment.getGoogleOrderId());
                    googlePaymentHist.setGooglePurchaseToken(googlePayment.getGooglePurchaseToken());
                    googlePaymentHist.setGoogleSku(googlePayment.getGoogleSku());
                    googlePaymentHist.setGooglePurchaseToken(googlePayment.getGooglePurchaseToken());
                    googlePaymentHist.setActualDate(googlePayment.getActualDate());
                    googlePaymentHist.setPaymentId(googlePayment.getPaymentId());
                }));

        saveAbstractPaymentHist(googlePayment);

    }

    //==================================================================================================================
    public synchronized void saveApplePaymentHist(ApplePayment applePayment) {
        applePaymentHistRepo.save(StmtProcessor.create(ApplePaymentHist.class,
                applePaymentHist -> {

                    applePaymentHist.setAppleProductId(applePayment.getAppleProductId());
                    applePaymentHist.setAppleTransactionId(applePayment.getAppleTransactionId());
                    applePaymentHist.setAppleOriginalTransactionId(applePayment.getAppleOriginalTransactionId());
                    applePaymentHist.setActualDate(applePayment.getActualDate());
                    applePaymentHist.setPaymentId(applePayment.getPaymentId());
                }));

        saveAbstractPaymentHist(applePayment);

    }

    public synchronized void savePayer(Payer payer) {
        payerRepo.save(payer);
    }
}
