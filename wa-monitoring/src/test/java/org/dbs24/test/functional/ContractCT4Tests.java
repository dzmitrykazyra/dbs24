package org.dbs24.test.functional;

import lombok.extern.log4j.Log4j2;
import org.dbs24.WaMonitoring;
import org.dbs24.config.WaServerConfig;
import org.dbs24.rest.api.CreatedUserSubscription;
import org.dbs24.rest.api.UserAttrsInfo;
import org.dbs24.rest.api.UserContractInfo;
import org.dbs24.rest.api.UserSubscriptionInfo;
import org.dbs24.test.AbstractMonitoringTest;
import org.dbs24.test.creator.RequestBodyObjectCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import static org.dbs24.consts.WaConsts.Classes.*;
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
public class ContractCT4Tests extends AbstractMonitoringTest {

    private Integer lastSubscriptionId;

    @Order(100)
    @Test
    @DisplayName("CASE: create new user; create new CT4 contract; create 4 subscriptions;" +
            " tryna create 5th subscription and get error message as a result")
    public void tryCreateFiveOrMoreSubscriptions() {

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

            log.info("GET NEW USER LOGIN TOKEN {}", loginToken);

            Mono<UserContractInfo> monoContractCT4 = RequestBodyObjectCreator.createCT4UserContractInfoMonoByUserId(this.getLastUserId());

            Integer createdContractId = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_CONTRACT)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoContractCT4, USER_CONTRACT_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_USER_CONTRACT_CLASS)
                    .returnResult()
                    .getResponseBody()
                    .getContractId();

            log.info("GET USER CONTRACT ID {}", createdContractId);

            //loop for creating 4 valid subscriptions
            for (int i = 0; i < 4; i++){
                final Mono<UserSubscriptionInfo> monoSubscription = RequestBodyObjectCreator.createRandomUserSubscriptionInfoMono();

                lastSubscriptionId = webTestClient
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

                log.info("GET USER SUBSCRIPTION #{} ID {}", i, createdContractId);
            }

            log.info("TRYNA CREATE ONE MORE (5TH) SUBSCRIPTION USING CT4 ACCOUNT");

            final Mono<UserSubscriptionInfo> monoSubscription = RequestBodyObjectCreator.createRandomUserSubscriptionInfoMono();

            CreatedUserSubscription responseBody = webTestClient
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
                    .getResponseBody();

            Assertions.assertFalse(responseBody.getNote().isEmpty());

            log.info("SUCCESS TEST CASE");
        });
    }
}