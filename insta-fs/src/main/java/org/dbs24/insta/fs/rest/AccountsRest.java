/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.fs.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.rest.api.ReactiveRestProcessor;
import static org.dbs24.rest.api.ReactiveRestProcessor.httpOk;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import org.dbs24.insta.fs.component.AccountsService;
import org.dbs24.insta.api.rest.AccountInfo;
import org.dbs24.insta.api.rest.CreatedAccount;
import org.dbs24.insta.fs.rest.api.InstaAccountInfo;
import static org.dbs24.insta.fs.consts.IfsConst.RestQueryParams.QP_INSTA_ID;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-fs")
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
}
