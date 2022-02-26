/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.reactor;

import org.dbs24.reactor.AbstractSubscriber;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.dbs24.api.MonitoringMessage;
import static org.dbs24.rsocket.api.MessageType.*;
import org.dbs24.spring.condition.DisabledCondition;
import org.springframework.context.annotation.Conditional;

@Component
@Log4j2
//@Conditional(DisabledCondition.class)
public class MonitoringReactor extends AbstractSubscriber<MonitoringMessage> {

    @Override
    public void onNext(MonitoringMessage s) {

        switch (s.getMessageType()) {
            case MONITORING_ABSTRACT_ERROR:
            case MONITORING_ERROR_STARTING_APPLICATION:
                log.error("{}: {}", s.getMessageType(), s.getBody());
                break;
            default:
                log.info("{}: {}", s.getMessageType(), s.getBody());
                break;
        }
    }
}
