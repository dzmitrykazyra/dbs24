/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.websocket;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.dbs24.service.RetailLoanContractWebSocketService;
import java.util.Collection;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;

@Component
@Log4j2
public class RetailLoanContractWebSocketHandler extends AbstractWebSocketHandler {
    
    @Autowired
    RetailLoanContractWebSocketService retailLoanContractWebSocketService;
    
    final Collection<String> collection = ServiceFuncs.<String>getOrCreateCollection(ServiceFuncs.COLLECTION_NULL);
    
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        
        return this.processReactor(mainRCreactor0, session);
    }

    //==========================================================================
    final WebSocketHandler mainRCreactor0 = session -> {
        
        log.debug("client session {}, {}", session.getId(), session.getHandshakeInfo());
        
        final Flux<WebSocketMessage> outMessages = session.receive()
                .map(msg -> "RECEIVED ON SERVER :: " + msg.getPayloadAsText())
                .map(session::textMessage)
                .doOnNext(msg -> {
                    log.debug("process clientmessage '{}'", msg.getPayloadAsText());
                    
                    collection.add(msg.getPayloadAsText());
                    
                });
        
        return session
                .send(outMessages)
                .doOnError(t -> log.error("{}: doOnError {}", session.getId(), t.getLocalizedMessage()))
                .doAfterTerminate(() -> log.debug("{}: session terminated", session.getId()))
                .doFinally(df -> {
                    log.debug("{}: CS = {}", df, collection.size());
                })
                .then();
    };
    //==========================================================================
    final WebSocketHandler mainRCreactor = session -> {
        
        final HandshakeInfo handshakeInfo = session.getHandshakeInfo();
        
        log.debug("client session {}, {}", session.getId(), handshakeInfo);

        // обработали входящие
        Mono<Void> input = session.receive()
                .doOnNext(message -> log.debug("{}: process clientmessage  '{}'", session.getId(), message.getPayloadAsText()))
                //concatMap(item -> item.getPayloadAsText())
                //.flatMap(message -> this.p1(message.getPayloadAsText()))
                .then();

        // подготовили исходящие для клиента
        Mono<Void> output = session.send(Flux.just("server msg1", "server msg2", "server msg3")
                .doOnNext(s -> log.debug("prepare message for client '{}'", s))
                .map(session::textMessage))
                .then();
        
        return Mono.zip(input, output)
                .then();
    };
}
