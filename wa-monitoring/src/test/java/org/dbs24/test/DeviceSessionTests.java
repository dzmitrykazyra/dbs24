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
import static org.dbs24.consts.WaConsts.*;
import static org.dbs24.consts.WaConsts.Uri.*;
import static org.dbs24.consts.WaConsts.Classes.*;
import org.dbs24.rest.api.DeviceSessionInfo;
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
public class DeviceSessionTests extends AbstractMonitoringTest {

    private Long lastSessionId;

    @Order(100)
    @Test
    @DisplayName("test1")
    @RepeatedTest(10)
    public void createDeviceSessions() {

        final Mono<DeviceSessionInfo> monoDeviceSession = Mono.just(StmtProcessor.create(DEVICE_SESSION_INFO_CLASS, deviceSessionInfo -> {
            deviceSessionInfo.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            deviceSessionInfo.setDeviceId(this.getLastDeviceId());
            deviceSessionInfo.setIpAddress(TestFuncs.generateTestString15());
            deviceSessionInfo.setNote(TestFuncs.generateTestString15());

        }));
        runTest(() -> {

            log.info("testing {}", URI_CREATE_DEVICE_SESSION);

            lastSessionId = webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_DEVICE_SESSION)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoDeviceSession, DEVICE_SESSION_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_DEVICE_SESSION_CLASS)
                    .returnResult()
                    .getResponseBody()
                    .getSessionId();
        });
    }

    @Order(200)
    @Test
    @DisplayName("test2")
    public void updateDeviceSessions() {

        log.info("testing {}, update sessionId = {}", URI_CREATE_DEVICE_SESSION, lastSessionId);

        final Mono<DeviceSessionInfo> monoDeviceSession = Mono.just(StmtProcessor.create(DEVICE_SESSION_INFO_CLASS, deviceSessionInfo -> {
            deviceSessionInfo.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            deviceSessionInfo.setDeviceId(this.getLastDeviceId());
            deviceSessionInfo.setIpAddress(TestFuncs.generateTestString15());
            deviceSessionInfo.setNote(TestFuncs.generateTestString15());
            deviceSessionInfo.setSessionId(lastSessionId);
        }));        
        runTest(() -> {

            log.info("testing {}", URI_CREATE_DEVICE_SESSION);

            webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_DEVICE_SESSION)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoDeviceSession, DEVICE_SESSION_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_DEVICE_SESSION_CLASS)
                    .returnResult()
                    .getResponseBody()
                    .getSessionId();
        });
    }
}

