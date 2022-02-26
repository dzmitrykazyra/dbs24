/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.insta.tmp.component.PostsService;
import org.dbs24.insta.tmp.rest.api.PostInfo;
import org.dbs24.insta.tmp.rest.api.CreatedPost;
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
public class PostsRest extends ReactiveRestProcessor {

    final PostsService postsService;

    public PostsRest(PostsService postsService) {
        this.postsService = postsService;
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdatePost(ServerRequest request) {

        return this.<PostInfo, CreatedPost>processServerRequest(request, PostInfo.class,
                postInfo -> postsService.createOrUpdatePost(postInfo));
    }
}
