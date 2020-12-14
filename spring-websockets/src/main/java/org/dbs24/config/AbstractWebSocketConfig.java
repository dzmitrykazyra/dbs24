/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import org.dbs24.websocket.*;
import static org.dbs24.consts.WebSocketConst.*;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.HandlerMapping;
import java.util.Map;
import java.util.HashMap;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.HandlerAdapter;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;

@Log4j2
public abstract class AbstractWebSocketConfig { //extends AbstractWebSecurityConfig {
// https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-websocket-server-handler

    @Value("${spring.websocket.mapping-path:/websocket}")
    protected String webSocketMappingPath;

    @Bean
    public WebSocketClient webSocketClient() {

        return new ReactorNettyWebSocketClient();
    }

    //==========================================================================
    @Bean
    public HandlerAdapter wsHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

//    @Bean
//    public WebSocketService webSocketService() {
//        TomcatRequestUpgradeStrategy strategy = new TomcatRequestUpgradeStrategy();
//        strategy.setMaxSessionIdleTimeout(0L);
//        return new HandshakeWebSocketService(strategy);
//    }    
    
    //==========================================================================
    @Bean
    public HandlerMapping handlerMapping(WebSocketHandler webSocketHandler) {
        final Map<String, WebSocketHandler> map = new HashMap<>();
        log.info("Register webSocketHandler ({})", webSocketHandler.getClass().getCanonicalName());
        map.put(webSocketMappingPath, webSocketHandler);

        final WebSocketHandler echoWebSocketHandler = new EchoHandler();
        log.info("Register echoWebSocketHandler ({}, {})", WS_ECHO, echoWebSocketHandler.getClass().getCanonicalName());

        map.put(WS_ECHO, echoWebSocketHandler);
        int order = -1; // before annotated controllers

        return new SimpleUrlHandlerMapping(map, order);

//    @Bean
//    public HandlerMapping handlerMapping() {
//        Map<String, WebSocketHandler> map = Map.of(
//                "/echo", new EchoHandler(),
//                "/time", new TimeHandler()
//        );
//        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
//        mapping.setUrlMap(map);
//        mapping.setOrder(-1);
//        return mapping;
//    }        
    }
}
