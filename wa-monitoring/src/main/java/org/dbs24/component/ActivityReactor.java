/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.entity.SubscriptionActivity;
import org.dbs24.entity.SubscriptionPhoneActivity;
import org.dbs24.entity.UserSubscription;
import org.dbs24.entity.dto.PhoneNumLoginTokenDto;
import org.dbs24.reactor.AbstractHotSubscriber;
import org.dbs24.repository.ActivityArcRepository;
import org.dbs24.repository.ActivityPhoneRepository;
import org.dbs24.repository.ActivityRepository;
import org.dbs24.rest.api.*;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static org.dbs24.application.core.locale.NLS.localDateTime2long;
import static org.dbs24.consts.WaConsts.Classes.LAST_SESSION_INFO_CLASS;
import static org.dbs24.consts.WaConsts.Classes.SUBSCRIPTION_ACTIVITY_CLASS;
import static org.dbs24.consts.WaConsts.Kafka.KAFKA_SUBSCRIPTION_ACTIVITIES;
import static org.dbs24.kafka.consts.KafkaConsts.RLC.LS_ID;
import static org.dbs24.stmt.StmtProcessor.*;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
@EqualsAndHashCode(callSuper = true)
public class ActivityReactor extends AbstractHotSubscriber<ActivityInfo> {

    @Value("${config.activities.shelf-life.term:30}")
    private Integer actShelfLifeTerm;
    @Value("${config.activities.shelf-life.arc-term:60}")
    private Integer actShelfLifeArcTerm;
    @Value("${config.history-data.shelf-life.term:90}")
    private Integer actHistDataShelfLifeTerm;

    final Collection<ActivityInfoBox> infoNotes;
    final PersistenceService persistenceEntityManager;
    final ActivityRepository activityRepository;
    final ActivityArcRepository activityArcRepository;
    final UserSubscriptionsService userSubscriptionsService;
    final ActivityPhoneRepository activityPhoneRepository;

    private AtomicInteger activityCount = create(AtomicInteger.class);

    private Integer defaultSubscriptionId;

    public ActivityReactor(PersistenceService persistenceEntityManager, ActivityRepository activityRepository, UserSubscriptionsService userSubscriptionsService, ActivityPhoneRepository activityPhoneRepository, ActivityArcRepository activityArcRepository) {
        this.infoNotes = ServiceFuncs.createConcurencyCollection();
        this.persistenceEntityManager = persistenceEntityManager;
        this.activityRepository = activityRepository;
        this.userSubscriptionsService = userSubscriptionsService;
        this.activityPhoneRepository = activityPhoneRepository;
        this.activityArcRepository = activityArcRepository;

        activityCount.set(0);
    }

    @KafkaListener(id = LS_ID, topics = KAFKA_SUBSCRIPTION_ACTIVITIES)
    public void receiveActiviies(Collection<ActivityRecord> records) {
        log.debug("{}: receive kaffka activities msg: {}", KAFKA_SUBSCRIPTION_ACTIVITIES, records.size());

        synchronized (ActivityReactor.class) {
            emitEvent(create(ActivityInfo.class, ai -> records.forEach(r -> ai.getRecords().add(r))));
        }
    }

    @Override
    protected void processEvent(ActivityInfo activityInfo) {

        log.debug("receive new activities ({} rec(s)", activityInfo.getRecords().size());
        infoNotes.add(create(ActivityInfoBox.class, box -> box.setRecords(activityInfo.getRecords())));
    }

    @Scheduled(fixedRateString = "${config.entity.visitnote.update.interval:5000}")
    public void saveActivity() {

        StmtProcessor.runNewThread(() -> {

            if (!infoNotes.isEmpty()) {

                infoNotes.removeIf(ActivityInfoBox::getIsAdded);

                final Collection<SubscriptionActivity> toStore = ServiceFuncs.<SubscriptionActivity>createCollection();

                infoNotes
                        .stream()
                        .filter(note -> !note.getIsAdded())
                        .map(this::getActivitiesList)
                        .forEach(record
                                -> record
                                .stream()
                                .filter(activity -> StmtProcessor.notNull(activity.getUserSubscription()))
                                .forEach(toStore::add));

                ifTrue(!toStore.isEmpty(), () -> {

                    activityRepository.saveAllAndFlush(toStore);

                    activityCount.getAndAdd(toStore.size());

                    log.debug("added: {}, total: {}",
                            toStore.size(), activityCount.get());
                });
            }

            if (!infoNotes.isEmpty()) {
                log.debug("remain running notes = {}", infoNotes.size());
            }
        });
    }

    public CreatedActivity createActivities(ActivityInfo activityInfo, Boolean userOraId) {

        log.info("try 2 add activities: ({} records)", activityInfo.getRecords().size());

        // ora_id -> subscription_id
        ifTrue(userOraId, ()
                -> { // search by old phone_id from oracle

            ifTrue(defaultSubscriptionId.equals(0),
                    () -> defaultSubscriptionId = userSubscriptionsService
                            .getUserSubscriptionRepository()
                            .findLastUserSubscription()
                            .getSubscriptionId());

            activityInfo
                    .getRecords()
                    .stream()
                    .forEach(activityRecord -> {

                        final Integer oraId = activityRecord.getSubscriptionId();

                        activityRecord.setSubscriptionId(defaultSubscriptionId);

                        final Optional<UserSubscription> optUserSubscription = userSubscriptionsService.getUserSubscriptionRepository().findByTmpOraId(oraId);

                        ifTrue(optUserSubscription.isPresent(), () -> activityRecord.setSubscriptionId(optUserSubscription.get().getSubscriptionId()));
                    });
        });

        emitEvent(activityInfo);

        log.info("created activities: ({} records)", activityInfo.getRecords().size());

        return create(CreatedActivity.class, createdActivity -> {
            createdActivity.setCode(0);
            createdActivity.setMessage(String.format("%s records", activityInfo.getRecords().size()));
        });
    }

    //==========================================================================
    public Collection<SubscriptionActivity> getActivitiesList(ActivityInfoBox activityInfoBox) {

        final Collection<SubscriptionActivity> collection
                = activityInfoBox
                .getRecords()
                .stream()
                .map(this::getActivity)
                .collect(Collectors.toList());

        activityInfoBox.setIsAdded(Boolean.TRUE);

        return collection;
    }

    //==========================================================================
    public SubscriptionActivity getActivity(ActivityRecord activityRecord) {
        return create(SUBSCRIPTION_ACTIVITY_CLASS,
                sa -> {

                    final Optional<UserSubscription> optUs = userSubscriptionsService.findUserSubscriptionOptional(activityRecord.getSubscriptionId());

                    optUs.ifPresentOrElse(sa::setUserSubscription,
                            () -> log.error("Can't find subscription - {}", activityRecord.getSubscriptionId()));

                    sa.setActualDate(NLS.long2LocalDateTime(activityRecord.getActualDate()));
                    sa.setIsOnline(activityRecord.getIsOnline());
                });
    }

    @Transactional(readOnly = true)
    public ActivityInfo getActivitiesD1D2(LocalDateTime d1, LocalDateTime d2, PhoneNumLoginTokenDto phoneNumLoginTokenDto) {

        return create(ActivityInfo.class, createdActivity -> {
            log.info("getActivities: find phone num {}, [{}, {}]), {}", phoneNumLoginTokenDto.getPhoneNumber(), d1, d2, phoneNumLoginTokenDto.getLoginToken());
            final UserSubscription userSubscription = findByPhoneNumLoginTokenDto(phoneNumLoginTokenDto);

            assertNotNull(
                    UserSubscription.class,
                    userSubscription,
                    String.format("userSubscription (loginToken=%s, phoneNum=%s)", phoneNumLoginTokenDto.getLoginToken(), phoneNumLoginTokenDto.getPhoneNumber())
            );
            assertNotNull(
                    UserSubscription.class,
                    userSubscription.getSubscriptionId(),
                    String.format("userSubscription (loginToken=%s, phoneNum=%s)", phoneNumLoginTokenDto.getLoginToken(), phoneNumLoginTokenDto.getPhoneNumber())
            );

            userSubscriptionsService
                    .getSubscriptionActivitiesD1D2(userSubscription, d1, d2)
                    .forEach(action -> createdActivity.getRecords().add(create(ActivityRecord.class, rec -> {
                        rec.setSubscriptionId(userSubscription.getSubscriptionId());
                        rec.setActualDate(localDateTime2long(action.getActualDate()));
                        rec.setIsOnline(action.getIsOnline());
                    })));

            log.info("loginToken {}, phoneNum = {}, records {}", phoneNumLoginTokenDto.getLoginToken(), phoneNumLoginTokenDto.getPhoneNumber(), createdActivity.getRecords().size());
        });
    }

    @Transactional(readOnly = true)
    public LastSessionInfo getLatestActivities(PhoneNumLoginTokenDto phoneNumLoginTokenDto) {

        final String phoneNum = phoneNumLoginTokenDto.getPhoneNumber();
        final String loginToken = phoneNumLoginTokenDto.getLoginToken();

        Assert.isTrue(!phoneNum.isEmpty(), "getLatestActivities: phone num is specified ");

        final UserSubscription userSubscription = userSubscriptionsService.findUserSubscription(loginToken, phoneNum);

        assertNotNull(UserSubscription.class, userSubscription, String.format("userSubscription (loginToken=%s, phoneNum=%s)", loginToken, phoneNum));

        assertNotNull(UserSubscription.class, userSubscription.getSubscriptionId(), String.format("userSubscription (loginToken=%s, phoneNum=%s)", loginToken, phoneNum));

        final Collection<SubscriptionActivity> latestActivities
                = userSubscriptionsService
                .getSubscriptionLastActivity(userSubscription, 50);

        log.debug("getLatestActivities: {}/{}: {} recs", phoneNum, loginToken, latestActivities.size());

        return getSessionInfo(latestActivities,
                userSubscription.getActualDate(),
                userSubscription.getUserContract().getActualDate());
    }

    private LastSessionInfo getSessionInfo(Collection<SubscriptionActivity> latestActivities, LocalDateTime subsActualDate, LocalDateTime contractActualDate) {

        return create(LAST_SESSION_INFO_CLASS, sessionInfo ->

                ifTrue(latestActivities.isEmpty(), () -> {

                    sessionInfo.setLastSessionStart(null);
                    sessionInfo.setLastSessionFinish(null);

                }, () -> {
                    // not empty

                    Assert.isTrue(!latestActivities.isEmpty(), "getLatestActivities: latestActivities is empty");

                    final Boolean lastIsOnline = latestActivities
                            .stream()
                            .sorted((a, b) -> b.getActualDate().compareTo(a.getActualDate()))
                            .limit(1)
                            .findFirst()
                            .map(SubscriptionActivity::getIsOnline)
                            .orElseThrow(() -> new RuntimeException(String.format("Cant find any activites (%s records) ", latestActivities.size())));

                    if (lastIsOnline) {

                        // last off
                        final Optional<SubscriptionActivity> lastOff
                                = latestActivities
                                .stream()
                                .filter(a -> !a.getIsOnline())
                                .sorted((a, b) -> b.getActualDate().compareTo(a.getActualDate()))
                                .limit(1)
                                .findFirst();

                        final LocalDateTime border = lastOff.isPresent() ? lastOff.orElseThrow().getActualDate() : subsActualDate.minusWeeks(1);

                        // first on
                        final LocalDateTime firstOnline
                                = latestActivities
                                .stream()
                                .filter(a -> a.getIsOnline() && a.getActualDate().compareTo(border) > 0)
                                .sorted((a, b) -> a.getActualDate().compareTo(b.getActualDate()))
                                .limit(1)
                                .findFirst()
                                .map(SubscriptionActivity::getActualDate)
                                .orElse(null);

                        sessionInfo.setLastSessionStart(localDateTime2long(firstOnline));
                        sessionInfo.setLastSessionFinish(localDateTime2long(null));

                    } else {
                        // lastIsOnline = false

                        // last on
                        final Optional<SubscriptionActivity> lastOn
                                = latestActivities
                                .stream()
                                .filter(SubscriptionActivity::getIsOnline)
                                .sorted((a, b) -> b.getActualDate().compareTo(a.getActualDate()))
                                .limit(1)
                                .findFirst();

                        final LocalDateTime startContractDate = contractActualDate;

                        final LocalDateTime border = lastOn.isPresent() ? lastOn.orElseThrow().getActualDate() : startContractDate;

                        // first off
                        final LocalDateTime firstOffline
                                = latestActivities
                                .stream()
                                .filter(a -> !a.getIsOnline() && a.getActualDate().compareTo(border) > 0)
                                .sorted((a, b) -> a.getActualDate().compareTo(b.getActualDate()))
                                .limit(1)
                                .findFirst()
                                .map(SubscriptionActivity::getActualDate)
                                .orElse(null);

                        sessionInfo.setLastSessionStart(localDateTime2long(border.equals(startContractDate) ? null : border));
                        sessionInfo.setLastSessionFinish(localDateTime2long(border.equals(startContractDate) ? null : firstOffline));
                    }
                })
        );
    }

    @Transactional(readOnly = true)
    public SubscriptionsSessions getSubscriptionsLatestActivities(String loginToken) {

        return create(SubscriptionsSessions.class, ss -> {

            final Collection<SubscriptionPhoneActivity> activities =
                    activityPhoneRepository.findLatestSubscriptionsActivities(loginToken, now().minusDays(2));

            activities
                    .stream()
                    .map(SubscriptionPhoneActivity::getSubscription_id)
                    .distinct()
                    .map(m -> activities.stream().filter(activity -> activity.getSubscription_id().equals(m)).findFirst().orElseThrow())
                    .forEach(m -> ss.getSessions().add(create(SubscriptionSessionInfo.class, ssi -> {

                        final LastSessionInfo lastSessionInfo = getSessionInfo(getSubscriptionActivities(activities, m.getSubscription_id()), m.getSubscription_actual_date(), m.getContract_actual_date());

                        ssi.setSubscriptionId(m.getSubscription_id());
                        ssi.setPhoneNum(m.getPhone_num());
                        ssi.setLastSessionStart(lastSessionInfo.getLastSessionStart());
                        ssi.setLastSessionFinish(lastSessionInfo.getLastSessionFinish());

                    })));
            activities.clear();
        });
    }

    //------------------------------------------------------------------------------------------------------------------
    private Collection<SubscriptionActivity> getSubscriptionActivities(Collection<SubscriptionPhoneActivity> activities, Integer subscriptionId) {
        return activities
                .stream()
                .filter(activity -> activity.getSubscription_id().equals(subscriptionId))
                .map(this::toSubscriptionActivity)
                .collect(Collectors.toList());
    }

    //------------------------------------------------------------------------------------------------------------------
    private SubscriptionActivity toSubscriptionActivity(SubscriptionPhoneActivity subscriptionPhoneActivity) {
        return create(SubscriptionActivity.class, subscriptionActivity -> {

            subscriptionActivity.setActivityId(subscriptionPhoneActivity.getActivity_id());
            subscriptionActivity.setUserSubscription(null);
            subscriptionActivity.setActualDate(subscriptionPhoneActivity.getActual_date());
            subscriptionActivity.setIsOnline(subscriptionPhoneActivity.getIs_online());

        });
    }

    //------------------------------------------------------------------------------------------------------------------
    private UserSubscription findByPhoneNumLoginTokenDto(PhoneNumLoginTokenDto phoneNumLoginTokenDto) {
        String phoneNumber = phoneNumLoginTokenDto.getPhoneNumber();
        String loginToken = phoneNumLoginTokenDto.getLoginToken();

        final UserSubscription userSubscription = userSubscriptionsService.findUserSubscription(loginToken,
                phoneNumber);

        assertNotNull(
                UserSubscription.class,
                userSubscription,
                String.format("userSubscription (loginToken=%s, phoneNum=%s)", loginToken, phoneNumber)
        );
        assertNotNull(
                UserSubscription.class,
                userSubscription.getSubscriptionId(),
                String.format("userSubscription (loginToken=%s, phoneNum=%s)", loginToken, phoneNumber)
        );

        return userSubscription;
    }

    public Boolean isBusy() {
        return !infoNotes.isEmpty();
    }

    @Scheduled(fixedRateString = "${config.activities.shelf-life.period:3600000}", cron = "${config.activities.processing-cron:}")
    public void deleteDeprecatedActivities() {

        final var arcDate = now().minusDays(actShelfLifeTerm);
        final var deprecatedDate = arcDate.minusDays(actShelfLifeArcTerm);

        log.debug("process deprecated activities (work repo, before {}, {} days)", arcDate, actShelfLifeTerm);
        log.debug("process deprecated activities (arc repo, before {}, {} days)", deprecatedDate, actShelfLifeTerm + actShelfLifeArcTerm);

        activityRepository.deleteDeprecatedActivities(arcDate, deprecatedDate);

        // history data
        final var deprecateHistDate = now().minusDays(actHistDataShelfLifeTerm);
        log.debug("process deprecated hist data (before {}, {} days)", deprecateHistDate, actHistDataShelfLifeTerm);

        activityRepository.deleteDeprecatedHistData(deprecateHistDate);

    }
}
