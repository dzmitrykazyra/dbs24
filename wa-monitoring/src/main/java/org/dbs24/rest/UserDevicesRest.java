/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.auth.server.api.WaUserSessionInfo;
import org.dbs24.component.UserDevicesService;
import org.dbs24.entity.dto.RemoveDeviceResult;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.dbs24.rest.api.UpdateDeviceResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.dbs24.consts.WaConsts.RestQueryParams.QP_DEVICE_UID;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserDevicesRest extends ReactiveRestProcessor {

    final UserDevicesService userDevicesService;

    public UserDevicesRest(UserDevicesService userDevicesService) {
        this.userDevicesService = userDevicesService;
    }

    //==========================================================================
    @Deprecated
    public Mono<ServerResponse> updateDeviceAttrs(ServerRequest request) {

        return this.<WaUserSessionInfo, UpdateDeviceResult>createResponse(request, WaUserSessionInfo.class, UpdateDeviceResult.class, userDevicesService::updateDeviceAttrs);
    }

    //==========================================================================
    public Mono<ServerResponse> removeDevicebyId(ServerRequest request) {

        return this.<RemoveDeviceResult>createResponse(request, RemoveDeviceResult.class, () -> userDevicesService.removeDevicebyId(getStringFromParam(request, QP_DEVICE_UID)));
    }
}
