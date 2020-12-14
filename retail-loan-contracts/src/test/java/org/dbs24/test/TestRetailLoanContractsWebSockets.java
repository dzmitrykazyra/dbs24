/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;
import org.dbs24.config.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Mono;
import reactor.core.Disposable;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.dbs24.test.core.AbstractWebSocketTest;
import org.dbs24.service.RetailLoanContractWebSocketService;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.WebSocketClient;
//import org.springframework.web.reactive.socket.client.StandardWebSocketClient;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.StandardWebSocketClient;
import java.time.Duration;
import static org.dbs24.consts.EntityReferenceConst.URI_CREATE_LOAN_CONTRACT;
import org.dbs24.entity.RetailLoanContract;
import reactor.core.publisher.Flux;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.http.MediaType;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestRetailLoanContractBootApp.class})
//@EnableAutoConfiguration(exclude = ReactiveSecurityAutoConfiguration.class)
@Import({RetailLoanContractWebSocketConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class TestRetailLoanContractsWebSockets extends TestUtil4LoanContract {

    @Autowired
    private RetailLoanContractWebSocketService webSocketService;

    private Disposable disposable;
    //private Mono<Void> response;

    @Test
    @Order(100)
    @RepeatedTest(3)
    public void testPingPong() {

        this.runTest(() -> {

            final String testName = "PingPong";

            final Mono<Void> response = webSocketService
                    .getWebSocketClient()
                    //.execute(webSocketService.buildUri("ws://104.197.253.120:%d%s", 7001), this.getTestHandler1());
                    //                    .execute(webSocketService.buildUri("ws://127.0.0.1", this.getPort()), session
                    //                            -> session.receive()
                    //                            .doOnNext(s -> log.debug("answer from server: {}", s.getPayloadAsText()))
                    //                            .then());
                    .execute(webSocketService.buildUri("ws://127.0.0.1", this.getPort()), session
                            //.execute(webSocketService.buildUri("ws://104.197.253.120", 7001), session
                            -> {

                        Mono<Void> inMsg = session.receive()
                                .doOnNext(s -> log.debug("process answer from server: '{}'", s.getPayloadAsText()))
                                .then();

                        Flux<WebSocketMessage> outMessages = Flux.just(
                                String.format("Client message 1 - %s", LocalDateTime.now().toString()),
                                String.format("Client message 2 - %s", LocalDateTime.now().toString()),
                                String.format("Client message 3 - %s", LocalDateTime.now().toString()))
                                .map(session::textMessage)
                                .doOnNext(s -> log.debug("sending to server '{}'", s.getPayloadAsText()));

                        return inMsg
                                .and(session.send(outMessages));
                    }).then();

            log.debug("{}: receive response  {}", testName, response.getClass().getCanonicalName());

            //this.disposable = response.subscribe();
            StmtProcessor.executeSilent(() -> response.block(Duration.ofSeconds(1L)));
            //response.block();

            //disp.dispose();
        });
    }

    @Test
    @Order(200)
    //@RepeatedTest(3)
    public void testPingPong2() {

        this.runTest(() -> {
            final String testName = "PingPong";

            final RetailLoanContract retailLoanContract = this.createTestContract_1Y_840();

//        final Class<RetailLoanContract> classRetailLoanContract = (Class<RetailLoanContract>) retailLoanContract.getClass();
//        final Mono<RetailLoanContract> monoRetailLoanContract = Mono.just(retailLoanContract);                
            final Mono<Void> response = webSocketService
                    .getWebSocketClient()
                    //.execute(webSocketService.buildUri("ws://104.197.253.120:%d%s", 7001), this.getTestHandler1());
                    //                    .execute(webSocketService.buildUri("ws://127.0.0.1", this.getPort()), session
                    //                            -> session.receive()
                    //                            .doOnNext(s -> log.debug("answer from server: {}", s.getPayloadAsText()))
                    //                            .then());
                    .execute(webSocketService.buildUri("ws://127.0.0.1", this.getPort()), session
                            //.execute(webSocketService.buildUri("ws://104.197.253.120", 7001), session
                            -> {

                        Mono<Void> inMsg = session.receive()
                                .doOnNext(s -> log.debug("process answer from server: '{}'", s.getPayloadAsText()))
                                .then();

                        Flux<WebSocketMessage> outMessages = Flux.just(retailLoanContract)
                                .map(c -> webSocketService.object2Json(c))
                                .map(session::textMessage);

                        return inMsg
                                .and(session.send(outMessages));
                    }).then();

            log.debug("{}: receive response  {}", testName, response.getClass().getCanonicalName());

            //this.disposable = response.subscribe();
            StmtProcessor.executeSilent(() -> response.block(Duration.ofSeconds(1L)));
            //response.block();
        });
    }

    @Test
    @Order(300)
    //@RepeatedTest(3)

    public void testPingPong3() {
        this.runTest(() -> {
            log.debug("300");
        });
    }

    @Test
    @Order(400)
    //@RepeatedTest(3)
    public void testPingPong4() {
        this.runTest(() -> {
            log.debug("400");
        });
    }

    @Test
    @Order(500)
    //@RepeatedTest(3)
    public void testPingPong5() {
        this.runTest(() -> {
            log.debug("500");
        });
    }

}
