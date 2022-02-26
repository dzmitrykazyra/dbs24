/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.insta.tmp.rest.api.*;
import org.dbs24.rest.api.ReactiveRestProcessor;
import static org.dbs24.rest.api.ReactiveRestProcessor.httpOk;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import org.dbs24.insta.tmp.component.AccountsService;

import static org.dbs24.insta.tmp.consts.IfsConst.RestQueryParams.QP_INSTA_ID;
import static org.dbs24.insta.tmp.consts.IfsConst.RestQueryParams.QP_ACCOUNT_ID;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-tmp")
public class AccountsRest extends ReactiveRestProcessor {

    final AccountsService accountsService;

    public AccountsRest(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdateAccount(ServerRequest request) {

        return this.<AccountInfo, CreatedAccount>processServerRequest(request, AccountInfo.class,
                accountInfo -> accountsService.createOrUpdateAccount(accountInfo));
    }

    //==========================================================================
    public Mono<ServerResponse> validateInstaAccount(ServerRequest request) {

        final Long instaId = getLongFromParam(request, QP_INSTA_ID);

        return this.<InstaAccountInfo>processServerRequest(request, () -> accountsService.validateInstaAccount(instaId));
    }
    //==========================================================================
    public Mono<ServerResponse> getAccount(ServerRequest request) {

        final Long accountId = getLongFromParam(request, QP_ACCOUNT_ID);

        return this.<AccountInfo>processServerRequest(request, () -> accountsService.getAccount(accountId));
    }

    //==========================================================================
    public Mono<ServerResponse> getAccounts(ServerRequest request) {

        final Long instaId = getLongFromParam(request, QP_INSTA_ID);

        return this.<AllAccounts>processServerRequest(request, () -> accountsService.getAccounts(instaId));
    }

    //==========================================================================
    public Mono<ServerResponse> deleteAll(ServerRequest request) {

        return this.<DeleteAllInfo>processServerRequest(request, accountsService::deleteAll);
    }
}
