/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationBean;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

@Data
@Log4j2
public abstract class AbstractWebSocketService extends AbstractApplicationBean implements WebSocketService {

    @Value("${spring.websocket.mapping-path:/websocket}")
    protected String webSocketMappingPath;

    @Autowired
    private WebSocketClient webSocketClient;

    @Autowired
    private ObjectMapper objectMapper;

    public final URI buildUri(String uriString, Integer port) {

        return this.buildUri(uriString, port, webSocketMappingPath);
    }

    public String object2Json(Object object) {
        return StmtProcessor.create(() -> objectMapper.writeValueAsString(object));
    }

    public <T> T json2Object(String json, Class<T> clazz) {
        return (T) objectMapper.convertValue(json, clazz);
    }

    //==========================================================================
    public final URI buildUri(String uriString, Integer port, String mappingPath) {

        return StmtProcessor.<URI>create(() -> new URI(uriString.concat(String.format(":%d%s", port, mappingPath))));
    }

    //==========================================================================
    final WebSocketHandler echoHandler = session -> {
        Mono<WebSocketMessage> outMessage = Mono.just(String.format("Echo message %s", LocalDateTime.now().toString()))
                .log()
                .doOnSubscribe(s -> {
                    s.request(Long.MAX_VALUE);
                    log.debug("{}: start messaging", session.getId());
                })
                .doOnNext(x -> log.info("{}: doOnNext '{}'", session.getId(), x))
                .doOnError(e -> log.error("{}: Error processing '{}'", session.getId(), e.getMessage()))
                .map(session::textMessage);

        log.debug("{}: Ураааа!!! - 1", session.getId());

        return session.send(outMessage);
    };
}
