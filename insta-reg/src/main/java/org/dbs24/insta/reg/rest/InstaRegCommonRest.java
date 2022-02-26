/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.component.CommonRest;
import org.dbs24.insta.reg.component.AccountBuilder;
import org.dbs24.insta.reg.component.AccountService;
import org.dbs24.insta.reg.component.EmailService;
import org.dbs24.insta.reg.component.ProxyService;
import org.dbs24.insta.reg.entity.Email;
import org.dbs24.insta.reg.rest.api.AccountActionsCollection;
import org.dbs24.insta.reg.rest.api.ExceptionInfo;
import org.dbs24.insta.reg.rest.api.FakedMail;
import org.dbs24.rest.api.SystemInfo;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.dbs24.consts.SecurityConst.SYSTEM_INFO_CLASS;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-reg")
public class InstaRegCommonRest extends CommonRest {

    final EmailService emailService;
    final ProxyService proxyService;
    final AccountBuilder accountBuilder;
    final AccountService accountService;
    final StopWatcher stopWatcher = StopWatcher.create("Server uptime");

    public InstaRegCommonRest(EmailService emailService, AccountBuilder accountBuilder, ProxyService proxyService, AccountService accountService, GenericApplicationContext genericApplicationContext) {
        super(genericApplicationContext);
        this.emailService = emailService;
        this.accountBuilder = accountBuilder;
        this.proxyService = proxyService;
        this.accountService = accountService;
    }

    //==========================================================================
    public Mono<ServerResponse> getFakedMail(ServerRequest request) {

        final String emailAddrAggr = accountBuilder.getAndReserveAnyAvailableFakedMail();

        final String emailAddr = emailAddrAggr.split(":")[0];
        final String fakedEmail = emailAddrAggr.split(":")[1];

        final Email email = emailService.findEmail(emailAddr).orElseThrow();

        return this.<FakedMail>processServerRequest(request,
                () -> StmtProcessor.create(FakedMail.class, fakedMail -> {

                    fakedMail.setEmailAddress(emailAddr);
                    fakedMail.setFakedEmailAddress(fakedEmail);
                    fakedMail.setCreateDate(email.getCreateDate());
                    fakedMail.setPwd(email.getPwd());
                    fakedMail.setAccountNotes(email.getAccountNotes());

                    log.info("return  {}", fakedMail);

                }));
    }

    //==========================================================================
    @Transactional(readOnly = true)
    public Mono<ServerResponse> getLatestExceptions(ServerRequest request) {

        return this.<AccountActionsCollection>processServerRequest(request,
                () -> StmtProcessor.create(AccountActionsCollection.class, actions
                        -> accountService.findLatestExceptions()
                        .forEach(action
                                -> actions.getActions().add(StmtProcessor.create(ExceptionInfo.class,
                                excInfo -> {

                                    excInfo.setExDate(action.getActionFinishDate());
                                    excInfo.setNotes(action.getActionsNotes());
                                    excInfo.setProxyId(action.getProxy().getProxyId());
                                    excInfo.setExMsg(action.getErrMsg());

                                }))
                        )
                ));
    }

    //==========================================================================
    @Override
    protected SystemInfo buildLiveNessRecord() {

        final int created = accountBuilder.getAccountsCreated().get();
        final int total = accountBuilder.getAccountsAttempts().get();
        final int perc = Math.round((created * 100) / (total + (total > 0 ? 0 : 1)));

        return StmtProcessor.create(SYSTEM_INFO_CLASS,
                object -> object.setSysInfo(String.format("Current time: %s, started: %s, %s, tasks in progress: %d, mailboxes available: %d, account(s) created: %d/%d (%d%%), total accounts avaiable: %d ",
                        NLS.localDateTime2String(LocalDateTime.now()),
                        stopWatcher.getStringStartDateTime(),
                        stopWatcher.getStringExecutionTime("Server uptime"),
                        proxyService.getThreadsInProgress(),
                        accountBuilder.getFactory().size(),
                        accountBuilder.getAccountsCreated().get(),
                        accountBuilder.getAccountsAttempts().get(),
                        perc,
                        accountService.getTotalReadyAccounts()))
        );
    }
}
