/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;
import org.dbs24.WaMigration;
import org.dbs24.config.WaServerConfig;
import static org.dbs24.consts.WaConsts.*;
import org.dbs24.rest.api.AuthKeyCollection;
import org.dbs24.rest.api.AuthKeyInfo;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

@Deprecated
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {WaMigration.class})
@Import({WaServerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthKeysTests extends AbstractMonitoringTest {

    @Order(100)
    @Test
    @DisplayName("test1")
    //@RepeatedTest(10)
    public void createAgents() {

        final Mono<AuthKeyInfo> monoAgent = Mono.just(StmtProcessor.create(AUTH_KEY_INFO_CLASS, agent -> {
            agent.setCreateTime(LocalDateTime.now());

            //user.set
        }));
        runTest(() -> {

            log.info("testing {}", URI_GET_AGENTS);

            final AuthKeyCollection authKeyCollection = webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_GET_AGENTS)
                            .build())
                    .header("id1", String.valueOf("0"))
                    .header("id2", String.valueOf("1000"))
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(AUTH_KEY_COLLECTION_CLASS)
                    .returnResult()
                    .getResponseBody();

            log.info("receive {} agents", authKeyCollection.getCollection().size());

        });
    }
}
