/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.websocket;

import org.dbs24.component.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.scheduling.annotation.Scheduled;

public class EventGenerator {

    
//    @SpringBootApplication
//@EnableScheduling // <- enable scheduling!!!
    
    private AtomicInteger counter = new AtomicInteger(0);

    private EventUnicastService eventUnicastService;

    @Autowired
    public EventGenerator(EventUnicastService eventUnicastService) {
        this.eventUnicastService = eventUnicastService;
    }

    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    public void generateEvent() {
        int count = counter.getAndIncrement();
        Event event = new Event("event", count);
        eventUnicastService.onNext(event);
    }
}
