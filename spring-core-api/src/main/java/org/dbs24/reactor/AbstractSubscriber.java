/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.reactor;

import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationBean;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Log4j2
public abstract class AbstractSubscriber<T> extends AbstractApplicationBean implements Subscriber<T> {

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
        log.debug("{} : {}", this.getClass(), s.getClass());
    }

    @Override
    public void onError(Throwable t) {
        log.error("{} : {}", this.getClass(), t.getMessage());
        t.printStackTrace();
    }

    @Override
    public void onComplete() {
        log.debug("onComplete : {}", this.getClass());
    }
}
