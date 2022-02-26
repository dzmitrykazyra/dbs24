package org.dbs24.test.core;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.rest.api.ResponseBody;
import org.dbs24.rest.api.ShutdownRequest;
import org.dbs24.spring.core.api.EntityInfo;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Hooks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

import static org.dbs24.consts.RestHttpConsts.*;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@Getter
@AutoConfigureWebTestClient
public abstract class AbstractWebTest extends AbstractTest {

    public static final String WEBTEST_CLIENT_NAME = "WebTestClient";

//    @Autowired
//    private WebClientMgmt webClientMgmt;
    @Autowired
    protected WebTestClient webTestClient;
    
    final Random random = new Random();

    //==========================================================================
    @Test
    @Order(10)
    public void initializeMainService() {
        //======================================================================

        StmtProcessor.assertNotNull(WebTestClient.class, webTestClient, "webTestClient");

        // действие тестирование сервиса
        webTestClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(URI_LIVENESS)
                        //.queryParam("secretWord", "bla-bla")
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                //.body(createdRetailLoanContract, classRetailLoanContract)
                .exchange()
                //.onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Pizza5xxException()))
                // and use the dedicated DSL to test assertions against the response
                .expectStatus()
                .isOk();
    }

    //==========================================================================
    @BeforeEach
    public void setUp() {

        StmtProcessor.assertNotNull(WebTestClient.class, webTestClient, "webTestClient");

        Hooks.onOperatorDebug();
        webTestClient = webTestClient
                .mutate()
                .responseTimeout(Duration.ofMillis(timeoutDefault))
                //.defaultHeader(USER_AGENT, WEBTEST_CLIENT_NAME)
                .build();
    }

    @AfterEach
    public void close() {
        Hooks.resetOnOperatorDebug();
    }

    //==========================================================================
    public static final String generateTestName(Class clazz) {
        return String
                .format("test_%s_%s", clazz.getCanonicalName(), NLS.getStringDateTime(LocalDateTime.now()))
                .replace(" ", "_");
    }

    //==========================================================================
//    @Transactional(readOnly = true, propagation = Propagation.NEVER)
//    public <T extends AbstractPersistenceEntity> T loadLastEntity(Class<T> entClass) {
//
//        final String className = entClass.getSimpleName();
//        final StopWatcher stopWatcher = StopWatcher.create(className);
//        final String query = String.format("Select e from %s e where e.entity_id = (select max(entity_id) from %s )",
//                className,
//                className);
//
//        log.debug("Reload last entity '{}'", className);
//
//        final Collection<T> collection = this.getPersistenceEntityManager()
//                .getEntityManager()
//                .createQuery(query)
//                .getResultList();
//
//        if (collection.isEmpty()) {
//            throw new RuntimeException(String.format("Entity not found", className));
//        }
//
//        final T entity = collection.iterator().next();
//
//        log.debug("Entity is loaded ({}, entity_id={})",
//                stopWatcher.getStringExecutionTime(),
//                entity.entityId()
//        );
//
//        return entity;
//    }

    //==========================================================================
//    @Transactional(readOnly = true, propagation = Propagation.NEVER)
//    public <T extends AbstractPersistenceEntity> Collection<T> loadAllEntities(Class<T> entClass) {
//
//        final String className = entClass.getSimpleName();
//        final StopWatcher stopWatcher = StopWatcher.create(className);
//        final String query = String.format("Select e from %s e ", className);
//
//        log.debug("Reload all entities ({})", className);
//
//        final Collection<T> collection = this.getPersistenceEntityManager()
//                .getEntityManager()
//                .createQuery(query)
//                .getResultList();
//
//        log.debug("{} : {} entities is loaded ({})",
//                className,
//                collection.size(),
//                stopWatcher.getStringExecutionTime());
//
//        return collection;
//    }

    @Order(Integer.MAX_VALUE)
    @Test
    @DisplayName("AbstractMonitoringTest")
    public void test4wait() throws InterruptedException {

        StmtProcessor.assertNotNull(WebTestClient.class, webTestClient, "webTestClient");

        this.runTest(() -> {

            Boolean isFinished = false;

            while (!isFinished) {

                StmtProcessor.sleep(2000);

                final ShutdownRequest shutdownRequest
                        = webTestClient
                                .post()
                                .uri(uriBuilder
                                        -> uriBuilder
                                        .path(URI_CAN_SHUTDOWN)
                                        .build())
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .exchange()
                                .expectStatus()
                                .isOk()
                                .expectBody(SHUTDOWN_REQUEST_CLASS)
                                .returnResult()
                                .getResponseBody();

                log.debug(shutdownRequest);

                isFinished = shutdownRequest.getCanShutDown();
            }
        });

        log.info("#finisk all tests {}", this.getClass().getCanonicalName());
    }

    //==========================================================================
    public String getTestRestAddress() {
        return String.format("http://127.0.0.1:%d", port);
    }

    //==================================================================================================================
    protected <T extends ResponseBody> void checkTestResult(T responseBody, String string) {

        log.info("Validate test results: {}", responseBody);

        final StringBuilder sb = new StringBuilder(255);

        sb.append(string);

        sb.append(String.format(", response code is: %s", responseBody.getCode().toString()));

        StmtProcessor.ifNotNull(responseBody.getMessage(), () -> sb.append(", message: ".concat(responseBody.getMessage())));

        StmtProcessor.ifNotNull(responseBody.getErrors(), errors -> errors.forEach(error -> sb.append("\n error: ".concat(error.toString()))));
        StmtProcessor.ifTrue(!responseBody.getCode().equals(OC_OK), () -> log.error(responseBody));
        StmtProcessor.assertTrue(responseBody.getCode().equals(OC_OK), sb.toString());
        StmtProcessor.assertNotNull(EntityInfo.class, responseBody.getCreatedEntity(), "created entity is null");
        StmtProcessor.assertIsNull(String.class, responseBody.getError(), "error object");

    }
}
