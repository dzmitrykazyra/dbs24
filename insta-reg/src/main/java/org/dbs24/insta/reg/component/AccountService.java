/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.insta.reg.entity.Email;
import org.dbs24.insta.reg.entity.Account;
import org.dbs24.insta.reg.entity.AccountAction;
import org.dbs24.insta.reg.entity.AccountHist;
import static org.dbs24.insta.reg.consts.InstaConsts.AccountStatuses.*;
//import org.dbs24.insta.reg.entity.AccountStatus;
import org.dbs24.insta.reg.repo.AccountActionRepo;
import org.dbs24.insta.reg.repo.AccountHistRepo;
import org.dbs24.insta.reg.repo.AccountRepo;
import org.dbs24.insta.reg.repo.BatchActionRepo;
import org.dbs24.insta.reg.repo.BatchRepo;
import org.dbs24.service.JavaFakerService;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Component
@Data
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-reg")
public class AccountService extends AbstractApplicationService {

    final AccountRepo accountRepo;
    final EmailService emailService;

    final AtomicInteger totalAccountsToday = new AtomicInteger();
    final AccountHistRepo accountHistRepo;
    final RefsService refsService;
    final JavaFakerService javaFakerService;
    final BatchActionRepo batchActionRepo;
    final BatchRepo batchRepo;
    final AccountActionRepo accountActionRepo;
    final GenericApplicationContext genericApplicationContext;

    public AccountService(AccountRepo accountRepo, AccountHistRepo accountHistRepo, RefsService refsService, EmailService emailService, BatchActionRepo batchActionRepo, BatchRepo batchRepo, GenericApplicationContext genericApplicationContext, AccountActionRepo accountActionRepo, JavaFakerService javaFakerService) {
        totalAccountsToday.set(0);
        this.accountRepo = accountRepo;
        this.accountHistRepo = accountHistRepo;
        this.refsService = refsService;
        this.emailService = emailService;
        this.batchActionRepo = batchActionRepo;
        this.batchRepo = batchRepo;
        this.genericApplicationContext = genericApplicationContext;
        this.accountActionRepo = accountActionRepo;
        this.javaFakerService = javaFakerService;
    }

    //==========================================================================
    public Collection<Account> findLimitAccounts() {

        return accountRepo
                .findByAccountStatus(refsService.findAccountStatus(AS_EMAIL_SHARING_LIMIT));

    }

    //==========================================================================
    public Collection<Account> findAnotherUsedAccounts() {

        return accountRepo
                .findByAccountStatus(refsService.findAccountStatus(AS_ANOTHER_ACCOUNT_USING));

    }

    //==========================================================================
    public Collection<String> findUsedIPs() {

        return accountRepo
                .getUsedIPs();

    }

    //==========================================================================
    public Collection<Account> getAccountsByEmail(String email) {
        // used faked logins
        return accountRepo
                .findByEmail(emailService.findEmail(email).orElseThrow(() -> new RuntimeException(" mail not found " + email)));
    }

    //==========================================================================
    public Collection<Account> findAccountsUsedFakedAccount(Email email) {
        // used faked logins
        return accountRepo
                .findUsedFakedAccounts((email));
    }

    public Account createAccount() {
        return StmtProcessor.create(Account.class, a -> {
            a.setCreateDate(LocalDateTime.now());
            a.setActualDate(LocalDateTime.now());
        });
    }

    public Account findAccount(Integer accountId) {

        return accountRepo
                .findById(accountId)
                .orElseThrow(() -> new RuntimeException(String.format("account not found (%d)", accountId)));
    }

    //==========================================================================
    public Account findOrCreateUserContract(Integer accountId) {
        return (Optional.ofNullable(accountId)
                .orElseGet(() -> 0) > 0)
                ? findAccount(accountId)
                : createAccount();
    }

    public void saveAccountHist(AccountHist accountHist) {
        accountHistRepo.save(accountHist);
    }

    public void saveAccountHist(Account account) {

        Optional.ofNullable(account.getAccountId())
                .ifPresent(id -> saveAccountHist(StmtProcessor.create(AccountHist.class, accountHist -> accountHist.assign(account))));
    }

    public void saveAccount(Account account) {
        accountRepo.save(account);
    }

    public void saveActions(Collection<AccountAction> actions) {
        accountActionRepo.saveAll(actions);
    }

    public Integer getTotalReadyAccounts() {
        return accountRepo.getTotalReadyAccounts();
    }

    //==========================================================================
    public Collection<AccountAction> findLatestExceptions() {
        return accountActionRepo
                .getLatestExceptions();
    }

}
