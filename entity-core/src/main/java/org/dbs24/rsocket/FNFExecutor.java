/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rsocket;

import org.dbs24.stmt.Stmt;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface FNFExecutor<T> {

    Mono<Void> execute(
            String route,
            String string, 
            Class<?> clazz, 
            String remoteAddrr,
            Integer remotePort,
            Integer actionId,
            Long userId);
}
