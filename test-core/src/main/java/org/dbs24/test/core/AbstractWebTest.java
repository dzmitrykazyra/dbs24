package org.dbs24.test.core;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.time.LocalDateTime;
import static org.dbs24.consts.RestHttpConsts.*;
import org.dbs24.application.core.locale.NLS;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.entity.core.AbstractPersistenceEntity;
import org.dbs24.component.PersistenceEntityManager;
import java.util.Collection;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
//import org.dbs24.service.WebClientMgmt;
import org.springframework.core.env.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.beans.factory.annotation.Value;
import java.time.Duration;
import static javax.ws.rs.core.HttpHeaders.USER_AGENT;
import org.springframework.http.MediaType;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@Data
@AutoConfigureWebTestClient
public abstract class AbstractWebTest extends AbstractTest {

    public static final String WEBTEST_CLIENT_NAME = "WebTestClient";

//    @Autowired
//    private WebClientMgmt webClientMgmt;

    @Autowired
    protected WebTestClient webTestClient;

    //==========================================================================
    @Test
    @Order(10)
    public void initializeMainService() {
        //======================================================================        
        // действие тестирование сервиса
        webTestClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(URI_LIVENESS)
                        .queryParam("secretWord", "bla-bla")
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

        webTestClient = webTestClient
                .mutate()
                .responseTimeout(Duration.ofMillis(timeoutDefault))
                .defaultHeader(USER_AGENT, WEBTEST_CLIENT_NAME)
                .build();
    }

    //==========================================================================
    public static final String generateTestName(Class clazz) {
        return String
                .format("test_%s_%s", clazz.getCanonicalName(), NLS.getStringDateTime(LocalDateTime.now()))
                .replace(" ", "_");
    }

    //==========================================================================
    @Transactional(readOnly = true, propagation = Propagation.NEVER)
    public <T extends AbstractPersistenceEntity> T loadLastEntity(Class<T> entClass) {

        final String className = entClass.getSimpleName();
        final StopWatcher stopWatcher = StopWatcher.create(className);
        final String query = String.format("Select e from %s e where e.entity_id = (select max(entity_id) from %s )",
                className,
                className);

        log.debug("Reload last entity '{}'", className);

        final Collection<T> collection = this.getPersistenceEntityManager()
                .getEntityManager()
                .createQuery(query)
                .getResultList();

        if (collection.isEmpty()) {
            throw new RuntimeException(String.format("Entity not found", className));
        }

        final T entity = collection.iterator().next();

        log.debug("Entity is loaded ({}, entity_id={})",
                stopWatcher.getStringExecutionTime(),
                entity.entityId()
        );

        return entity;
    }

    //==========================================================================
    @Transactional(readOnly = true, propagation = Propagation.NEVER)
    public <T extends AbstractPersistenceEntity> Collection<T> loadAllEntities(Class<T> entClass) {

        final String className = entClass.getSimpleName();
        final StopWatcher stopWatcher = StopWatcher.create(className);
        final String query = String.format("Select e from %s e ", className);

        log.debug("Reload all entities ({})", className);

        final Collection<T> collection = this.getPersistenceEntityManager()
                .getEntityManager()
                .createQuery(query)
                .getResultList();

        log.debug("{} : {} entities is loaded ({})",
                className,
                collection.size(),
                stopWatcher.getStringExecutionTime());

        return collection;
    }

    //==========================================================================
    public String getTestRestAddress() {
        return String.format("http://127.0.0.1:%d", port);
    }
}
