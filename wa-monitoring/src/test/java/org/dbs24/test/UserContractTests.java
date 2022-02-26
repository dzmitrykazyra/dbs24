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
import org.dbs24.config.WaServerConfig;
import org.dbs24.rest.api.CreatedUserContract;
import org.dbs24.rest.api.UserContractFromPaymentInfo;
import org.dbs24.rest.api.UserContractInfo;
import org.dbs24.rest.api.UserInfo;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.dbs24.consts.WaConsts.Classes.*;
import static org.dbs24.consts.WaConsts.References.*;
import static org.dbs24.consts.WaConsts.Uri.URI_CREATE_CONTRACT;
import static org.dbs24.consts.WaConsts.Uri.URI_GET_CREATE_CONTRACT_FROM_PAYMENT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {WaMonitoring.class})
@Import({WaServerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class UserContractTests extends AbstractMonitoringTest {

    final UserContractsService userContractsService;

    @Autowired
    public UserContractTests(UserContractsService userContractsService) {
        this.userContractsService = userContractsService;
    }

    @Value("${config.wa.user.trial.length:1}")
    private Integer trialLength;
    //==========================================================================
    private Integer lastContractId;

    @Order(100)
    @Test
    @DisplayName("createTrialContracts")
    //@RepeatedTest(10)
    void createTrialContracts() {

        // new trial contracts
        final Mono<UserContractInfo> monoContract = Mono.just(StmtProcessor.create(USER_CONTRACT_INFO_CLASS, contract -> {
            //user.set
            contract.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            contract.setBeginDate(NLS.localDateTime2long(LocalDateTime.now().minusDays(5)));
            //contract.setCancelDate(LocalDateTime.MIN);
            contract.setEndDate(NLS.localDateTime2long(NLS.long2LocalDateTime(contract.getBeginDate()).plusDays(trialLength)));
            contract.setContractStatusId(CS_ACTUAL);
            contract.setContractTypeId(CT_TRIAL);
            contract.setUserId(this.getLastUserId());
            contract.setSubscriptionsAmount(TestFuncs.generateTestInteger(2, 4, 6, 8, 10));
        }));

        runTest(() -> {

            log.info("testing {}", URI_CREATE_CONTRACT);

            lastContractId = webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_CONTRACT)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoContract, USER_CONTRACT_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_USER_CONTRACT_CLASS)
                    .returnResult()
                    .getResponseBody()
                    .getContractId();
        });
    }

    @Order(110)
    @Test
    @DisplayName("test1")
    //@RepeatedTest(10)
    void createCT4Contracts() {

        final Mono<UserContractInfo> monoContract = Mono.just(StmtProcessor.create(USER_CONTRACT_INFO_CLASS, contract -> {
            //user.set
            contract.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            contract.setBeginDate(NLS.localDateTime2long(LocalDateTime.now()));
            //contract.setCancelDate(LocalDateTime.MIN);
            contract.setEndDate(NLS.localDateTime2long(NLS.long2LocalDateTime(contract.getBeginDate()).plusDays(1)));
            contract.setContractStatusId(CS_ACTUAL);
            contract.setContractTypeId(CT_BASIC);
            contract.setUserId(this.getLastUserId());
            contract.setSubscriptionsAmount(TestFuncs.generateTestInteger(2, 4, 6, 8, 10));
        }));
        runTest(() -> {

            log.info("testing {}", URI_CREATE_CONTRACT);

            lastContractId = webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_CONTRACT)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoContract, USER_CONTRACT_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_USER_CONTRACT_CLASS)
                    .returnResult()
                    .getResponseBody()
                    .getContractId();
        });
    }

    @Order(200)
    @Test
    @DisplayName("test1")
    void updateContracts() {

        final Mono<UserContractInfo> monoContract = Mono.just(StmtProcessor.create(USER_CONTRACT_INFO_CLASS, contract -> {
            //user.set
            contract.setContractId(lastContractId);
            contract.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            contract.setBeginDate(NLS.localDateTime2long(LocalDateTime.now()));
            //contract.setCancelDate(LocalDateTime.MIN);
            //contract.setCloseDate(LocalDateTime.MAX);
            contract.setContractStatusId(CS_ACTUAL);
            contract.setContractTypeId(CT_STANDART);
            contract.setUserId(this.getLastUserId());
            contract.setSubscriptionsAmount(TestFuncs.generateTestInteger(2, 4, 6, 8, 10));
        }));
        runTest(() -> {

            log.info("testing updating contract {}", URI_CREATE_CONTRACT);

            lastContractId = webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_CONTRACT)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoContract, USER_CONTRACT_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_USER_CONTRACT_CLASS)
                    .returnResult()
                    .getResponseBody()
                    .getContractId();
        });
    }
    //==========================================================================
    // create test loginToken 4 contract

    private String lastLoginToken;

    @Order(300)
    @Test
    void createUsers() {

        final Mono<UserInfo> monoUser = Mono.just(StmtProcessor.create(USER_INFO_CLASS, user -> {
            user.setCountryId("112");
            user.setEmail(TestFuncs.generateTestString15());
            user.setUserName(TestFuncs.generateTestString15());
            user.setUserPhoneNum(TestFuncs.generateTestString15());

            //user.set
        }));
    }

    @Order(310)
    @Test
    @RepeatedTest(5)
    void createErrorContractFromPayment() {

        // new trial contracts
        final Mono<UserContractFromPaymentInfo> monoContract = Mono.just(StmtProcessor.create(UserContractFromPaymentInfo.class, contract -> {
            //user.set
            contract.setBeginDate(NLS.localDateTime2long(LocalDateTime.now().minusDays(5)));
            contract.setEndDate(NLS.localDateTime2long(NLS.long2LocalDateTime(contract.getBeginDate()).plusDays(trialLength)));
            contract.setLoginToken(lastLoginToken);
            contract.setSubscriptionsAmount(TestFuncs.generateTestInteger(2, 4, 6, 8, 10));
        }));

        runTest(() -> {

            log.info("testing {}", URI_GET_CREATE_CONTRACT_FROM_PAYMENT);

            final CreatedUserContract createdUserContract = webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_GET_CREATE_CONTRACT_FROM_PAYMENT)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoContract, USER_CONTRACT_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedUserContract.class)
                    .returnResult()
                    .getResponseBody();

            log.info("createContractsFromPayment: {}, answerCode = {}, notes = {}", createdUserContract, createdUserContract.getAnswerCode(), createdUserContract.getNote());

        });
    }

    @Order(320)
    @Test
    @RepeatedTest(5)
    void createLegalContractFromPayment() {

        // new trial contracts
        final Mono<UserContractFromPaymentInfo> monoContract = Mono.just(StmtProcessor.create(UserContractFromPaymentInfo.class, contract -> {
            //user.set
            contract.setBeginDate(NLS.localDateTime2long(LocalDateTime.now().minusDays(5)));
            contract.setEndDate(NLS.localDateTime2long(NLS.long2LocalDateTime(contract.getBeginDate()).plusDays(trialLength)));
            contract.setLoginToken(lastLoginToken);
            contract.setSubscriptionsAmount(TestFuncs.selectFromCollection(ALL_CONTRACT_TYPES_IDS));
        }));

        runTest(() -> {

            log.info("testing {}", URI_GET_CREATE_CONTRACT_FROM_PAYMENT);

            final CreatedUserContract createdUserContract = webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_GET_CREATE_CONTRACT_FROM_PAYMENT)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoContract, USER_CONTRACT_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedUserContract.class)
                    .returnResult()
                    .getResponseBody();

            log.info("createContractsFromPayment: {}, answerCode = {}, notes = {}", createdUserContract, createdUserContract.getAnswerCode(), createdUserContract.getNote());

        });
    }

    @Order(400)
    @Test
    @DisplayName("test1")
    void closeDeprecatedContracts() {
        userContractsService.closeDeprecatedContracts();
    }
}
