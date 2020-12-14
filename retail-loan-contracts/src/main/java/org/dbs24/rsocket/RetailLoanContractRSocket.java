/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rsocket;

import io.rsocket.Payload;

import io.rsocket.util.DefaultPayload;
import io.rsocket.core.RSocketClient;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.dbs24.consts.RetailLoanContractConst.*;
import org.dbs24.entity.RetailLoanContract;
import org.dbs24.service.RetailLoanContractActionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
//import io.rsocket.RSocketFactory;

@Log4j2
@Deprecated
public class RetailLoanContractRSocket extends AbstractRSocket {

    @Autowired
    private RetailLoanContractRSocketProcessor retailLoanContractRSocketProcessor;

    @Override
    public Mono<Void> fireAndForget(Payload payload) {

        return retailLoanContractRSocketProcessor.createContract(payload);
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {

        return retailLoanContractRSocketProcessor.requestResponse(payload);
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        return retailLoanContractRSocketProcessor.requestStream(payload);
    }
}
