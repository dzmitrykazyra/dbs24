/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.insta.tmp.component.FacesService;
import org.dbs24.insta.tmp.rest.api.CreatedFace;
import org.dbs24.insta.tmp.rest.api.FaceInfo;
import org.dbs24.rest.api.ReactiveRestProcessor;
import static org.dbs24.rest.api.ReactiveRestProcessor.httpOk;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-tmp")
public class FacesRest extends ReactiveRestProcessor {

    final FacesService facesService;

    public FacesRest(FacesService facesService) {
        this.facesService = facesService;
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdateFace(ServerRequest request) {

        return this.<FaceInfo, CreatedFace>processServerRequest(request, FaceInfo.class,
                faceInfo -> facesService.createOrUpdateFace(faceInfo));
    }
}
