/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.fs.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.insta.fs.component.SourcesService;
import org.dbs24.insta.fs.rest.api.CreatedSource;
import org.dbs24.insta.fs.rest.api.SourceInfo;
import org.dbs24.rest.api.ReactiveRestProcessor;
import static org.dbs24.rest.api.ReactiveRestProcessor.httpOk;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-fs")
public class SourcesRest extends ReactiveRestProcessor {

    final SourcesService postsService;

    public SourcesRest(SourcesService postsService) {
        this.postsService = postsService;
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdateSource(ServerRequest request) {

        return this.<SourceInfo, CreatedSource>processServerRequest(request, SourceInfo.class,
                postInfo -> postsService.createOrUpdateSource(postInfo));
    }
}
