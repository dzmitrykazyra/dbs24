/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.component.*;
import org.dbs24.entity.DeviceType;
import org.dbs24.rest.api.*;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.dbs24.consts.WaConsts.Classes.USER_ATTRS_INFO_CLASS;
import static org.dbs24.consts.WaConsts.RestQueryParams.*;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UsersRest extends ReactiveRestProcessor {

    final UserContractsService userContractsService;
    final RefsService refsService;
    final UsersService usersService;
    final UserDevicesService userDevicesService;
    final UserBuilderService userBuilderService;

    public UsersRest(UserContractsService userContractsService, RefsService refsService, UsersService usersService, UserDevicesService userDevicesService, UserBuilderService userBuilderService) {
        this.userContractsService = userContractsService;
        this.refsService = refsService;
        this.usersService = usersService;
        this.userDevicesService = userDevicesService;
        this.userBuilderService = userBuilderService;
    }

    public Mono<ServerResponse> createOrUpdateMultiUser(ServerRequest request) {

        return this.<UserAttrsInfo, CreatedUser>createResponse(request, USER_ATTRS_INFO_CLASS, CreatedUser.class, userBuilderService::createOrUpdateMultiUser);
    }

    //==========================================================================
    public Mono<ServerResponse> checkTokenValidity(ServerRequest request) {

        final String loginToken = getStringFromParam(request, QP_LOGIN_TOKEN);

        return this.<LoginTokenInfo>createResponse(request, LoginTokenInfo.class, () -> usersService.checkTokenValidity(loginToken));
    }

    //==========================================================================
    public Mono<ServerResponse> generateTestUsers(ServerRequest request) {

        final Integer amountDef = 10;
        final String phoneMaskDef = "9";
        final Integer activityLimitDef = 8;

        final Integer amount = StmtProcessor.<Integer>nvl(getOptionalIntegerFromParam(request, QP_AMOUNT), amountDef);
        final String phoneMask = StmtProcessor.<String>nvl(getOptionalStringFromParam(request, QP_PHONE_MASK), phoneMaskDef);
        final Integer activityLimit = StmtProcessor.<Integer>nvl(getOptionalIntegerFromParam(request, QP_ACTIVITY_LIMIT), activityLimitDef);


        return this.<UsersAmount>createResponse(request, UsersAmount.class,
                () -> userBuilderService.generateTestUsers(
                        amount.compareTo(0) > 0 ? amount : amountDef,
                        !phoneMask.isEmpty() ? phoneMask : phoneMaskDef,
                        activityLimit.compareTo(0) > 0 ? activityLimit : activityLimit
                ));
    }


    //==========================================================================
    public Mono<ServerResponse> existsDevice(ServerRequest request) {

        final String deviceTypeString = getStringFromParam(request, QP_DEVICE_TYPE);
        final String deviceUid = getStringFromParam(request, QP_DEVICE_UID);

        final DeviceType deviceType = refsService.findDeviceType(Integer.valueOf(deviceTypeString));

        return this.<ExistsDeviceInfo>createResponse(request, ExistsDeviceInfo.class, () -> userDevicesService.existsDevice(deviceType, deviceUid));
    }
}
