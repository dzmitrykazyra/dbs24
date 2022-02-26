package org.dbs24.test.functional;

import lombok.extern.log4j.Log4j2;
import org.dbs24.WaMonitoring;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.config.WaServerConfig;
import org.dbs24.rest.api.UserAttrsInfo;
import org.dbs24.rest.api.UserSubscriptionInfo;
import org.dbs24.rest.api.UserSubscriptionNotifyStatusExtInfoCollection;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.test.AbstractMonitoringTest;
import org.dbs24.test.creator.RequestBodyObjectCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.dbs24.consts.WaConsts.Classes.*;
import static org.dbs24.consts.WaConsts.Classes.CREATED_USER_SUBSCRIPTION_CLASS;
import static org.dbs24.consts.WaConsts.References.*;
import static org.dbs24.consts.WaConsts.RestQueryParams.*;
import static org.dbs24.consts.WaConsts.Uri.*;
import static org.dbs24.consts.WaConsts.Uri.URI_UPDATE_SUBSCRIPTION_STATUS;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@AutoConfigureWebTestClient
    @Log4j2
    @ExtendWith(SpringExtension.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {WaMonitoring.class})
    @Import({WaServerConfig.class})
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class RecreateSubscriptionTests extends AbstractMonitoringTest {

        @Order(100)
        @Test
        @DisplayName("")
        public void existing() {
            runTest(() -> {

                UserSubscriptionInfo newSubscription = StmtProcessor.create(USER_SUBSCRIPTION_INFO_CLASS, subscription -> {
                    subscription.setUserId(11103);
                    subscription.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                    subscription.setOnlineNotify(true);
                    subscription.setPhoneNum("+375297555556");
                    subscription.setSubscriptionName("J78 subscription4updated");
                    subscription.setSubscriptionStatusId(SS_CONFIRMED);
                });

                Integer createdSubscriptionId = webTestClient
                        .post()
                        .uri(uriBuilder
                                -> uriBuilder
                                .path(URI_CREATE_OR_UPDATE_SUBSCRIPTION)
                                .queryParam(QP_LOGIN_TOKEN, getLastUserLoginToken())
                                .build())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .body(Mono.just(newSubscription), USER_SUBSCRIPTION_INFO_CLASS)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(CREATED_USER_SUBSCRIPTION_CLASS)
                        .returnResult()
                        .getResponseBody()
                        .getSubscriptionId();
                log.info("GET ANDROID USER CREATED SUBSCRIPTION ID: {}", createdSubscriptionId);
            });
        }

        @Order(100)
        //@Test
        @DisplayName("")
        public void CreateUndoCreateSubscriptionTest() {

            runTest(() -> {
                log.info("STARTING EXECUTING TEST...");

                final Mono<UserAttrsInfo> monoMultiPlatformUser = RequestBodyObjectCreator.createRandomAndroidUserAttrsInfoMono();
                String loginToken = webTestClient
                        .post()
                        .uri(uriBuilder
                                -> uriBuilder
                                .path(URI_CREATE_OR_UPDATE_MP_USER)
                                .build())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .body(monoMultiPlatformUser, USER_ATTRS_INFO_CLASS)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(CREATED_USER_CLASS)
                        .returnResult()
                        .getResponseBody()
                        .getLoginToken();
                log.info("NEW ANDROID USER LOGIN TOKEN: {}", loginToken);


                UserSubscriptionInfo oldSubscription = StmtProcessor.create(USER_SUBSCRIPTION_INFO_CLASS, subscription -> {
                    subscription.setUserId(getLastUserId());
                    subscription.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                    subscription.setOnlineNotify(true);
                    subscription.setPhoneNum("+375297555556");
                    subscription.setSubscriptionName("J78 subscription4");
                    subscription.setSubscriptionStatusId(SS_CONFIRMED);
                });

                Integer createdSubscriptionId = webTestClient
                        .post()
                        .uri(uriBuilder
                                -> uriBuilder
                                .path(URI_CREATE_OR_UPDATE_SUBSCRIPTION)
                                .queryParam(QP_LOGIN_TOKEN, loginToken)
                                .build())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .body(Mono.just(oldSubscription), USER_SUBSCRIPTION_INFO_CLASS)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(CREATED_USER_SUBSCRIPTION_CLASS)
                        .returnResult()
                        .getResponseBody()
                        .getSubscriptionId();
                log.info("GET ANDROID USER CREATED SUBSCRIPTION ID: {}", createdSubscriptionId);


                webTestClient
                        .post()
                        .uri(uriBuilder
                                -> uriBuilder
                                .path(URI_UPDATE_SUBSCRIPTION_STATUS)
                                .queryParam(QP_SUBSCRIPTION_ID, createdSubscriptionId)
                                .queryParam(QP_SUBSCRIPTION_STATUS, SS_CANCELLED)
                                .build())
                        .accept(APPLICATION_JSON)
                        .exchange()
                        .expectStatus()
                        .isOk();
                log.info("UNDO USER SUBSCRIPTION WITH ID: {} ", createdSubscriptionId);


                final Mono<UserSubscriptionInfo> recreatedSubscriptionMono = Mono.just(
                        StmtProcessor.create(UserSubscriptionInfo.class,
                                (subscription) -> {
                            subscription.setUserId(oldSubscription.getUserId());
                            subscription.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                            subscription.setPhoneNum(oldSubscription.getPhoneNum());
                            subscription.setSubscriptionName(oldSubscription.getSubscriptionName());
                            subscription.setSubscriptionStatusId(SS_CONFIRMED);
                        }));
                Integer recreatedSubscriptionId = webTestClient
                        .post()
                        .uri(uriBuilder
                                -> uriBuilder
                                .path(URI_CREATE_OR_UPDATE_SUBSCRIPTION)
                                .queryParam(QP_LOGIN_TOKEN, loginToken)
                                .build())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .body(recreatedSubscriptionMono, USER_SUBSCRIPTION_INFO_CLASS)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(CREATED_USER_SUBSCRIPTION_CLASS)
                        .returnResult()
                        .getResponseBody()
                        .getSubscriptionId();
                log.info("GET ANDROID USER RECREATED SUBSCRIPTION ID: {}", createdSubscriptionId);


                final UserSubscriptionNotifyStatusExtInfoCollection allUserSubscriptions
                        = webTestClient
                        .get()
                        .uri(uriBuilder
                                -> uriBuilder
                                .path(URI_GET_ALL_USER_SUBSCRIPTIONS_NOTIFY_STATUS)
                                .queryParam(QP_USER_ID, oldSubscription.getUserId())
                                .build())
                        .accept(APPLICATION_JSON)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(USER_SUBSCRIPTION_NOTIFY_STATUS_EXT_INFO_COLLECTION_CLASS)
                        .returnResult()
                        .getResponseBody();
                log.info("OUTPUT ALL SUBSCRIPTION LIST");
                allUserSubscriptions.getUserSubscriptionCollection().forEach(log::info);
                log.info("TEST WAS SUCCESSFULLY FINISHED");
            });

        }
    }
