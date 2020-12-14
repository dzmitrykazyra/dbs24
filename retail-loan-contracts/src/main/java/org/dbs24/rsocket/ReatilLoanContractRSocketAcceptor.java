/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rsocket;

import io.rsocket.ConnectionSetupPayload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Log4j2
@Deprecated
public class ReatilLoanContractRSocketAcceptor extends AbstractApplicationBean implements SocketAcceptor {

    final AbstractRSocket abstractRSocket;

    public ReatilLoanContractRSocketAcceptor(AbstractRSocket abstractRSocket) {
        this.abstractRSocket = abstractRSocket;
    }

    @Override
    public Mono<RSocket> accept(ConnectionSetupPayload connectionSetupPayload, RSocket rSocket) {
        return Mono.just(abstractRSocket);
    }
}
