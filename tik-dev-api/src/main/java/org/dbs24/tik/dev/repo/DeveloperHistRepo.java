package org.dbs24.tik.dev.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.dev.entity.DeveloperHist;
import org.dbs24.tik.dev.entity.DeveloperHistPK;

public interface DeveloperHistRepo extends ApplicationJpaRepository<DeveloperHist, DeveloperHistPK> {

}
