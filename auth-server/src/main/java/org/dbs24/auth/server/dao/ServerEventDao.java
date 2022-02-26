package org.dbs24.auth.server.dao;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.auth.server.entity.Event;
import org.dbs24.auth.server.entity.ServerEvent;
import org.dbs24.auth.server.repo.EventRepo;
import org.dbs24.auth.server.repo.ServerEventRepo;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.springframework.stereotype.Component;

@Getter
@Log4j2
@Component
public class ServerEventDao extends DaoAbstractApplicationService {

    final ServerEventRepo serverEventRepo;
    final EventRepo eventRepo;

    public ServerEventDao(ServerEventRepo serverEventRepo, EventRepo eventRepo) {
        this.serverEventRepo = serverEventRepo;
        this.eventRepo = eventRepo;
    }

    public void saveServerEvent(ServerEvent serverEvent) {
        serverEventRepo.save(serverEvent);
    }

    public void saveEvent(Event event) {
        eventRepo.save(event);
    }

}
