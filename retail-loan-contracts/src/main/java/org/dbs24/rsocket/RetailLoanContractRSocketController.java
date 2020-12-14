/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rsocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.dbs24.entity.*;
import static org.dbs24.consts.RetailLoanContractConst.*;
import lombok.extern.log4j.Log4j2;
import org.dbs24.rsocket.AbstractEntityRSocketController;
import org.dbs24.entity.reactor.RetailLoanContractReactor;
import java.util.Map;
import java.util.Collection;
import org.dbs24.service.MonitoringRSocketService;

@Controller
@Log4j2
public class RetailLoanContractRSocketController extends AbstractEntityRSocketController {

    @Autowired
    RetailLoanContractReactor retailLoanContractReactor;

    @MessageMapping(R_RETAIL_LOAN_CONTRACT)
    public void createRetailLoanContractContract(@Payload Mono<Collection<RetailLoanContract>> retailContracts, @Headers Map<String, Object> metadata) {

        log.debug("createRetailLoanContractContract {} ", retailContracts);

        log.debug("metadata {} ", metadata);

        retailContracts
                .doOnNext(t -> t.forEach(entity -> entity.setMetaData(metadata)))
                .doOnError(e -> log.error("log.error = {}", e.getLocalizedMessage()))
                .subscribe(retailLoanContractReactor);

    }
}
