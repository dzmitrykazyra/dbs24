/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rsocket;

import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketServer;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.server.TcpServerTransport;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import reactor.core.Disposable;
import org.dbs24.spring.core.api.AbstractApplicationBean;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.util.Assert;

@Log4j2
public abstract class AbstractRSocketServer extends AbstractApplicationBean {

    private Disposable disposable;
    private String serverName;
    private Integer rSocketPort;
    private RSocketServer rSocketServer;

    //==========================================================================
    @Deprecated
    public void start(Class<? extends SocketAcceptor> socketAcceptorClass, Integer rSocketPort, String rsocketMappingPath) {

        log.error("Execute deprecated method (start)");

        this.rSocketServer = RSocketServer.create();

//        log.debug("starting RSocketServer ({}{}, {})",
//                rSocketPort,
//                rsocketMappingPath,
//                socketAcceptorClass.getSimpleName());
        Assert.notNull(rSocketServer, String.format("%s: fucking RSocketServer is null!",
                socketAcceptorClass.getCanonicalName()));

        rSocketServer.acceptor(StmtProcessor.create(socketAcceptorClass));
        rSocketServer.payloadDecoder(PayloadDecoder.ZERO_COPY);
        disposable = rSocketServer.bind(TcpServerTransport.create(rSocketPort))
                .subscribe();

        Assert.notNull(disposable, String.format("%s:%d : fucking binding RSocketServer is null!",
                socketAcceptorClass.getCanonicalName(),
                rSocketPort));

        this.serverName = socketAcceptorClass.getSimpleName();
        this.rSocketPort = rSocketPort;

        log.info("RSocketServer is ready ({}{}, {})",
                rSocketPort,
                rsocketMappingPath,
                socketAcceptorClass.getSimpleName());
    }

    //==========================================================================
    public void start(SocketAcceptor socketAcceptor, Integer rSocketPort, String rsocketMappingPath) {

        this.rSocketServer = RSocketServer.create();

//        log.debug("starting RSocketServer ({}{}, {})",
//                rSocketPort,
//                rsocketMappingPath,
//                socketAcceptorClass.getSimpleName());
        Assert.notNull(rSocketServer, String.format("%s: fucking RSocketServer is null!",
                socketAcceptor.getClass().getCanonicalName()));

        rSocketServer.acceptor(socketAcceptor);
        rSocketServer.payloadDecoder(PayloadDecoder.ZERO_COPY);

        disposable = rSocketServer.bind(TcpServerTransport.create(rSocketPort))
                .subscribe();

        Assert.notNull(disposable, String.format("%s:%d : fucking binding RSocketServer is null!",
                socketAcceptor.getClass().getCanonicalName(),
                rSocketPort));

        this.serverName = socketAcceptor.getClass().getSimpleName();
        this.rSocketPort = rSocketPort;

        log.info("RSocketServer is ready ({}{}, {})",
                rSocketPort,
                rsocketMappingPath,
                socketAcceptor.getClass().getSimpleName());
    }

    //==========================================================================
    protected void stop() {

        Optional.ofNullable(disposable)
                .ifPresent(server -> {

                    //rSocketServer.
                    server.dispose();
                    if (server.isDisposed()) {

                        log.info("RSocketServer is shutting down ({}, {})",
                                serverName,
                                rSocketPort
                        );

                        disposable = null;
                    }
                });
    }

    //==========================================================================
    @Override
    public void destroy() {
        this.stop();
    }
}
