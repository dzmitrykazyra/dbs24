package org.dbs24.tik.mobile.rest;

import org.dbs24.tik.mobile.entity.dto.payment.UserDepositDto;
import org.dbs24.tik.mobile.entity.dto.payment.UserDepositIncreaseDto;
import org.dbs24.tik.mobile.service.TokenHolder;
import org.dbs24.tik.mobile.service.UserDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class PaymentRest {

    private final UserDepositService userDepositService;
    private final TokenHolder tokenHolder;

    @Autowired
    public PaymentRest(UserDepositService userDepositService, TokenHolder tokenHolder) {
        this.userDepositService = userDepositService;
        this.tokenHolder = tokenHolder;
    }

    @ResponseStatus
    public Mono<ServerResponse> getActualUserBalance(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userDepositService.getCurrentBalance(tokenHolder.extractUserIdFromServerRequest(request)),
                        UserDepositDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> increaseUserBalance(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userDepositService.increaseUserBalance(
                                tokenHolder.extractUserIdFromServerRequest(request),
                                request.bodyToMono(UserDepositIncreaseDto.class)
                        ),
                        UserDepositDto.class
                );
    }
}
