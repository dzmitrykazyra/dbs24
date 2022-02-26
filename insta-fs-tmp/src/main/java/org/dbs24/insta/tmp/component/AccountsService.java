/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.consts.SysConst;
import org.dbs24.insta.tmp.kafka.api.IgAccount;

import static org.dbs24.insta.api.consts.InstaConsts.Consumers.CON_GROUP_ID;
import static org.dbs24.insta.tmp.consts.IfsConst.Kafka.*;
import static org.dbs24.insta.tmp.consts.IfsConst.References.AccountStatuses.AS_ACTUAL;

import org.dbs24.insta.tmp.rest.api.*;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.dbs24.insta.tmp.repo.*;
import org.dbs24.insta.tmp.entity.Account;
import org.dbs24.insta.tmp.entity.dto.AccountDto;
import org.dbs24.insta.tmp.entity.AccountHist;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-tmp")
public class AccountsService extends AbstractApplicationService {

    final AccountRepo accountRepo;
    final AccountDtoRepo accountDtoRepo;
    final AccountHistRepo accountHistRepo;
    final RefsService refsService;
    final BotHistRepo botHistRepo;
    final BotRepo botRepo;
    final SourceRepo sourceRepo;
    final FaceRepo faceRepo;
    final PostRepo postRepo;
    final TaskRepo taskRepo;

    final Collection<IgAccount> hotAccounts;

    final AtomicInteger accCounter = new AtomicInteger();

    public AccountsService(AccountRepo accountRepo, AccountHistRepo accountHistRepo, RefsService refsService, BotHistRepo botHistRepo, BotRepo botRepo, FaceRepo faceRepo, PostRepo postRepo, SourceRepo sourceRepo, TaskRepo taskRepo, AccountDtoRepo accountDtoRepo) {

        this.hotAccounts = ServiceFuncs.createConcurencyCollection();

        this.accountRepo = accountRepo;
        this.accountDtoRepo = accountDtoRepo;
        this.accountHistRepo = accountHistRepo;
        this.refsService = refsService;
        this.botHistRepo = botHistRepo;
        this.botRepo = botRepo;
        this.faceRepo = faceRepo;
        this.postRepo = postRepo;
        this.sourceRepo = sourceRepo;
        this.taskRepo = taskRepo;

        accCounter.set(0);

    }

    @KafkaListener(id = KAFKA_ACCOUNTS, groupId = CON_GROUP_ID, topics = KAFKA_ACCOUNTS)
    public void createAccountsFromKafka(Collection<IgAccount> accounts) {

        accCounter.addAndGet(accounts.size());

        log.debug("{}: receive accounts: {}/{}", KAFKA_ACCOUNTS, accounts.size(), accCounter.get());

        hotAccounts.addAll(accounts);
    }

    //    @Scheduled(fixedRateString = "${config.crw.accounts.processing-interval:1000}", cron = "${config.crw.accounts.processing-interval.processing-cron:}")
//    @Transactional
    protected void saveAllAccount() {

        hotAccounts
                .stream()
                .filter(account -> !account.getIsAdded())
                .forEach(account -> {
                    accountRepo.bulkInsert(
                            AS_ACTUAL,
                            LocalDateTime.now(),
                            account.getInstaId(),
                            account.getInstaUserName(),
                            account.getInstaFullName(),
                            account.getMediacount(),
                            account.getFollowers(),
                            account.getFollowees(),
                            account.getInstaBiography(),
                            account.getIsPrivate() ? 1 : 0,
                            account.getIsVerified() ? 1 : 0,
                            account.getProfilePicHdUrl(),
                            account.getProfilePicUrl());
                    account.setIsAdded(Boolean.TRUE);
                });

        hotAccounts.removeIf(account -> account.getIsAdded());

    }

    @Scheduled(fixedRateString = "${config.crw.accounts.processing-interval:1000}", cron = "${config.crw.accounts.processing-interval.processing-cron:}")
    @Transactional
    protected void saveAccounts() {

        final Collection<AccountDto> newAccounts = hotAccounts
                .stream()
                .filter(account -> !account.getIsAdded())
                .map(this::createAccountEntity)
                .collect(Collectors.toList());

        StmtProcessor.ifTrue(!newAccounts.isEmpty(), () -> {
            log.debug("{}: store accounts: {}/{}", KAFKA_ACCOUNTS, newAccounts.size(), hotAccounts.size());
            saveAccounts(newAccounts);
            hotAccounts.removeIf(account -> account.getIsAdded());
        });

    }

    private Account createAccount(IgAccount igAccount) {

        final Account account = findAccountByInstaId(igAccount.getInstaId());

        account.setActualDate(LocalDateTime.now());
        account.setAccountStatus(refsService.findAccountStatus(AS_ACTUAL));
        account.setBiography(igAccount.getInstaBiography());
        account.setFollowees(igAccount.getFollowees());
        account.setFollowers(igAccount.getFollowers());
        account.setFullName(igAccount.getInstaFullName());
        account.setInstaId(igAccount.getInstaId());
        account.setIsPrivate(igAccount.getIsPrivate() ? 1 : 0);
        account.setIsVerified(igAccount.getIsVerified() ? 1 : 0);
        account.setMediaCount(igAccount.getMediacount());
        account.setProfilePicUrlHd(igAccount.getProfilePicHdUrl());
        account.setProfilePicUrl(igAccount.getProfilePicUrl());
        account.setUserName(igAccount.getInstaUserName());

        log.debug("registry account (instaId={}, accountId={}) ", igAccount.getInstaId(), account.getAccountId());

        igAccount.setIsAdded(Boolean.TRUE);

        return account;

    }

    //==========================================================================
    @Deprecated
    private AccountDto createAccountEntity(IgAccount igAccount) {

        final AccountDto account = StmtProcessor.create(AccountDto.class, accountDto -> {

            accountDto.setActualDate(LocalDateTime.now());
            accountDto.setAccountStatus(refsService.findAccountStatus(AS_ACTUAL));
            accountDto.setBiography(igAccount.getInstaBiography());
            accountDto.setFollowees(igAccount.getFollowees());
            accountDto.setFollowers(igAccount.getFollowers());
            accountDto.setFullName(igAccount.getInstaFullName());
            accountDto.setInstaId(igAccount.getInstaId());
            accountDto.setIsPrivate(igAccount.getIsPrivate() ? 1 : 0);
            accountDto.setIsVerified(igAccount.getIsVerified() ? 1 : 0);
            accountDto.setMediaCount(igAccount.getMediacount());
            accountDto.setProfilePicUrlHd(igAccount.getProfilePicHdUrl());
            accountDto.setProfilePicUrl(igAccount.getProfilePicUrl());
            accountDto.setUserName(igAccount.getInstaUserName());

        });

        log.debug("registry account (instaId={}, accountId={}) ", igAccount.getInstaId(), account.getAccountId());

        igAccount.setIsAdded(Boolean.TRUE);

        return account;

    }

    //==========================================================================
    @Transactional
    public DeleteAllInfo deleteAll() {

        return StmtProcessor.create(DeleteAllInfo.class,
                ia -> {

                    final Long deleted = accountRepo.count();

                    hotAccounts.clear();

                    ia.setDeleted(deleted);

                    faceRepo.deleteAll();

                    sourceRepo.deleteAll();

                    postRepo.deleteAll();

                    taskRepo.deleteAll();
//            botRepo.deleteAll();
//            botHistRepo.deleteAll();

                    accountRepo.deleteAll();

                    log.info(
                            "delete all: {} account(s) deleted", deleted);

                });
    }

    @Transactional
    public CreatedAccount createOrUpdateAccount(AccountInfo accountInfo) {

        final Account accountInternal = findOrCreateAccount(accountInfo.getAccountId());

        final Account account = StmtProcessor.notNull(accountInternal.getAccountId()) ? accountInternal : findAccountByInstaId(accountInfo.getInstaId());

        // copy 2 history
        //saveAccountHistory(account);
        account.setActualDate((LocalDateTime) StmtProcessor.nvl(NLS.long2LocalDateTime(accountInfo.getActualDate()), LocalDateTime.now()));
        account.setAccountStatus(refsService.findAccountStatus(accountInfo.getAccountStatusId()));
        account.setBiography(accountInfo.getBiography());
        account.setFollowees(accountInfo.getFollowees());
        account.setFollowers(accountInfo.getFollowers());
        account.setFullName(accountInfo.getFullName());
        account.setInstaId(accountInfo.getInstaId());
        account.setIsPrivate(accountInfo.getIsPrivate() ? 1 : 0);
        account.setIsVerified(accountInfo.getIsVerified() ? 1 : 0);
        account.setMediaCount(accountInfo.getMediaCount());
        account.setProfilePicUrlHd(accountInfo.getProfilePicHdUrl());
        account.setProfilePicUrl(accountInfo.getProfilePicUrl());
        account.setUserName(accountInfo.getUserName());

        saveAccount(account);

        return StmtProcessor.create(CreatedAccount.class,
                ca -> {

                    ca.setAccountId(account.getAccountId());
                    ca.setInstaId(account.getInstaId());

                    log.debug(
                            "try 2 create/update account: {}", account.getAccountId());

                });
    }

    //==========================================================================
    public Account findAccountByInstaId(Long instaId) {
        return StmtProcessor.isNull(instaId) ? createAccount() : accountRepo.findByInstaId(instaId).stream().findAny().orElseGet(this::createAccount);
    }

    //==========================================================================
    public AccountInfo getAccount(Long accountId) {

        return StmtProcessor.create(AccountInfo.class,
                ia -> ia.assign(accountRepo.findById(accountId).orElseThrow()));
    }

    public AllAccounts getAccounts(Long instaId) {

        return StmtProcessor.create(AllAccounts.class,
                aa -> accountRepo
                        .findByInstaId(instaId)
                        .stream()
                        .forEach(account -> aa.getAccounts().add(AccountInfo.createAccountInfo(account))));
    }

    //==========================================================================
    public InstaAccountInfo validateInstaAccount(Long instaId) {

        final Optional<Account> optAccount = accountRepo.findByInstaId(instaId).stream().findAny();

        return StmtProcessor.create(InstaAccountInfo.class,
                ia -> {

                    ia.setIsExists(optAccount.isPresent());
                    ia.setInstaId(instaId);

                    ia.setActualDate(NLS.localDateTime2long(optAccount.isPresent() ? optAccount.get().getActualDate() : LocalDateTime.now()));

                    log.debug(
                            "validate instaId {}: {}", instaId, ia);

                });
    }

    //==========================================================================
    public void buildFutureAccount(Account account) {
        account.setActualDate(LocalDateTime.now());
        account.setAccountStatus(refsService.findAccountStatus(AS_ACTUAL));
        account.setBiography(SysConst.UNKNOWN);
        account.setFullName(SysConst.UNKNOWN);
        account.setUserName(SysConst.UNKNOWN);
        account.setProfilePicUrlHd(SysConst.UNKNOWN);
        account.setProfilePicUrl(SysConst.UNKNOWN);
    }

    //==========================================================================
    public Account createAccount() {
        return StmtProcessor.create(Account.class,
                a -> {
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
                .ifPresent(id -> saveAccountHistory((StmtProcessor.create(AccountHist.class,
                        accountHist -> accountHist.assign(account)))));
    }

    public void saveAccount(Account account) {
        accountRepo.save(account);
    }

    @Transactional
    public synchronized void saveAccounts(Collection<AccountDto> accounts) {
        accountDtoRepo.saveAllAndFlush(accounts);
    }
}
