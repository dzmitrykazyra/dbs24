package org.dbs24.tik.mobile.rest;

import org.dbs24.tik.mobile.constant.RequestQueryParam;
import org.dbs24.tik.mobile.entity.dto.device.DeviceIdResponseDto;
import org.dbs24.tik.mobile.entity.dto.device.UpdateDeviceAttributesRequestDto;
import org.dbs24.tik.mobile.entity.dto.device.UserDeviceListDto;
import org.dbs24.tik.mobile.service.TokenHolder;
import org.dbs24.tik.mobile.service.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserDeviceRest {

    private final UserDeviceService userDeviceService;
    private final TokenHolder tokenHolder;

    @Autowired
    public UserDeviceRest(UserDeviceService userDeviceService, TokenHolder tokenHolder) {
        this.userDeviceService = userDeviceService;
        this.tokenHolder = tokenHolder;
    }

    public Mono<ServerResponse> findAllUserDevices(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userDeviceService.getAllUserDevices(tokenHolder.extractUserIdFromServerRequest(request)),
                        UserDeviceListDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> removeDeviceById(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userDeviceService.removeDeviceById(request.queryParam(RequestQueryParam.QP_DEVICE_UID).get()),
                        DeviceIdResponseDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> createOrUpdateDeviceAttributes(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userDeviceService.createOrUpdateDeviceAttributes(
                                request.bodyToMono(UpdateDeviceAttributesRequestDto.class),
                                tokenHolder.extractUserIdFromServerRequest(request)
                        ),
                        DeviceIdResponseDto.class
                );
    }


}
