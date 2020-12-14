package org.dbs24.test;

import java.util.Collection;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.dbs24.config.*;
import org.dbs24.service.*;
import static org.dbs24.consts.SysConst.SERVICE_USER_ID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import org.dbs24.test.core.TestRunner;
import org.dbs24.test.core.AbstractRSocketTest;
import static org.dbs24.rsocket.api.MessageType.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestServiceMonitoringBoot.class})
//@EnableAutoConfiguration(exclude = ReactiveSecurityAutoConfiguration.class)
@Import({TestServiceMonitoringConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestServiceMonitoring extends AbstractRSocketTest {

    @Autowired
    MonitoringRSocketService monitoringRSocketService;

    private Boolean isRemoteTest = Boolean.TRUE;
    private String remoteAddrr = isRemoteTest ? "104.197.253.120" : "127.0.0.1";

    @BeforeEach
    public void setUp() {
        Hooks.onOperatorDebug();

//        getTransportPair().responder.awaitAllInteractionTermination(getTimeout());
//        getTransportPair().dispose();
//        getTransportPair().awaitClosed();
//        RuntimeException throwable = new RuntimeException();
//
//        try {
//            getTransportPair().byteBufAllocator2.assertHasNoLeaks();
//        } catch (Throwable t) {
//            throwable = Exceptions.addSuppressed(throwable, t);
//        }
//
//        try {
//            getTransportPair().byteBufAllocator1.assertHasNoLeaks();
//        } catch (Throwable t) {
//            throwable = Exceptions.addSuppressed(throwable, t);
//        }
//
//        if (throwable.getSuppressed().length > 0) {
//            throw throwable;
//        }
    }

    //==========================================================================
    @AfterEach
    public void close() {
        Hooks.resetOnOperatorDebug();

        if (!isRemoteTest) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //==========================================================================
    final TestRunner testRunner = () -> {

        monitoringRSocketService.send(MONITORING_LIVENESS, "Hello from TestServiceMonitoring");
        
    };

    @Order(99)
    @Test
    @DisplayName("Routing")
    @RepeatedTest(10)
    public void testRoutingMultiply() {

        runTest(testRunner);
    }
}
