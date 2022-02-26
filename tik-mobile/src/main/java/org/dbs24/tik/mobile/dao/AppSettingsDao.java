package org.dbs24.tik.mobile.dao;

import org.dbs24.tik.mobile.entity.domain.AppSettings;

import java.util.Optional;

public interface AppSettingsDao {

    Optional<AppSettings> findSettingsByPackageName(String packageName);

    AppSettings save(AppSettings appSettings);

    AppSettings update(AppSettings appSettings);

    void saveAppSettingsHist(AppSettings appSettings);
}
