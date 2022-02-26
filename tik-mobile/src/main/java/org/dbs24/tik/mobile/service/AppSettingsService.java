package org.dbs24.tik.mobile.service;

import org.dbs24.tik.mobile.entity.dto.settings.AppSettingsRequestDto;
import org.dbs24.tik.mobile.entity.dto.settings.AppSettingsResponseDto;
import org.dbs24.tik.mobile.entity.dto.settings.PackageNameDto;
import reactor.core.publisher.Mono;

public interface AppSettingsService {

    Mono<AppSettingsResponseDto> findAppSettingsByPackageName(String packageName);

    Mono<PackageNameDto> createOrUpdateAppSettings(Mono<AppSettingsRequestDto> appSettingsRequestMono);
}
