/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import lombok.extern.log4j.Log4j2;
import org.dbs24.WaMonitoring;
import org.dbs24.config.WaServerConfig;
import static org.dbs24.consts.WaConsts.*;
import static org.dbs24.consts.WaConsts.Uri.*;
import static org.dbs24.consts.WaConsts.Classes.*;
import org.dbs24.rest.api.UserTokenInfo;
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
public class UserTokenTests extends AbstractMonitoringTest {

    private Long lastTokenId;

    @Order(100)
    @Test
    @DisplayName("test1")
    @RepeatedTest(10)
    public void createUserToken() {

        final Mono<UserTokenInfo> monoUserToken = Mono.just(StmtProcessor.create(USER_TOKEN_INFO_CLASS, userTokenInfo -> {
            userTokenInfo.setIsValid(Boolean.TRUE);
            userTokenInfo.setUserId(getLastUserId());
        }));
        runTest(() -> {

            log.info("testing {}", URI_CREATE_TOKEN);

            lastTokenId = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_TOKEN)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoUserToken, USER_TOKEN_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_USER_TOKEN_CLASS)
                    .returnResult()
                    .getResponseBody()
                    .getTokenId();
        });
    }

    @Order(200)
    @Test
    @DisplayName("test2")
    public void updateUserToken() {

        log.info("testing {}, update sessionId = {}", URI_CREATE_TOKEN, lastTokenId);

        final Mono<UserTokenInfo> monoUserToken = Mono.just(StmtProcessor.create(USER_TOKEN_INFO_CLASS, userTokenInfo -> {
            userTokenInfo.setTokenId(lastTokenId);
            userTokenInfo.setIsValid(Boolean.FALSE);            
            userTokenInfo.setUserId(getLastUserId());
        }));
        runTest(() -> {

            log.info("testing {}", URI_CREATE_TOKEN);

            lastTokenId = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_TOKEN)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoUserToken, USER_TOKEN_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_USER_TOKEN_CLASS)
                    .returnResult()
                    .getResponseBody()
                    .getTokenId();
        });
    }
}
