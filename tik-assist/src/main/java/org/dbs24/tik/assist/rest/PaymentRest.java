package org.dbs24.tik.assist.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.dbs24.tik.assist.entity.dto.payment.IncreaseUserDepositDto;
import org.dbs24.tik.assist.entity.dto.payment.MonthlySubscriptionPaymentDto;
import org.dbs24.tik.assist.entity.dto.payment.UserDepositBalanceDto;
import org.dbs24.tik.assist.entity.dto.user.UserIdDto;
import org.dbs24.tik.assist.service.hierarchy.UserSubscriptionService;
import org.dbs24.tik.assist.service.payment.PaymentsService;
import org.dbs24.tik.assist.service.security.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class PaymentRest extends ReactiveRestProcessor {

    private final PaymentsService paymentsService;
    private final AuthenticationService authenticationService;
    private final UserSubscriptionService userSubscriptionService;

    public PaymentRest(PaymentsService paymentsService, AuthenticationService authenticationService, UserSubscriptionService userSubscriptionService) {

        this.paymentsService = paymentsService;
        this.authenticationService = authenticationService;
        this.userSubscriptionService = userSubscriptionService;
    }

    public Mono<ServerResponse> increaseDeposit(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        paymentsService.increaseUserDeposit(
                                request.bodyToMono(IncreaseUserDepositDto.class),
                                authenticationService.extractUserIdFromServerRequest(request)
                        ),
                        UserDepositBalanceDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> payForSubscription(ServerRequest request) {

        return this.<MonthlySubscriptionPaymentDto, UserIdDto>createResponse(
                request,
                MonthlySubscriptionPaymentDto.class,
                UserIdDto.class,
                userSubscriptionService::extendMonthlySubscription);
    }

    public Mono<ServerResponse> getDepositBalance(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        paymentsService.getUserDepositBalance(authenticationService.extractUserIdFromServerRequest(request)),
                        UserDepositBalanceDto.class
                );
    }
}
