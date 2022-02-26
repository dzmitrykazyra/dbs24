/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import lombok.extern.log4j.Log4j2;
import org.dbs24.WaMigration;
import org.dbs24.config.WaServerConfig;
import static org.dbs24.consts.WaConsts.*;
import org.dbs24.rest.api.*;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {WaMigration.class})
@Import({WaServerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsersTests extends AbstractMonitoringTest {

    private Integer lastAgentId;

    @Order(100)
    @Test
    @DisplayName("test1")
    //@RepeatedTest(10)
    public void getUsers() {

        runTest(() -> {

            log.info("testing {}", URI_GET_APP_USERS);

            final AppUserCollection appUserCollection = webTestClient
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_GET_APP_USERS)
                            .build())
                    //                    .contentType(APPLICATION_JSON)
                    .header("id1", String.valueOf("1000000"))
                    .header("id2", String.valueOf("1000020"))
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(APP_USER_COLLECTION_CLASS)
                    .returnResult()
                    .getResponseBody();

            log.info("receive {} users", appUserCollection.getCollection().size());

        });
    }

    //==========================================================================
    @Order(200)
    @Test
    @DisplayName("test1")
    //@RepeatedTest(10)
    public void getActualUsers() {

        runTest(() -> {

            log.info("testing {}", URI_GET_ACTUAL_APP_USERS);

            final AppUserIdCollection appUserIdCollection = webTestClient
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_GET_ACTUAL_APP_USERS)
                            .build())
                    //                    .contentType(APPLICATION_JSON)
                    //.header("id1", String.valueOf("1000000"))
                    //.header("id2", String.valueOf("1000020"))
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(APP_USER_ID_COLLECTION_CLASS)
                    .returnResult()
                    .getResponseBody();

            log.info("receive {} users", appUserIdCollection.getCollection().size());

        });
    }
}
