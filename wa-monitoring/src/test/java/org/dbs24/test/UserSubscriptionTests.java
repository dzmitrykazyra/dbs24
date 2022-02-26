/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import lombok.extern.log4j.Log4j2;
import org.dbs24.WaMonitoring;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.component.UserContractsService;
import org.dbs24.component.UserSubscriptionsService;
import org.dbs24.config.WaServerConfig;
import org.dbs24.entity.UserSubscription;
import org.dbs24.rest.api.*;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.dbs24.consts.WaConsts.Classes.*;
import static org.dbs24.consts.WaConsts.References.*;
import static org.dbs24.consts.WaConsts.RestQueryParams.*;
import static org.dbs24.consts.WaConsts.Uri.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {WaMonitoring.class})
@Import({WaServerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserSubscriptionTests extends AbstractMonitoringTest {

    final UserContractsService userContractsService;
    final UserSubscriptionsService userSubscriptionsService;

    @Autowired
    public UserSubscriptionTests(UserContractsService userContractsService, UserSubscriptionsService userSubscriptionsService) {
        this.userContractsService = userContractsService;
        this.userSubscriptionsService = userSubscriptionsService;
    }

    private Integer lastSubscriptionId;
    private String newUserLoginToken = "DEV_D75623A469EE4AD19EA4D3DBDE";
    private String phonenum = "79268858222";
    private String loginToken = "F26974E0C1F0484DB41A61B0B85A0F";

    @Order(100)
    @DisplayName("createSubscriptions")
    @RepeatedTest(10)
    void createSubscriptions() {
        //======================================================================
        // always create new user 4 test
        final Mono<UserInfo> monoUser = Mono.just(StmtProcessor.create(USER_INFO_CLASS, user -> {
            user.setCountryId("112");
            user.setEmail(TestFuncs.generateTestString15());
            user.setUserName(TestFuncs.generateTestString15());
            user.setUserPhoneNum(TestFuncs.generateTestString15());
        }));

        //======================================================================
        // create new subscription - 1
        final Mono<UserSubscriptionInfo> monoSubscription1 = Mono.just(StmtProcessor.create(USER_SUBSCRIPTION_INFO_CLASS, subscription -> {
            subscription.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            subscription.setOnlineNotify(TestFuncs.generateBool());
            subscription.setPhoneNum(TestFuncs.generateTestString15());
            subscription.setSubscriptionName(TestFuncs.generateTestString15());
            subscription.setSubscriptionStatusId(SS_CREATED);
        }));

        runTest(() -> {

            //------------------------------------------------------------------
            log.info("testing {}", URI_CREATE_OR_UPDATE_SUBSCRIPTION);

            lastSubscriptionId = webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_OR_UPDATE_SUBSCRIPTION)
                            .queryParam(QP_LOGIN_TOKEN, newUserLoginToken)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoSubscription1, USER_SUBSCRIPTION_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_USER_SUBSCRIPTION_CLASS)
                    .returnResult()
                    .getResponseBody()
                    .getSubscriptionId();

            log.info("{}: create subscriptionId = {}", URI_CREATE_OR_UPDATE_SUBSCRIPTION, lastSubscriptionId);

        });

        //----------------------------------------------------------------------
        runTest(() -> {
            log.info("testing close subscription {}", URI_CREATE_OR_UPDATE_SUBSCRIPTION);

            monoSubscription1.block().setSubscriptionStatusId(SS_CLOSED);

            lastSubscriptionId = webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_OR_UPDATE_SUBSCRIPTION)
                            .queryParam(QP_LOGIN_TOKEN, newUserLoginToken)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoSubscription1, USER_SUBSCRIPTION_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_USER_SUBSCRIPTION_CLASS)
                    .returnResult()
                    .getResponseBody()
                    .getSubscriptionId();

            log.info("{}: update subscriptionId = {}", URI_CREATE_OR_UPDATE_SUBSCRIPTION, lastSubscriptionId);
            //------------------------------------------------------------------
        });

        //======================================================================
        // create new subscription - 2
        final Mono<UserSubscriptionInfo> monoSubscription2 = Mono.just(StmtProcessor.create(USER_SUBSCRIPTION_INFO_CLASS, subscription -> {
            subscription.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            //subscription.setAgentId(getLastAgentId());
            //subscription.setContractId(getLastConractId());
            subscription.setOnlineNotify(TestFuncs.generateBool());
            subscription.setPhoneNum(TestFuncs.generateTestString15());
            subscription.setSubscriptionName(TestFuncs.generateTestString15());
            subscription.setSubscriptionStatusId(SS_CREATED);
        }));

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_SUBSCRIPTION);

            lastSubscriptionId = webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_OR_UPDATE_SUBSCRIPTION)
                            .queryParam(QP_LOGIN_TOKEN, newUserLoginToken)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoSubscription2, USER_SUBSCRIPTION_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_USER_SUBSCRIPTION_CLASS)
                    .returnResult()
                    .getResponseBody()
                    .getSubscriptionId();

            log.info("{}: create subscriptionId = {}", URI_CREATE_OR_UPDATE_SUBSCRIPTION, lastSubscriptionId);
        });
    }

    //==========================================================================
    @Order(200)
    @Test
    @DisplayName("updateSubscriptions")
    void updateSubscriptions() {

        final Mono<UserSubscriptionInfo> monoSubscription = Mono.just(StmtProcessor.create(USER_SUBSCRIPTION_INFO_CLASS, subscription -> {
            subscription.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            subscription.setOnlineNotify(TestFuncs.generateBool());
            subscription.setPhoneNum(TestFuncs.generateTestString15());
            subscription.setSubscriptionName(TestFuncs.generateTestString15());
            subscription.setSubscriptionStatusId(SS_CLOSED);
        }));
        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_SUBSCRIPTION);

            lastSubscriptionId = webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_OR_UPDATE_SUBSCRIPTION)
                            .queryParam(QP_LOGIN_TOKEN, newUserLoginToken)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoSubscription, USER_SUBSCRIPTION_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_USER_SUBSCRIPTION_CLASS)
                    .returnResult()
                    .getResponseBody()
                    .getSubscriptionId();
        });
    }

    //==========================================================================
    @Order(220)
    @Test
    @DisplayName("cancelSubscriptions")
    void cancelSubscriptions() {

        final Mono<UserSubscriptionInfo> monoSubscription = Mono.just(StmtProcessor.create(USER_SUBSCRIPTION_INFO_CLASS, subscription -> {
            subscription.setSubscriptionId(44175);
            subscription.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            subscription.setOnlineNotify(TestFuncs.generateBool());
            subscription.setPhoneNum("375445593261");
            subscription.setSubscriptionName(TestFuncs.generateTestString15());
            subscription.setSubscriptionStatusId(SS_CANCELLED);
        }));
        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_SUBSCRIPTION);

            lastSubscriptionId = webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_OR_UPDATE_SUBSCRIPTION)
                            .queryParam(QP_LOGIN_TOKEN, "ST_CF5C07C2D8C84E16B0FEDB2F20C")
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoSubscription, USER_SUBSCRIPTION_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_USER_SUBSCRIPTION_CLASS)
                    .returnResult()
                    .getResponseBody()
                    .getSubscriptionId();
        });
    }

    //==========================================================================
    @Order(300)
    @Test
    @Transactional(readOnly = true)
    void getLastSubscription() {

        runTest(() -> {

            log.info("testing {}", URI_GET_SUBSCRIPTION);

            final UserSubscription userSubscription
                    = userSubscriptionsService.findUserSubscription(lastSubscriptionId);

            final UserSubscriptionInfo userSubscriptionInfo = webTestClient
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_GET_SUBSCRIPTION)
                            .queryParam(QP_LOGIN_TOKEN, newUserLoginToken)
                            .queryParam(QP_PHONE, userSubscription.getPhoneNum().replaceAll("[^\\d.]", ""))
                            .build())
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(USER_SUBSCRIPTION_INFO_CLASS)
                    .returnResult()
                    .getResponseBody();

            log.info("receive usserSubscriptionInfo = {} ", userSubscriptionInfo);

        });
    }

    //==========================================================================
    @Order(400)
    @Test
    @Transactional(readOnly = true)
    void getAllUserSubscription() {

        runTest(() -> {

            log.info("testing {}", URI_GET_ALL_SUBSCRIPTIONS);

            final UserSubscription userSubscription
                    = userSubscriptionsService.findUserSubscription(lastSubscriptionId);

            final String loginToken = usersService.getUserRepository().findById(userSubscription.getUser().getUserId()).get().getLoginToken();

            final UserSubscriptionInfoCollection userSubscriptionInfoCollection = webTestClient
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_GET_ALL_SUBSCRIPTIONS)
                            .queryParam(QP_LOGIN_TOKEN, loginToken)
                            .build())
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(USER_SUBSCRIPTION_INFO_COLLECTION_CLASS)
                    .returnResult()
                    .getResponseBody();

            log.info("receive {} user subscription(s) ", userSubscriptionInfoCollection.getUserSubscriptionInfoCollection().size());

        });
    }

    //==========================================================================
    @Order(500)
    @RepeatedTest(10)
    @Transactional(readOnly = true)
    void getModifiesUserSubscriptions() {

        runTest(() -> {

            log.info("testing {}", URI_GET_MODIFIED_SUBSCRIPTIONS);

            final UserSubscriptionInfoCollection userSubscriptionInfoCollection = webTestClient
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_GET_MODIFIED_SUBSCRIPTIONS)
                            .queryParam(QP_ACTUAL_DATE, NLS.localDateTime2long(LocalDateTime.now().minusDays(1)))
                            .build())
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(USER_SUBSCRIPTION_INFO_COLLECTION_CLASS)
                    .returnResult()
                    .getResponseBody();

            log.info("receive {} modified user subscription(s) ", userSubscriptionInfoCollection.getUserSubscriptionInfoCollection().size());

        });
    }

    //==========================================================================
    @Order(600)
    @Test
    void disableSingleSubscription() {

        runTest(() -> {

            log.info("testing {}", URI_UPDATE_SUBSCRIPTION_STATUS);

            webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_UPDATE_SUBSCRIPTION_STATUS)
                            .queryParam(QP_SUBSCRIPTION_ID, lastSubscriptionId)
                            .queryParam(QP_SUBSCRIPTION_STATUS, SS_CANCELLED)
                            .build())
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk();

            log.info("disable user susbcription {} ", lastSubscriptionId);

        });
    }

    //==========================================================================
    @Order(1000)
    @Test
    @DisplayName("test1")
    void closeDeprecatedContracts() {

        // create new user
        final Mono<UserInfo> monoUser = Mono.just(StmtProcessor.create(USER_INFO_CLASS, user -> {
            user.setCountryId("112");
            user.setEmail(TestFuncs.generateTestString15());
            user.setUserName(TestFuncs.generateTestString15());
            user.setUserPhoneNum(TestFuncs.generateTestString15());
            // create Old date 4 test !!!
            user.setActualDate(NLS.localDateTime2long(LocalDateTime.now().minusDays(5)));
        }));

        // create new subscription
        final Mono<UserSubscriptionInfo> monoSubscription = Mono.just(StmtProcessor.create(USER_SUBSCRIPTION_INFO_CLASS, subscription -> {
            subscription.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            //subscription.setAgentId(getLastAgentId());
            //subscription.setContractId(getLastConractId());
            subscription.setOnlineNotify(TestFuncs.generateBool());
            subscription.setPhoneNum(TestFuncs.generateTestString15());
            subscription.setSubscriptionName(TestFuncs.generateTestString15());
            subscription.setSubscriptionStatusId(SS_CREATED);
        }));

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_SUBSCRIPTION);

            lastSubscriptionId = webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_OR_UPDATE_SUBSCRIPTION)
                            .queryParam(QP_LOGIN_TOKEN, newUserLoginToken)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoSubscription, USER_SUBSCRIPTION_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_USER_SUBSCRIPTION_CLASS)
                    .returnResult()
                    .getResponseBody()
                    .getSubscriptionId();
        });

        // close deprecated contracts
        userContractsService.closeDeprecatedContracts();
    }

    //==========================================================================
    @Order(1100)
    @Test
    void getSubscriptionNotifyStatus() {

        runTest(() -> {

            log.info("testing {}, lastSubscriptionId = {} ", URI_GET_SUBSCRIPTIONS_NOTIFY_STATUS, getLastSubsriptionId());

            final UserSubscriptionNotifyStatusInfo userSubscriptionNotifyStatusInfo
                    = webTestClient
                            .get()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_GET_SUBSCRIPTIONS_NOTIFY_STATUS)
                                    .queryParam(QP_SUBSCRIPTION_ID, getLastSubsriptionId())
                                    .build())
                            .accept(APPLICATION_JSON)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(USER_SUBSCRIPTION_NOTIFY_STATUS_INFO_CLASS)
                            .returnResult()
                            .getResponseBody();

            log.info("getSubscriptionNotifyStatus: {} ", userSubscriptionNotifyStatusInfo);

        });
    }

    //==========================================================================
    @Order(1200)
    @Test
    void getAllUserSubscriptionNotifyStatus() {

        runTest(() -> {

            final Integer userId = 17051; //getLastUserId();

            log.info("testing {}, lastUserId = {} ", URI_GET_ALL_USER_SUBSCRIPTIONS_NOTIFY_STATUS, userId);

            final UserSubscriptionNotifyStatusExtInfoCollection userSubscriptionNotifyStatusExtInfoCollection
                    = webTestClient
                            .get()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_GET_ALL_USER_SUBSCRIPTIONS_NOTIFY_STATUS)
                                    .queryParam(QP_USER_ID, userId)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(USER_SUBSCRIPTION_NOTIFY_STATUS_EXT_INFO_COLLECTION_CLASS)
                            .returnResult()
                            .getResponseBody();

            log.info("getAllUserSubscriptionNotifyStatus.size: {} ", userSubscriptionNotifyStatusExtInfoCollection.getUserSubscriptionCollection().size());

        });
    }

    //==========================================================================
    @Order(1300)
    @Test
    void createTestSubscriptions() {

        runTest(() -> {

            final Mono<TestMassSubscriptionsInfo> monoSubscription = Mono.just(StmtProcessor.create(
                    TestMassSubscriptionsInfo.class,
                    subscription -> {
                        subscription.setSubscriptionsAmount(2);
                        subscription.setSubscriptionsMask("79");
                    }));

            log.info("testing {}", URI_CREATE_TEST_SUBSCRIPTIONS);

            final Integer createdSubscription = webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_TEST_SUBSCRIPTIONS)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoSubscription, TestMassSubscriptionsInfo.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreateTestSubscriptionResult.class)
                    .returnResult()
                    .getResponseBody()
                    .getAmount();

            log.info("{}: created subscriptions: {}", URI_CREATE_TEST_SUBSCRIPTIONS, createdSubscription);

        });
    }

    //==========================================================================
    @Order(1400)
    @Test
    void createAvatar() {

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_AVATAR);

            lastSubscriptionId = 22880;

            final Mono<AvatarInfo> mono = Mono.just(StmtProcessor.create(
                    AvatarInfo.class,
                    subscription -> {
                        subscription.setAvatarImg(TestFuncs.generate100Bytes());
                        subscription.setAvatarId(TestFuncs.generateUnsignedLong());
                        subscription.setSubscriptionId(lastSubscriptionId);
                    }));

            final CreatedAvatar createdAvatar = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_OR_UPDATE_AVATAR)
                            //.queryParam(QP_LOGIN_TOKEN, newUserLoginToken)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(mono, AvatarInfo.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedAvatar.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: created avatar: {}", URI_CREATE_OR_UPDATE_AVATAR, createdAvatar);

        });

    }

    //==========================================================================
    @Order(1500)
    //@Test
    void getAvatar() {

        runTest(() -> {

            log.info("testing {}, lastSubscriptionId = {} ", URI_GET_AVATAR, lastSubscriptionId);

            final AvatarInfo avatarInfo
                    = webTestClient
                            .get()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_GET_AVATAR)
                                    .queryParam(QP_SUBSCRIPTION_ID, lastSubscriptionId)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(AvatarInfo.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: {} ", URI_GET_AVATAR, avatarInfo);

        });
    }

    //==========================================================================
    @Order(1600)
    @Test
    void createCustomAvatar() {

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_CUSTOM_AVATAR);

            final Mono<AvatarInfoBySubscriptionPhone> mono = Mono.just(StmtProcessor.create(
                    AvatarInfoBySubscriptionPhone.class,
                    subscription -> {
                        subscription.setAvatarImg(TestFuncs.generate100Bytes());
                    }));

            final CreatedAvatar createdAvatar = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_OR_UPDATE_CUSTOM_AVATAR)
                            .queryParam(QP_LOGIN_TOKEN, loginToken)
                            .queryParam(QP_PHONE, phonenum.replaceAll("[^\\d.]", ""))
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(mono, AvatarInfoBySubscriptionPhone.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedAvatar.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: created avatar: {}", URI_CREATE_OR_UPDATE_CUSTOM_AVATAR, createdAvatar);

        });
    }

    //==========================================================================
    @Order(1610)
    @Test
    void getCustomAvatar() {

        runTest(() -> {

            log.info("testing {}, {}/{} ", URI_GET_CUSTOM_AVATAR, loginToken, phonenum);

            final AvatarInfoBySubscriptionPhone avatarInfo
                    = webTestClient
                            .get()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_GET_CUSTOM_AVATAR)
                                    .queryParam(QP_LOGIN_TOKEN, loginToken)
                                    .queryParam(QP_PHONE, phonenum.replaceAll("[^\\d.]", ""))
                                    .build())
                            .accept(APPLICATION_JSON)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(AvatarInfoBySubscriptionPhone.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: {} ", URI_GET_CUSTOM_AVATAR, avatarInfo);

        });
    }

    //==========================================================================
    @Order(1620)
    @Test
    void resetCustomAvatar() {

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_CUSTOM_AVATAR);

            final Mono<AvatarInfoBySubscriptionPhone> mono = Mono.just(StmtProcessor.create(
                    AvatarInfoBySubscriptionPhone.class,
                    subscription -> {
                        subscription.setAvatarImg(null);
                    }));

            final CreatedAvatar createdAvatar = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_OR_UPDATE_CUSTOM_AVATAR)
                            .queryParam(QP_LOGIN_TOKEN, loginToken)
                            .queryParam(QP_PHONE, phonenum.replaceAll("[^\\d.]", ""))
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(mono, AvatarInfoBySubscriptionPhone.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedAvatar.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: created avatar: {}", URI_CREATE_OR_UPDATE_CUSTOM_AVATAR, createdAvatar);

        });
    }
}
