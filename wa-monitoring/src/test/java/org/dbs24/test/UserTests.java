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
import org.dbs24.config.WaServerConfig;
import org.dbs24.rest.api.*;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.dbs24.consts.WaConsts.Classes.*;
import static org.dbs24.consts.WaConsts.RestQueryParams.QP_LOGIN_TOKEN;
import static org.dbs24.consts.WaConsts.Uri.URI_CHECK_TOKEN_VALIDITY;
import static org.dbs24.consts.WaConsts.Uri.URI_CREATE_OR_UPDATE_MP_USER;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {WaMonitoring.class})
@Import({WaServerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTests extends AbstractMonitoringTest {

    private String lastLoginToken;

    @Order(100)
    //@Test
    @DisplayName("test1")
    //@RepeatedTest(10)
    public void createUsers() {

        final Mono<UserInfo> monoUser = Mono.just(StmtProcessor.create(USER_INFO_CLASS, user -> {
            user.setCountryId("112");
            user.setEmail(TestFuncs.generateTestString15());
            user.setUserName(TestFuncs.generateTestString15());
            user.setUserPhoneNum(TestFuncs.generateTestString15());

            //user.set
        }));
    }


    @Order(500)
    @Test
    public void createMpAndroidUser() {

        final Mono<UserAttrsInfo> monoUser = Mono.just(StmtProcessor.create(USER_ATTRS_INFO_CLASS, user -> {

            user.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            user.setAppName(TestFuncs.generateTestString15());
            user.setAppVersion(TestFuncs.generateTestString15());
            //user.setLoginToken(TestFuncs.generateTestString15());
            user.setMakAddr(TestFuncs.generateTestString15());
            user.setAndroidAttrs(StmtProcessor.create(
                    AndroidAttrs.class, aa -> {
                        aa.setAndroidId(TestFuncs.generateTestString15());
                        aa.setBoard(TestFuncs.generateTestString15());
                        aa.setFingerprint(TestFuncs.generateTestString15());
                        aa.setGcmToken(TestFuncs.generateTestString15());
                        aa.setGsfId(TestFuncs.generateTestString15());
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

            lastLoginToken = webTestClient
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
                    .getResponseBody()
                    .getLoginToken();
        });
    }

    @Order(510)
    @Test
    public void updateMpAndroidUser() {

        final Mono<UserAttrsInfo> monoUser = Mono.just(StmtProcessor.create(USER_ATTRS_INFO_CLASS, user -> {

            user.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            user.setAppName(TestFuncs.generateTestString15());
            user.setAppVersion(TestFuncs.generateTestString15());
            user.setLoginToken(lastLoginToken);
            user.setMakAddr(TestFuncs.generateTestString15());
            user.setAndroidAttrs(StmtProcessor.create(
                    AndroidAttrs.class, aa -> {
                        //aa.g
                        aa.setAndroidId(TestFuncs.generateTestString15());
                        aa.setBoard(TestFuncs.generateTestString15());
                        aa.setFingerprint(TestFuncs.generateTestString15());
                        aa.setGcmToken(TestFuncs.generateTestString15());
                        aa.setGsfId(TestFuncs.generateTestString15());
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

            lastLoginToken = webTestClient
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
                    .getResponseBody()
                    .getLoginToken();
        });
    }

    @Order(600)
    @Test
    public void createMpIosUser() {

        final Mono<UserAttrsInfo> monoUser = Mono.just(StmtProcessor.create(USER_ATTRS_INFO_CLASS, user -> {

            user.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            user.setAppName(TestFuncs.generateTestString15());
            user.setAppVersion(TestFuncs.generateTestString15());
            user.setMakAddr(TestFuncs.generateTestString15());
            user.setIosAttrs(StmtProcessor.create(
                    IosAttrs.class, ia -> {
                        ia.setGcmToken(TestFuncs.generateTestString15());
                        ia.setIdentifierForVendor(TestFuncs.generateTestString15());
                        ia.setModel(TestFuncs.generateTestString15());
                        ia.setSystemVersion(TestFuncs.generateTestString15());
                        ia.setUstnameMachine(TestFuncs.generateTestString15());
                        ia.setUstnameRelease(TestFuncs.generateTestString15());
                        ia.setUstnameVersion(TestFuncs.generateTestString15());
                    }));
        }));

        runTest(() -> {

            log.info("testing/ios {}", URI_CREATE_OR_UPDATE_MP_USER);

            lastLoginToken = webTestClient
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
                    .getResponseBody()
                    .getLoginToken();
        });
    }

    @Order(610)
    @Test
    public void updateMpIosUser() {

        final Mono<UserAttrsInfo> monoUser = Mono.just(StmtProcessor.create(USER_ATTRS_INFO_CLASS, user -> {

            user.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            user.setAppName(TestFuncs.generateTestString15());
            user.setAppVersion(TestFuncs.generateTestString15());
            user.setLoginToken(lastLoginToken);
            user.setMakAddr(TestFuncs.generateTestString15());
            user.setIosAttrs(StmtProcessor.create(
                    IosAttrs.class, ia -> {
                        ia.setGcmToken(TestFuncs.generateTestString15());
                        ia.setIdentifierForVendor(TestFuncs.generateTestString15());
                        ia.setModel(TestFuncs.generateTestString15());
                        ia.setSystemVersion(TestFuncs.generateTestString15());
                        ia.setUstnameMachine(TestFuncs.generateTestString15());
                        ia.setUstnameRelease(TestFuncs.generateTestString15());
                        ia.setUstnameVersion(TestFuncs.generateTestString15());
                    }));
        }));

        runTest(() -> {

            log.info("testing/ios {}", URI_CREATE_OR_UPDATE_MP_USER);

            lastLoginToken = webTestClient
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
                    .getResponseBody()
                    .getLoginToken();
        });
    }

    @Order(700)
    @Test
    public void checkTokenValidity_Exists() {
        runTest(() -> {

            log.info("testing {}", URI_CHECK_TOKEN_VALIDITY);

            final LoginTokenInfo loginTokenInfo = webTestClient
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CHECK_TOKEN_VALIDITY)
                            .queryParam(QP_LOGIN_TOKEN, lastLoginToken)
                            .build())
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(LOGIN_TOKEN_INFO_CLASS)
                    .returnResult()
                    .getResponseBody();

            log.info("receive loginTokenInfo = {} ", loginTokenInfo);

        });
    }

    @Order(700)
    @Test
    public void checkTokenValidity_NotExists() {
        runTest(() -> {

            log.info("testing {}", URI_CHECK_TOKEN_VALIDITY);

            final LoginTokenInfo loginTokenInfo = webTestClient
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CHECK_TOKEN_VALIDITY)
                            .queryParam(QP_LOGIN_TOKEN, "unknownBlaBlaToken")
                            .build())
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(LOGIN_TOKEN_INFO_CLASS)
                    .returnResult()
                    .getResponseBody();

            log.info("receive loginTokenInfo = {} ", loginTokenInfo);

        });
    }
}
