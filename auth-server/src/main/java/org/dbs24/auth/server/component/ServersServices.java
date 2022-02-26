package org.dbs24.auth.server.component;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.auth.server.consts.AuthConsts.ServerEventEnum;
import org.dbs24.auth.server.dao.ServerDao;
import org.dbs24.auth.server.dao.ServerEventDao;
import org.dbs24.auth.server.entity.Event;
import org.dbs24.auth.server.entity.Server;
import org.dbs24.impl.api.ServerState;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.function.BiFunction;

import static java.time.LocalDateTime.now;
import static org.dbs24.application.core.locale.NLS.long2LocalDateTime;
import static org.dbs24.auth.server.consts.AuthConsts.Applications.WA_MONITORING;
import static org.dbs24.auth.server.consts.AuthConsts.ServerEventEnum.*;
import static org.dbs24.auth.server.consts.AuthConsts.ServerStatusEnum.SS_ACTIVE;
import static org.dbs24.auth.server.consts.AuthConsts.ServerStatusEnum.SS_CLOSED;
import static org.dbs24.consts.SysConst.INTEGER_ZERO;
import static org.dbs24.impl.api.MultiApiConsts.Consumers.ATTEMPTS_RETRY;
import static org.dbs24.stmt.StmtProcessor.*;

@Log4j2
@Component
@Getter
public class ServersServices extends AbstractApplicationService {

    final ServerDao serverDao;
    final ServerEventDao serverEventDao;
    final RefsService refsService;

    public ServersServices(ServerDao serverDao, RefsService refsService, ServerEventDao serverEventDao) {
        this.serverDao = serverDao;
        this.refsService = refsService;
        this.serverEventDao = serverEventDao;
    }

    final BiFunction<ServerState, Server, Server> st2s = (serverState, server) -> {
        server.setServerAddress(serverState.getAddress());
        server.setPid(serverState.getPid());
        server.setActualDate(now());
        server.setServerStatus(getRefsService().findServerStatus(SS_ACTIVE));
        server.setApplication(getRefsService().findApplication(WA_MONITORING));
        server.setUserCapacity(serverState.getUserCapacity());
        server.setCountryCode(serverState.getCountryCode());
        server.setRegistryDate(now());
        server.setDeadlineDate(long2LocalDateTime(serverState.getRebootDeadLine()));
        server.setRebootDate(long2LocalDateTime(serverState.getNextReboot()));

        return server;
    };

    @Transactional
    public void createOrUpdateServer(ServerState serverState, String note) {

        //log.info("{}: {}", serverState.getAddress(), serverState);

        final Server server = findOrCreateServer(serverState.getAddress(), serverState.getPid());
        final Boolean isNewServer = isNull(server.getServerId());

        serverDao.saveServerHist(server);
        serverDao.saveServer(st2s.apply(serverState, server));

        final ServerEventEnum serverEventEnum = isNewServer ? SE_REGISTERED :
                serverState.getIsShoutDown() ? SE_SHUTDOWN :
                        serverState.getAttemptRetry().compareTo(INTEGER_ZERO) <= INTEGER_ZERO ? SE_CONNETION_TIME_OUT :
                                !serverState.getAttemptRetry().equals(ATTEMPTS_RETRY) ? SE_CONNETION_ERROR :
                                        null;

        // register event
        ifNotNull(serverEventEnum, () -> {

            ifTrue(CONNECT_TIME_OUT.test(serverEventEnum) || SHUTDOWN.test(serverEventEnum),
                    () -> {
                        server.setServerStatus(getRefsService().findServerStatus(SS_CLOSED));
                        server.setCloseDate(now());
                    });

            registerServerEvent(server, serverEventEnum, note);
        });
    }

    public Server findOrCreateServer(String serverAddress, Long pid) {
        return serverDao.findOrCreateServer(serverAddress, pid);
    }

    public void registerServerEvent(Server server, ServerEventEnum serverEventEnum, String note) {

        serverEventDao.saveEvent(create(Event.class, event -> {
            event.setServerId(server.getServerId());
            event.setServerEvent(refsService.findServerEvent(serverEventEnum));
            event.setServerEventDate(now());
            event.setNote(note);

        }));
    }

    public Collection<Server> loadActualServers() {
        return serverDao.loadActualServers();
    }
}
