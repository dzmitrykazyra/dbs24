package org.dbs24.repository;

import org.dbs24.entity.AppSettings;
import org.dbs24.entity.TariffPlan;
import org.dbs24.spring.core.data.ApplicationJpaRepository;

import java.util.Optional;

public interface AppSettingsRepository  extends ApplicationJpaRepository<AppSettings, Integer> {

    Optional<AppSettings> findByPackageName(String packageName);

}
