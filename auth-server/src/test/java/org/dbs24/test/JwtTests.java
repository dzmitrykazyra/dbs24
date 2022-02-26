/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.auth.server.AuthServer;
import org.dbs24.auth.server.api.WaUserSessionInfo;
import org.dbs24.auth.server.config.AuthorizationServerConfig;
import org.dbs24.rest.api.LoginResult;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import static org.dbs24.consts.RestHttpConsts.URI_LOGIN;
import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@AutoConfigureWebTestClient
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {AuthServer.class})
@Import({AuthorizationServerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EnableCaching
@EntityScan(basePackages = {ALL_PACKAGES})
public class JwtTests extends AbstractAuthTest {
    
    @Order(100)
    @Test
    //@RepeatedTest(100)
    public void createJwt() {
        
        final Mono<WaUserSessionInfo> monoUserSessionInfo = Mono.just(StmtProcessor.create(WaUserSessionInfo.class, usi -> {
            //user.set
            usi.setDeviceId(TestFuncs.generateTestInteger(1, 1000000));
            usi.setAppName(TestFuncs.generateTestString15());
            usi.setAppVersion(TestFuncs.generateTestString15());
            usi.setFcmToken(TestFuncs.generateTestString15());
        }));
        
        this.runTest(() -> {
            log.info("testing {}", URI_LOGIN);
            
            final String token = webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_LOGIN)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoUserSessionInfo, WaUserSessionInfo.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(LoginResult.class)
                    .returnResult()
                    .getResponseBody()
                    .getToken();
            
            log.info("{}: created jwt token: {}", URI_LOGIN, token);
            
        });
    }
}
