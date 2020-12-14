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
import org.springframework.stereotype.Component;

//@Component
public class ReactiveWebSocketHandler implements WebSocketHandler {

    private Flux<String> publisher;
    
// http://kojotdev.com/2019/08/spring-webflux-websocket-with-vue-js/    
//    private final GreetingsService greetingsService = new GreetingsService();
//    private final GreetingsPublisher greetingsPublisher;
//
//    
//    public ReactiveWebSocketHandler(GreetingsPublisher greetingsPublisher) {
//        this.greetingsPublisher = greetingsPublisher;
//        this.publisher = Flux.create(greetingsPublisher).share();
//    }

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        webSocketSession
                .receive()
                .map(webSocketMessage -> webSocketMessage.getPayloadAsText())
                //                .map(helloMessage -> greetingsService.greeting(helloMessage))
                //                .doOnNext(greetings -> greetingsPublisher.push(greetings))
                .subscribe();
        final Flux<WebSocketMessage> message = publisher
                .map(greetings -> webSocketSession.textMessage(greetings));
        return webSocketSession.send(message);
    }
}
