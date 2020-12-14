/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.reactor;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.dbs24.service.MonitoringMessage;
import static org.dbs24.rsocket.api.MessageType.*;

@Component
@Log4j2
public class MonitoringReactor extends SimpleSubscriber<MonitoringMessage> {

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
