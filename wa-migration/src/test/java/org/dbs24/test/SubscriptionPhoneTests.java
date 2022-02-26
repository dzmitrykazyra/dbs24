/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;
import org.dbs24.WaMigration;
import org.dbs24.config.WaServerConfig;
import static org.dbs24.consts.WaConsts.*;
import org.dbs24.rest.api.*;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.*;
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
public class SubscriptionPhoneTests extends AbstractMonitoringTest {

    @Order(100)
    @Test
    @DisplayName("test1")
    //@RepeatedTest(10)
    public void getSubscriptions() {

        runTest(() -> {

            log.info("testing {}", URI_GET_ALL_SUBSCRIPTIONS);

            final SubscriptionPhoneCollection subscriptionPhoneCollection = webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_GET_ALL_SUBSCRIPTIONS)
                            .build())
                    //                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(SUBSCRIPTION_PHONE_COLLECTION_CLASS)
                    .returnResult()
                    .getResponseBody();

            log.info("receive {} phone subscriptions", subscriptionPhoneCollection.getCollection().size());

        });
    }
}

