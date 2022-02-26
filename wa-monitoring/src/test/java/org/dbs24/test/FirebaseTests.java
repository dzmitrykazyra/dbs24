/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import lombok.extern.log4j.Log4j2;
import org.dbs24.WaMonitoring;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.config.WaServerConfig;
import org.dbs24.rest.api.AllFirebaseApplications;
import org.dbs24.rest.api.CreatedFireBaseApplication;
import org.dbs24.rest.api.FireBaseApplicationInfo;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import static org.dbs24.consts.WaConsts.Uri.URI_ALL_FIREBASE_APPLICATION;
import static org.dbs24.consts.WaConsts.Uri.URI_CREATE_OR_UPDATE_FIREBASE_APPLICATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {WaMonitoring.class})
@Import({WaServerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
 class FirebaseTests extends AbstractMonitoringTest {

    private Integer lastApplicationId;

    @Order(100)
    @RepeatedTest(10)
     void createFirebaseApplication() {

        final Mono<FireBaseApplicationInfo> mono = Mono.just(StmtProcessor.create(FireBaseApplicationInfo.class, appInfo -> {

            appInfo.setActualDate(null);
            appInfo.setAdminSdk(TestFuncs.generateTestString15());
            appInfo.setDbUrl(TestFuncs.generateTestString15());
            appInfo.setIsActual(TestFuncs.generateBool());
            appInfo.setName(TestFuncs.generateTestString15());
            appInfo.setPackageName(TestFuncs.generateTestString15());

        }));

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_FIREBASE_APPLICATION);

            lastApplicationId = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_OR_UPDATE_FIREBASE_APPLICATION)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(mono, FireBaseApplicationInfo.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedFireBaseApplication.class)
                    .returnResult()
                    .getResponseBody()
                    .getFirebaseAppId();

            log.info("{}: created application", URI_CREATE_OR_UPDATE_FIREBASE_APPLICATION, lastApplicationId);

        });
    }

    @Order(200)
    @Test
     void updateFirebaseApplication() {

        final Mono<FireBaseApplicationInfo> mono = Mono.just(StmtProcessor.create(FireBaseApplicationInfo.class, appInfo -> {

            appInfo.setFirebaseAppId(lastApplicationId);
            appInfo.setActualDate(null);
            appInfo.setAdminSdk(TestFuncs.generateTestString15());
            appInfo.setDbUrl("dbUrl");
            appInfo.setIsActual(null);
            appInfo.setName(TestFuncs.generateTestString15());
            appInfo.setPackageName(TestFuncs.generateTestString15());

        }));

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_FIREBASE_APPLICATION);

            lastApplicationId = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_OR_UPDATE_FIREBASE_APPLICATION)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(mono, FireBaseApplicationInfo.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedFireBaseApplication.class)
                    .returnResult()
                    .getResponseBody()
                    .getFirebaseAppId();

            log.info("{}: created application", URI_CREATE_OR_UPDATE_FIREBASE_APPLICATION, lastApplicationId);

        });
    }

    //==========================================================================
    @Order(300)
    @Test
     void getAllFirebaseApplications() {

        runTest(() -> {

            log.info("testing {}", URI_ALL_FIREBASE_APPLICATION);

            final AllFirebaseApplications allFirebaseApplications
                    = webTestClient
                            .get()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_ALL_FIREBASE_APPLICATION)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(AllFirebaseApplications.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: {}", URI_ALL_FIREBASE_APPLICATION, allFirebaseApplications);

        });
    }
}
