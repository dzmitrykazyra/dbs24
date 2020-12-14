/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component.api;

import reactor.core.publisher.Flux;

public interface EventUnicastService {
        /**
     * Add message to stream
     * @param next - message which will be added to stream
     */
    void onNext(Event next);

    Flux<Event> getMessages();
}
