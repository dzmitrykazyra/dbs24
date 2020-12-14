/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import io.rsocket.Payload;
import static org.dbs24.consts.SysConst.*;
import io.rsocket.util.DefaultPayload;
import java.math.BigDecimal;
import reactor.core.publisher.Flux;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import lombok.extern.log4j.Log4j2;
import org.dbs24.config.*;
import org.junit.jupiter.api.*;
import org.dbs24.test.core.AbstractRSocketTest;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import org.dbs24.mime.MimeTypes;
import org.dbs24.entity.*;
import org.dbs24.rsocket.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import static org.dbs24.consts.RetailLoanContractConst.*;
import org.dbs24.test.service.TestRetailLoanContractsService;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.test.StepVerifier;
import org.assertj.core.api.Assertions;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.entity.security.ApplicationUser;
import static org.dbs24.consts.SysConst.SERVICE_USER_ID;
import static org.dbs24.consts.EntityCoreConst.*;
import static org.dbs24.consts.SecurityConst.ACT_CREATE_OR_MODIFY_USER;
import org.dbs24.test.core.TestRunner;
//import io.rsocket.metadata.T
//import io.rsocket.test.SlowTest;
import reactor.core.publisher.Hooks;

//@SlowTest
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestRetailLoanContractBootApp.class})
//@EnableAutoConfiguration(exclude = ReactiveSecurityAutoConfiguration.class)
@Import({RetailLoanContractRSocketConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestRSocket extends AbstractRSocketTest {

    @Autowired
    private TestRetailLoanContractsService testRetailLoanContractsService;

    @Autowired
    private AbstractEntityRSocketClient abstractEntityRSocketClient;

    private RSocketRequester rRemoteSocketRequester;
    private Boolean isRemoteTest = Boolean.FALSE;
    //private Boolean isRemoteTest = Boolean.TRUE;
    private String remoteAddrr = isRemoteTest ? "104.197.253.120" : "127.0.0.1";

    //@BeforeAll
    public void SetupRemote() {

        final Integer remotePort = rSocketPort;

        rRemoteSocketRequester = getRSocketService()
                .getRSocketRequester(remoteAddrr, remotePort)
                .block(Duration.ofMillis(blockDelay));

        Assert.notNull(rRemoteSocketRequester, "fucking RSocketRequester is null!");

        log.info("Remote RSocketRequester is created ({}, {})",
                remoteAddrr,
                remotePort);
    }

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

//    @Order(99)
//    @Test
//    @DisplayName("Routing")
//    @RepeatedTest(3)
    public void testRouting() {

        runTest(() -> {

            final RetailLoanContract retailLoanContract = testRetailLoanContractsService.createTestContract_1Y_840();

            final Mono<Void> mono
                    = getRSocketService()
                            .getRSocketRequester(remoteAddrr, rSocketPort)
                            .map(r -> r.route(R_RETAIL_LOAN_CONTRACT)
                            .metadata(metadataSpec -> {
                                metadataSpec.metadata(String.valueOf(MODIFY_INDIVIDUAL_LOAN_CONTRACT), MimeTypeUtils.parseMimeType(MimeTypes.ENTITY_ACTION_ID.getValue()));
                                metadataSpec.metadata(String.valueOf(SERVICE_USER_ID), MimeTypeUtils.parseMimeType(MimeTypes.ENTITY_USER_ID.getValue()));
                            })
                            .data(retailLoanContract))
                            .block(Duration.ofMillis(blockDelay))
                            .send();

            StepVerifier.create(mono)
                    .expectComplete()
                    .verify();

            log.debug("after verify");

        });
    }

    //==========================================================================
    final TestRunner testRunner = () -> {

        final String testString = TestFuncs.generateTestString15();
//
//        final ApplicationUser applicationUser = testRetailLoanContractsService
//                .getSecurityActionsService()
//                .createAndSaveUser(testString, testString, testString, testString, testString);
        // conversion test
        //this.testJSonConversion(retailLoanContract);
        final Integer remotePort = rSocketPort;

        final Collection<ApplicationUser> collection4Users = ServiceFuncs.<ApplicationUser>createCollection();

        Integer count = 1;
        Integer i = 0;
        String s = "";

        while (++i <= count) {
            s = String.format("%s", i);
            collection4Users.add(testRetailLoanContractsService
                    .getSecurityActionsService()
                    .createUser(testString.concat(s), testString.concat(s), testString.concat(s), testString.concat(s), testString.concat(s)));
        }

        final Mono<Void> monoUsers = abstractEntityRSocketClient
                .sendFNF(collection4Users, ApplicationUser.class, remoteAddrr, remotePort, ACT_CREATE_OR_MODIFY_USER, SERVICE_USER_ID);

        monoUsers.subscribe();

//        StepVerifier.create(monoUsers)
//                .expectComplete()
//                .verify();
        count = 1;
        i = 0;

        final Collection<RetailLoanContract> collection4Contracts = ServiceFuncs.<RetailLoanContract>createCollection();

        while (++i <= count) {
            collection4Contracts.add(testRetailLoanContractsService.createTestContract_1Y_840());
        }

        final Mono<Void> monoContracts = abstractEntityRSocketClient
                .sendFNF(collection4Contracts, RetailLoanContract.class, remoteAddrr, remotePort, MODIFY_INDIVIDUAL_LOAN_CONTRACT, SERVICE_USER_ID);

        monoContracts.subscribe();

//        StepVerifier.create(monoContracts)
//                .expectComplete()
//                .verify();
        log.debug("after verify");

    };

    @Order(99)
    @Test
    @DisplayName("Routing")
    @RepeatedTest(10)
    public void testRoutingMultiply() {

        runTest(testRunner);
    }

    @Order(100)
    @Test
    @DisplayName("Routing2")
    @RepeatedTest(10)
    public void testRoutingMultiply2() {

        runTest(testRunner);
    }

//    @Order(101)
//    @Test
//    @DisplayName("Routing3")
//    @RepeatedTest(130)
    public void testRoutingMultiply3() {

        runTest(testRunner);
    }

//    @Order(200)
//    @Test
    @DisplayName("fireAndForget")
    public void fireAndForget() {

        runTest(() -> {

            final RetailLoanContract retailLoanContract = testRetailLoanContractsService.createTestContract_1Y_840();

            // conversion test
            this.testJSonConversion(retailLoanContract);

            final Integer remotePort = rSocketPort + 1;

            Mono.just(retailLoanContract)
                    .map(getRSocketService()::toPayload)
                    .flatMap(contract -> getRSocketService()
                    .getRSocketRequester(remoteAddrr, remotePort)
                    .block(Duration.ofMillis(blockDelay))
                    .rsocket()
                    .fireAndForget(contract))
                    .block(Duration.ofMillis(blockDelay));
        });
    }

//    @Order(201)
//    @Test
    @DisplayName("fireRemoteAndForget")
    public void fireRemoteAndForget() {

        final RetailLoanContract retailLoanContract = testRetailLoanContractsService.createTestContract_1Y_840();

        // conversion test
        this.testJSonConversion(retailLoanContract);

        Mono.just(retailLoanContract)
                .map(getRSocketService()::toPayload)
                .flatMap(contract -> rRemoteSocketRequester
                .rsocket()
                .fireAndForget(contract))
                .block(Duration.ofMillis(blockDelay));
        //        runTest(() -> getRequestPayload()
        //                .flatMap(payload -> rRemoteSocketRequester
        //                .rsocket()
        //                .fireAndForget(payload))
        //                .blockLast(Duration.ofMillis(blockDelay))
        //);
        //    @Order(200)
        //    @Test
        //    @DisplayName("requestAndResponse")
        //    public void requestAndResponse() {
        //        runTest(() -> getRequestPayload()
        //                .flatMap(payload -> this.getRSocketService().getRSocketClient().requestResponse(payload))
        //                .doOnNext(response -> log.debug("Response from server :: '{}' ", response.getDataUtf8()))
        //                .blockLast(Duration.ofMinutes(1))
        //        );
        //    }
        //
        //    @Order(300)
        //    @Test
        //    @DisplayName("requestAndResponseStream")
        //    public void requestAndResponseStream() {
        //        runTest(() -> getRequestPayload()
        //                .flatMap(payload -> this.getRSocketService().getRSocketClient().requestStream(payload))
        //                .doOnNext(response -> log.debug("Response from server :: '{}'", response.getDataUtf8()))
        //                .blockLast(Duration.ofMinutes(1))
        //        );
        //    }
    }

    private Flux<Payload> getRequestPayload() {
        return Flux.just("hi", "hello", "how", "are", "you")
                .delayElements(Duration.ofSeconds(1))
                .map(DefaultPayload::create);
    }

    @AfterAll
    public void CloseAll() {
        Optional.ofNullable(rRemoteSocketRequester)
                .ifPresent(req -> req.rsocket().dispose());
    }
}
