/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.entity.*;
import org.dbs24.rest.api.*;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static java.util.stream.Stream.generate;
import static org.dbs24.consts.SysConst.*;
import static org.dbs24.consts.WaConsts.Classes.CREATED_USER_CLASS;
import static org.dbs24.consts.WaConsts.References.*;
import static org.dbs24.stmt.StmtProcessor.*;

@Log4j2
@Component
@Getter
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
public class UserBuilderService extends AbstractApplicationService {

    final UserContractsService userContractsService;
    final UsersService usersService;
    final RefsService refsService;
    final UserDevicesService userDevicesService;
    final AgentsService agentsService;
    final UserSubscriptionsService userSubscriptionsService;

    public UserBuilderService(UserContractsService userContractsService, UsersService usersService, RefsService refsService, UserDevicesService userDevicesService, AgentsService agentsService, UserSubscriptionsService userSubscriptionsService) {
        this.userContractsService = userContractsService;
        this.usersService = usersService;
        this.refsService = refsService;
        this.userDevicesService = userDevicesService;
        this.agentsService = agentsService;
        this.userSubscriptionsService = userSubscriptionsService;
    }

    public static final Function<UserDeviceAndroid, Integer> androidDevice2int = device -> device.getDeviceId();
    public static final Function<UserDeviceIos, Integer> iosDevice2int = device -> device.getDeviceId();

    //==========================================================================
    final Function<AndroidAttrs, Void> assignFakedJsfId = androidAttrs -> {
        androidAttrs.setGsfId(String.format("android device without gsdId (created %s)", now().toString()));
        return null;
    };

    @Transactional
    public CreatedUser createOrUpdateMultiUser(UserAttrsInfo userAttrsInfo) {

        return create(CREATED_USER_CLASS, createdUser -> {

            log.info("=== CREATE/UPDATE USER: {}", userAttrsInfo);

            // validate attrs
            final Integer deviceTypeId = ofNullable(userAttrsInfo.getAndroidAttrs()).isPresent() ? DT_ANDROID : DT_IOS;

            ifTrue(deviceTypeId.equals(DT_ANDROID), () -> assertNotNull(AndroidAttrs.class, userAttrsInfo.getAndroidAttrs(), "AndroidAttrs not specified"));
            //ifTrue(deviceTypeId.equals(DT_ANDROID), () -> StmtProcessor.assertNotNull(AndroidAttrs.class, userAttrsInfo.getAndroidAttrs().getGsfId(), "gsfId attr is not specified"));
            // gsfId can be null
            ifTrue(deviceTypeId.equals(DT_ANDROID),
                    () -> ifNull(userAttrsInfo.getAndroidAttrs().getGsfId(),
                            () -> assignFakedJsfId.apply(userAttrsInfo.getAndroidAttrs()),
                            () -> ifTrue(userAttrsInfo.getAndroidAttrs().getGsfId().trim().isEmpty(), () -> assignFakedJsfId.apply(userAttrsInfo.getAndroidAttrs()))));

            ifTrue(deviceTypeId.equals(DT_IOS), () -> assertNotNull(IosAttrs.class, userAttrsInfo.getIosAttrs(), "IosAttrs not specified"));
            ifTrue(deviceTypeId.equals(DT_IOS), () -> assertNotNull(IosAttrs.class, userAttrsInfo.getIosAttrs().getIdentifierForVendor(), "identifierForVendor attr is not specified"));
            //ifTrue(deviceTypeId.equals(DT_IOS), () -> assertNotNull(IosAttrs.class, userAttrsInfo.getIosAttrs().getAppleId(), "appleId attr is not specified"));

            ifTrue(notNull(userAttrsInfo.getAndroidAttrs()) && notNull(userAttrsInfo.getIosAttrs()), () -> {
                throw new RuntimeException("Illegal userAttrsInfo config (AndroidAttrs||IosAttrs)");
            });

            final String loginToken = userAttrsInfo.getLoginToken();
            final Boolean nullLoginToken = isNull(loginToken);

            // find hidder user by device attrs
            final Optional<User> hiddenUser = findUserByDevice(userAttrsInfo, deviceTypeId);

            // detecting user by loginToken
            final Optional<User> userByLoginToken = !nullLoginToken ? usersService.findOptionalUserByLoginToken(loginToken) : Optional.empty();

            log.info("hiddenUser = {}, userByLoginToken = {}", hiddenUser, userByLoginToken);

            // some validation
            ifTrue(!nullLoginToken, () -> Assert.isTrue(userByLoginToken.isPresent(), String.format("User not exists (loginToken=%s)", loginToken)));

            // identity user 1) userByLoginToken 2) hiddenUser
            final User user = userByLoginToken.orElse(hiddenUser.orElse(usersService.findOrCreateUser(loginToken)));

            log.info("final user = {}, hiddenUser = {}, userByLoginToken = {}", user, hiddenUser, userByLoginToken);

            // store user 2 hist
            log.info("store user 2 history: {}", user);
            usersService.saveUserHist(user);

            user.setCountry(refsService.findCountry(112));

            ifNull(userAttrsInfo.getActualDate(), () -> userAttrsInfo.setActualDate(NLS.localDateTime2long(now())));

            user.setEmail(STRING_NULL);

            ofNullable(loginToken)
                    .ifPresent(login -> ifNull(user.getLoginToken(), () -> user.setLoginToken(login)));

            user.setUserName(NOT_DEFINED);
            user.setUserPhoneNum(NOT_DEFINED);
            user.setActualDate(NLS.long2LocalDateTime(userAttrsInfo.getActualDate()));

            log.debug("user 4 store: {}", user);

            // Abstract UserDevice
            final DeviceType deviceType = refsService.findDeviceType(deviceTypeId);

            log.debug("deviceTypeId: {}", deviceType);

            // all users devices
            final Collection<UserDevice> allUserDevices = userDevicesService.findAllUserDevices(hiddenUser.orElse(user));

            log.debug("allUserDevices: {}", allUserDevices.size());

            // find absctract device
            final UserDevice userDevice = findDevice(userAttrsInfo);

            //==================================================================
            log.debug("try 2 create/update user: {}", user);

            final Boolean isNewUser = isNull(user.getUserId());

            usersService.saveUser(user);

            // store device 2 hist
            userDevicesService.saveUserDeviceHist(userDevice);

            // update device
            userDevice.setActualDate(now());
            userDevice.setDeviceType(deviceType);
            userDevice.setMacAddr(userAttrsInfo.getMakAddr());
            userDevice.setAppName(userAttrsInfo.getAppName());
            userDevice.setAppVersion(userAttrsInfo.getAppVersion());

            // save abstract device
            userDevice.setUser(user);

            // move single devices to new user
            ifTrue(hiddenUser.isPresent()
                            && !hiddenUser.orElse(user).getUserId().equals(user.getUserId()),
                    () -> processCollection(allUserDevices, aud
                            -> aud
                            .stream()
                            .filter(ud -> ud.getDeviceId().equals(userDevice.getDeviceId())) // single device
                            .forEach(ud -> {
                                //userDevicesService.saveUserDeviceHist(ud);
                                ud.setUser(user);
                                userDevicesService.saveUserDevice(ud);
                            })));

            createdUser.setLoginToken(user.getLoginToken());
            createdUser.setDeviceId(userDevice.getDeviceId());

            // create and store trial contract
            StmtProcessor.ifTrue(isNewUser, () -> userContractsService.saveContract(userContractsService.createTrialContract(user, deviceType, user.getActualDate(), user.getActualDate())));

            //==================================================================
            log.info("try 2 create/update device_id: {}", userDevice);

            userDevicesService.saveUserDevice(userDevice);

            createdUser.setDeviceId(userDevice.getDeviceId());

            log.info("created/update userDevice: {}", userDevice);

            // ANDROID ATTRS
            ofNullable(userAttrsInfo.getAndroidAttrs())
                    .ifPresent(androidAttrs -> saveAndroidAttrs(userAttrsInfo, androidAttrs, userDevice.getDeviceId(), userDevice.getAppName(), BOOLEAN_TRUE));

            // IOS ATTRS
            ofNullable(userAttrsInfo.getIosAttrs())
                    .ifPresent(iosAttrs -> saveIosAttrs(userAttrsInfo, iosAttrs, userDevice.getDeviceId(), userDevice.getAppName(), BOOLEAN_TRUE));

        });
    }

    //@Scheduled(fixedRateString = "${config.test.create-users.period:60000}", cron = "${config.testing:}")
    public void perform() {
        generateTestUsers(1, TestFuncs.generateTestString("375", "9", "7"), 20);
    }

    @Transactional
    public UsersAmount generateTestUsers(Integer amount, String phoneMask, Integer activityLimit) {

        return create(UsersAmount.class, usersAmount -> {

            log.info("build users 4 test: {}", amount);

            usersAmount.setCreatedUsers(INTEGER_ZERO);

            final Collection<UserSubscription> userSubscriptions4test =
                    userSubscriptionsService
                            .getUserSubscriptionRepository()
                            .findByPhoneNumStartingWith(phoneMask)
                            .stream()
                            .filter(s -> !s.getSubscriptionStatus().getSubscriptionStatusId().equals(SS_PHONE_NOT_EXISTS))
                            .collect(Collectors.toList());


            log.info("build users 4 test: {}, mask: {}, activityLimit: {}", amount, phoneMask, activityLimit);


            ifTrue(!userSubscriptions4test.isEmpty(), () -> {

                generate(new Random()::nextInt)
                        .limit(amount)
                        .forEach(record -> {

                            final User user = usersService.findOrCreateUser(STRING_NULL);

                            user.setActualDate(now());
                            user.setUserName(String.format("User created 4 test at %s", now().toString()));
                            user.setUserPhoneNum(NOT_DEFINED);
                            user.setCountry(refsService.findCountry(112));

                            usersService.saveUser(user);
                            //log.info("create user: {}", user);

                            // trial contract 4 new user
                            final UserContract userContract = userContractsService.createTrialContract(user, refsService.findDeviceType(TestFuncs.generateBool() ? DT_ANDROID : DT_IOS), user.getActualDate(), user.getActualDate());
                            userContractsService.saveUserContract(userContract);

                            //log.info("create contract: {}", userContract);

                            create(UserAttrsInfo.class, userAttrsInfo -> {

                                userAttrsInfo.setAppName(TestFuncs.generateTestString15());
                                userAttrsInfo.setAppVersion(TestFuncs.generateTestString15());
                                userAttrsInfo.setMakAddr(TestFuncs.generateTestString15());

                                ifTrue(TestFuncs.generateBool(), () ->
                                        userAttrsInfo.setAndroidAttrs(create(AndroidAttrs.class, androidAttrs -> {
                                            androidAttrs.setAndroidId(TestFuncs.generateTestString15());
                                            androidAttrs.setGsfId(TestFuncs.generateTestString15());
                                            androidAttrs.setBoard(TestFuncs.generateTestString15());
                                            androidAttrs.setGcmToken(TestFuncs.generateTestString15());
                                            androidAttrs.setModel(TestFuncs.generateTestString15());
                                            androidAttrs.setProduct(TestFuncs.generateTestString15());
                                            androidAttrs.setSecureId(TestFuncs.generateTestString15());
                                            androidAttrs.setSupportedAbis(TestFuncs.generateTestString15());
                                            androidAttrs.setVersionRelease(TestFuncs.generateTestString15());
                                            androidAttrs.setFingerprint(TestFuncs.generateTestString15());
                                            androidAttrs.setVersionSdkInt(TestFuncs.generateTestRangeInteger(1, 10));
                                            androidAttrs.setManufacturer(TestFuncs.generateTestString15());
                                        })), () -> userAttrsInfo.setIosAttrs(create(IosAttrs.class, iosAttrs -> {
                                    iosAttrs.setModel(TestFuncs.generateTestString15());
                                    iosAttrs.setGcmToken(TestFuncs.generateTestString15());
                                    iosAttrs.setAppleId(TestFuncs.generateTestString15());
                                    iosAttrs.setSystemVersion(TestFuncs.generateTestString15());
                                    iosAttrs.setUstnameMachine(TestFuncs.generateTestString15());
                                    iosAttrs.setUstnameVersion(TestFuncs.generateTestString15());
                                    iosAttrs.setSystemVersion(TestFuncs.generateTestString15());
                                    iosAttrs.setIdentifierForVendor(TestFuncs.generateTestString15());
                                    iosAttrs.setUstnameRelease(TestFuncs.generateTestString15());
                                })));

                                // validate attrs
                                final Integer deviceTypeId = ofNullable(userAttrsInfo.getAndroidAttrs()).isPresent() ? DT_ANDROID : DT_IOS;

                                ifTrue(deviceTypeId.equals(DT_IOS), () -> assertNotNull(IosAttrs.class, userAttrsInfo.getIosAttrs(), "IosAttrs not specified"));
                                ifTrue(deviceTypeId.equals(DT_IOS), () -> assertNotNull(IosAttrs.class, userAttrsInfo.getIosAttrs().getIdentifierForVendor(), "identifierForVendor attr is not specified"));

                                // find absctract device
                                final UserDevice userDevice = findDevice(userAttrsInfo);

                                // Abstract UserDevice
                                final DeviceType deviceType = refsService.findDeviceType(deviceTypeId);

                                // store device 2 hist
                                //userDevicesService.saveUserDeviceHist(userDevice);

                                // update device
                                userDevice.setActualDate(now());
                                userDevice.setDeviceType(deviceType);
                                userDevice.setMacAddr(userAttrsInfo.getMakAddr());
                                userDevice.setAppName(userAttrsInfo.getAppName());
                                userDevice.setAppVersion(userAttrsInfo.getAppVersion());

                                // save abstract device
                                userDevice.setUser(user);

                                userDevicesService.saveUserDevice(userDevice);

                                //log.info("created/update userDevice: {}", userDevice);

                                // ANDROID ATTRS
                                ofNullable(userAttrsInfo.getAndroidAttrs())
                                        .ifPresent(androidAttrs -> saveAndroidAttrs(userAttrsInfo, androidAttrs, userDevice.getDeviceId(), userDevice.getAppName(), BOOLEAN_FALSE));

                                // IOS ATTRS
                                ofNullable(userAttrsInfo.getIosAttrs())
                                        .ifPresent(iosAttrs -> saveIosAttrs(userAttrsInfo, iosAttrs, userDevice.getDeviceId(), userDevice.getAppName(), BOOLEAN_FALSE));

                                // add subscription

                                final UserSubscription userSubscription = userSubscriptionsService.createUserSubscription();

                                userSubscription.setUser(user);
                                //userSubscription.setIsTestSubscription(BOOLEAN_TRUE);

                                // assign actualDate
                                userSubscription.setActualDate(now());

                                // assign or validate service Agent
                                userSubscription.setAgent(agentsService.validateOrReplaceAgent(userSubscription.getAgent()));

                                final Integer subsLimit = userSubscriptions4test.size() == 1 ? 0 : TestFuncs.generateTestRangeInteger(0, userSubscriptions4test.size() - 1);

                                userSubscription.setOnlineNotify(TestFuncs.generateBool());
                                userSubscription.setPhoneNum(userSubscriptions4test
                                        .stream()
                                        .skip(subsLimit)
                                        .findFirst()
                                        .orElseThrow()
                                        .getPhoneNum());
                                userSubscription.setSubscriptionName(userSubscription.getPhoneNum());

                                //final Byte subscriptionStatusId = SS_CLOSED;
                                final Byte subscriptionStatusId = SS_CREATED;

                                userSubscription.setSubscriptionStatus(refsService.findSubscriptionStatus(subscriptionStatusId));
                                userSubscription.setUser(user);
                                userSubscription.setUserContract(userContract);

                                userSubscriptionsService.saveUserSubscription(userSubscription, BOOLEAN_FALSE);

                                //======================================================================================
                                // activities
                                final Collection<SubscriptionActivity> toStore = ServiceFuncs.<SubscriptionActivity>createCollection();

                                final LocalDateTime ldt = now();

                                generate(new Random()::nextInt)
                                        .limit(activityLimit)
                                        .forEach(i -> toStore.add(create(SubscriptionActivity.class, activity -> {
                                            //log.info("i: {}", i);
                                            activity.setActualDate(ldt.minusSeconds(toStore.size()));
                                            activity.setUserSubscription(userSubscription);
                                            activity.setIsOnline(TestFuncs.generateBool());
                                        })));

                                //log.info("store activities: {}", toStore.size());
                                userSubscriptionsService.getActivityRepository().saveAllAndFlush(toStore);
                            });
                        });

                //==========================================================================================================
                usersAmount.setCreatedUsers(amount);
            }, () -> log.error("No subscriptions available 4 phone mask: '{}'", phoneMask));
        });
    }

    //==========================================================================
    private Integer getIosDeviceId(UserAttrsInfo userAttrsInfo) {

        return ofNullable(userAttrsInfo.getIosAttrs().getAppleId())
                .map(appleId -> getUserDevicesService()
                        .findUserDeviceIosByAppleId(userAttrsInfo.getIosAttrs().getAppleId())
                        .map(iosDevice2int))
                .orElse(Optional.empty())
                .orElseGet(() -> getUserDevicesService()
                        .findUserDeviceIosByIdentifierForVendor(userAttrsInfo.getIosAttrs().getIdentifierForVendor())
                        .map(iosDevice2int)
                        .orElse(null));
    }

    private UserDevice findDevice(UserAttrsInfo userAttrsInfo) {

        final UserDevice userDeviceByPhoneId = userDevicesService.createUserDevice();

        // ANDROID ATTRS
        ofNullable(userAttrsInfo.getAndroidAttrs())
                .ifPresent(androidAttrs -> userDeviceByPhoneId.setDeviceId(userDevicesService.findUserDeviceAndroidByGsfId(userAttrsInfo.getAndroidAttrs().getGsfId()).map(androidDevice2int).orElse(null)));

        // IOS ATTRS
        ofNullable(userAttrsInfo.getIosAttrs())
                .ifPresent(iosAttrs -> userDeviceByPhoneId.setDeviceId(getIosDeviceId(userAttrsInfo)));

        return userDevicesService.findOrCreateUserDevice(userDeviceByPhoneId.getDeviceId());

    }

    //==========================================================================
    private Optional<Integer> androidDeviceId(UserAttrsInfo userAttrsInfo, Integer deviceTypeId) {
        return ofNullable(isAndroid.test(deviceTypeId) ? getUserDevicesService().findUserDeviceAndroidByGsfId(userAttrsInfo.getAndroidAttrs().getGsfId()).map(androidDevice2int).orElse(null) : null);
    }

    private Optional<Integer> iosDeviceId(UserAttrsInfo userAttrsInfo, Integer deviceTypeId) {
        return ofNullable(isIos.test(deviceTypeId) ? getIosDeviceId(userAttrsInfo) : null);
    }


    private Optional<User> findUserByDevice(UserAttrsInfo userAttrsInfo, Integer deviceTypeId) {

        final Optional<Integer> optDeviceId = Stream.of(
                        androidDeviceId(userAttrsInfo, deviceTypeId),
                        iosDeviceId(userAttrsInfo, deviceTypeId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();

        log.info("optDeviceId = {}", optDeviceId);

        return optDeviceId.map(m -> userDevicesService.findUserByDeviceId(m.intValue())).orElse(Optional.empty());

    }

    //==========================================================================
    private void saveAndroidAttrs(UserAttrsInfo userAttrsInfo, AndroidAttrs androidAttrs, Integer deviceId, String appName, Boolean notifyKafka) {

        final String gsfId = androidAttrs.getGsfId();

        // check device unique
        assertNotNull(AndroidAttrs.class,
                gsfId, "Android attribute GCFID ");

        final Optional<UserDeviceAndroid> optUserDeviceAndroid = userDevicesService.findUserDeviceAndroidByGsfId(gsfId);

        // save 2 history
        optUserDeviceAndroid.ifPresent(uda -> userDevicesService.saveUserDeviceAndroidHist(uda));

        final UserDeviceAndroid userDeviceAndroid = optUserDeviceAndroid
                .orElseGet(() -> create(UserDeviceAndroid.class,
                        uda -> {
                            uda.setDeviceId(deviceId);
                            uda.setActualDate(NLS.long2LocalDateTime(userAttrsInfo.getActualDate()));
                        }));

        ofNullable(userDeviceAndroid)
                .ifPresent(uda -> {
                    uda.setActualDate(now());
                    uda.setBoard(androidAttrs.getBoard());
                    uda.setDeviceFingerprint(androidAttrs.getFingerprint());
                    uda.setFcmToken(androidAttrs.getGcmToken());
                    uda.setGsfId(androidAttrs.getGsfId());
                    uda.setManufacter(androidAttrs.getManufacturer());
                    uda.setSecureId(androidAttrs.getSecureId());
                    uda.setSupportedAbis(androidAttrs.getSupportedAbis());
                    uda.setVersionRelease(androidAttrs.getVersionRelease());
                    uda.setVersionSdkInt(androidAttrs.getVersionSdkInt().toString());
                });

        userDevicesService.saveUserDeviceAndroid(userDeviceAndroid, appName, notifyKafka);

    }

    //==========================================================================
    private void saveIosAttrs(UserAttrsInfo userAttrsInfo, IosAttrs iosAttrs, Integer deviceId, String appName, Boolean notifyKafka) {

        final String appleId = iosAttrs.getAppleId();

        // check device unique
        //assertNotNull(IosAttrs.class, appleId, "Ios attribute 'appleId' ");

        final Optional<UserDeviceIos> optUserDeviceIos = userDevicesService.findUserDeviceIosByAppleId(appleId);

        // save 2 history
        optUserDeviceIos.ifPresent(udi -> userDevicesService.saveUserDeviceIosHist(udi));

        final UserDeviceIos userDeviceIos = optUserDeviceIos
                .orElseGet(() -> create(UserDeviceIos.class,
                        udi -> {
                            udi.setDeviceId(deviceId);
                            udi.setActualDate(NLS.long2LocalDateTime(userAttrsInfo.getActualDate()));
                        }));

        ofNullable(userDeviceIos)
                .ifPresent(udi -> {
                    udi.setActualDate(now());
                    ifNotNull(iosAttrs.getAppleId(), () -> udi.setAppleId(iosAttrs.getAppleId()), () -> udi.setAppleId("faked appleId - ".concat(TestFuncs.generateTestString20())));
                    udi.setIcmToken(iosAttrs.getGcmToken());
                    udi.setIdentifierForVendor(iosAttrs.getIdentifierForVendor());
                    udi.setModel(iosAttrs.getModel());
                    udi.setSystemVersion(iosAttrs.getSystemVersion());
                    udi.setUstnameMachine(iosAttrs.getUstnameMachine());
                    udi.setUstnameRelease(iosAttrs.getUstnameRelease());
                    udi.setUstnameVersion(iosAttrs.getUstnameVersion());
                });

        userDevicesService.saveUserDeviceIos(userDeviceIos, appName, notifyKafka);

    }
}
