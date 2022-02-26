package org.dbs24.tik.dev.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.dev.entity.DeviceHist;
import org.dbs24.tik.dev.entity.DeviceHistPK;

public interface DeviceHistRepo extends ApplicationJpaRepository<DeviceHist, DeviceHistPK> {

}
