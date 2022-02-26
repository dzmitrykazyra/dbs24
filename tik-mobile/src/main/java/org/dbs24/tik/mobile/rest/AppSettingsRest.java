package org.dbs24.tik.mobile.rest;

import org.dbs24.tik.mobile.entity.dto.settings.AppSettingsRequestDto;
import org.dbs24.tik.mobile.entity.dto.settings.AppSettingsResponseDto;
import org.dbs24.tik.mobile.entity.dto.settings.PackageNameDto;
import org.dbs24.tik.mobile.service.AppSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.dbs24.tik.mobile.constant.RequestQueryParam.QP_PACKAGE_NAME;

@Component
public class AppSettingsRest {

    private final AppSettingsService appSettingsService;

    @Autowired
    public AppSettingsRest(AppSettingsService appSettingsService) {
        this.appSettingsService = appSettingsService;
    }

    @ResponseStatus
    public Mono<ServerResponse> getSettingsByPackageName(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        appSettingsService.findAppSettingsByPackageName(request.queryParam(QP_PACKAGE_NAME).get()),
                        AppSettingsResponseDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> createOrUpdateAppSettings(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        appSettingsService.createOrUpdateAppSettings(request.bodyToMono(AppSettingsRequestDto.class)),
                        PackageNameDto.class
                );
    }
}