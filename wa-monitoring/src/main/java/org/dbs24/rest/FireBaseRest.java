/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.component.FireBaseApplicationService;
import org.dbs24.component.RefsService;
import org.dbs24.rest.api.AllFirebaseApplications;
import org.dbs24.rest.api.CreatedFireBaseApplication;
import org.dbs24.rest.api.FireBaseApplicationInfo;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class FireBaseRest extends ReactiveRestProcessor {

    final RefsService refsService;
    final FireBaseApplicationService fireBaseApplicationService;

    public FireBaseRest(RefsService refsService, FireBaseApplicationService fireBaseApplicationService) {
        this.refsService = refsService;
        this.fireBaseApplicationService = fireBaseApplicationService;
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdateFireBaseApplication(ServerRequest request) {

        return processServerRequest(
                request,
                FireBaseApplicationInfo.class,
                CreatedFireBaseApplication.class,
                fireBaseApplicationService::couFireBaseApplication);
    }

    //==========================================================================
    public Mono<ServerResponse> getAllFirebaseApplications(ServerRequest request) {

        return this.<AllFirebaseApplications>createResponse(request, AllFirebaseApplications.class, fireBaseApplicationService::getAllApplications);

    }
}
