package org.dbs24.tik.mobile.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.mobile.entity.dto.user.*;
import org.dbs24.tik.mobile.service.TokenHolder;
import org.dbs24.tik.mobile.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class UserRest {

    private final UserService userService;
    private final TokenHolder tokenHolder;

    public UserRest(UserService userService, TokenHolder tokenHolder) {

        this.userService = userService;
        this.tokenHolder = tokenHolder;
    }

    @ResponseStatus
    public Mono<ServerResponse> register(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.register(request.bodyToMono(UserDto.class)),
                        TokenDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> login(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.login(request.bodyToMono(UserDto.class)),
                        TokenDto.class
                );
    }

    public Mono<ServerResponse> logout(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.logout(tokenHolder.extractJwtFromServerRequest(request)),
                        UserVerificationDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> isUserEmailBounded(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.checkUserEmailExistence(tokenHolder.extractJwtFromServerRequest(request)),
                        UserEmailDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> boundEmailRequest(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.boundEmailRequest(request.bodyToMono(UserEmailBoundingDto.class)),
                        UserEmailBoundingKeysetDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> boundEmail(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.activateBoundedEmail(request.bodyToMono(UserEmailBoundingKeysetDto.class)),
                        TokenDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> forgotPassword(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.forgotPassword(request.bodyToMono(UserEmailDto.class)),
                        UserForgottenPasswordKeysetDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> changeForgottenPassword(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.changeForgottenPassword(request.bodyToMono(UserForgottenPasswordKeysetDto.class)),
                        TokenDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> refreshToken(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.refreshToken(request.bodyToMono(TokenDto.class)),
                        TokenDto.class
                );
    }
}
