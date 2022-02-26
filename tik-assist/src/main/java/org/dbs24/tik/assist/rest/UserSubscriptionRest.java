package org.dbs24.tik.assist.rest;

import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.dbs24.tik.assist.constant.RequestQueryParam;
import org.dbs24.tik.assist.entity.dto.plan.CustomPlanConstraint;
import org.dbs24.tik.assist.entity.dto.subscription.*;
import org.dbs24.tik.assist.entity.dto.tiktok.TiktokPlanDtoList;
import org.dbs24.tik.assist.service.exception.JwtIsExpiredException;
import org.dbs24.tik.assist.service.hierarchy.UserSubscriptionService;
import org.dbs24.tik.assist.service.hierarchy.resolver.SumResolver;
import org.dbs24.tik.assist.service.security.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Log4j2
@Component
public class UserSubscriptionRest extends ReactiveRestProcessor {

    private final UserSubscriptionService userSubscriptionService;
    private final AuthenticationService authenticationService;
    private final SumResolver sumResolver;

    public UserSubscriptionRest(UserSubscriptionService userSubscriptionService, AuthenticationService authenticationService, SumResolver sumResolver) {

        this.userSubscriptionService = userSubscriptionService;
        this.authenticationService = authenticationService;
        this.sumResolver = sumResolver;
    }

    public Mono<ServerResponse> createUserSubscriptionByTemplate(ServerRequest serverRequest) {

        return this.<ByTemplateUserSubscriptionDto, UserSubscriptionIdDto>createResponse(
                serverRequest,
                ByTemplateUserSubscriptionDto.class,
                UserSubscriptionIdDto.class,
                byTemplateUserSubscriptionDto -> userSubscriptionService.createUserSubscriptionByTemplate(
                        authenticationService.extractUserIdFromServerRequest(serverRequest),
                        byTemplateUserSubscriptionDto
                )
        );
    }

    public Mono<ServerResponse> createUserSubscriptionCustom(ServerRequest serverRequest) {

        return this.<CustomUserSubscriptionDto, UserSubscriptionIdDto>createResponse(
                serverRequest,
                CustomUserSubscriptionDto.class,
                UserSubscriptionIdDto.class,
                customUserSubscriptionDto -> userSubscriptionService.createUserSubscriptionCustom(
                        authenticationService.extractUserIdFromServerRequest(serverRequest),
                        customUserSubscriptionDto
                )
        );
    }

    @ResponseStatus
    public Mono<ServerResponse> updateUserSubscriptionByTemplate(ServerRequest serverRequest) {

        return this.<UpdateUserSubscriptionByTemplateDto, UserSubscriptionIdDto>createResponse(
                serverRequest,
                UpdateUserSubscriptionByTemplateDto.class,
                UserSubscriptionIdDto.class,
                updateByTemplateDto -> userSubscriptionService.updateUserSubscriptionByTemplate(
                        authenticationService.extractUserIdFromServerRequest(serverRequest),
                        updateByTemplateDto
                ));
    }

    @ResponseStatus
    public Mono<ServerResponse> updateUserSubscriptionCustom(ServerRequest serverRequest) {

        return this.<CustomPlanConstraint, UserSubscriptionIdDto>createResponse(
                serverRequest,
                CustomPlanConstraint.class,
                UserSubscriptionIdDto.class,
                constraintsDto -> userSubscriptionService.updateUserSubscriptionCustom(
                        authenticationService.extractUserIdFromServerRequest(serverRequest),
                        constraintsDto
                ));
    }

    @ResponseStatus
    public Mono<ServerResponse> getActiveSubscription(ServerRequest serverRequest) {

        return this.<UserSubscriptionPlanStatisticsDto>createResponse(
                serverRequest,
                UserSubscriptionPlanStatisticsDto.class,
                () -> userSubscriptionService.getActiveSubscriptionDtoByUserIdAndTiktokUsername(
                        authenticationService.extractUserIdFromServerRequest(serverRequest),
                        serverRequest.queryParam(RequestQueryParam.QP_TIKTOK_USERNAME).orElse("")
                )
        );
    }

    public Mono<ServerResponse> calculateUserSubscriptionSum(ServerRequest serverRequest) {

        return this.<CalculateSubscriptionCostDto, SubscriptionCostDto>createResponse(
                serverRequest,
                CalculateSubscriptionCostDto.class,
                SubscriptionCostDto.class,
                calculateSubscriptionCostDto -> SubscriptionCostDto.of(
                        sumResolver.calculateSubscriptionSum(
                                Either.right(calculateSubscriptionCostDto.getCustomPlanConstraint()),
                                Optional.empty(),
                                calculateSubscriptionCostDto.getAccountsQuantity()
                        )
                ));
    }

    public Mono<ServerResponse> undoUserSubscription(ServerRequest serverRequest) {

        return this.<UserSubscriptionIdDto, TiktokPlanDtoList>createResponse(
                serverRequest,
                UserSubscriptionIdDto.class,
                TiktokPlanDtoList.class,
                userSubscription -> {
                    if (!authenticationService.isTokenValid(authenticationService.extractJwtFromServerRequest(serverRequest))) {
                        throw new JwtIsExpiredException(HttpStatus.UNAUTHORIZED);
                    }
                    return userSubscriptionService.undoUserSubscription(authenticationService.extractUserIdFromServerRequest(serverRequest), userSubscription.getUserSubscriptionId());
                });
    }
}
