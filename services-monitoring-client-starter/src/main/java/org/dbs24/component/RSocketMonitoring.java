/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import io.rsocket.metadata.WellKnownMimeType;
import java.time.Duration;
import org.dbs24.rsocket.api.MessageType;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.dbs24.api.MonitoringMessage;
import static org.dbs24.consts.ServicesMonitoringConst.R_SOCKET_URI_MONITORING;
import org.dbs24.service.AbstractRSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.security.rsocket.metadata.UsernamePasswordMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

@Component
@Log4j2
//@Qualifier(R_QUALIFIER_ABSTRACT_CLIENT)
public class RSocketMonitoring extends AbstractRSocketService<Map<MessageType, String>> {

    @Override
    protected void processEvent(Map<MessageType, String> msg) {

        if (enabledMonitoring) {

            log.debug("RSocket msg 2 {}:{}/{} [{}]", rSocketAddr, rSocketPort, rSocketRoute, msg.values().iterator().next());

            //SocketAcceptor socketAcceptor = RSocketMessageHandler.responder(rsocketStrategies, new ClientHandler());
            //log.debug(" uid/pwd {}/{} ", uid, pwd);
            getRSocketRequester(rSocketAddr, rSocketPort, builder -> builder
                    .setupMetadata(usernamePasswordMetadata, mimeType)
                    .setupRoute(rSocketRoute)
            //.rsocketConnector(connector -> connector.acceptor(rSocketMessageHandler.responder()));
            )
                    .map(r -> r.route(rSocketRoute)
                    .metadata(usernamePasswordMetadata, mimeType)
                    .data(MonitoringMessage.create(MonitoringMessage.class, msg.keySet().iterator().next(),
                            String.format("%s: %s", genericApplicationContext.getDisplayName(), msg.values().iterator().next()))))
                    .block(Duration.ofMillis(blockDelay))
                    .send()
                    .subscribe();

        } else {
            log.warn("Services monitoring is not available ({}:{})", rSocketAddr, rSocketPort);
        }

    }

    @Value("${config.monitoring.enabled:false}")
    protected Boolean enabledMonitoring;

    @Value("${config.monitoring.address:127.0.0.1}")
    protected String rSocketAddr;

    @Value("${config.monitoring.port:6666}")
    protected Integer rSocketPort;

    @Value(R_SOCKET_URI_MONITORING)
    protected String rSocketRoute;

    @Value("${spring.rsocket.server.monitoring.send.block.delay:6000}") // ms
    protected Integer blockDelay;

    @Value("${sm.uid:smuser}")
    private String uid = "smuser";

    @Value("${sm.pwd:smpwd}")
    private String pwd = "smpwd";

    final GenericApplicationContext genericApplicationContext;
    final UsernamePasswordMetadata usernamePasswordMetadata;
    final MimeType mimeType = MimeTypeUtils.parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString());

    //==========================================================================    
    @Autowired
    public RSocketMonitoring(GenericApplicationContext genericApplicationContext) {
        this.genericApplicationContext = genericApplicationContext;
        this.usernamePasswordMetadata = new UsernamePasswordMetadata(uid, pwd);
    }
}
