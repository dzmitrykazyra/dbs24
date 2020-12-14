/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.component.PersistenceEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import static org.dbs24.consts.WebSocketConst.*;
import org.dbs24.stmt.StmtProcessor;

@Log4j2
@Data
public abstract class AbstractTest {

    @LocalServerPort
    protected int port;

    @Value("${reactive.rest.timeout:200000}")
    protected Integer timeoutDefault;

    @Autowired
    private PersistenceEntityManager persistenceEntityManager;

    @Autowired
    private Environment environment;

    @Autowired
    private ObjectMapper objectMapper;

    //==========================================================================
    final public void runTest(TestRunner testRunner) {

        log.info("execute test {}", this.getClass().getSimpleName());

        final StopWatcher stopWatcher = StopWatcher.create();

        StmtProcessor.create(() -> testRunner.execute());

        log.info(stopWatcher.getStringExecutionTime());
    }

    //==========================================================================
    final public <T> void testJSonConversion(T object) {

        this.runTest(() -> {

            final Class<T> clazz = (Class<T>) object.getClass();

            log.debug("{}: start conversion test for ", clazz.getSimpleName());
            final String string = StmtProcessor.create(() -> objectMapper.writeValueAsString(object));
            log.debug("{}: json is '{}' ", clazz.getSimpleName(), string);

            final T reverseObject = StmtProcessor.create(() -> objectMapper.readValue(string, clazz));

            log.debug("{}: successfull conversion test, created reverseObject is",
                    reverseObject.getClass().getSimpleName());
        });
    }
}
