/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.AnnotationFuncs;
import org.dbs24.application.core.service.funcs.ReflectionFuncs;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.insta.reg.action.AbstractAction;
import org.dbs24.insta.reg.action.ActionCodeId;
import org.dbs24.insta.reg.action.ActionResult;
import org.dbs24.reactor.AbstractHotSubscriber;
import org.dbs24.insta.reg.entity.Account;
import org.dbs24.insta.reg.entity.Email;
import org.dbs24.insta.reg.entity.AccountAction;
import org.dbs24.insta.reg.entity.BatchAction;
import static org.dbs24.insta.reg.consts.InstaConsts.AccountProcessing.*;
import org.dbs24.stmt.StmtProcessor;
import lombok.Data;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.StringFuncs;
import org.dbs24.consts.SysConst;
import static org.dbs24.insta.reg.consts.InstaConsts.AccountStatuses.AS_JUST_CREATED;
import static org.dbs24.insta.reg.consts.InstaConsts.AccountStatuses.AS_READY;
import org.dbs24.insta.reg.entity.AccountStatus;
import org.dbs24.service.JavaFakerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.dbs24.proxy.starter.service.ProxyProviderService;

@Data
@Log4j2
@Service
@EnableScheduling
@EnableAsync
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-reg")
public class AccountBuilder extends AbstractHotSubscriber<Account> {

    @Value("${config.email.faked-account-limit:200}")
    private Integer fakedAccountLimit;

    @Value("${config.accounts.default-batch:1}")
    private Integer defaultBatch;

    @Value("${config.accounts.online-limit:100}")
    private Integer onlineLimit;

    final AccountService accountService;
    final GenericApplicationContext genericApplicationContext;
    final RefsService refsService;
    final ProxyService proxyService;
    final EmailService emailService;
    final JavaFakerService javaFakerService;
    final ProxyProviderService proxyProviderService;

    final Map<Email, Collection<Account>> factory = ServiceFuncs.createConcurencyMap();
    final Map<Email, Collection<String>> availAbleFakedMails = ServiceFuncs.createConcurencyMap();

    final AtomicInteger accountsCreated = new AtomicInteger();
    final AtomicInteger accountsAttempts = new AtomicInteger();

    //==========================================================================
    final Collection<Class<? extends AbstractAction>> actionClasses = ServiceFuncs.<Class<? extends AbstractAction>>createCollection();

    public AccountBuilder(AccountService accountService, GenericApplicationContext genericApplicationContext, RefsService refsService, ProxyService proxyService, EmailService emailService, JavaFakerService javaFakerService, ProxyProviderService proxyProviderService) {

        accountsCreated.set(0);
        accountsAttempts.set(0);

        this.accountService = accountService;
        this.genericApplicationContext = genericApplicationContext;
        this.refsService = refsService;
        this.proxyService = proxyService;
        this.emailService = emailService;
        this.javaFakerService = javaFakerService;
        this.proxyProviderService = proxyProviderService;

        // значения для справочника берутся из аннотаций классов
        ReflectionFuncs.processPkgClassesCollection("org.dbs24.insta.reg.action", AbstractAction.class, ActionCodeId.class,
                actClass -> {

                    actionClasses.add(actClass);
                    log.info("register action '{}'", actClass.getCanonicalName());

                    genericApplicationContext.registerBean(actClass,
                            bd -> {
                                bd.setBeanClassName(actClass.getCanonicalName());
                                bd.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
                                bd.setAutowireCandidate(true);
                            });
                });

        log.info("account actions size = '{}'", actionClasses.size());
    }

    //@Scheduled(fixedRateString = "${config.accounts.processing-interval:10000}", cron = "${config.accounts.processing-cron:}")
    public void perform() {
        log.info("perform()");

        StmtProcessor.ifTrue(availAbleFakedMails.isEmpty(),
                () -> log.error("no legal mail accounts available".toUpperCase()),
                () -> availAbleFakedMails
                        .entrySet()
                        .stream()
                        .filter(email -> !email.getValue().isEmpty() && !email.getKey().getInUse())
                        .sorted((a, b) -> a.getKey().getUsedTimes().compareTo(b.getKey().getUsedTimes()))
                        .limit(onlineLimit - proxyService.getThreadsInProgress())
                        .forEach(email -> {

                            StmtProcessor.sleep(100);

                            log.info("find available proxy 4 {}", email.getKey().getEmailAddress());

                            final AccountStatus asCreated = refsService.findAccountStatus(AS_JUST_CREATED);

                            Optional.ofNullable(proxyService.findAvailAbleProxy())
                                    .ifPresent(proxy -> {

                                        // used FakedMail
                                        final String fakedMail = email.getValue().stream().sorted((a, b) -> getRandom().ints(-1, 1).findFirst().getAsInt()).findAny().orElseThrow();

                                        // mark mail as inUse
                                        markMailToUse(email.getKey().getEmailAddress(), SysConst.BOOLEAN_TRUE);

                                        // increment counter
                                        incrementMailUsing(email.getKey().getEmailAddress());

                                        log.info("start new account creation 4 {}/{}", fakedMail, email.getKey());

                                        //email.setUsedTimes(email.getUsedTimes() + 1);
                                        // prepare instagram account
                                        final Account account = accountService.createAccount();

                                        account.setBatch(refsService.findBatch(defaultBatch));
                                        account.setAccountStatus(asCreated);
                                        account.setLogin(email.getKey().getEmailAddress().split("@")[0]);
                                        account.setEmail(email.getKey());
                                        account.setProxy(proxy);
                                        account.setFakedEmail(fakedMail);
                                        account.setBatch(refsService.findBatch(defaultBatch));
                                        account.setFirstName(javaFakerService.createFirstName());
                                        account.setLastName(javaFakerService.createLastName());
                                        account.setPwd(javaFakerService.createPassword());
                                        account.setYear(getRandom().ints(1988, 2000).findFirst().getAsInt());
                                        account.setMonth(getRandom().ints(1, 12).findFirst().getAsInt());
                                        account.setDay(getRandom().ints(1, 28).findFirst().getAsInt());

                                        // start account building
                                        this.emitEvent(account);

                                    });
                        }));
    }

    //==========================================================================
    private void incrementMailUsing(String emailAddess) {
        // mark mail as inUse
        final Email email
                = factory
                        .entrySet()
                        .stream()
                        .filter(e -> e.getKey().getEmailAddress().equals(emailAddess))
                        .findAny()
                        .orElseThrow()
                        .getKey();

        email.setUsedTimes(email.getUsedTimes() + 1);
    }

    //==========================================================================
    private void markMailToUse(String emailAddess, Boolean useType) {
        // mark mail as inUse
        factory
                .entrySet()
                .stream()
                .filter(e -> e.getKey().getEmailAddress().equals(emailAddess))
                .findAny()
                .orElseThrow()
                .getKey().setInUse(useType);
    }

    @Override
    protected void processEvent(Account account) {

        // separate thread
        StmtProcessor.runNewThread(() -> createAccount(account));
    }

    //==========================================================================
    //@Transactional
    private void createAccount(Account account) {

        StmtProcessor.execute(() -> {

            log.info("create insta Account '{}/{}'", account.getFakedEmail(), account.getEmail().getEmailAddress());

            // list of actions
            final Collection<BatchAction> actions = refsService.findBatchActions(account.getBatch().getBatchId());
            final Collection<AccountAction> executedActions = ServiceFuncs.<AccountAction>createCollection();

            for (final BatchAction batchAction : actions) {

                final Integer actRefId = batchAction.getAction().getActionRefId();
                final LocalDateTime start = LocalDateTime.now();

                //log.info("perform action {}, {}", batchAction.getClass(), actRefId);
                final Class clazz
                        = actionClasses
                                .stream()
                                .filter(cl -> actRefId.equals(AnnotationFuncs.<ActionCodeId>getAnnotation(cl, ActionCodeId.class).value()))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("Action not implemented yet: act_ref_id = " + batchAction.getAction().getActionRefId()));

                final AbstractAction action = genericApplicationContext.<AbstractAction>getBean(clazz);

                // execute action
                final ActionResult actionResult = action.execute(account);

                //log.info("{}: store result: '{}'", batchAction.getClass().getSimpleName(), actRefId);
                //store result
                executedActions.add(StmtProcessor.create(AccountAction.class, ea -> {

                    ea.setAccount(account);
                    ea.setActionStartDate(start);
                    ea.setActionFinishDate(LocalDateTime.now());
                    ea.setErrMsg(actionResult.getMessage());
                    ea.setActionsNotes(StringFuncs.truncString(actionResult.getNote(), 2000));
                    ea.setAction(refsService.findAction(actRefId));
                    ea.setProxy(account.getProxy());
                }));

                if (actionResult.getCode() < 0) {
                    log.error("{}: stop executing loop, actions code = {} ", action.getClass().getSimpleName(), actionResult.getCode());
                    break;

                } else {

                    StmtProcessor.sleep(StmtProcessor.notNull(batchAction.getActionDelay()) ? batchAction.getActionDelay() : 100);
                }

            }

            if (actions.size()
                    == executedActions.size()) {
                releaseFakedMail(account.getEmail().getEmailAddress(), account.getFakedEmail());
            } else {
                account.setFakedEmail(String.format("failed[%d]: %s",
                        NLS.localDateTime2long(LocalDateTime.now()),
                        account.getFakedEmail()));
            }

            proxyService.releaseProxy(account.getProxy().getProxyId());

            // release mails
            releaseMail(account.getEmail().getEmailAddress());

            // save all
            saveAccount(account, executedActions);

        });

    }
//==========================================================================

    @Transactional
    private synchronized void saveAccount(final Account account, Collection<AccountAction> executedActions) {

        accountService.saveAccount(account);
        //log.info("added new account: {}", account.getFakedEmail());
        accountService.saveActions(executedActions);
        //log.info("added new actions: {}", executedActions.size());

        accountsAttempts.incrementAndGet();

        StmtProcessor.ifTrue(account.getAccountStatus().getAccountStatusId().equals(AS_READY), () -> accountsCreated.incrementAndGet());

    }

    //==========================================================================
    private void releaseMail(String mail) {

        //log.info("release mail " + mail);
        markMailToUse(mail, SysConst.BOOLEAN_FALSE);

    }

    //==========================================================================
    private void releaseFakedMail(String mail, String fakedMail) {

        // release fake mails
        availAbleFakedMails.entrySet()
                .stream()
                .filter(fm -> fm.getKey().getEmailAddress().equals(mail))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("releaseFakedMail: not found " + mail))
                .getValue()
                .removeIf(e -> e.equals(fakedMail));
    }

    //==========================================================================
    public String getAndReserveAnyAvailableFakedMail() {

        final Map.Entry<Email, Collection<String>> entry
                = availAbleFakedMails.entrySet()
                        .stream()
                        .filter(en -> !en.getValue().isEmpty())
                        .skip(availAbleFakedMails.size() == 1 ? 0 : getRandom().ints(0, availAbleFakedMails.size() - 1).findFirst().getAsInt())
                        .findAny()
                        .orElseThrow();

        final String fakedMail = entry.getValue().stream().findAny().orElseThrow();
        final String email = entry.getKey().getEmailAddress();

        log.info("getAndReserveAnyAvailableFakedMail: {}/{}", email, fakedMail);

        releaseFakedMail(email, fakedMail);

        return email + ":" + fakedMail;
    }

    //==========================================================================
    public void load() {

        // actualMails
        final Collection<Email> collection = emailService.findActualEmails();
        //email_sharing_limit
        // TODO - учесть количство дней карантина
        final Collection<Account> limitAccounts = accountService.findLimitAccounts();
        log.info("find share limited accounts  = {}", limitAccounts.size());
        //another used accounts
        final Collection<Account> anotherUsedAccounts = accountService.findAnotherUsedAccounts();
        log.info("find another used accounts  = {}", anotherUsedAccounts.size());

        // availAble mails
        final Collection<Email> actualMailsList
                = collection
                        .stream()
                        // skip limit share
                        .filter(m -> limitAccounts.stream().filter(a -> a.getEmail().getEmailAddress().equals(m.getEmailAddress())).findFirst().isEmpty())
                        // skip another used
                        .filter(m -> anotherUsedAccounts.stream().filter(a -> a.getEmail().getEmailAddress().equals(m.getEmailAddress())).findFirst().isEmpty())
                        .collect(Collectors.toList());

        log.info("available mails: {}/{}", actualMailsList.size(), collection.size());

        if (actualMailsList.size() < 20) {
            log.info("available mails: {}", actualMailsList);
        }

        actualMailsList
                .stream()
                .forEach(email -> registerEmail(email));

//                    final Collection<String> usedFakedEmails = accountBuilder.getUsedFakeMails(email.getEmailAddress());
//                    log.info("{}: used faked accounts: {}", email.getEmailAddress(), usedFakedEmails.size());
    }

    //==========================================================================
    @Transactional(readOnly = true)
    public void registerEmail(Email email) {

        if (!email.getEmailAddress().contains("@gmail.com")) {
            throw new RuntimeException("Bad email address specified - " + email.getEmailAddress());
        }

        final String mailLogin = email.getEmailAddress().split("@")[0];
        final String mailDomen = email.getEmailAddress().split("@")[1];

        // used faked accounts
        final Collection<String> usedFakedAccounts = accountService
                .findAccountsUsedFakedAccount(email)
                .stream()
                .map(a -> a.getFakedEmail())
                .collect(Collectors.toList());

        //log.info("{}: used faked accounts: {}, {}", email.getEmailAddress(), usedFakedAccounts.size(), email.getEmailStatus().getEmailStatusName());
        // faked accounts
        availAbleFakedMails.put(
                email,
                emailService
                        .buildFakedLogins(mailLogin, fakedAccountLimit)
                        .stream()
                        .skip(2)
                        .map(m -> m.concat("@").concat(mailDomen))
                        .filter(fl -> !usedFakedAccounts.contains(fl))
                        .collect(Collectors.toList()));

        // future created accounts
        factory.put(email, ServiceFuncs.<Account>createConcurencyCollection());

    }
}
