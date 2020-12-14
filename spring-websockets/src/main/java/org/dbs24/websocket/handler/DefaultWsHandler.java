/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.websocket.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Log4j2
public class DefaultWsHandler implements WebSocketHandler {

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        log.debug("client session {}, {}", session.getId(), session.getHandshakeInfo());

        return session
                .send(session.receive()
                        .map(msg -> "RECEIVED ON SERVER :: " + msg.getPayloadAsText())
                        .map(session::textMessage)
                        .doOnNext(msg -> log.debug("process clientmessage '{}'", msg.getPayloadAsText()))
                )
                .doOnError(t -> log.error("{}: doOnError {}", session.getId(), t.getLocalizedMessage()))
                .doAfterTerminate(() -> log.debug("{}: session terminated", session.getId()))
                .then();
    }
}
