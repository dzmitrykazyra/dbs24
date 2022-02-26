package org.dbs24.auth.server.repo;

import org.dbs24.auth.server.entity.Event;
import org.dbs24.spring.core.data.ApplicationJpaRepository;

public interface EventRepo extends ApplicationJpaRepository<Event, Integer> {

}
