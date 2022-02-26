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
import org.dbs24.auth.server.api.WaUserSessionInfo;
import org.dbs24.config.WaServerConfig;
import static org.dbs24.consts.WaConsts.Uri.*;
import static org.dbs24.consts.WaConsts.Classes.*;
import static org.dbs24.consts.WaConsts.References.*;
import static org.dbs24.consts.WaConsts.RestQueryParams.QP_USER_ID;
import org.dbs24.rest.api.UpdateDeviceResult;
import org.dbs24.rest.api.UserDeviceInfo;
import org.dbs24.rest.api.UserDevicesInfoCollection;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {WaMonitoring.class})
@Import({WaServerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDeviceTests extends AbstractMonitoringTest {

    private Integer lastDeviceId;

    @Order(100)
    @Test
    @DisplayName("test1")
    @RepeatedTest(10)
    public void createUserDevices() {

        final Mono<UserDeviceInfo> monoUserDevice = Mono.just(StmtProcessor.create(USER_DEVICE_INFO_CLASS, userDevice -> {
            userDevice.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            userDevice.setAppName(TestFuncs.generateTestString15());
            userDevice.setAppVersion(TestFuncs.generateTestString15());
            userDevice.setCpuId(TestFuncs.generateTestString15());

            userDevice.setDeviceTypeId(DT_ANDROID);
            userDevice.setDeviceFingerprint(TestFuncs.generateTestString15());
            userDevice.setGsfId(TestFuncs.generateTestString15());
            userDevice.setIosKey(TestFuncs.generateTestString15());
            userDevice.setIpAddress(TestFuncs.generateTestString15());
            userDevice.setSecureId(TestFuncs.generateTestString15());
            userDevice.setUserId(getLastUserId());

            //user.set
        }));
        runTest(() -> {

            log.info("testing {}", URI_CREATE_DEVICE);

            lastDeviceId = webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_DEVICE)
                            .queryParam("datex", "dd")
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoUserDevice, USER_DEVICE_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_USER_DEVICE_CLASS)
                    .returnResult()
                    .getResponseBody()
                    .getDeviceId();

            log.info("{}: create device {}", URI_CREATE_DEVICE, lastDeviceId);

        });
    }

    @Order(200)
    @Test
    @DisplayName("test2")
    public void updateUserDevices() {

        log.info("testing {}, update deviceId = {}", URI_CREATE_DEVICE, lastDeviceId);

        final Mono<UserDeviceInfo> monoUserDevice = Mono.just(StmtProcessor.create(USER_DEVICE_INFO_CLASS, userDevice -> {
            userDevice.setDeviceId(lastDeviceId);
            userDevice.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            userDevice.setAppName(TestFuncs.generateTestString15());
            userDevice.setAppVersion(TestFuncs.generateTestString15());
            userDevice.setCpuId(TestFuncs.generateTestString15());

            userDevice.setDeviceTypeId(DT_IOS);
            userDevice.setGcmToken(TestFuncs.generateTestString15());
            userDevice.setDeviceFingerprint(TestFuncs.generateTestString15());
            userDevice.setGsfId(TestFuncs.generateTestString15());
            userDevice.setIosKey(TestFuncs.generateTestString15());
            userDevice.setIpAddress(TestFuncs.generateTestString15());
            userDevice.setSecureId(TestFuncs.generateTestString15());
            userDevice.setUserId(getLastUserId());

        }));

        runTest(() -> {

            log.info("testing {}", URI_CREATE_DEVICE);

            webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_DEVICE)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoUserDevice, USER_DEVICE_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_USER_DEVICE_CLASS)
                    .returnResult()
                    .getResponseBody()
                    .getDeviceId();

            log.info("{}: update device {}", URI_CREATE_DEVICE, lastDeviceId);
        });
    }

    @Order(300)
    @Test
    @DisplayName("test2")
    //@Transactional(readOnly = true)
    public void getAllUserDevices() {

        runTest(() -> {

            final Integer userId = getLastUserId();

            log.info("testing {}, user_id = {}", URI_GET_ALL_USER_DEVICES, userId);

            final UserDevicesInfoCollection userDevicesInfoCollection
                    = webTestClient
                            .get()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_GET_ALL_USER_DEVICES)
                                    .queryParam(QP_USER_ID, userId)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(UserDevicesInfoCollection.class)
                            .returnResult()
                            .getResponseBody();

            log.info("receive {} user device(s) ", userDevicesInfoCollection.getUserDevices().size());

        });
    }

    @Order(400)
    @Test
    public void updateUserDeviceAttrsWhileLogin() {
       
        runTest(() -> {

            final Mono<WaUserSessionInfo> monoWaUserSessionInfo = Mono.just(StmtProcessor.create(WaUserSessionInfo.class, userDevice -> {
                userDevice.setDeviceId(lastDeviceId);
                userDevice.setAppName(TestFuncs.generateTestString15());
                userDevice.setAppVersion(TestFuncs.generateTestString15());
                userDevice.setFcmToken(TestFuncs.generateTestString15());
            }));

            log.info("testing {}, lastDeviceId = {}", URI_UPDATE_DEVICE_ATTRS, lastDeviceId);

            final UpdateDeviceResult updateDeviceResult
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_UPDATE_DEVICE_ATTRS)
                                    .build())
                            .contentType(APPLICATION_JSON)
                            .accept(APPLICATION_JSON)
                            .body(monoWaUserSessionInfo, WaUserSessionInfo.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(UpdateDeviceResult.class)
                            .returnResult()
                            .getResponseBody();

            log.info("update device attrs: answerCode = {}, '{}'",
                    updateDeviceResult.getAnswerCode(), updateDeviceResult.getNote());

        });
    }
}
