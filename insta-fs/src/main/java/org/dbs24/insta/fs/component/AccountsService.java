/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.fs.component;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.dbs24.insta.fs.repo.AccountHistRepo;
import org.dbs24.insta.fs.repo.AccountRepo;
import org.dbs24.insta.fs.entity.Account;
import org.dbs24.insta.fs.entity.AccountHist;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.transaction.annotation.Transactional;
import org.dbs24.insta.api.rest.AccountInfo;
import org.dbs24.insta.api.rest.CreatedAccount;
import org.dbs24.insta.fs.rest.api.InstaAccountInfo;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-fs")
public class AccountsService extends AbstractApplicationService {

    final AccountRepo accountRepo;
    final AccountHistRepo accountHistRepo;
    final RefsService refsService;

    public AccountsService(AccountRepo accountRepo, AccountHistRepo accountHistRepo, RefsService refsService) {
        this.accountRepo = accountRepo;
        this.accountHistRepo = accountHistRepo;
        this.refsService = refsService;
    }
    //==========================================================================

    @Transactional
    public CreatedAccount createOrUpdateAccount(AccountInfo accountInfo) {

        final Long instaId = accountInfo.getInstaId();

        final Optional<Account> optAccount = Optional.ofNullable(instaId).map(accountRepo::findByInstaId).orElseGet(Optional::empty);

        final Account account = optAccount.orElseGet(() -> findOrCreateAccount(accountInfo.getAccountId()));

        // copy 2 history
        saveAccountHistory(account);

        account.setActualDate((LocalDateTime) StmtProcessor.nvl(NLS.long2LocalDateTime(accountInfo.getActualDate()), LocalDateTime.now()));
        account.setAccountStatus(refsService.findAccountStatus(accountInfo.getAccountStatusId()));
        account.setBiography(accountInfo.getBiography());
        account.setFollowees(accountInfo.getFollowees());
        account.setFollowers(accountInfo.getFollowers());
        account.setFullName(accountInfo.getFullName());
        account.setInstaId(accountInfo.getInstaId());
        account.setIsPrivate(accountInfo.getIsPrivate());
        account.setIsVerified(accountInfo.getIsVerified());
        account.setMediaCount(accountInfo.getMediaCount());
        account.setProfilePicHdUrl(accountInfo.getProfilePicHdUrl());
        account.setProfilePicUrl(accountInfo.getProfilePicUrl());
        account.setUserName(accountInfo.getUserName());

        saveAccount(account);

        return StmtProcessor.create(CreatedAccount.class, ca -> {

            ca.setAccountId(account.getAccountId());

            log.debug("try 2 create/update account: {}", account);

        });
    }

//    @Transactional(readOnly = true)
    public InstaAccountInfo validateInstaAccount(Long instaId) {

        final Optional<Account> optAccount = accountRepo.findByInstaId(instaId);

        return StmtProcessor.create(InstaAccountInfo.class, ia -> {

            ia.setIsExists(optAccount.isPresent());
            ia.setInstaId(instaId);
            ia.setActualDate(NLS.localDateTime2long(optAccount.isPresent() ? optAccount.get().getActualDate() : LocalDateTime.now()));

            log.info("validate instaId {}: {}", instaId, ia);

        });
    }

    public Account createAccount() {
        return StmtProcessor.create(Account.class, a -> {
            a.setActualDate(LocalDateTime.now());
        });
    }

    public Account findAccount(Long accountId) {

        return accountRepo
                .findById(accountId)
                .orElseThrow(() -> new RuntimeException(String.format("accountId not found (%d)", accountId)));
    }

    public Account findOrCreateAccount(Long accountId) {
        return (Optional.ofNullable(accountId)
                .orElseGet(() -> Long.valueOf("0")) > 0)
                ? findAccount(accountId)
                : createAccount();
    }

    public void saveAccountHistory(AccountHist accountHist) {
        accountHistRepo.save(accountHist);
    }

    public void saveAccountHistory(Account account) {
        Optional.ofNullable(account.getAccountId())
                .ifPresent(id -> saveAccountHistory((StmtProcessor.create(AccountHist.class, accountHist -> accountHist.assign(account)))));
    }

    public void saveAccount(Account account) {
        accountRepo.save(account);
    }

}
