/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.websocket;

import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.dbs24.component.api.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import static org.dbs24.consts.WebSocketConst.*;

@Log4j2
public abstract class AbstractWebSocketHandler extends AbstractApplicationBean implements WebSocketHandler {

    @Autowired
    protected EventUnicastService eventUnicastService;

    @Autowired
    protected ObjectMapper objectMapper;

    @Value("${spring.websocket.mapping-path:/websocket}")
    protected String webSocketMappingPath;

    //==========================================================================
    protected Mono<Void> processReactor(WebSocketHandler webSocketHandler, WebSocketSession session) {
        return webSocketHandler.handle(session);
    }

    protected Mono<Void> defaultHandle(WebSocketSession session) {

        log.debug("open webSocketSession {}", session.getId());

        Flux<WebSocketMessage> messages = session.receive()
                // .doOnNext(message -> { read message here or in the block below })
                .flatMap(message -> {
                    // or read message here
                    return eventUnicastService.getMessages();
                })
                .flatMap(o -> {
                    try {
                        return Mono.just(objectMapper.writeValueAsString(o));
                    } catch (Throwable e) {
                        return Mono.error(e);
                    }
                }).map(session::textMessage);
        return session.send(messages);
    }

    @Override
    public void initialize() {
        log.info("Websocket server is created ({}, ({}))",
                webSocketMappingPath,
                this.getClass().getSimpleName());
    }
}
