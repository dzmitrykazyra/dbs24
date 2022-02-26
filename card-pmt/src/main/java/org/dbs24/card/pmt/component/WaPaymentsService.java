/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.component;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.card.pmt.dao.PaymentDao;
import org.dbs24.card.pmt.entity.ApplePayment;
import org.dbs24.card.pmt.entity.GooglePayment;
import org.dbs24.card.pmt.entity.Payer;
import org.dbs24.card.pmt.rest.payment.validator.CancelWaPaymentInfoWalidator;
import org.dbs24.card.pmt.rest.payment.validator.WaPaymentInfoValidator;
import org.dbs24.card.pmt.rest.payment.wa.api.*;
import org.dbs24.card.pmt.rest.payment.wa.api.cancel.CreateCancelWaPaymentRequest;
import org.dbs24.card.pmt.rest.payment.wa.api.cancel.CreatedCancelWaPayment;
import org.dbs24.card.pmt.rest.payment.wa.api.cancel.CreatedCancelWaPaymentResponse;
import org.dbs24.consts.SysConst;
import org.dbs24.rest.api.ResponseBody;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.dbs24.card.pmt.consts.CardPaymentConsts.ApplicationEnum.APP_WA_TRACKER;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.PaymentServiceEnum.PS_APPLE_PAY;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.PaymentServiceEnum.PS_GOOGLE_PAY;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.PaymentStatusEnum.PS_ACTUAL;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.PaymentStatusEnum.PS_CANCELLED;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.*;
import static org.dbs24.stmt.StmtProcessor.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "card-pmt")
@EqualsAndHashCode(callSuper = true)
public class WaPaymentsService extends AbstractRestApplicationService {

    @Value("${config.wa-back-end.server.uri:127.0.0.1}")
    private String uriBase;

    @Value("${config.wa-back-end.server.createContract:/api/createOrUpdateContract}")
    private String createContractPath;

    final PaymentsService paymentsService;
    final WaPaymentInfoValidator waPaymentInfoValidator;
    final CancelWaPaymentInfoWalidator cancelWaPaymentInfoWalidator;
    final PaymentDao paymentDao;

    public WaPaymentsService(PaymentsService paymentsService, WaPaymentInfoValidator waPaymentInfoValidator, PaymentDao paymentDao, CancelWaPaymentInfoWalidator cancelWaPaymentInfoWalidator) {
        this.paymentsService = paymentsService;
        this.waPaymentInfoValidator = waPaymentInfoValidator;
        this.paymentDao = paymentDao;
        this.cancelWaPaymentInfoWalidator = cancelWaPaymentInfoWalidator;
    }

    //==========================================================================
    @FunctionalInterface
    private interface NewPaymentAction {
        void processNewPayment();
    }

    @Transactional
    public CreatedWaPaymentResponse createOrUpdateWaPayment(Mono<CreateWaPaymentRequest> monoRequest) {

        return this.<CreatedWaPayment, CreatedWaPaymentResponse>createAnswer(CreatedWaPaymentResponse.class,
                (responseBody, createdWaPayment) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(waPaymentInfoValidator.validateConditional(request.getEntityInfo(), paymentInfo ->

                                updateWaPaymentIfExists(paymentInfo, createdWaPayment, responseBody, () -> {

                                    final Mono<UserContractFromPaymentInfo> mono = Mono.just(StmtProcessor.create(UserContractFromPaymentInfo.class, uci -> {
                                        uci.setBeginDate(paymentInfo.getPaymentDate());
                                        uci.setEndDate(paymentInfo.getEndDate());
                                        uci.setLoginToken(paymentInfo.getLoginToken());
                                        uci.setSubscriptionsAmount(paymentInfo.getSubsriptionAmount());
                                        uci.setContractTypeId(paymentInfo.getContractTypeId());

                                        log.debug("try 2 registry contract: {}{}, [{}]", uriBase, createContractPath, uci);

                                    }));

                                    // create contract
                                    getWebClient()
                                            .post()
                                            .uri(uriBuilder
                                                    -> uriBuilder
                                                    .path(createContractPath)
                                                    .build())
                                            .contentType(APPLICATION_JSON)
                                            .accept(APPLICATION_JSON)
                                            .body(mono, UserContractFromPaymentInfo.class)
                                            .retrieve()
                                            .bodyToMono(CreatedUserContract.class)
                                            .doOnCancel(() -> log.warn("{}: cancel ", createContractPath))
                                            .doOnError(error -> {
                                                        log.error("{}: exception: {}", createContractPath, error.getMessage());

                                                        responseBody.setCode(OC_GENERAL_ERROR);
                                                        responseBody.setMessage(String.format("Can't create contract [%s]", error.getMessage()).toUpperCase());
                                                        responseBody.complete();
                                                    }
                                            )
                                            .subscribe(cuc -> {

                                                log.debug("CreatedUserContract =  {}", cuc);

                                                ifTrue(!cuc.getAnswerCode().equals(SysConst.INTEGER_ZERO), () -> {
                                                    // can't create contract

                                                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                                                    responseBody.setMessage(String.format("Can't create contract [backEnd answerCode: %d, backEnd message='%s]", cuc.getAnswerCode(), cuc.getNote()));
                                                    responseBody.complete();

                                                }, () -> {

                                                    final SimpleActionInfo simpleActionInfo = request.getEntityAction();

                                                    log.debug("simpleActionInfo =  {}, {}", simpleActionInfo, paymentInfo);

                                                    final Payer payer = paymentDao.findPayer(paymentInfo.getLoginToken())
                                                            .orElseGet(() -> StmtProcessor.create(Payer.class, p -> {
                                                                p.setLoginToken(paymentInfo.getLoginToken());
                                                                p.setApplication(paymentsService.getRefsService().findApplication(APP_WA_TRACKER.getCode()));
                                                                // save new payer
                                                                paymentDao.savePayer(p);
                                                            }));

                                                    // IOS
                                                    ifTrue(paymentInfo.getPaymentServiceId().equals(PS_APPLE_PAY.getCode()), () -> {

                                                        final ApplePayment payment = paymentsService.findOrCreateApplePayment(paymentInfo.getPaymentId());

                                                        final Boolean isNewPayment = StmtProcessor.isNull(payment.getPaymentId());

                                                        // assign payer
                                                        payment.setPayer(payer);

                                                        // assign paymentInfo
                                                        assignApplePaymentInfo(payment, paymentInfo);

                                                        paymentDao.saveApplePayment(payment);

                                                        createdWaPayment.setCreatedPaymentId(payment.getPaymentId());

                                                        responseBody.setMessage(String.format("ApplePayment is %s (%d)", isNewPayment ? "created" : "updated", payment.getPaymentId()));

                                                    });

                                                    // Google
                                                    ifTrue(paymentInfo.getPaymentServiceId().equals(PS_GOOGLE_PAY.getCode()), () -> {

                                                        final GooglePayment googlePayment = paymentsService.findOrCreateGooglePayment(paymentInfo.getPaymentId());

                                                        final Boolean isNewPayment = StmtProcessor.isNull(googlePayment.getPaymentId());

                                                        // assign payer
                                                        googlePayment.setPayer(payer);

                                                        // assign paymentInfo
                                                        assignGooglePaymentInfo(googlePayment, paymentInfo);

                                                        paymentDao.saveGooglePayment(googlePayment);

                                                        createdWaPayment.setCreatedPaymentId(googlePayment.getPaymentId());

                                                        responseBody.setMessage(String.format("GooglePayment is %s (%d)", isNewPayment ? "created" : "updated", googlePayment.getPaymentId()));

                                                    });

                                                    responseBody.setCode(OC_OK);
                                                    responseBody.complete();

                                                    log.debug("finish payment processing: {}", responseBody);

                                                });
                                            }, throwable -> {
                                                responseBody.setCode(OC_GENERAL_ERROR);
                                                responseBody.setMessage(throwable.getMessage());

                                                throwable.printStackTrace();
                                                responseBody.complete();
                                            });
                                })

                        //waitForComplected(responseBody);
                        , errorInfos -> {
                            responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                            responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                        }))));
    }

    @Transactional
    public CreatedCancelWaPaymentResponse cancelWaPayment(Mono<CreateCancelWaPaymentRequest> monoRequest) {

        return this.<CreatedCancelWaPayment, CreatedCancelWaPaymentResponse>createAnswer(CreatedCancelWaPaymentResponse.class,
                (responseBody, createdCancelWaPayment) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(cancelWaPaymentInfoWalidator.validateConditional(request.getEntityInfo(), cancelWaPaymentInfo ->

                                // paymentId
                                ifTrue(notNull(cancelWaPaymentInfo.getPaymentId()), () -> {

                                    final Optional<ApplePayment> optionalApplePayment = paymentDao.findApplePaymentById(cancelWaPaymentInfo.getPaymentId());
                                    final Optional<GooglePayment> optionalGooglePayment = paymentDao.findGooglePaymentById(cancelWaPaymentInfo.getPaymentId());

                                    cancelApplePayment(optionalApplePayment, cancelWaPaymentInfo.getCancelNote(), createdCancelWaPayment);
                                    cancelGooglePayment(optionalGooglePayment, cancelWaPaymentInfo.getCancelNote(), createdCancelWaPayment);

                                }, () -> {

                                    ifNotNull(cancelWaPaymentInfo.getAppleTransactionId(), () -> {

                                        final Optional<ApplePayment> optionalApplePayment = paymentDao.findAppleTransactionIdPayment(cancelWaPaymentInfo.getAppleTransactionId());

                                        // find ApplePayment by
                                        cancelApplePayment(optionalApplePayment, cancelWaPaymentInfo.getCancelNote(), createdCancelWaPayment);
                                    });

                                    ifNotNull(cancelWaPaymentInfo.getGoogleOrderId(), () -> {

                                        final Optional<GooglePayment> optionalGooglePayment = paymentDao.findGoogleOrderIdPayment(cancelWaPaymentInfo.getGoogleOrderId());

                                        // find GooglePayment by
                                        cancelGooglePayment(optionalGooglePayment, cancelWaPaymentInfo.getCancelNote(), createdCancelWaPayment);
                                    });
                                })
                        //waitForComplected(responseBody);
                        , errorInfos -> {
                            responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                            responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                        }))));
    }

    private void cancelApplePayment(Optional<ApplePayment> optionalApplePayment, String cancelNote, CreatedCancelWaPayment createdCancelWaPayment) {
        optionalApplePayment.ifPresent(applePayment -> {
            paymentDao.saveApplePaymentHist(applePayment);
            applePayment.setPaymentStatus(paymentsService.getRefsService().findPaymentStatus(PS_CANCELLED.getCode()));
            applePayment.setPmtNote(nvl(cancelNote, "Cancel test payment"));
            paymentDao.saveApplePayment(applePayment);
            createdCancelWaPayment.setCancelledPaymentId(applePayment.getPaymentId());
        });
    }

    private void cancelGooglePayment(Optional<GooglePayment> optionalGooglePayment, String cancelNote, CreatedCancelWaPayment createdCancelWaPayment) {
        optionalGooglePayment.ifPresent(googlePayment -> {
            paymentDao.saveGooglePayment(googlePayment);
            googlePayment.setPaymentStatus(paymentsService.getRefsService().findPaymentStatus(PS_CANCELLED.getCode()));
            googlePayment.setPmtNote(nvl(cancelNote, "Cancel test payment"));
            paymentDao.saveGooglePayment(googlePayment);
            createdCancelWaPayment.setCancelledPaymentId(googlePayment.getPaymentId());
        });
    }

    //==========================================================================
    private void assignGooglePaymentInfo(GooglePayment payment, WaPaymentInfo paymentInfo) {

        payment.setApplication(paymentsService.getRefsService().findApplication(APP_WA_TRACKER.getCode()));
        payment.setActualDate(LocalDateTime.now());
        //payment.setCountry(paymentsService.getRefsService().findCountry(notNull(paymentInfo.getCountryCode()) ? paymentInfo.getCountryCode() : "BY"));
        payment.setCurrency(paymentsService.getRefsService().findCurrency(paymentInfo.getCurrencyIso()));
        payment.setPaymentDate(NLS.long2LocalDateTime(paymentInfo.getPaymentDate()));
        payment.setPaymentService(paymentsService.getRefsService().findPaymentService(paymentInfo.getPaymentServiceId()));
        payment.setPaymentStatus(paymentsService.getRefsService().findPaymentStatus(PS_ACTUAL.getCode()));
        payment.setPaymentSummMicros(paymentInfo.getPaymentSumm());
        payment.setPaymentSumm(new BigDecimal(paymentInfo.getPaymentSumm()).divide(new BigDecimal("1000000")));
        payment.setPmtNote(paymentInfo.getPmtNote());
        payment.setGoogleOrderId(paymentInfo.getGoogleOrderId());
        payment.setGooglePurchaseToken(paymentInfo.getGooglePurchaseToken());
        payment.setGoogleSku(paymentInfo.getGoogleSku());
        payment.setPlatform(paymentInfo.getPlatform());
        payment.setAppPackage(paymentInfo.getPackageName());

    }

    //==========================================================================
    private void assignApplePaymentInfo(ApplePayment payment, WaPaymentInfo paymentInfo) {

        payment.setApplication(paymentsService.getRefsService().findApplication(APP_WA_TRACKER.getCode()));
        payment.setActualDate(LocalDateTime.now());
        //payment.setCountry(paymentsService.getRefsService().findCountry(notNull(paymentInfo.getCountryCode()) ? paymentInfo.getCountryCode() : "BY"));
        payment.setCurrency(paymentsService.getRefsService().findCurrency(paymentInfo.getCurrencyIso()));
        payment.setPaymentDate(NLS.long2LocalDateTime(paymentInfo.getPaymentDate()));
        payment.setPaymentService(paymentsService.getRefsService().findPaymentService(paymentInfo.getPaymentServiceId()));
        payment.setPaymentStatus(paymentsService.getRefsService().findPaymentStatus(PS_ACTUAL.getCode()));
        payment.setPaymentSummMicros(paymentInfo.getPaymentSumm());
        payment.setPaymentSumm(new BigDecimal(paymentInfo.getPaymentSumm()).divide(new BigDecimal("1000000")).setScale(2, BigDecimal.ROUND_HALF_UP));
        payment.setPmtNote(paymentInfo.getPmtNote());
        payment.setAppleOriginalTransactionId(paymentInfo.getAppleOriginalTransactionId());
        payment.setAppleProductId(paymentInfo.getAppleProductId());
        payment.setAppleTransactionId(paymentInfo.getAppleTransactionId());
        payment.setPlatform(paymentInfo.getPlatform());
        payment.setAppPackage(paymentInfo.getPackageName());

    }

    //==========================================================================
    private void updateWaPaymentIfExists(WaPaymentInfo paymentInfo, CreatedWaPayment сreatedWaPayment, ResponseBody<CreatedWaPayment> responseBody, NewPaymentAction newPaymentAction) {

        // IOS
        ifTrue(paymentInfo.getPaymentServiceId().equals(PS_APPLE_PAY.getCode()), () -> {

            paymentDao.findApplePaymentByAppleTransactionId(paymentInfo.getAppleTransactionId())
                    .ifPresentOrElse(applePayment -> {

                        final String logMsg = String.format("ApplePayment is updated by appleTransactionId (paymentId=%d, appleTransactionId = '%s') ",
                                applePayment.getPaymentId(),
                                applePayment.getAppleTransactionId()
                        );

                        log.warn(logMsg.concat(", new paymentInfo = {}"), paymentInfo);

                        // update existing payment
                        paymentDao.saveApplePaymentHist(applePayment);

                        // assign paymentInfo
                        assignApplePaymentInfo(applePayment, paymentInfo);

                        // save payment
                        paymentDao.saveApplePayment(applePayment);

                        responseBody.setMessage(logMsg);
                        сreatedWaPayment.setCreatedPaymentId(applePayment.getPaymentId());
                        responseBody.complete();

                    }, newPaymentAction::processNewPayment);

        });

        // Google
        ifTrue(paymentInfo.getPaymentServiceId().equals(PS_GOOGLE_PAY.getCode()), () -> {

            paymentDao.findGooglePaymentByGoogleOrderId(paymentInfo.getGoogleOrderId())
                    .ifPresentOrElse(googlePayment -> {

                        final String logMsg = String.format("GooglePayment is updated by googleOrderId  (paymentId=%d, googleOrderId = '%s', actualDate=%s) ",
                                googlePayment.getPaymentId(),
                                googlePayment.getGoogleOrderId(),
                                googlePayment.getActualDate().toString()
                        );

                        log.warn(logMsg.concat(", new paymentInfo = {}"), paymentInfo);

                        // update existing payment
                        paymentDao.saveGooglePaymentHist(googlePayment);

                        // assign paymentInfo
                        assignGooglePaymentInfo(googlePayment, paymentInfo);

                        // save payment
                        paymentDao.saveGooglePayment(googlePayment);

                        responseBody.setMessage(logMsg);
                        сreatedWaPayment.setCreatedPaymentId(googlePayment.getPaymentId());
                        responseBody.complete();

                    }, newPaymentAction::processNewPayment);
        });
    }

    //==========================================================================
    @PostConstruct
    public void initializeService() {

        StmtProcessor.assertNotNull(String.class, uriBase, "parameter ${config.wa-back-end.server.uri}'");
        setWebClient(setupSSL(WebClient.builder())
                .baseUrl(uriBase)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build());
        log.debug("webClient: successfull created ({})", uriBase);

    }

    /////////////////////////////////////////////////////////////////////////////
    private WebClient.Builder setupSSL(WebClient.Builder builder) {

        if (uriBase.startsWith("https://")) {
            setupClientConnector(builder);
        }

        return builder;

    }

    //===========================================================================
    private void setupClientConnector(WebClient.Builder builder) {

        StmtProcessor.execute(() -> {

            final SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            log.debug("SslContext is created: {} ", sslContext.getClass().getCanonicalName());

            final HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

            log.debug("HttpClient is created: {} ", httpClient.getClass().getCanonicalName());

            final ReactorClientHttpConnector reactorClientHttpConnector = new ReactorClientHttpConnector(httpClient);

            log.debug("ReactorClientHttpConnector is created: {} ", reactorClientHttpConnector.getClass().getCanonicalName());

            builder.clientConnector(reactorClientHttpConnector);

        });
    }
}
