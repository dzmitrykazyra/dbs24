/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.reactor;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Log4j2
@Data
public abstract class AbstractHotSubscriber<T> extends AbstractSubscriber<T> {

    final Sinks.Many<T> hotSource = Sinks.many().multicast().directBestEffort();
    final Flux<T> hotFlux = hotSource.asFlux();

    @Override
    public void initialize() {
        super.initialize();
        hotFlux.subscribe(this);
    }

    @Override
    public void destroy() throws Exception {
        hotSource.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST);
        super.destroy();
    }

    @Override
    public void onNext(T t) {
        processEvent(t);
    }

    public void emitEvent(T t) {
        getHotSource().emitNext(t, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    protected abstract void processEvent(T t);
}
