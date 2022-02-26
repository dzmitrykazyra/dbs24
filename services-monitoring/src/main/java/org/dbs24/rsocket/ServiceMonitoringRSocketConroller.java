/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rsocket;

import org.dbs24.spring.condition.DisabledCondition;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.dbs24.controller.AbstractRSocketController;
import org.dbs24.reactor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.dbs24.api.MonitoringMessage;
import static org.dbs24.consts.ServicesMonitoringConst.R_SOCKET_URI_MONITORING;
import org.springframework.context.annotation.Conditional;

@Log4j2
@Controller
public class ServiceMonitoringRSocketConroller extends AbstractRSocketController {

    final MonitoringReactor reactor;

    @Autowired
    public ServiceMonitoringRSocketConroller(MonitoringReactor reactor) {
        this.reactor = reactor;
    }

    @PreAuthorize("hasRole('SMROLE')")
    @MessageMapping(R_SOCKET_URI_MONITORING)
    public void logInfoMsg(@Payload MonitoringMessage monitoringMessage,
            @Headers Map<String, Object> metadata,
            @AuthenticationPrincipal UserDetails user) {

        Mono.just(monitoringMessage).subscribe(reactor);
    }
}
