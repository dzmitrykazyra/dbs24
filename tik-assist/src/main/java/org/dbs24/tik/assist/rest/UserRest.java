/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.service.security.AuthenticationService;
import org.dbs24.tik.assist.service.user.UserService;
import org.dbs24.tik.assist.entity.dto.user.*;
import org.dbs24.tik.assist.entity.dto.user.AuthDto;
import org.dbs24.tik.assist.entity.dto.user.UserIdDto;
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

    final UserService userService;
    final AuthenticationService authenticationService;

    public UserRest(UserService userService, AuthenticationService authenticationService) {

        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @ResponseStatus
    public Mono<ServerResponse> register(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.registerDefault(request.bodyToMono(UserDto.class)),
                        UserActivationDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> resendActivationEmail(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.sendActivationEmail(request.bodyToMono(UserEmailDto.class)),
                        UserActivationDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> activateUserByKey(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.activateUserByKey(request.bodyToMono(UserActivationDto.class)),
                        AuthDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> login(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.loginDefault(request.bodyToMono(UserDto.class)),
                        AuthDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> loginWithFacebook(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.loginWithFacebook(request.bodyToMono(FacebookLoginUserDto.class)),
                        AuthDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> loginWithGoogle(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.loginWithGoogle(request.bodyToMono(GoogleLoginUserDto.class)),
                        AuthDto.class
                );
    }

    public Mono<ServerResponse> logout(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Mono.just(
                                StmtProcessor.create(
                                        UserIdDto.class,
                                        userIdDto -> userIdDto.setUserId(authenticationService.removeToken(request))
                                )
                        ),
                        UserIdDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> changePassword(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.changePassword(
                                request.bodyToMono(ChangePasswordDto.class),
                                authenticationService.extractUserIdFromServerRequest(request)
                        ),
                        AuthDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> forgotPassword(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.sendEmailToChangePassword(request.bodyToMono(ForgotPasswordDto.class)),
                        UserIdDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> forgotPasswordAuthenticated(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.sendEmailToChangePasswordAuthenticated(authenticationService.extractUserIdFromServerRequest(request)),
                        UserIdDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> changeForgottenPassword(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.changeForgottenPassword(request.bodyToMono(ChangeForgottenPasswordDto.class)),
                        AuthDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> refreshToken(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.refreshToken(authenticationService.extractJwtFromServerRequest(request)),
                        AuthDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> isKeySetValid(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userService.isKeySetValid(request.bodyToMono(PasswordKeySetDto.class)),
                        Boolean.class
                );
    }
}
