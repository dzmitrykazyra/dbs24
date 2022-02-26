package org.dbs24.tik.assist.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.entity.dto.user.UserEmailDto;
import org.dbs24.tik.assist.entity.dto.user.UserMailingDto;
import org.dbs24.tik.assist.service.PromocodeService;
import org.dbs24.tik.assist.service.security.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class PromocodeRest {

    private final PromocodeService promocodeService;
    private final AuthenticationService authenticationService;

    public PromocodeRest(PromocodeService promocodeService, AuthenticationService authenticationService) {

        this.promocodeService = promocodeService;
        this.authenticationService = authenticationService;
    }

    public Mono<ServerResponse> createMailingPromocode(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        promocodeService.sendMailingPromocode(
                                authenticationService.extractUserIdFromServerRequest(request),
                                request.bodyToMono(UserEmailDto.class)
                        ),
                        UserMailingDto.class
                );
    }
}
