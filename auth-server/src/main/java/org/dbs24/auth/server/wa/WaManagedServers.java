package org.dbs24.auth.server.wa;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.auth.server.component.ServersServices;
import org.dbs24.auth.server.entity.Server;
import org.dbs24.impl.api.ServerState;
import org.dbs24.impl.service.ApplicationProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.function.Function;

import static java.time.LocalDateTime.MIN;
import static java.util.stream.Collectors.toList;
import static org.dbs24.application.core.locale.NLS.localDateTime2long;
import static org.dbs24.consts.SysConst.*;
import static org.dbs24.impl.api.MultiApiConsts.Consumers.ATTEMPTS_RETRY;
import static org.dbs24.stmt.StmtProcessor.create;

@Log4j2
@Getter
@Service
@EqualsAndHashCode(callSuper = true)
public class WaManagedServers extends ApplicationProcessor {

    final ServersServices serversServices;

    public WaManagedServers(ServersServices serversServices) {
        this.serversServices = serversServices;
    }

    @Override
    public void registerServer(ServerState serverState) {
        serversServices.createOrUpdateServer(serverState, STRING_NULL);
    }

    @Override
    public void registerServer(ServerState serverState, String note) {
        serversServices.createOrUpdateServer(serverState, note);
    }

    final Function<Server, ServerState> s2s = server -> create(ServerState.class, serverState -> {

        serverState.setAddress(server.getServerAddress());
        serverState.setPid(server.getPid());
        serverState.setUsersProceed(INTEGER_ZERO);
        serverState.setAttemptRetry(ATTEMPTS_RETRY);
        serverState.setIsShoutDown(BOOLEAN_FALSE);
        serverState.setNextReboot(localDateTime2long(server.getRebootDate()));
        serverState.setCountryCode(server.getCountryCode());
        serverState.setLastProceed(MIN);
        serverState.setUserCapacity(server.getUserCapacity());
        serverState.setRebootDeadLine(localDateTime2long(server.getDeadlineDate()));

    });

    @Override
    @Transactional(readOnly = true)
    public Collection<ServerState> initServersList() {

        return serversServices
                .loadActualServers()
                .stream()
                .map(s2s)
                .collect(toList());

    }
}
