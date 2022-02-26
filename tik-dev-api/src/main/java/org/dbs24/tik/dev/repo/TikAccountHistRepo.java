package org.dbs24.tik.dev.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.dev.entity.TikAccountHist;
import org.dbs24.tik.dev.entity.TikAccountHistPK;

public interface TikAccountHistRepo extends ApplicationJpaRepository<TikAccountHist, TikAccountHistPK> {

}
