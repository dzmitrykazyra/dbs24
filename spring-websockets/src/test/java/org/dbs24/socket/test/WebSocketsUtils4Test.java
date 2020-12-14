/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.socket.test;

import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.StandardWebSocketClient;
import java.net.URI;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.socket.WebSocketMessage;
import java.time.Duration;
import org.springframework.web.reactive.socket.client.WebSocketClient;

public class WebSocketsUtils4Test {

    public void doTest() throws Throwable {

        //UndertowWebSocketClient client = new UndertowWebSocketClient();
        ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();

        URI url = new URI("ws://localhost:8080/path");

        client.execute(url, session -> session.receive()
                .doOnNext(System.out::println)
                .then());
    }

    public void doTest2() throws Throwable {

        ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();
        client.execute(
                URI.create("ws://localhost:8080/event-emitter"),
                session -> session.send(
                        Mono.just(session.textMessage("event-spring-reactive-client-websocket")))
                        .thenMany(session.receive()
                                .map(WebSocketMessage::getPayloadAsText)
                                .log())
                        .then())
                .block(Duration.ofSeconds(10L));
    }

    public void doTest3() throws Throwable {
        
        WebSocketClient client = new StandardWebSocketClient();

//        WebSocketStompClient stompClient = new WebSocketStompClient(client);
//        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
//
//        StompSessionHandler sessionHandler = new MyStompSessionHandler();
//        stompClient.connect(URL, sessionHandler);
//
//        new Scanner(System.in).nextLine(); // Don't close immediately.
//        
//        
//        	@Override
//	public void afterConnected(
//	  StompSession session, StompHeaders connectedHeaders) {
//	    session.subscribe("/topic/messages", this);
//	    session.send("/app/chat", getSampleMessage());
//	}
//	@Override
//	public void handleFrame(StompHeaders headers, Object payload) {
//	    Message msg = (Message) payload;
//	    logger.info("Received : " + msg.getText()+ " from : " + msg.getFrom());
//	}
        
    }
}
