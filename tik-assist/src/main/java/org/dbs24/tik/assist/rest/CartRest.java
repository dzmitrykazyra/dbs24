package org.dbs24.tik.assist.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.entity.dto.cart.CartOrderDto;
import org.dbs24.tik.assist.entity.dto.cart.CreatedCartOrderDto;
import org.dbs24.tik.assist.service.hierarchy.CartService;
import org.dbs24.tik.assist.service.security.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class CartRest {

    private final AuthenticationService authenticationService;
    private final CartService cartService;

    public CartRest(AuthenticationService authenticationService, CartService cartService) {

        this.authenticationService = authenticationService;
        this.cartService = cartService;
    }

    public Mono<ServerResponse> createAllHierarchyEntities(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        cartService.createAllHierarchyEntities(
                                authenticationService.extractUserIdFromServerRequest(request),
                                request.bodyToMono(CartOrderDto.class)
                        ),
                        CreatedCartOrderDto.class
                );
    }
}
