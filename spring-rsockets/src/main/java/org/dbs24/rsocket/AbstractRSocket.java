/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rsocket;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import java.util.Optional;
import lombok.Data;
import org.dbs24.service.AbstractRSocketService;
import org.dbs24.spring.core.api.AbstractApplicationBean;
import org.reactivestreams.Publisher;
import org.dbs24.spring.core.api.ServiceLocator;
import org.dbs24.exception.RSocketServiceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Data
public abstract class AbstractRSocket extends AbstractApplicationBean implements RSocket {

//    @Autowired
//    private AbstractRSocketService rSocketService;    
    
    protected AbstractRSocketServer rSocketServer;
    

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        payload.release();
        return Mono.error(new UnsupportedOperationException("Fire and forget not implemented."));
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        payload.release();
        return Mono.error(new UnsupportedOperationException("Request-Response not implemented."));
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        payload.release();
        return Flux.error(new UnsupportedOperationException("Request-Stream not implemented."));
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        return Flux.error(new UnsupportedOperationException("Request-Channel not implemented."));
    }

    @Override
    public Mono<Void> metadataPush(Payload payload) {
        payload.release();
        return Mono.error(new UnsupportedOperationException("Metadata-Push not implemented."));
    }
}
