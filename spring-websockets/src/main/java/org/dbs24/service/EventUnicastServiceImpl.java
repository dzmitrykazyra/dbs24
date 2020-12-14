/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import org.dbs24.component.api.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.EmitterProcessor;

@Service
public class EventUnicastServiceImpl implements EventUnicastService {

    private EmitterProcessor<Event> processor = EmitterProcessor.create();

    @Override
    public void onNext(Event next) {
        processor.onNext(next);
    }

    @Override
    public Flux<Event> getMessages() {
        return processor.publish().autoConnect();
    }
}
