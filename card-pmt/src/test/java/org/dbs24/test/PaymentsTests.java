/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import java.math.BigDecimal;
import org.dbs24.card.pmt.rest.payment.api.*;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.TestFuncs;
import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.dbs24.card.pmt.config.CardPaymentConfig;
import org.dbs24.card.pmt.CardPaymentApplication;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.ApplicationEnum.APPLICATIONS_LIST_IDS;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.Countries.COUNTRIES_IDS;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.Currencies.CURRENCIES_IDS;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.PaymentServiceEnum.PAYMENT_SERVICES_LIST_IDS;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.PaymentStatusEnum.PAYMENT_STATUSES_LIST_IDS;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.UriConsts.*;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.stmt.StmtProcessor;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import reactor.core.publisher.Mono;

@Data
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {CardPaymentApplication.class})
@Import({CardPaymentConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class PaymentsTests extends AbstractPaymentTest {

    private Integer createdPaymentId;

    @Order(100)
    @Test
    @RepeatedTest(5)
    public void createPayment() {

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_PAYMENT);

            final Mono<CreatePaymentRequest> mono = Mono.just(StmtProcessor.create(CreatePaymentRequest.class, paymentMessageBody -> {

                // Entity Body
                paymentMessageBody.setEntityInfo(StmtProcessor.create(PaymentInfo.class, paymentInfo -> {

                    paymentInfo.setPaymentId(null); // new payment
                    paymentInfo.setApplicationId(TestFuncs.<Integer>selectFromCollection(APPLICATIONS_LIST_IDS));
                    paymentInfo.setCountryCode(TestFuncs.<String>selectFromCollection(COUNTRIES_IDS));
                    paymentInfo.setCurrencyIso(TestFuncs.<String>selectFromCollection(CURRENCIES_IDS));
                    paymentInfo.setPaymentDate(NLS.localDateTime2long(LocalDateTime.now()));
                    paymentInfo.setPaymentServiceId(TestFuncs.<Integer>selectFromCollection(PAYMENT_SERVICES_LIST_IDS));
                    paymentInfo.setPaymentStatusId(TestFuncs.<Integer>selectFromCollection(PAYMENT_STATUSES_LIST_IDS));
                    paymentInfo.setPaymentSumm(TestFuncs.generateBigDecimal(new BigDecimal("1000000")));
                    paymentInfo.setPaymentSummEqu(TestFuncs.generateBigDecimal(new BigDecimal("1000000")));
                    paymentInfo.setPmtNote(null);
                    paymentInfo.setTag(null);

                }));

                // Action Info
                paymentMessageBody.setEntityAction(StmtProcessor.create(SimpleActionInfo.class,
                        sai -> {
                            //sai.setActionCode(MODIFY_ENTITY);
                        }
                ));
            }));

            final CreatedPaymentResponse result
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_PAYMENT)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(mono, CreatePaymentRequest.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedPaymentResponse.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_PAYMENT, result);

            createdPaymentId = result.getCreatedEntity().getCreatedPaymentId();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_PAYMENT, createdPaymentId);

        });
    }

    @Order(200)
    @Test
    public void updatePayment() {

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_PAYMENT);

            final Mono<CreatePaymentRequest> mono = Mono.just(StmtProcessor.create(CreatePaymentRequest.class, paymentMessageBody -> {

                // Entity Body
                paymentMessageBody.setEntityInfo(StmtProcessor.create(PaymentInfo.class, paymentInfo -> {

                    paymentInfo.setPaymentId(createdPaymentId); // new payment
                    paymentInfo.setApplicationId(TestFuncs.<Integer>selectFromCollection(APPLICATIONS_LIST_IDS));
                    paymentInfo.setCountryCode(TestFuncs.<String>selectFromCollection(COUNTRIES_IDS));
                    paymentInfo.setCurrencyIso(TestFuncs.<String>selectFromCollection(CURRENCIES_IDS));
                    paymentInfo.setPaymentDate(NLS.localDateTime2long(LocalDateTime.now()));
                    paymentInfo.setPaymentServiceId(TestFuncs.<Integer>selectFromCollection(PAYMENT_SERVICES_LIST_IDS));
                    paymentInfo.setPaymentStatusId(TestFuncs.<Integer>selectFromCollection(PAYMENT_STATUSES_LIST_IDS));
                    paymentInfo.setPaymentSumm(TestFuncs.generateBigDecimal(new BigDecimal("1000000")));
                    paymentInfo.setPaymentSummEqu(TestFuncs.generateBigDecimal(new BigDecimal("1000000")));
                    paymentInfo.setPmtNote(null);
                    paymentInfo.setTag(null);

                }));

                // Action Info
                paymentMessageBody.setEntityAction(StmtProcessor.create(SimpleActionInfo.class,
                        sai -> {
                            //sai.setActionClass(ActCreateOrUpdateLoanContract.class);
                            //sai.setActionCode(MODIFY_ENTITY);
                        }
                ));

            }));

            final CreatedPaymentResponse createdPaymentResult
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_PAYMENT)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(mono, CreatePaymentRequest.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedPaymentResponse.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_PAYMENT, createdPaymentResult);

        });

    }

//    @Order(300)
//    @Test
//    public void getPayment() {
//
//        runTest(() -> {
//
//            log.info("testing {}, get paymentId: {} ", URI_GET_PROXY, createdPaymentId);
//
//            final PaymentInfoResponse paymentInfoResponse
//                    = webTestClient
//                            .get()
//                            .uri(uriBuilder
//                                    -> uriBuilder
//                                    .path(URI_GET_PROXY)
//                                    .queryParam(QP_PROXY_ID, createdPaymentId)
//                                    .build())
//                            .accept(APPLICATION_JSON)
//                            .exchange()
//                            .expectStatus()
//                            .isOk()
//                            .expectBody(PaymentInfoResponse.class)
//                            .returnResult()
//                            .getResponseBody();
//
//            log.info("{}: payment details: {}", URI_GET_PROXY, paymentInfoResponse);
//
//        });
//    }
}
