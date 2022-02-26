/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.component.ActivityReactor;
import org.dbs24.component.UserSubscriptionsService;
import org.dbs24.entity.dto.PhoneNumLoginTokenDto;
import org.dbs24.rest.api.*;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.dbs24.consts.WaConsts.Classes.ACTIVITY_INFO_CLASS;
import static org.dbs24.consts.WaConsts.Classes.CREATED_ACTIVITY_CLASS;
import static org.dbs24.consts.WaConsts.RestQueryParams.*;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
@EqualsAndHashCode(callSuper = true)
public class ActivitiesRest extends ReactiveRestProcessor {

    final ActivityReactor activityReactor;
    final UserSubscriptionsService userSubscriptionsService;

    @Value("${config.migration.use-ora-id:true}")
    private Boolean userOraId;

    public ActivitiesRest(ActivityReactor activityReactor, UserSubscriptionsService userSubscriptionsService) {
        this.activityReactor = activityReactor;
        this.userSubscriptionsService = userSubscriptionsService;
    }

    private Integer defaultSubscriptionId;

    @Deprecated
    public Mono<ServerResponse> createActivities(ServerRequest request) {

        return this.<ActivityInfo, CreatedActivity>createResponse(
                request,
                ACTIVITY_INFO_CLASS,
                CreatedActivity.class,
                activityInfo -> StmtProcessor.create(
                        CREATED_ACTIVITY_CLASS,
                        createdActivity -> activityReactor.createActivities(activityInfo, userOraId)));
    }

    public Mono<ServerResponse> getActivitiesD1D2(ServerRequest request) {

        LocalDateTime d1 = getLocalDateTimeFromLongParam(request, QP_D1);
        LocalDateTime d2 = getLocalDateTimeFromLongParam(request, QP_D2);

        final PhoneNumLoginTokenDto phoneNumLoginTokenDto = new PhoneNumLoginTokenDto(
                this.getStringFromParam(request, QP_PHONE).replaceAll("[^\\d.]", ""),
                this.getStringFromParam(request, QP_LOGIN_TOKEN)
        );

        return this.<ActivityInfo>createResponse(
                request,
                ActivityInfo.class,
                () -> StmtProcessor.create(
                        ACTIVITY_INFO_CLASS,
                        createdActivity
                                -> createdActivity.setRecords(
                                activityReactor
                                        .getActivitiesD1D2(d1, d2, phoneNumLoginTokenDto)
                                        .getRecords()))
        );
    }

    public Mono<ServerResponse> getLatestActivities(ServerRequest request) {

        PhoneNumLoginTokenDto phoneNumLoginTokenDto = new PhoneNumLoginTokenDto(
                this.getStringFromParam(request, QP_PHONE).replaceAll("[^\\d.]", ""),
                this.getStringFromParam(request, QP_LOGIN_TOKEN)
        );

        return this.<LastSessionInfo>createResponse(
                request, LastSessionInfo.class, () -> activityReactor.getLatestActivities(phoneNumLoginTokenDto));
    }

    public Mono<ServerResponse> getSubscriptionsLatestActivities(ServerRequest request) {

        return this.<SubscriptionsSessions>createResponse(
                request, SubscriptionsSessions.class, () -> activityReactor.getSubscriptionsLatestActivities(getStringFromParam(request, QP_LOGIN_TOKEN)));
    }
}
