/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.entity.*;
import org.dbs24.kafka.KafkaService;
import org.dbs24.repository.ActivityRepository;
import org.dbs24.repository.UserSubscriptionHistRepository;
import org.dbs24.repository.UserSubscriptionRepository;
import org.dbs24.rest.api.*;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.spring.core.api.ServiceLocator;
import org.dbs24.spring.core.data.PageLoader;
import org.dbs24.stmt.StmtProcessor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Comparator.comparing;
import static org.dbs24.application.core.locale.NLS.localDateTime2String;
import static org.dbs24.application.core.locale.NLS.localDateTime2long;
import static org.dbs24.application.core.service.funcs.ServiceFuncs.createConcurencyCollection;
import static org.dbs24.component.UserContractsService.isFutured;
import static org.dbs24.consts.SysConst.*;
import static org.dbs24.consts.WaConsts.Classes.ACTION_RESULT_CLASS;
import static org.dbs24.consts.WaConsts.Classes.USER_SUBSCRIPTION_CLASS;
import static org.dbs24.consts.WaConsts.OperCode.*;
import static org.dbs24.consts.WaConsts.References.*;
import static org.dbs24.rest.api.consts.RestApiConst.OperCode.OC_OK;
import static org.dbs24.rest.api.consts.RestApiConst.OperCode.OC_OK_STR;
import static org.dbs24.stmt.StmtProcessor.*;

@Getter
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
@EqualsAndHashCode(callSuper = true)
public class UserSubscriptionsService extends AbstractApplicationService {

    @Value("${config.wa.subscription.min-liveness-time:600000}")
    private Integer minSubscriptionLivenessTime;

    final RefsService refsService;
    final UsersService usersService;
    final UserSubscriptionRepository userSubscriptionRepository;
    final UserSubscriptionHistRepository userSubscriptionHistRepository;
    final ActivityRepository activityRepository;
    final KafkaService kafkaService;
    final ModelMapper modelMapper;
    final AvatarsService avatarsService;

    public UserSubscriptionsService(UserSubscriptionRepository userSubscriptionRepository, UserSubscriptionHistRepository userSubscriptionHistRepository, ActivityRepository activityRepository, UsersService usersService, RefsService refsService, KafkaService kafkaService, ModelMapper modelMapper, AvatarsService avatarsService) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.userSubscriptionHistRepository = userSubscriptionHistRepository;
        this.activityRepository = activityRepository;
        this.usersService = usersService;
        this.refsService = refsService;
        this.kafkaService = kafkaService;
        this.modelMapper = modelMapper;
        this.avatarsService = avatarsService;
    }

    static final Predicate<UserSubscription> isConfirmedSubsription = userSubscription -> userSubscription.getSubscriptionStatus().getSubscriptionStatusId().equals(SS_CONFIRMED);
    static final Predicate<UserSubscription> isCreatedSubsription = userSubscription -> userSubscription.getSubscriptionStatus().getSubscriptionStatusId().equals(SS_CREATED);
    static final Predicate<UserSubscription> isClosedSubsription = userSubscription -> userSubscription.getSubscriptionStatus().getSubscriptionStatusId().equals(SS_CLOSED);
    static final Predicate<UserSubscription> isCancelledSubsription = userSubscription -> userSubscription.getSubscriptionStatus().getSubscriptionStatusId().equals(SS_CANCELLED);
    static final Predicate<UserSubscription> phoneNotExistsSubsription = userSubscription -> userSubscription.getSubscriptionStatus().getSubscriptionStatusId().equals(SS_PHONE_NOT_EXISTS);

    public void saveUserSubscription(UserSubscription userSubscription, Boolean notifyKaffka) {
        userSubscriptionRepository.saveAndFlush(userSubscription);

        log.debug("save subscription: {}", userSubscription.getSubscriptionId());

        ifTrue(notifyKaffka,
                () -> kafkaService.notifyModifiedSubscription(modelMapper.map(userSubscription, UserSubscriptionInfo.class)));
    }

    public void saveUserSubscriptionSilent(UserSubscription userSubscription, Boolean notifyKaffka) {
        userSubscriptionRepository.saveAndFlush(userSubscription);
        ifTrue(notifyKaffka,
                () -> kafkaService.notifyModifiedSubscription(modelMapper.map(userSubscription, UserSubscriptionInfo.class)));
    }

    public UserSubscription createUserSubscription() {
        return StmtProcessor.create(USER_SUBSCRIPTION_CLASS, a -> {
            a.setActualDate(now());
            a.setSubscriptionStatus(refsService.findSubscriptionStatus(SS_CREATED));
        });
    }

    public Optional<UserSubscription> findUserSubscriptionOptional(Integer subscriptionId) {

        return userSubscriptionRepository.findById(subscriptionId);
    }

    public UserSubscription findUserSubscription(Integer subscriptionId) {

        return userSubscriptionRepository.findById(subscriptionId).orElseThrow(() -> new RuntimeException(format("Can't find subscription - %d ", subscriptionId)));
    }

    public UserSubscription findUserSubscription(String userLoginToken, String subscriptionPhone) {

        // getting user
        final var user = usersService.findUserByLoginToken(userLoginToken);

        return userSubscriptionRepository.findByUserAndPhoneNum(user, subscriptionPhone)
                .stream()
                .sorted((a, b) -> b.getSubscriptionId().compareTo(a.getSubscriptionId()))
                .limit(1)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(format("No passed subscriptions found (loginToken=%s, phoneNum=%s)", userLoginToken, subscriptionPhone)));

    }

    public Integer findActualSubscriptionsAmount(UserContract userContract) {
        StmtProcessor.assertNotNull(UserContract.class, userContract.getContractId(), "userContract.contractId is null");
        return userSubscriptionRepository.findActualSubscriptionsAmount(userContract.getContractId());
    }

    public Collection<UserSubscription> findActualSubscriptions(UserContract userContract) {
        StmtProcessor.assertNotNull(UserContract.class, userContract.getContractId(), "userContract.contractId is null");
        return userSubscriptionRepository.findActualSubscriptions(userContract.getContractId());
    }

    public Collection<UserSubscription> findAgentSubscriptions(Agent agent, Boolean actualOnly) {

        return userSubscriptionRepository.findByAgent(agent.getAgentId(), actualOnly.compareTo(false));
    }

    public Collection<UserSubscription> findAllUserSubscriptions(String userLoginToken, Boolean confirmedOnly) {

        // getting user
        final var user = usersService.findUserByLoginToken(userLoginToken);

        return userSubscriptionRepository.findByUser(user).stream().filter(ss -> ((!confirmedOnly) || (ss.getSubscriptionStatus().getSubscriptionStatusId().equals(SS_CONFIRMED)))).collect(Collectors.toList());
    }

    public Collection<UserSubscription> findAllDeprecatedUserSubscriptions(String userLoginToken) {

        return findAllUserSubscriptions(userLoginToken, BOOLEAN_FALSE)
                .stream()
                .filter(isClosedSubsription)
                .collect(Collectors.toList());

    }

    public Collection<UserSubscription> findInvalidSubscriptions() {

        return userSubscriptionRepository.findInvalidSubscriptions();
    }

    public Collection<UserSubscription> findInvalidActivitySubscriptions(Integer hours) {

        final var limit = now().minusHours(hours);

        return userSubscriptionRepository.findInvalidActivitySubscriptions(limit);
    }

    public Collection<UserSubscription> findActualUserSubscriptions(String userLoginToken) {

        // getting user
        final var user = usersService.findUserByLoginToken(userLoginToken);

        return userSubscriptionRepository.findByUser(user).stream().filter(ss
                -> ss.getSubscriptionStatus().getSubscriptionStatusId().equals(SS_CREATED)
                || ss.getSubscriptionStatus().getSubscriptionStatusId().equals(SS_CONFIRMED)
                || ss.getSubscriptionStatus().getSubscriptionStatusId().equals(SS_PHONE_NOT_EXISTS)).collect(Collectors.toList());
    }

    public Collection<UserSubscription> findAllUserSubscriptions(Integer userId, Boolean actualOnly) {

        // getting user
        final var user = usersService.findUser(userId);

        return userSubscriptionRepository.findByUser(user).stream().filter(ss -> ((!actualOnly) || (ss.getSubscriptionStatus().getSubscriptionStatusId().equals(SS_CONFIRMED)))).collect(Collectors.toList());
    }

    public Collection<UserSubscription> findExistsUserSubscriptions(User user, String subscriptionPhone) {

        return userSubscriptionRepository.findByUserAndPhoneNum(user, subscriptionPhone)
                .stream()
                .collect(Collectors.toList());
    }

    public Collection<UserSubscription> findSubscriptionsByPhoneNum(String phoneMask) {
        return userSubscriptionRepository.findByPhoneNumStartingWith(phoneMask);
    }

    //==========================================================================
    public void saveUserSubscriptionHist(UserSubscriptionHist userSubscriptionHist) {
        userSubscriptionHistRepository.save(userSubscriptionHist);
    }

    public void saveUserSubscriptionHist(UserSubscription userSubscription) {
        Optional.ofNullable(userSubscription.getSubscriptionId())
                .ifPresent(id -> saveUserSubscriptionHist(getModelMapper().map(userSubscription, UserSubscriptionHist.class)));
    }

    public Collection<UserSubscription> findValidUserSubscriptions(User user, String subscriptionPhone) {

        return userSubscriptionRepository.findByUserAndPhoneNumAndSubscriptionStatus(
                user,
                subscriptionPhone,
                refsService.findSubscriptionStatus(SS_CONFIRMED)
        );
    }

    //==========================================================================
    public Collection<SubscriptionActivity> getSubscriptionActivitiesD1D2(UserSubscription userSubscription, LocalDateTime d1, LocalDateTime d2) {

        return activityRepository
                .findByUserSubscriptionAndActualDateBetween(userSubscription, d1, d2)
                .stream()
                .sorted((a, b) -> b.getActualDate().compareTo(a.getActualDate()))
                .collect(Collectors.toList());

    }

    //==========================================================================
    // 4 users request
    public Collection<SubscriptionActivity> getSubscriptionLastActivity(UserSubscription userSubscription, Integer limit) {

        return activityRepository.findLatestActivities(userSubscription, limit)
                .stream()
                .sorted((a, b) -> a.getActualDate().compareTo(b.getActualDate()))
                .collect(Collectors.toList());
    }

    //==========================================================================
    final Function<Integer, SubscriptionActivity> createFinalActivity = subscriptionId ->
            StmtProcessor.create(SubscriptionActivity.class, ar -> {
                ar.setUserSubscription(findUserSubscription(subscriptionId));
                ar.setActualDate(now());
                ar.setIsOnline(BOOLEAN_FALSE);
            });


    public void closeSubscription(UserSubscription userSubscription) {
        log.info("close subscription {} ", userSubscription.getSubscriptionId());
        // faked cancel of subscripotion
        createAndSaveFakedInactivity(userSubscription);
        saveUserSubscriptionHist(userSubscription);
        userSubscription.setSubscriptionStatus(refsService.findSubscriptionStatus(SS_CLOSED));
        userSubscription.setActualDate(now());
        saveUserSubscription(userSubscription, BOOLEAN_TRUE);
    }

    //==========================================================================
    @Transactional
    public CreatedAvatar createOrUpdateAvatar(AvatarInfo avatarInfo) {
        return StmtProcessor.create(CreatedAvatar.class,
                avatar -> {

                    log.info("create/update avatar (subscriptionId = {}, avatar_id = {}) ", avatarInfo.getSubscriptionId(), avatarInfo.getAvatarId());

                    final var userSubscription
                            = findUserSubscription(avatarInfo.getSubscriptionId());

                    // store 2 history
                    saveUserSubscriptionHist(userSubscription);

                    // exist subscription
                    ifNotNull(userSubscription.getSubscriptionId(), () -> {

                        userSubscription.setAvatar(avatarInfo.getAvatarImg());
                        userSubscription.setAvatarId(avatarInfo.getAvatarId());
                        userSubscription.setActualDate(now());
                        userSubscription.setAvatarModifyDate(userSubscription.getActualDate());

                    }, () -> {
                        userSubscription.setAvatar(avatarInfo.getAvatarImg());
                        userSubscription.setAvatarId(avatarInfo.getAvatarId());
                        userSubscription.setActualDate(now());
                        userSubscription.setAvatarModifyDate(userSubscription.getActualDate());
                    });

                    saveUserSubscriptionSilent(userSubscription, TRUE);

                    // answer
                    avatar.setAvatarId(userSubscription.getAvatarId());

                });
    }

    //==========================================================================
    @Transactional
    public CreatedAvatar createOrUpdateCustomAvatar(AvatarInfoBySubscriptionPhone avatarInfo, String loginToken, String phone) {
        return create(CreatedAvatar.class,
                avatar -> {

                    log.info("create/update custom avatar ({}, {}) ", loginToken, phone);

                    final var userSubscription
                            = findUserSubscription(loginToken, phone);

                    // store 2 history
                    saveUserSubscriptionHist(userSubscription);
                    userSubscription.setActualDate(now());
                    userSubscription.setCustomAvatar(avatarInfo.getAvatarImg());
                    userSubscription.setAvatarModifyDate(userSubscription.getActualDate());

                    ifNotNull(avatarInfo.getAvatarImg(), img ->
                            avatarsService.downsizeImage(img)
                                    .subscribe(userSubscription::setCustomAvatar));

                    // store
                    saveUserSubscriptionSilent(userSubscription, FALSE);

                    avatar.setAvatarId(userSubscription.getAvatarId());

                });
    }

    //==========================================================================
    @Transactional(readOnly = true)
    public AvatarInfo getAvatar(Integer subscriptionId) {
        return create(AvatarInfo.class,
                avatarInfo -> {

                    log.info("getting avatar (subscriptionId = {}) ", subscriptionId);

                    final var userSubscription = findUserSubscription(subscriptionId);

                    avatarInfo.setAvatarId(userSubscription.getAvatarId());
                    avatarInfo.setAvatarImg(nvl(userSubscription.getCustomAvatar(), userSubscription.getAvatar()));
                    avatarInfo.setSubscriptionId(userSubscription.getSubscriptionId());
                    avatarInfo.setCustomAvatar(notNull(userSubscription.getCustomAvatar()));
                    avatarInfo.setAvatarModifyDate(localDateTime2long(userSubscription.getAvatarModifyDate()));

                });
    }

    //==========================================================================
    @Transactional(readOnly = true)
    public AvatarInfoBySubscriptionPhone getCustomAvatar(String userLoginToken, String subscriptionPhone) {
        return create(AvatarInfoBySubscriptionPhone.class,
                avatar -> {

                    log.info("getting custom avatar ({}/{}) ", userLoginToken, subscriptionPhone);

                    final var userSubscription = findUserSubscription(userLoginToken, subscriptionPhone);

                    avatar.setAvatarId(userSubscription.getAvatarId());
                    avatar.setAvatarImg(nvl(userSubscription.getCustomAvatar(), userSubscription.getAvatar()));
                    avatar.setAvatarModifyDate(localDateTime2long(userSubscription.getAvatarModifyDate()));

                });
    }

    public Collection<UserSubscription> findByUserContract(UserContract userContract) {

        return userSubscriptionRepository.findByUserContract(userContract);
    }

    public UserSubscription createSubscriptionBasedOnNewContract(UserSubscription subscription, UserContract newContract) {

        return create(UserSubscription.class, newSubscription -> {
            newSubscription.setActualDate(subscription.getActualDate());
            newSubscription.setCreateDate(subscription.getActualDate());
            newSubscription.setLastStatusChangeDate(subscription.getActualDate());
            newSubscription.setSubscriptionName(subscription.getSubscriptionName());
            newSubscription.setPhoneNum(subscription.getPhoneNum());
            newSubscription.setUser(subscription.getUser());
            newSubscription.setAgent(subscription.getAgent());
            newSubscription.setUserContract(newContract);
            newSubscription.setSubscriptionStatus(subscription.getSubscriptionStatus());
            newSubscription.setOnlineNotify(subscription.getOnlineNotify());
            newSubscription.setAvatar(subscription.getAvatar());
            newSubscription.setAvatarId(subscription.getAvatarId());
            newSubscription.setTmpOraId(subscription.getTmpOraId());
        });
    }

    //==========================================================================
    @Transactional
    public CreatedUserSubscription createOrUpdateSubscription(UserSubscriptionInfo userSubscriptionInfo, String loginToken) {

        return create(CreatedUserSubscription.class, createdUserSubscription -> {

            log.debug("createOrUpdateSubscription: {}", userSubscriptionInfo);

            // back door
            final var userContractsService = ServiceLocator.findService(UserContractsService.class);
            final var agentsService = ServiceLocator.findService(AgentsService.class);

            final var user = usersService.findUserByLoginToken(loginToken);
            final var subsPhone = userSubscriptionInfo.getPhoneNum().replaceAll("[^\\d.]", "");

            createdUserSubscription.setAnswerCode(OC_OK);
            createdUserSubscription.setNote("OK");

            // find all matched subscriptions
            final var existSubscriptions
                    = findExistsUserSubscriptions(user, subsPhone);

            log.debug("existSubscriptions (userId={}, phoneNum={}, existSubscriptions.size={})",
                    user.getUserId(), subsPhone, existSubscriptions.size());

            // new empty subscription
            ifTrue(existSubscriptions.isEmpty(), () -> existSubscriptions.add(createUserSubscription()));

            existSubscriptions
                    .stream()
                    .skip(1)
                    // drop duplicates
                    .forEach(invalidSubscription -> {

                        log.warn("drop exists subscription ({}, set status = -1)", invalidSubscription.getSubscriptionId());

                        saveUserSubscriptionHist(invalidSubscription);
                        invalidSubscription.setSubscriptionStatus(refsService.findSubscriptionStatus(SS_CANCELLED));
                        invalidSubscription.setActualDate(now());
                        saveUserSubscription(invalidSubscription, BOOLEAN_TRUE);
                        createAndSaveFakedInactivity(invalidSubscription);

                    });

            // actual user contracts
            final var userContracts = userContractsService.findActualUserContracts(user);

            // latest actual contracts
            final var optUserContract = userContracts.stream()
                    .max(comparing(UserContract::getBeginDate));

            existSubscriptions
                    .forEach(userSubscription -> {

                                // cancel subscription
                                ifNotNull(userSubscriptionInfo.getSubscriptionStatusId(), () ->
                                        ifNotNull(userSubscription.getSubscriptionId(), () ->
                                                ifTrue(userSubscriptionInfo.getSubscriptionStatusId().equals(SS_CANCELLED)
                                                                && !phoneNotExistsSubsription.test(userSubscription), () -> {

                                                            var nextAllowedUpdate = userSubscription.getCreateDate().plusSeconds(minSubscriptionLivenessTime / 1000);

                                                            log.debug("subscriptionId: {}, nextAllowedUpdate: {}, minTrackingTime: {}", userSubscription.getSubscriptionId(), nextAllowedUpdate, minSubscriptionLivenessTime);

                                                            ifTrue(nextAllowedUpdate.compareTo(now()) > 0, () -> {

                                                                var errMsg = format("operation is not available until '%s' ", localDateTime2String(nextAllowedUpdate)).toUpperCase();

                                                                log.warn("SubscriptionId: {} - cancelling not allowed: {}", userSubscription.getSubscriptionId(), errMsg);

                                                                createdUserSubscription.setAllowedActionDate(localDateTime2long(nextAllowedUpdate));
                                                                createdUserSubscription.setAnswerCode(OC_OPERATION_NOT_ALLOWED);
                                                                createdUserSubscription.setNote(errMsg);

                                                            });
                                                        }
                                                )));

                                ifTrue(createdUserSubscription.getAnswerCode().equals(OC_OK), () -> {

                                    ifTrue(optUserContract.isEmpty(), () -> {

                                        createdUserSubscription.setAnswerCode(OC_NO_ACTIVE_CONTRACT);
                                        createdUserSubscription.setNote(format("No active contracts for user_id = %d or contract is expired ", user.getUserId()));
                                        createdUserSubscription.setSubscriptionId(StmtProcessor.nvl(userSubscriptionInfo.getSubscriptionId(), 0));

                                        log.warn(createdUserSubscription.getNote());

                                    }, () -> {
                                        final var userContract = optUserContract.get();
                                        log.info("found latest actual userContract (contractId = {}, user_id = {})", userContract.getContractId(), user.getUserId());

                                        // status of added subscription
                                        final Predicate<UserSubscription> isNewSubscription =
                                                us -> isNull(us.getSubscriptionId());

                                        final Predicate<SubscriptionStatus> isRestored =
                                                ss -> isNull(ss)
                                                        ? BOOLEAN_FALSE
                                                        : ss.getSubscriptionStatusId().equals(SS_CANCELLED) || ss.getSubscriptionStatusId().equals(SS_CLOSED);

                                        final var createNew = isNewSubscription.test(userSubscription) || isRestored.test(userSubscription.getSubscriptionStatus());

                                        // new subscription - check limit
                                        ifTrue(createNew, () -> {

                                            // getting existing subscription
                                            final var existAmt = findActualUserSubscriptions(loginToken).size() + 1;
                                            final var existLimit = userContract.getSubscriptionsAmount();

                                            log.info("validate subscription limit: {}/{} (userContract = {})", existAmt, existLimit, userContract.getContractId());

                                            ifTrue(existLimit.compareTo((int) existAmt) < INTEGER_ZERO, () -> {

                                                final var errMsg = format("subscription limit reached (limit=%d, contractId=%d)",
                                                        existLimit, userContract.getContractId());

                                                createdUserSubscription.setAnswerCode(OC_LIMIT_REACHED);
                                                createdUserSubscription.setNote(errMsg);
                                                createdUserSubscription.setSubscriptionId(StmtProcessor.nvl(userSubscriptionInfo.getSubscriptionId(), 0));

                                                log.warn(errMsg);
                                            });
                                        });

                                        // no errors reached
                                        ifTrue(createdUserSubscription.getAnswerCode().equals(OC_OK), () -> {

                                            // store 2 history
                                            saveUserSubscriptionHist(userSubscription);
                                            // set new actual date
                                            userSubscription.setActualDate(now());

                                            // status of new subscription
                                            ifTrue(createNew, () -> {
                                                userSubscription.setCreateDate(now());
                                                userSubscription.setSubscriptionStatus(refsService.findSubscriptionStatus(SS_CREATED));
                                                userSubscription.setLastStatusChangeDate(userSubscription.getCreateDate());
                                            }, () -> ifTrue(!userSubscription.getSubscriptionStatus().getSubscriptionStatusId().equals(userSubscriptionInfo.getSubscriptionStatusId()),
                                                    () -> userSubscription.setLastStatusChangeDate(now())));

                                            // assign actualDate
                                            userSubscription.setActualDate(now());

                                            // assign or validate service Agent
                                            userSubscription.setAgent(agentsService.validateOrReplaceAgent(userSubscription.getAgent()));

                                            userSubscription.setOnlineNotify(userSubscriptionInfo.getOnlineNotify());
                                            userSubscription.setPhoneNum(subsPhone);
                                            userSubscription.setSubscriptionName(userSubscriptionInfo.getSubscriptionName());

                                            final var subscriptionStatusId = StmtProcessor.nvl(userSubscriptionInfo.getSubscriptionStatusId(),
                                                    isNull(userSubscription.getSubscriptionId()) ? 0 : userSubscription.getSubscriptionStatus().getSubscriptionStatusId());

                                            userSubscription.setSubscriptionStatus(refsService.findSubscriptionStatus(subscriptionStatusId));
                                            userSubscription.setUser(user);
                                            userSubscription.setUserContract(userContract);
                                            ifNotNull(userSubscriptionInfo.getAvatarId(), () -> userSubscription.setAvatarId(userSubscriptionInfo.getAvatarId()));

                                            saveUserSubscription(userSubscription, !isFutured.test(userContract));

                                            // cancel subscription
                                            ifTrue(isCancelledSubsription.test(userSubscription), () -> createAndSaveFakedInactivity(userSubscription));

                                            createdUserSubscription.setSubscriptionId(userSubscription.getSubscriptionId());

                                        });
                                    });
                                });
                            }
                    );

            log.debug("created/update subscription: {}", createdUserSubscription.getSubscriptionId());
            existSubscriptions.clear();
        });
    }

    @Transactional
    public ActionResult updateSubscriptionStatus(Integer subscriptionId, String stringSubscriptionStatus) {

        return create(ACTION_RESULT_CLASS, actionResult -> {

            final var newSubscriptionStatus = refsService.findSubscriptionStatus(Byte.valueOf(stringSubscriptionStatus));
            final var userSubscription = findUserSubscription(subscriptionId);

            // store 2 history
            saveUserSubscriptionHist(userSubscription);
            // set new subscription status
            userSubscription.setActualDate(now());
            userSubscription.setSubscriptionStatus(newSubscriptionStatus);
            userSubscription.setLastStatusChangeDate(userSubscription.getActualDate());
            // save
            saveUserSubscription(userSubscription, BOOLEAN_FALSE);

            final var msg = format("set new subscription status (subscriptionId=%d, new status=%s)", subscriptionId, newSubscriptionStatus.getSubscriptionStatusName());

            log.debug(msg);

            actionResult.setCode(INTEGER_ZERO);
            actionResult.setMessage(msg);

        });
    }

    private void createAndSaveFakedInactivity(UserSubscription userSubscription) {
        getActivityRepository()
                .saveAndFlush(getCreateFinalActivity().apply(userSubscription.getSubscriptionId()));
    }

    //==========================================================================
    public Collection<UserSubscription> loadAllSubscriptions(SubscriptionStatus subscriptionStatus) {

        final Collection<UserSubscription> subscriptions = createConcurencyCollection();

        (new PageLoader<UserSubscription>() {
            @Override
            public void addPage(Collection<UserSubscription> pageUserSubscriptions) {
                log.debug("load subscription ({}): {} records(s)", subscriptionStatus.getSubscriptionStatusId(), pageUserSubscriptions.size());
                subscriptions.addAll(pageUserSubscriptions);
            }
        }).loadRecords(userSubscriptionRepository, () -> (r, cq, cb) -> {

            final var predicate = cb.conjunction();

            predicate.getExpressions().add(cb.equal(r.get("subscriptionStatus"), subscriptionStatus.getSubscriptionStatusId()));

            return predicate;

        }, 1000);

        return subscriptions;
    }

    //=========================================================================
    public void saveUserSubscriptions(Collection<UserSubscription> userSubscriptions) {
        userSubscriptionRepository.saveAllAndFlush(userSubscriptions);

        //userSubscriptions.forEach(userSubscription -> kafkaService.notifyModifiedSubscription(StmtProcessor.create(UserSubscriptionInfo.class, usi -> usi.assign(userSubscription))));

    }

    //=========================================================================
    public void saveUserSubscriptionsHist(Collection<UserSubscriptionHist> userSubscriptions) {
        userSubscriptionHistRepository.saveAllAndFlush(userSubscriptions);
    }

    //==========================================================================
    public PrepareSubscriptionAnswer prepareAllActualUserSubsriptions(Integer pageSize) {

        final var minSize = 100;
        final var maxSize = 500;
        final var actualPageSize = Math.max(nvl(pageSize, maxSize) > maxSize ? maxSize : nvl(pageSize, maxSize), minSize);

        var prepareSubscriptionAnswer = StmtProcessor.create(PrepareSubscriptionAnswer.class, prepareSubscriptionAnswer1 -> {

            prepareSubscriptionAnswer1.setPageSize(actualPageSize);
            prepareSubscriptionAnswer1.setAnswerCode(OC_OK);
            prepareSubscriptionAnswer1.setNote(OC_OK_STR);

        });

        runNewThread(() -> {

                    // actual subscriptions
                    (new PageLoader<UserSubscription>() {
                        @Override
                        public void addPage(Collection<UserSubscription> pageUserSubscriptions) {

                            log.debug("send actual subscription ({}): {} records(s)", SS_CONFIRMED, pageUserSubscriptions.size());

                            pageUserSubscriptions.forEach(userSubscription -> kafkaService.existSubscription(modelMapper.map(userSubscription, UserSubscriptionInfo.class)));

                        }
                    }).loadRecords(userSubscriptionRepository, () -> (r, cq, cb) -> {

                        final var predicate = cb.conjunction();

                        predicate.getExpressions().add(cb.equal(r.get("subscriptionStatus"), SS_CONFIRMED));

                        return predicate;

                    }, actualPageSize);

                    // unhandled subscriptions
                    (new PageLoader<UserSubscription>() {
                        @Override
                        public void addPage(Collection<UserSubscription> pageUserSubscriptions) {

                            log.debug("send unhandled subscription: {} records(s)", pageUserSubscriptions.size());

                            pageUserSubscriptions.forEach(userSubscription -> kafkaService.existSubscription(modelMapper.map(userSubscription, UserSubscriptionInfo.class)));

                        }
                    }).loadRecords(userSubscriptionRepository::findUnhandledSubscriptions, actualPageSize);

                }
        );

        return prepareSubscriptionAnswer;
    }

    //==================================================================================================================
    public Collection<UserSubscription> findInsuranceSubscriptions(Integer limit) {
        return userSubscriptionRepository.findInsuranceSubscriptions(limit);
    }
}
