/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rsocket;

import io.rsocket.Payload;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import io.rsocket.Payload;
import io.rsocket.util.DefaultPayload;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.dbs24.consts.RetailLoanContractConst.*;
import org.dbs24.entity.RetailLoanContract;
import org.dbs24.service.AbstractRSocketService;
import org.dbs24.service.RetailLoanContractActionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@Data
@Deprecated
public class RetailLoanContractRSocketProcessor extends AbstractRSocketProcessor {

    @Autowired
    private AbstractRSocketService rSocketService;

    public Mono<Void> createContract(Payload payload) {

        // just print the received string
        final String str = payload.getDataUtf8();

        //log.debug("retailLoanContractActionsService = {}", retailLoanContractActionsService);
        final RetailLoanContract retailLoanContract
                = this.getRSocketService().<RetailLoanContract>fromPayload(payload, RETAIL_LOAN_CONTRACT_CLASS);

        // обработка сущности из запроса
//        this.retailLoanContractActionsService.executeAction(
//                retailLoanContract,
//                MODIFY_INDIVIDUAL_LOAN_CONTRACT,
//                request.queryParams());
        log.debug("Received entity for create/update {}", retailLoanContract.getClass().getSimpleName());

        return Mono.empty();

    }

    public Mono<Payload> requestResponse(Payload payload) {
        // just convert to upper case
        final String str = payload.getDataUtf8();
        return Mono.just(DefaultPayload.create(str.toUpperCase()));
    }

    public Flux<Payload> requestStream(Payload payload) {
        // convert the given str to char array and return
        final String str = payload.getDataUtf8();
        return Flux.fromStream(str.chars().mapToObj(i -> (char) i))
                .map(Object::toString)
                .map(DefaultPayload::create);
    }

}
