/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import java.time.LocalDateTime;

import lombok.extern.log4j.Log4j2;
import org.dbs24.WaMonitoring;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.config.WaServerConfig;

import static org.dbs24.consts.WaConsts.Classes.USER_ATTRS_INFO_CLASS;

import org.dbs24.rest.api.*;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.dbs24.consts.WaConsts.Uri.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {WaMonitoring.class})
@Import({WaServerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TmpTest extends AbstractMonitoringTest {

    @Test
    public void generateTestUser() {

        this.runTest(() -> {

            log.info("testing {}", URI_GENERATE_TEST_USERS);

            final UsersAmount usersAmount
                    = webTestClient
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_GENERATE_TEST_USERS)
                            //.queryParam(QP_AMOUNT, 3)
                            .build())
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(UsersAmount.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: created users: {}", URI_GENERATE_TEST_USERS, usersAmount.getCreatedUsers());

        });
    }

    //@Order(510)
    //@Test
    public void updateMpAndroidUser() {

        final Mono<UserAttrsInfo> monoUser = Mono.just(StmtProcessor.create(USER_ATTRS_INFO_CLASS, user -> {

            user.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            user.setAppName(TestFuncs.generateTestString15());
            user.setAppVersion(TestFuncs.generateTestString15());
            //user.setLoginToken(lastLoginToken);
            user.setMakAddr(TestFuncs.generateTestString15());
            user.setAndroidAttrs(StmtProcessor.create(
                    AndroidAttrs.class, aa -> {
                        //aa.g
                        aa.setAndroidId(TestFuncs.generateTestString15());
                        aa.setBoard(TestFuncs.generateTestString15());
                        aa.setFingerprint(TestFuncs.generateTestString15());
                        aa.setGcmToken(TestFuncs.generateTestString15());
                        aa.setGsfId("37f597809ce56077");
                        aa.setManufacturer(TestFuncs.generateTestString15());
                        aa.setModel(TestFuncs.generateTestString15());
                        aa.setProduct(TestFuncs.generateTestString15());
                        aa.setSecureId(TestFuncs.generateTestString15());
                        aa.setSupportedAbis(TestFuncs.generateTestString15());
                        aa.setVersionRelease(TestFuncs.generateTestString15());
                        aa.setVersionSdkInt(10);
                    }));
        }));

        runTest(() -> {

            log.info("testing/android {}", URI_CREATE_OR_UPDATE_MP_USER);

            final CreatedUser createdUser = webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_OR_UPDATE_MP_USER)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoUser, USER_ATTRS_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedUser.class)
                    .returnResult()
                    .getResponseBody();

            log.info("testing/android {}", createdUser);

        });
    }

}
