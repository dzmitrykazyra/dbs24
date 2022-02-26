/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.auth.server.dao;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.auth.server.component.RefsService;
import org.dbs24.auth.server.entity.Server;
import org.dbs24.auth.server.entity.ServerHist;
import org.dbs24.auth.server.repo.ServerHistRepo;
import org.dbs24.auth.server.repo.ServerRepo;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Supplier;

import static java.time.LocalDateTime.now;
import static org.dbs24.auth.server.consts.AuthConsts.ServerStatusEnum.SS_ACTIVE;
import static org.dbs24.stmt.StmtProcessor.create;
import static org.dbs24.stmt.StmtProcessor.ifNotNull;

@Getter
@Log4j2
@Component
public class ServerDao extends DaoAbstractApplicationService {

    final ServerRepo serverRepo;
    final ServerHistRepo serverHistRepo;
    final RefsService refsService;

    final Supplier<Server> newServer = () -> StmtProcessor.create(Server.class, server -> {

        server.setActualDate(now());

    });

    public ServerDao(ServerRepo serverRepo, ServerHistRepo serverHistRepo, RefsService refsService) {
        this.serverRepo = serverRepo;
        this.serverHistRepo = serverHistRepo;
        this.refsService = refsService;
    }

    public Server findOrCreateServer(String serverAddress, Long pid) {
        return serverRepo.findByServerAddressAndPid(serverAddress, pid)
                .orElseGet(newServer);
    }

    //==========================================================================
    public void saveServerHist(Server server) {

        ifNotNull(server.getServerId(), serverId -> {

            serverHistRepo.save(create(ServerHist.class, serverHist -> {
                serverHist.setServerId(serverId);
                serverHist.setPid(server.getPid());
                serverHist.setServerStatus(server.getServerStatus());
                serverHist.setServerAddress(server.getServerAddress());
                serverHist.setApplication(server.getApplication());
                serverHist.setActualDate(server.getActualDate());
                serverHist.setCloseDate(server.getCloseDate());
                serverHist.setDeadlineDate(server.getDeadlineDate());
                serverHist.setRebootDate(server.getRebootDate());
                serverHist.setRegistryDate(server.getRegistryDate());
                serverHist.setUserCapacity(server.getUserCapacity());
                serverHist.setCountryCode(server.getCountryCode());

            }));
        });
    }

    public void saveServer(Server server) {
        serverRepo.save(server);
    }

    public Collection<Server> loadActualServers() {
        return serverRepo.findByServerStatus(refsService.findServerStatus(SS_ACTIVE));
    }

}
