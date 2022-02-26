package org.dbs24.test.functional;

import lombok.extern.log4j.Log4j2;
import org.dbs24.WaMonitoring;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.config.WaServerConfig;
import org.dbs24.entity.ContractStatus;
import org.dbs24.entity.User;
import org.dbs24.entity.UserContract;
import org.dbs24.entity.UserSubscription;
import org.dbs24.rest.api.*;
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
import java.util.Collection;
import java.util.stream.Collectors;

import static org.dbs24.consts.WaConsts.Classes.*;
import static org.dbs24.consts.WaConsts.Classes.CREATED_USER_CONTRACT_CLASS;
import static org.dbs24.consts.WaConsts.References.SS_CREATED;
import static org.dbs24.consts.WaConsts.RestQueryParams.QP_LOGIN_TOKEN;
import static org.dbs24.consts.WaConsts.Uri.*;
import static org.dbs24.consts.WaConsts.Uri.URI_CREATE_OR_UPDATE_SUBSCRIPTION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@AutoConfigureWebTestClient
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {WaMonitoring.class})
@Import({WaServerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NewContractPreviousSubscriptionsTests extends AbstractMonitoringTest {

    @Order(100)
    @Test
    @DisplayName("")
    public void createNewContractWithSubscriptionsFromPreviousContract() {

        runTest(() -> {
            log.info("STARTED EXECUTING TEST...");
            Collection<UserSubscription> previousUserSubscriptions;
            Collection<UserSubscription> newContractUserSubscriptions;

            Mono<UserAttrsInfo> monoMultiPlatformUser = RequestBodyObjectCreator.createRandomAndroidUserAttrsInfoMono();

            CreatedUser createdUser = webTestClient
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
                    .getResponseBody();

            log.info("CREATED NEW USER: {}", createdUser.toString());

            Mono<UserContractInfo> monoContractCT4 = RequestBodyObjectCreator.createCT4UserContractInfoMonoByUserId(this.getLastUserId());

            CreatedUserContract createdFirstUserContract = webTestClient
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
                    .getResponseBody();

            log.info("CREATED NEW CT4 CONTRACT: {}", createdFirstUserContract.toString());

            for (int i = 0; i < 4; i++) {
                final Mono<UserSubscriptionInfo> monoSubscription = RequestBodyObjectCreator.createRandomUserSubscriptionInfoMonoByUserId(this.getLastUserId());

                CreatedUserSubscription createdSubscription = webTestClient
                        .post()
                        .uri(uriBuilder
                                -> uriBuilder
                                .path(URI_CREATE_OR_UPDATE_SUBSCRIPTION)
                                .queryParam(QP_LOGIN_TOKEN, createdUser.getLoginToken())
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

                log.info("CREATED SUBSCRIPTION #{} FOR NEW USER; SUBSCRIPTION: {}", i, createdSubscription.toString());
            }

            User user = usersService.findUser(createdUser.getLoginToken());

            UserContract previousContract = userContractRepository.findByUser(user).stream().collect(Collectors.toList()).get(1);
            previousUserSubscriptions = userSubscriptionRepository.findByUserContract(previousContract);

            previousContract.setContractStatus(StmtProcessor.create(
                    ContractStatus.class, contractStatus -> contractStatus.setContractStatusId((byte) 1))
            );
            userContractsService.saveContract(previousContract);
            log.info("OLD CONTRACT WAS SUCCESSFULLY CLOSED");

            Mono<UserContractInfo> monoContractCT10 = RequestBodyObjectCreator.createCT10UserContractInfoMonoByUserId(this.getLastUserId());

            CreatedUserContract createdSecondUserContract = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_CONTRACT)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoContractCT10, USER_CONTRACT_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_USER_CONTRACT_CLASS)
                    .returnResult()
                    .getResponseBody();

            log.info("CREATED NEW USER CONTRACT: {}", createdSecondUserContract);

            UserContract newContract = userContractRepository.findByUser(user).stream().collect(Collectors.toList()).get(0);

            log.info("FOUND THIS CONTRACT IN REPO: {}", newContract);

            newContractUserSubscriptions = userSubscriptionRepository.findByUserContract(newContract);

            log.info("TEST SUCCESSFULLY FINISHED");
        });
    }

    @Order(200)
    //@Test
    @DisplayName("")
    public void createNewContractFromPaymentWithSubscriptionsFromPreviousContract() {

        runTest(() -> {
            log.info("STARTED EXECUTING TEST...");
            Collection<UserSubscription> previousUserSubscriptions;
            Collection<UserSubscription> newContractUserSubscriptions;

            Mono<UserAttrsInfo> monoMultiPlatformUser = RequestBodyObjectCreator.createRandomAndroidUserAttrsInfoMono();

            CreatedUser createdUser = webTestClient
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
                    .getResponseBody();

            log.info("CREATED NEW USER: {}", createdUser.toString());

            Mono<UserContractFromPaymentInfo> previousContractFromPaymentMono = RequestBodyObjectCreator
                    .createRandomUserContractFromPaymentInfoMonoByLoginToken(this.getLastUserLoginToken());

            CreatedUserContract createdFirstUserContract = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_GET_CREATE_CONTRACT_FROM_PAYMENT)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(previousContractFromPaymentMono, UserContractFromPaymentInfo.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_USER_CONTRACT_CLASS)
                    .returnResult()
                    .getResponseBody();

            log.info("CREATED NEW CT4 CONTRACT: {}", createdFirstUserContract.toString());

            for (int i = 0; i < 4; i++) {
                final Mono<UserSubscriptionInfo> monoSubscription = RequestBodyObjectCreator.createRandomUserSubscriptionInfoMonoByUserId(this.getLastUserId());

                CreatedUserSubscription createdSubscription = webTestClient
                        .post()
                        .uri(uriBuilder
                                -> uriBuilder
                                .path(URI_CREATE_OR_UPDATE_SUBSCRIPTION)
                                .queryParam(QP_LOGIN_TOKEN, createdUser.getLoginToken())
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

                log.info("CREATED SUBSCRIPTION #{} FOR NEW USER; SUBSCRIPTION: {}", i, createdSubscription.toString());
            }

            User user = usersService.findUser(createdUser.getLoginToken());

            UserContract previousContract = userContractRepository.findByUser(user).stream().collect(Collectors.toList()).get(1);
            previousUserSubscriptions = userSubscriptionRepository.findByUserContract(previousContract);

            previousContract.setContractStatus(StmtProcessor.create(
                    ContractStatus.class, contractStatus -> contractStatus.setContractStatusId((byte) 1))
            );
            userContractsService.saveContract(previousContract);
            log.info("OLD CONTRACT WAS SUCCESSFULLY CLOSED");

            Mono<UserContractFromPaymentInfo> newContractFromPaymentMono = RequestBodyObjectCreator
                    .createRandomUserContractFromPaymentInfoMonoByLoginToken(this.getLastUserLoginToken());

            CreatedUserContract createdSecondUserContract = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_GET_CREATE_CONTRACT_FROM_PAYMENT)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(newContractFromPaymentMono, UserContractFromPaymentInfo.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_USER_CONTRACT_CLASS)
                    .returnResult()
                    .getResponseBody();

            log.info("CREATED NEW USER CONTRACT: {}", createdSecondUserContract);

            UserContract newContract = userContractRepository.findByUser(user).stream().collect(Collectors.toList()).get(0);

            log.info("FOUND THIS CONTRACT IN REPO: {}", newContract);

            newContractUserSubscriptions = userSubscriptionRepository.findByUserContract(newContract);

            log.info("TEST SUCCESSFULLY FINISHED");});
    }
}