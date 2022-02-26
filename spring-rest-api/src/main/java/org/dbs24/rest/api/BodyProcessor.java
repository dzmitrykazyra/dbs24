/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import reactor.core.publisher.Mono;
import org.dbs24.spring.core.api.RequestBody;
import org.dbs24.spring.core.api.EntityInfo;

@FunctionalInterface
public interface BodyProcessor<E extends EntityInfo, T extends RequestBody, V extends ResponseBody<E>> {
    V processBody(Mono<T> mono);
}
