package org.dbs24.tik.assist.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.dbs24.tik.assist.entity.dto.tiktok.*;
import org.dbs24.tik.assist.service.exception.JwtIsExpiredException;
import org.dbs24.tik.assist.service.security.AuthenticationService;
import org.dbs24.tik.assist.service.tiktok.TiktokAccountService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "tik-assist")
public class TiktokAccountRest extends ReactiveRestProcessor {

    final TiktokAccountService tiktokAccountService;

    final AuthenticationService authenticationService;

    public TiktokAccountRest(TiktokAccountService tiktokAccountService, AuthenticationService authenticationService) {

        this.tiktokAccountService = tiktokAccountService;
        this.authenticationService = authenticationService;
    }

    @ResponseStatus
    public Mono<ServerResponse> addAccountToProfile(ServerRequest serverRequest) {

        return this.<TiktokAccountDto, TiktokPlanDtoList>createResponse(
                serverRequest,
                TiktokAccountDto.class,
                TiktokPlanDtoList.class,
                tiktokAccountDto -> tiktokAccountService.boundAccountWithProfile(
                        authenticationService.extractUserIdFromServerRequest(serverRequest),
                        tiktokAccountDto
                ));
    }

    public Mono<ServerResponse> removeAccountFromProfile(ServerRequest serverRequest) {

        return this.<TiktokAccountDto, TiktokPlanDtoList>createResponse(
                serverRequest,
                TiktokAccountDto.class,
                TiktokPlanDtoList.class,
                tiktokAccountDto -> tiktokAccountService.unboundAccountWithProfile(
                        authenticationService.extractUserIdFromServerRequest(serverRequest),
                        tiktokAccountDto
                ));
    }

    public Mono<ServerResponse> getAllAccountProfiles(ServerRequest serverRequest) {

        return this.<TiktokPlanDtoList>createResponse(
                serverRequest,
                TiktokPlanDtoList.class,
                () -> tiktokAccountService.getAllUserAccounts(
                        authenticationService.extractUserIdFromServerRequest(serverRequest)
                ));
    }

    @ResponseStatus
    public Mono<ServerResponse> getLastSelectedAccountInfo(ServerRequest serverRequest) {

        return this.<LastSelectedAccountDto, TiktokAccountInfoDto>createResponse(
                serverRequest,
                LastSelectedAccountDto.class,
                TiktokAccountInfoDto.class,
                lastSelectedAccountDto -> tiktokAccountService.getLastSelectedUserAccount(
                        authenticationService.extractUserIdFromServerRequest(serverRequest),
                        lastSelectedAccountDto
                ));
    }

    public Mono<ServerResponse> getPlanByTiktokAccount(ServerRequest serverRequest) {

        return this.<TiktokAccountDto, TiktokPlanDto>createResponse(
                serverRequest,
                TiktokAccountDto.class,
                TiktokPlanDto.class,
                tiktokAccountDto -> {
                    if (!authenticationService.isTokenValid(authenticationService.extractJwtFromServerRequest(serverRequest))) {
                        throw new JwtIsExpiredException(HttpStatus.UNAUTHORIZED);
                    }
                    return tiktokAccountService.getActualPlanByTiktokAccount(authenticationService.extractUserIdFromServerRequest(serverRequest), tiktokAccountDto);
                });
    }

    public Mono<ServerResponse> getAllSubscriptionsByUser(ServerRequest serverRequest) {

        return this.<TiktokExistSubscriptionDtoList>createResponse(
                serverRequest,
                TiktokExistSubscriptionDtoList.class,
                () -> tiktokAccountService.getAllSubscriptions(authenticationService.extractUserIdFromServerRequest(serverRequest))
        );
    }
}
