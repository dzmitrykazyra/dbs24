/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.entity.UserSubscription;
import org.dbs24.rest.api.*;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.dbs24.consts.WaConsts.OperCode.OC_INVALID_MASK;
import static org.dbs24.consts.WaConsts.OperCode.OC_INVALID_NUM;
import static org.dbs24.consts.WaConsts.References.SS_CREATED;
import static org.dbs24.rest.api.consts.RestApiConst.OperCode.OC_OK;
import static org.dbs24.rest.api.consts.RestApiConst.OperCode.OC_OK_STR;
import static org.dbs24.stmt.StmtProcessor.ifNull;
import static org.dbs24.stmt.StmtProcessor.ifTrue;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.testing.enabled", havingValue = "true")
public class TestingService extends AbstractApplicationService {

    @Value("${config.testing.subscription.max:10000}")
    private Integer subscriptionMax;

    final UserSubscriptionsService userSubscriptionsService;
    final UserBuilderService userBuilderService;

    public TestingService(UserSubscriptionsService userSubscriptionsService, UserBuilderService userBuilderService) {
        this.userSubscriptionsService = userSubscriptionsService;
        this.userBuilderService = userBuilderService;
    }

    //==========================================================================
    @Transactional
    public CreateTestSubscriptionResult createUserSubscriptions4test(TestMassSubscriptionsInfo testMassSubscriptionsInfo) {
        return StmtProcessor.create(CreateTestSubscriptionResult.class, ctsr -> {

            log.info("try to create test subscriptions: {}", testMassSubscriptionsInfo);

            ctsr.setAmount(0);
            ctsr.setAnswerCode(OC_OK);
            ctsr.setNote(OC_OK_STR);

            ifNull(testMassSubscriptionsInfo.getSubscriptionsAmount(),
                    () -> {
                        ctsr.setAnswerCode(OC_INVALID_MASK);
                        ctsr.setNote("subscription amount is null");
                    }, () -> ifTrue(!(testMassSubscriptionsInfo.getSubscriptionsAmount() > 0 && testMassSubscriptionsInfo.getSubscriptionsAmount() <= subscriptionMax),
                            () -> {
                                ctsr.setAnswerCode(OC_INVALID_NUM);
                                ctsr.setNote(String.format("Invalid subcriptions number (0-%d)", subscriptionMax));
                            })
            );

            ifNull(testMassSubscriptionsInfo.getSubscriptionsMask(),
                    () -> {
                        ctsr.setAnswerCode(OC_INVALID_MASK);
                        ctsr.setNote("Invalid subcriptions mask (null)");
                    }, () -> ifTrue(testMassSubscriptionsInfo.getSubscriptionsMask().isEmpty(),
                            () -> {
                                ctsr.setAnswerCode(OC_INVALID_MASK);
                                ctsr.setNote("subcriptions mask is empty");
                            }));

            // search 
            final Collection<UserSubscription> existsSubs = userSubscriptionsService.findSubscriptionsByPhoneNum(testMassSubscriptionsInfo.getSubscriptionsMask());

            log.info("find {} exists subscriptions", existsSubs.size());

            existsSubs
                    .stream()
                    .sorted((a, b) -> a.getActualDate().compareTo(b.getActualDate()))
                    .limit(testMassSubscriptionsInfo.getSubscriptionsAmount())
                    .forEach(s -> StmtProcessor.create(UserAttrsInfo.class, ua -> {

                ua.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                ua.setAppName(TestFuncs.generateTestString15());
                ua.setAppVersion(TestFuncs.generateTestString15());
                //user.setLoginToken(TestFuncs.generateTestString15());
                ua.setMakAddr(TestFuncs.generateTestString15());

                StmtProcessor.create(AndroidAttrs.class, aa -> {

                    aa.setAndroidId(TestFuncs.generateTestString15());
                    aa.setBoard(TestFuncs.generateTestString15());
                    aa.setFingerprint(TestFuncs.generateTestString15());
                    aa.setGcmToken(TestFuncs.generateTestString(30));
                    aa.setGsfId(TestFuncs.generateTestString(30));
                    aa.setManufacturer(TestFuncs.generateTestString15());
                    aa.setModel(TestFuncs.generateTestString15());
                    aa.setProduct(TestFuncs.generateTestString15());
                    aa.setSecureId(TestFuncs.generateTestString15());
                    aa.setSupportedAbis(TestFuncs.generateTestString15());
                    aa.setVersionRelease(TestFuncs.generateTestString15());
                    aa.setVersionSdkInt(10);

                    ua.setAndroidAttrs(aa);

                });

                final CreatedUser createdUser = userBuilderService.createOrUpdateMultiUser(ua);
                log.info("create user 4 test: {} ", createdUser);

                StmtProcessor.create(UserSubscriptionInfo.class, usi -> {

                    usi.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                    usi.setOnlineNotify(TestFuncs.generateBool());
                    // phone num from list
                    usi.setPhoneNum(s.getPhoneNum());
                    usi.setSubscriptionName(TestFuncs.generateTestString15());
                    usi.setSubscriptionStatusId(SS_CREATED);

                    // create subscription
                    userSubscriptionsService.createOrUpdateSubscription(usi, createdUser.getLoginToken());

                });

                // Increment count
                ctsr.setAmount(ctsr.getAmount() + 1);

            }));

            log.info("create test subscription: {} ({}, {})", ctsr, ctsr.getAnswerCode(), ctsr.getNote());
        });
    }
}
