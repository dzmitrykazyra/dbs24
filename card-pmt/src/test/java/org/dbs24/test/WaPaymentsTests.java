/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.card.pmt.CardPaymentApplication;
import org.dbs24.card.pmt.config.CardPaymentConfig;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.ApplicationEnum.APPLICATIONS_LIST_IDS;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.Countries.COUNTRIES_IDS;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.Currencies.CURRENCIES_IDS;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.PaymentServiceEnum.*;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.PaymentStatusEnum.PAYMENT_STATUSES_LIST_IDS;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.Platforms.*;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.UriConsts.URI_CREATE_OR_UPDATE_WA_PAYMENT;
import org.dbs24.card.pmt.rest.payment.wa.api.*;
import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

@Data
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {CardPaymentApplication.class})
@Import({CardPaymentConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class WaPaymentsTests extends AbstractPaymentTest {

    private Integer createdPaymentId;

    @Order(100)
    @Test
    @RepeatedTest(5)
    public void createAppleWaPayment() {

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_WA_PAYMENT);

            final Mono<CreateWaPaymentRequest> mono = Mono.just(StmtProcessor.create(CreateWaPaymentRequest.class, paymentMessageBody -> {

                // Entity Body
                paymentMessageBody.setEntityInfo(StmtProcessor.create(WaPaymentInfo.class, paymentInfo -> {

                    //paymentInfo.setPaymentId(null); // new payment
                    //paymentInfo.setApplicationId(TestFuncs.<Integer>selectFromCollection(APPLICATIONS_LIST_IDS));
                    paymentInfo.setCountryCode(TestFuncs.<String>selectFromCollection(COUNTRIES_IDS));
                    paymentInfo.setPackageName(TestFuncs.generateTestString15());
                    paymentInfo.setCurrencyIso(TestFuncs.<String>selectFromCollection(CURRENCIES_IDS));
                    paymentInfo.setPaymentDate(NLS.localDateTime2long(LocalDateTime.now()));
                    paymentInfo.setPaymentServiceId(PS_APPLE_PAY.getCode());
                    paymentInfo.setPaymentSumm(TestFuncs.generateUnsignedLong());
                    paymentInfo.setPlatform(PTF_IOS);
                    paymentInfo.setPmtNote(TestFuncs.generateTestString15());
                    //paymentInfo.setLoginToken("FAKED LOGIN TOKEN");
                    paymentInfo.setLoginToken("4NPOF6A99Z1OU0AECT3VRQGYJS7R4Q");
                    paymentInfo.setSubsriptionAmount(4);
                    paymentInfo.setEndDate(NLS.localDateTime2long(LocalDateTime.now().plusDays(100)));

                    paymentInfo.setAppleOriginalTransactionId(TestFuncs.generateTestString15());
                    paymentInfo.setAppleProductId(TestFuncs.generateTestString15());
                    paymentInfo.setAppleTransactionId(TestFuncs.generateTestString15());
                    paymentInfo.setAppleTransactionId("b060b1b-41ab-4");

                    log.info("create paytment: {}", paymentInfo);

                }));

                // Action Info
                paymentMessageBody.setEntityAction(StmtProcessor.create(SimpleActionInfo.class,
                        sai -> {
                            //sai.setActionCode(MODIFY_ENTITY);
                        }
                ));
            }));

            final CreatedWaPaymentResponse result
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_WA_PAYMENT)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(mono, CreateWaPaymentRequest.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedWaPaymentResponse.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_WA_PAYMENT, result);

            createdPaymentId = result.getCreatedEntity().getCreatedPaymentId();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_WA_PAYMENT, createdPaymentId);

        });
    }

    @Order(200)
    @Test
    @RepeatedTest(5)
    public void createGoogleWaPayment() {

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_WA_PAYMENT);

            final Mono<CreateWaPaymentRequest> mono = Mono.just(StmtProcessor.create(CreateWaPaymentRequest.class, paymentMessageBody -> {

                // Entity Body
                paymentMessageBody.setEntityInfo(StmtProcessor.create(WaPaymentInfo.class, paymentInfo -> {

                    //paymentInfo.setPaymentId(null); // new payment
                    //paymentInfo.setApplicationId(TestFuncs.<Integer>selectFromCollection(APPLICATIONS_LIST_IDS));
                    paymentInfo.setCountryCode(TestFuncs.<String>selectFromCollection(COUNTRIES_IDS));
                    paymentInfo.setPackageName(TestFuncs.generateTestString15());
                    paymentInfo.setCurrencyIso(TestFuncs.<String>selectFromCollection(CURRENCIES_IDS));
                    paymentInfo.setPaymentDate(NLS.localDateTime2long(LocalDateTime.now()));
                    paymentInfo.setPaymentServiceId(PS_GOOGLE_PAY.getCode());
                    paymentInfo.setPaymentSumm(TestFuncs.generateUnsignedLong());
                    paymentInfo.setPlatform(PTF_ANDROID);
                    paymentInfo.setPmtNote(TestFuncs.generateTestString15());
                    //paymentInfo.setLoginToken("FAKED LOGIN TOKEN");
                    paymentInfo.setLoginToken("4NPOF6A99Z1OU0AECT3VRQGYJS7R4Q");
                    paymentInfo.setSubsriptionAmount(10);
                    paymentInfo.setEndDate(NLS.localDateTime2long(LocalDateTime.now().plusDays(100)));

                    paymentInfo.setGoogleOrderId(TestFuncs.generateTestString15());
                    paymentInfo.setGooglePurchaseToken(TestFuncs.generateTestString15());
                    paymentInfo.setGoogleSku(TestFuncs.generateTestString15());

                    log.info("create paytment: {}", paymentInfo);

                }));

                // Action Info
                paymentMessageBody.setEntityAction(StmtProcessor.create(SimpleActionInfo.class,
                        sai -> {
                            //sai.setActionCode(MODIFY_ENTITY);
                        }
                ));
            }));

            final CreatedWaPaymentResponse result
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_WA_PAYMENT)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(mono, CreateWaPaymentRequest.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedWaPaymentResponse.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_WA_PAYMENT, result);

            createdPaymentId = result.getCreatedEntity().getCreatedPaymentId();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_WA_PAYMENT, createdPaymentId);

        });
    }
}
