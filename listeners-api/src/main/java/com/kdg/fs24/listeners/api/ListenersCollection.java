/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.listeners.api;

import java.util.Collection;
import com.kdg.fs24.application.core.service.funcs.ServiceFuncs;
import com.kdg.fs24.application.core.service.funcs.AnnotationFuncs;

/**
 *
 * @author Козыро Дмитрий
 */
public final class ListenersCollection<T extends EventListener> {

    private final Collection<T> listeners = ServiceFuncs.<T>createCollection();

    public static ListenersCollection createListenersCollection() {
        return new ListenersCollection();
    }

    //==========================================================================
    public void addListener(final T listener) {
        this.getListeners().add(listener);
    }
    //==========================================================================

    public T findListener(final int listenerId) {

        final T listener = ServiceFuncs.<T>getCollectionElement_silent(this.getListeners(),
                //l -> l.getClass()
                l -> ((ListenerId) AnnotationFuncs.getAnnotation(l.getClass(), ListenerId.class)).value() == listenerId);
        return listener;
    }

    public Collection<T> getListeners() {
        return listeners;
    }

    //==========================================================================
    public void processEvent(final Event event) {
        this.getListeners()
                .stream()
                .unordered()
                .forEach((listener) -> listener.processEvent(event));
    }

}
