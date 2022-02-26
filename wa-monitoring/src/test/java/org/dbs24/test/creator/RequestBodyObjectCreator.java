package org.dbs24.test.creator;

import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.rest.api.*;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.dbs24.consts.WaConsts.Classes.USER_SUBSCRIPTION_INFO_CLASS;
import static org.dbs24.consts.WaConsts.References.*;

public class RequestBodyObjectCreator {

    public static Mono<UserAttrsInfo> createRandomAndroidUserAttrsInfoMono() {
        return Mono.just(StmtProcessor.create(UserAttrsInfo.class, multiPlatformUser -> {
            multiPlatformUser.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            multiPlatformUser.setAppName(TestFuncs.generateTestString15());
            multiPlatformUser.setAppVersion(TestFuncs.generateTestString15());
            multiPlatformUser.setMakAddr(TestFuncs.generateTestString15());

            multiPlatformUser.setAndroidAttrs(StmtProcessor.create(
                    AndroidAttrs.class, androidAttributes -> {
                        androidAttributes.setAndroidId(TestFuncs.generateTestString15());
                        androidAttributes.setBoard(TestFuncs.generateTestString15());
                        androidAttributes.setFingerprint(TestFuncs.generateTestString15());
                        androidAttributes.setGcmToken(TestFuncs.generateTestString15());
                        androidAttributes.setGsfId(TestFuncs.generateTestString15());
                        androidAttributes.setManufacturer(TestFuncs.generateTestString15());
                        androidAttributes.setModel(TestFuncs.generateTestString15());
                        androidAttributes.setProduct(TestFuncs.generateTestString15());
                        androidAttributes.setSecureId(TestFuncs.generateTestString15());
                        androidAttributes.setSupportedAbis(TestFuncs.generateTestString15());
                        androidAttributes.setVersionRelease(TestFuncs.generateTestString15());
                        androidAttributes.setVersionSdkInt(10);
                    }));
        }));
    }

    public static Mono<UserContractInfo> createRandomTrialUserContractInfoMonoByUserId(Integer userId, Integer trialLength) {
        return Mono.just(StmtProcessor.create(UserContractInfo.class, contract -> {
            contract.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            contract.setUserId(userId);
            contract.setContractTypeId(CT_BASIC);
            contract.setContractStatusId(CS_ACTUAL);
            contract.setBeginDate(NLS.localDateTime2long(LocalDateTime.now()));
            contract.setEndDate(NLS.localDateTime2long(NLS.long2LocalDateTime(contract.getBeginDate()).plusDays(trialLength)));
            contract.setSubscriptionsAmount(4);
        }));
    }

    public static Mono<UserContractInfo> createCT4UserContractInfoMonoByUserId(Integer userId) {
        return Mono.just(StmtProcessor.create(UserContractInfo.class, contract -> {
            contract.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            contract.setUserId(userId);
            contract.setContractTypeId(CT_BASIC);
            contract.setContractStatusId(CS_ACTUAL);
            contract.setBeginDate(NLS.localDateTime2long(LocalDateTime.now()));
            contract.setEndDate(NLS.localDateTime2long(NLS.long2LocalDateTime(contract.getBeginDate()).plusMonths(3)));
            contract.setSubscriptionsAmount(4);
        }));
    }

    public static Mono<UserContractInfo> createCT10UserContractInfoMonoByUserId(Integer userId) {
        return Mono.just(StmtProcessor.create(UserContractInfo.class, contract -> {
            contract.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            contract.setUserId(userId);
            contract.setContractTypeId(CT_STANDART);
            contract.setContractStatusId(CS_ACTUAL);
            contract.setBeginDate(NLS.localDateTime2long(LocalDateTime.now()));
            contract.setEndDate(NLS.localDateTime2long(NLS.long2LocalDateTime(contract.getBeginDate()).plusMonths(3)));
            contract.setSubscriptionsAmount(4);
        }));
    }

    public static Mono<UserSubscriptionInfo> createRandomUserSubscriptionInfoMono() {
        return Mono.just(StmtProcessor.create(USER_SUBSCRIPTION_INFO_CLASS, subscription -> {
            subscription.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            subscription.setOnlineNotify(TestFuncs.generateBool());
            subscription.setPhoneNum(TestFuncs.generateTestString15());
            subscription.setSubscriptionName(TestFuncs.generateTestString15());
            subscription.setSubscriptionStatusId(SS_CREATED);
        }));
    }

    public static Mono<UserSubscriptionInfo> createRandomUserSubscriptionInfoMonoByUserId(Integer userId) {
        return Mono.just(StmtProcessor.create(USER_SUBSCRIPTION_INFO_CLASS, subscription -> {
            subscription.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
            subscription.setOnlineNotify(TestFuncs.generateBool());
            subscription.setPhoneNum(TestFuncs.generateTestString15());
            subscription.setSubscriptionName(TestFuncs.generateTestString15());
            subscription.setSubscriptionStatusId(SS_CREATED);
            subscription.setUserId(userId);
        }));
    }

    public static Mono<UserContractFromPaymentInfo> createRandomUserContractFromPaymentInfoMonoByLoginToken(String loginToken) {
        return Mono.just(StmtProcessor.create(UserContractFromPaymentInfo.class, contract -> {
            contract.setBeginDate(NLS.localDateTime2long(LocalDateTime.now().minusDays(5)));
            contract.setEndDate(NLS.localDateTime2long(NLS.long2LocalDateTime(contract.getBeginDate()).plusDays(1)));
            contract.setLoginToken(loginToken);
            contract.setSubscriptionsAmount(4);
        }));
    }
}
