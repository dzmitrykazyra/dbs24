package org.dbs24.test.functional;

import lombok.extern.log4j.Log4j2;
import org.dbs24.WaMonitoring;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.config.WaServerConfig;
import org.dbs24.entity.User;
import org.dbs24.rest.api.*;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.test.AbstractMonitoringTest;
import org.dbs24.test.creator.RequestBodyObjectCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.dbs24.consts.WaConsts.Classes.*;
import static org.dbs24.consts.WaConsts.References.*;
import static org.dbs24.consts.WaConsts.RestQueryParams.QP_LOGIN_TOKEN;
import static org.dbs24.consts.WaConsts.Uri.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@AutoConfigureWebTestClient
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {WaMonitoring.class})
@Import({WaServerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NewUserTrialContractTests extends AbstractMonitoringTest {

    @Value("${config.wa.user.trial.length:1}")
    private Integer trialLength;

    @Order(100)
    @Test
    @DisplayName("CASE: create new ANDROID MP user; new user gets trial period ability; invalidate trial period")
    public void createNewAndroidUserTrialContract() {

        final Mono<UserAttrsInfo> monoMultiPlatformUser = RequestBodyObjectCreator.createRandomAndroidUserAttrsInfoMono();

        runTest(() -> {
            log.info("STARTING EXECUTING TEST...");

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

            log.info("GET ANDROID USER LOGIN TOKEN : {}", loginToken);

            final Mono<UserContractInfo> monoContract = RequestBodyObjectCreator.createRandomTrialUserContractInfoMonoByUserId(
                    this.getLastUserId(),
                    trialLength
            );

            Integer createdContractId = webTestClient
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

            log.info("GET ANDROID USER TRIAL CONTRACT ID : {}", createdContractId);

            final Mono<UserSubscriptionInfo> monoSubscription = RequestBodyObjectCreator.createRandomUserSubscriptionInfoMono();

            Integer createdSubscriptionId = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_OR_UPDATE_SUBSCRIPTION)
                            .queryParam(QP_LOGIN_TOKEN, loginToken)
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

            log.info("GET ANDROID USER NEW SUBSCRIPTION ID : {}", createdSubscriptionId);

            Assertions.assertNotNull(loginToken);
            Assertions.assertNotNull(createdContractId);
            Assertions.assertNotNull(createdSubscriptionId);

            log.info("FINISHED TEST FOR NEW ANDROID USER [CREATE USER, CREATE TRIAL CONTRACT, CREATE SUBSCRIPTION]");
        });
    }

    @Order(200)
    @Test
    @DisplayName("CASE: create new IOS MP user; new user gets trial period ability; invalidate trial period")
    public void createNewIosUserTrialContract() {

        final Mono<UserAttrsInfo> monoMultiPlatformUser = RequestBodyObjectCreator.createRandomAndroidUserAttrsInfoMono();

        runTest(() -> {
            log.info("STARTING EXECUTING TEST...");

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

            log.info("GET IOS USER LOGIN TOKEN : {}", loginToken);

            final Mono<UserContractInfo> monoContract = RequestBodyObjectCreator.createRandomTrialUserContractInfoMonoByUserId(
                    this.getLastUserId(),
                    trialLength
            );

            Integer createdContractId = webTestClient
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

            log.info("GET IOS USER TRIAL CONTRACT ID : {}", createdContractId);

            final Mono<UserSubscriptionInfo> monoSubscription = RequestBodyObjectCreator.createRandomUserSubscriptionInfoMono();

            Integer createdSubscriptionId = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_OR_UPDATE_SUBSCRIPTION)
                            .queryParam(QP_LOGIN_TOKEN, loginToken)
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

            log.info("GET IOS USER NEW SUBSCRIPTION ID : {}", createdSubscriptionId);

            Assertions.assertNotNull(loginToken);
            Assertions.assertNotNull(createdContractId);
            Assertions.assertNotNull(createdSubscriptionId);

            log.info("FINISHED TEST FOR NEW IOS USER [CREATE USER, CREATE TRIAL CONTRACT, CREATE SUBSCRIPTION]");
        });
    }
}
