/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import io.rsocket.metadata.WellKnownMimeType;
import java.time.Duration;
import lombok.extern.log4j.Log4j2;
import org.dbs24.rsocket.api.AbstractRSocketMessage;
import org.dbs24.rsocket.api.MessageType;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.rsocket.metadata.UsernamePasswordMetadata;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import static org.dbs24.consts.ServicesMonitoringConst.R_SOCKET_URI_MONITORING;

@Primary
@Service
@Log4j2
public class MonitoringRSocketService extends AbstractRSocketService {

    @Value("${spring.rsocket.server.monitoring.enabled:false}")
    protected Boolean enabledMonitoring;

    @Value("${spring.rsocket.server.monitoring.address:127.0.0.1}")
    protected String rSocketAddr;

    @Value("${spring.rsocket.server.monitoring.port:6666}")
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
    final PasswordEncoder passwordEncoder;
    final UsernamePasswordMetadata usernamePasswordMetadata;
    final MimeType mimeType = MimeTypeUtils.parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString());

    //==========================================================================    
    @Autowired
    public MonitoringRSocketService(GenericApplicationContext genericApplicationContext,
            PasswordEncoder passwordEncoder) {
        this.genericApplicationContext = genericApplicationContext;
        this.passwordEncoder = passwordEncoder;
        this.usernamePasswordMetadata = new UsernamePasswordMetadata(uid, pwd);
    }

    public void send(MessageType messageType, String message) {

        if (enabledMonitoring) {

            //SocketAcceptor socketAcceptor = RSocketMessageHandler.responder(rsocketStrategies, new ClientHandler());
            log.debug(" uid/pwd {}/{} ", uid, pwd);

            StmtProcessor.execute(() -> getRSocketRequester(rSocketAddr, rSocketPort, builder -> builder
                    .setupMetadata(usernamePasswordMetadata, mimeType)
                    .setupRoute(rSocketRoute)
            //.rsocketConnector(connector -> connector.acceptor(rSocketMessageHandler.responder()));
            )
                    .map(r -> r.route(rSocketRoute)
                    .metadata(usernamePasswordMetadata, mimeType)
                    .data(MonitoringMessage.create(MonitoringMessage.class, messageType, String.format("%s: %s", genericApplicationContext.getDisplayName(), message))))
                    .block(Duration.ofMillis(blockDelay))
                    .send()
                    .subscribe());
        }
    }
}
