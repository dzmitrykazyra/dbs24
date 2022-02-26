/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.dbs24.tik.assist.entity.dto.order.*;
import org.dbs24.tik.assist.service.hierarchy.UserOrderService;
import org.dbs24.tik.assist.service.security.AuthenticationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "tik-assist")
public class OrderRest extends ReactiveRestProcessor {

    private final UserOrderService userOrderService;
    private final AuthenticationService authenticationService;

    public OrderRest(UserOrderService userOrderService, AuthenticationService authenticationService) {

        this.userOrderService = userOrderService;
        this.authenticationService = authenticationService;
    }

    @ResponseStatus
    public Mono<ServerResponse> createOrUpdateOrder(ServerRequest request) {

        return this.<UserOrderDto, CreatedUserOrderDto>createResponse(
                request,
                UserOrderDto.class,
                CreatedUserOrderDto.class,
                userOrderService::createOrUpdateOrder);
    }

    @ResponseStatus
    public Mono<ServerResponse> createLikesOrder(ServerRequest request) {

        return this.<CreateVideoOrderDto, CreatedUserOrderDto>createResponse(
                request,
                CreateVideoOrderDto.class,
                CreatedUserOrderDto.class,
                createLikesOrderDto -> userOrderService.createLikesOrder(
                        authenticationService.extractUserIdFromServerRequest(request),
                        createLikesOrderDto
                ));
    }

    @ResponseStatus
    public Mono<ServerResponse> createFollowersOrder(ServerRequest request) {

        return this.<CreateFollowersOrderDto, CreatedUserOrderDto>createResponse(
                request,
                CreateFollowersOrderDto.class,
                CreatedUserOrderDto.class,
                createFollowersOrder -> userOrderService.createFollowersOrder(
                        authenticationService.extractUserIdFromServerRequest(request),
                        createFollowersOrder
                ));
    }

    public Mono<ServerResponse> createViewsOrder(ServerRequest request) {

        return this.<CreateVideoOrderDto, CreatedUserOrderDto>createResponse(
                request,
                CreateVideoOrderDto.class,
                CreatedUserOrderDto.class,
                createViewsOrderDto -> userOrderService.createViewsOrder(
                        authenticationService.extractUserIdFromServerRequest(request),
                        createViewsOrderDto
                ));
    }

    @ResponseStatus
    public Mono<ServerResponse> verifyFollowersOrder(ServerRequest request) {

        return this.<CreateFollowersOrderDto, OrderValidityDto>createResponse(
                request,
                CreateFollowersOrderDto.class,
                OrderValidityDto.class,
                createFollowersOrder -> userOrderService.verifyFollowersOrder(
                        authenticationService.extractUserIdFromServerRequest(request),
                        createFollowersOrder
                ));
    }

    @ResponseStatus
    public Mono<ServerResponse> verifyLikesOrder(ServerRequest request) {

        return this.<CreateVideoOrderDto, OrderValidityDto>createResponse(
                request,
                CreateVideoOrderDto.class,
                OrderValidityDto.class,
                verifyLikesOrderDto -> userOrderService.verifyVideoOrder(
                        authenticationService.extractUserIdFromServerRequest(request),
                        verifyLikesOrderDto
                ));
    }

    @ResponseStatus
    public Mono<ServerResponse> verifyViewsOrder(ServerRequest request) {

        return this.<CreateVideoOrderDto, OrderValidityDto>createResponse(
                request,
                CreateVideoOrderDto.class,
                OrderValidityDto.class,
                verifyViewsOrderDto -> userOrderService.verifyVideoOrder(
                        authenticationService.extractUserIdFromServerRequest(request),
                        verifyViewsOrderDto
                ));
    }
}
