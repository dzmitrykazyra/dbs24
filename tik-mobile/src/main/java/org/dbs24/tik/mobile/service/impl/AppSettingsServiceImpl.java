package org.dbs24.tik.mobile.service.impl;

import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.dao.AppSettingsDao;
import org.dbs24.tik.mobile.entity.domain.AppSettings;
import org.dbs24.tik.mobile.entity.dto.settings.AppSettingsRequestDto;
import org.dbs24.tik.mobile.entity.dto.settings.AppSettingsResponseDto;
import org.dbs24.tik.mobile.entity.dto.settings.PackageNameDto;
import org.dbs24.tik.mobile.service.AppSettingsService;
import org.dbs24.tik.mobile.service.exception.http.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AppSettingsServiceImpl implements AppSettingsService {

    private final AppSettingsDao appSettingsDao;

    @Autowired
    public AppSettingsServiceImpl(AppSettingsDao appSettingsDao) {
        this.appSettingsDao = appSettingsDao;
    }

    @Override
    public Mono<AppSettingsResponseDto> findAppSettingsByPackageName(String packageName) {

        final Optional<AppSettings> appSettingsOptional = appSettingsDao.findSettingsByPackageName(packageName);

        return Mono.just(
                AppSettingsResponseDto.of(
                        appSettingsOptional.orElseThrow(BadRequestException::new)
                )
        );
    }

    @Override
    @Transactional
    public Mono<PackageNameDto> createOrUpdateAppSettings(Mono<AppSettingsRequestDto> appSettingsRequestMono) {

        return appSettingsRequestMono.map(appSettingsDto -> {

            final Optional<AppSettings> appSettingsOptional = appSettingsDao.findSettingsByPackageName(appSettingsDto.getPackageName());

            final AppSettings appSettings = appSettingsOptional.orElse(createAppSettings(appSettingsDto));

            StmtProcessor.ifTrue(appSettings.getAppSettingsId() == null,
                    () -> appSettingsDao.save(appSettings),
                    () -> {
                        appSettingsDao.saveAppSettingsHist(appSettings);
                        appSettingsDao.update(updateAppSettings(appSettings, appSettingsDto));
                    }
            );

            return StmtProcessor.create(PackageNameDto.class, packageNameDto -> {
                packageNameDto.setPackageNameDto(appSettings.getPackageName());
            });
        });
    }

    private AppSettings createAppSettings(AppSettingsRequestDto appSettingsRequestDto) {

        return StmtProcessor.create(AppSettings.class,
                appSettings -> {
                    appSettings.setAppName(appSettingsRequestDto.getAppName());
                    appSettings.setActualDate(LocalDateTime.now());
                    appSettings.setEmail(appSettingsRequestDto.getEmail());
                    appSettings.setCompanyName(appSettingsRequestDto.getCompanyName());
                    appSettings.setMinVersion(appSettingsRequestDto.getMinVersion());
                    appSettings.setMinVersionCode(appSettingsRequestDto.getMinVersionCode());
                    appSettings.setNote(appSettingsRequestDto.getNote());
                    appSettings.setSiteUrl(appSettingsRequestDto.getSiteUrl());
                    appSettings.setWhatsappId(appSettingsRequestDto.getWhatsappId());
                    appSettings.setPackageName(appSettingsRequestDto.getPackageName());
                }
        );
    }

    private AppSettings updateAppSettings(AppSettings appSettings, AppSettingsRequestDto appSettingsRequestDto) {
        appSettings.setAppName(appSettingsRequestDto.getAppName());
        appSettings.setNote(appSettingsRequestDto.getNote());
        appSettings.setWhatsappId(appSettingsRequestDto.getWhatsappId());
        appSettings.setEmail(appSettingsRequestDto.getEmail());
        appSettings.setSiteUrl(appSettingsRequestDto.getSiteUrl());
        appSettings.setMinVersionCode(appSettingsRequestDto.getMinVersionCode());
        appSettings.setMinVersion(appSettingsRequestDto.getMinVersion());
        appSettings.setCompanyName(appSettingsRequestDto.getCompanyName());

        return appSettings;
    }
}
