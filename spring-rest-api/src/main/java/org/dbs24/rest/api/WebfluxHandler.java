/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.UndertowHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * Custom Undertow {@link HttpHandler} that uses
 * {@link UndertowHttpHandlerAdapter} provided by Spring 5.
 *
 * @author zhurlik@gmail.com
 */
public class WebfluxHandler implements HttpHandler {

    private final static Logger LOG = LoggerFactory.getLogger(WebfluxHandler.class);

    private final static HttpHandler UNDERTOW_ADAPTER = new UndertowHttpHandlerAdapter(RouterFunctions.toHttpHandler(
            RouterFunctions.route(
                    GET("/test1").and(accept(APPLICATION_JSON)),
                    request -> {
//                                Mono<User> userMono = Mono.just(
//                                        new User("User test1", new Random().nextInt(10)));
//                                return userMono.flatMap(usr -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(usr)));
                        return null;
                    })
                    .and(RouterFunctions.route(
                            GET("/test2").and(accept(MediaType.APPLICATION_STREAM_JSON)), request -> {
//                        Flux<User> userFlux = Flux.just(
//                                new User("User 1", new Random().nextInt(5)),
//                                new User("User 2", new Random().nextInt(5)))
//                                .delayElements(Duration.ofSeconds(3));
//                        return ServerResponse.ok().contentType(APPLICATION_STREAM_JSON).body(userFlux, User.class);
                        return null;
                    }
                    ))
    ));

    /**
     * Wildfly requires this constructor.
     *
     * @param httpHandler
     */
    public WebfluxHandler(final HttpHandler httpHandler) {
        LOG.info(">> WebfluxHandler has been created...");
        // TODO: check why we need to have http handler?
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        LOG.info(">> Handling a http request...");
        UNDERTOW_ADAPTER.handleRequest(exchange);
    }
}
