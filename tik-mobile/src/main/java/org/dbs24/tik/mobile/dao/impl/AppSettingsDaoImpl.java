package org.dbs24.tik.mobile.dao.impl;

import org.dbs24.tik.mobile.dao.AppSettingsDao;
import org.dbs24.tik.mobile.entity.domain.AppSettings;
import org.dbs24.tik.mobile.entity.domain.AppSettingsHist;
import org.dbs24.tik.mobile.repo.AppSettingsHistRepo;
import org.dbs24.tik.mobile.repo.AppSettingsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class AppSettingsDaoImpl implements AppSettingsDao {

    private final AppSettingsRepo appSettingsRepo;
    private final AppSettingsHistRepo appSettingsHistRepo;

    @Autowired
    public AppSettingsDaoImpl(AppSettingsRepo appSettingsRepo, AppSettingsHistRepo appSettingsHistRepo) {
        this.appSettingsRepo = appSettingsRepo;
        this.appSettingsHistRepo = appSettingsHistRepo;
    }

    @Override
    public Optional<AppSettings> findSettingsByPackageName(String packageName) {

        return appSettingsRepo.findByPackageName(packageName);
    }

    @Override
    public AppSettings save(AppSettings appSettings) {
        return appSettingsRepo.save(appSettings);
    }

    @Override
    public AppSettings update(AppSettings appSettings) {
        appSettings.setActualDate(LocalDateTime.now());

        return save(appSettings);
    }

    @Override
    public void saveAppSettingsHist(AppSettings appSettings) {

        appSettingsHistRepo.save(AppSettingsHist.toHist(appSettings));
    }

}
