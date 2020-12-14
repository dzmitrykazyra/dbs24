/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import io.rsocket.Payload;
import io.rsocket.util.DefaultPayload;
import org.dbs24.rsocket.AbstractRSocket;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class SimpleRSocket extends AbstractRSocket {

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        // just print the received string
        String str = payload.getDataUtf8();
        System.out.println("Received :: " + str);
        return Mono.empty();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        // just convert to upper case
        String str = payload.getDataUtf8();
        return Mono.just(DefaultPayload.create(str.toUpperCase()));
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        // convert the given str to char array and return
        String str = payload.getDataUtf8();
        return Flux.fromStream(str.chars().mapToObj(i -> (char) i))
                .map(Object::toString)
                .map(DefaultPayload::create);
    }
}
