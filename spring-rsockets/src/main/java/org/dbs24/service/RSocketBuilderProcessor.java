/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import org.springframework.messaging.rsocket.RSocketRequester.Builder;

@FunctionalInterface
public interface RSocketBuilderProcessor {
    void processRSocketBuilder(Builder builder);
}
