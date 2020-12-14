/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rsocket;

import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import static org.dbs24.consts.LiveAccretionsConsts.*;

@Controller
@Log4j2
public class LiveAccretionRSocketController extends AbstractEntityRSocketController {

    @PreAuthorize("hasRole('SMROLE')")
    @MessageMapping(R_SOCKET_URI_CALCULATE_TARIFF)
    public void tariffCalculate(@Payload CalculateTariffsMessage calculateTariffsMessage,
            @Headers Map<String, Object> metadata,
            @AuthenticationPrincipal UserDetails user) {

//        log.info("md = {}", metadata);
//        log.info("user = {}", user);
//        Mono.just(monitoringMessage).subscribe(reactor);
    }

}
