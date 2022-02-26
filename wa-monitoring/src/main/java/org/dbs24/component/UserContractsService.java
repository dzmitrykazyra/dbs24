/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.entity.*;
import org.dbs24.entity.dto.ModifyContractBySupportInfo;
import org.dbs24.entity.dto.ModifyContractEndDateInfo;
import org.dbs24.entity.dto.ModifyContractInfo;
import org.dbs24.exception.UserContractIsNotFound;
import org.dbs24.jpa.spec.ActualUserContracts;
import org.dbs24.jpa.spec.DeprecatedUserContracts;
import org.dbs24.jpa.spec.FuturedUserContracts;
import org.dbs24.repository.UserContractHistRepository;
import org.dbs24.repository.UserContractRepository;
import org.dbs24.rest.api.*;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.validator.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.dbs24.application.core.locale.NLS.long2LocalDateTime;
import static org.dbs24.component.TariffPlansService.latestTariffPlanActualDateSort;
import static org.dbs24.component.UserDevicesService.latestDeviceSort;
import static org.dbs24.component.UserSubscriptionsService.*;
import static org.dbs24.consts.SysConst.*;
import static org.dbs24.consts.WaConsts.Classes.*;
import static org.dbs24.consts.WaConsts.References.*;
import static org.dbs24.stmt.StmtProcessor.*;
import static org.dbs24.validator.Error.INVALID_ENTITY_ATTR;
import static org.dbs24.validator.Field.LOGIN_TOKEN;

@Data
@EnableAsync
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
public class UserContractsService extends AbstractApplicationService {

    @Value("${config.wa.user.future-trial.waiting:120}")
    private Integer futureTrialWaiting;

    @Value("${config.wa.user.future-trial.length:24}")
    private Integer futureTrialLength;

    final UsersService usersService;
    final RefsService refsService;
    final UserContractRepository userContractRepository;
    final UserContractHistRepository userContractHistRepository;
    final UserSubscriptionsService userSubscriptionsService;
    final PaymentContractValidator paymentContractValidator;
    final FutureTrialContractValidator futureTrialContractValidator;
    final ModifyContractValidator modifyContractValidator;
    final ModifyContractBySupportValidator modifyContractBySupportValidator;
    final ModifyContractEndDateValidator modifyContractEndDateValidator;
    final AgentsService agentsService;
    final TariffPlansService tariffPlansService;
    final UserDevicesService userDevicesService;

    public UserContractsService(RefsService refsService, UserContractRepository userContractRepository, UserContractHistRepository userContractHistRepository, UserSubscriptionsService userSubscriptionsService, UsersService usersService, PaymentContractValidator paymentContractValidator, FutureTrialContractValidator trialContractValidator, ModifyContractValidator modifyContractValidator, ModifyContractEndDateValidator modifyContractEndDateValidator, AgentsService agentsService, TariffPlansService tariffPlansService, ModifyContractBySupportValidator modifyContractBySupportValidator, UserDevicesService userDevicesService) {
        this.refsService = refsService;
        this.userContractRepository = userContractRepository;
        this.userContractHistRepository = userContractHistRepository;
        this.userSubscriptionsService = userSubscriptionsService;
        this.usersService = usersService;
        this.paymentContractValidator = paymentContractValidator;
        this.futureTrialContractValidator = trialContractValidator;
        this.modifyContractValidator = modifyContractValidator;
        this.modifyContractBySupportValidator = modifyContractBySupportValidator;
        this.modifyContractEndDateValidator = modifyContractEndDateValidator;
        this.agentsService = agentsService;
        this.tariffPlansService = tariffPlansService;
        this.userDevicesService = userDevicesService;
    }

    static final Comparator<UserContract> latestContractActualDateSort = (a, b) -> b.getActualDate().compareTo(a.getActualDate());
    static final Comparator<UserContract> latestContractEndDateSort = (a, b) -> StmtProcessor.nvl(b.getEndDate(), LocalDateTime.MAX).compareTo(StmtProcessor.nvl(a.getEndDate(), LocalDateTime.MAX));
    static final java.util.function.Predicate<UserContract> isTrial = userContract -> userContract.getContractType().getContractTypeId().equals(CT_TRIAL);
    static final java.util.function.Predicate<TariffPlan> isTariffPlanContractTrial = tariffPlan -> tariffPlan.getContractType().getContractTypeId().equals(CT_TRIAL);
    static final Comparator<UserSubscription> earlySubscriptionsOrder = (a, b) -> a.getSubscriptionId().compareTo(b.getSubscriptionId());
    static final java.util.function.Predicate<UserContract> isFutured = userContract -> userContract.getContractStatus().getContractStatusId().equals(CS_FUTURED);

    @Transactional
    public CreatedUserContract createOrUpdateContract(UserContractInfo userContractInfo) {

        return create(CREATED_USER_CONTRACT_CLASS, createdUserContract -> {

            final var user = usersService.findUser(userContractInfo.getUserId());
            final var userContract = findOrCreateUserContract(userContractInfo.getContractId());
            final var isContractNew = StmtProcessor.isNull(userContractInfo.getContractId());

            saveUserContractHist(userContract);

            ifNull(userContract.getCreateDate(), () -> userContract.setCreateDate(now()));
            userContract.setActualDate(long2LocalDateTime(userContractInfo.getActualDate()));
            userContract.setBeginDate(long2LocalDateTime(userContractInfo.getBeginDate()));
            userContract.setCancelDate(long2LocalDateTime(userContractInfo.getCancelDate()));
            userContract.setEndDate(long2LocalDateTime(userContractInfo.getEndDate()));
            userContract.setSubscriptionsAmount(userContractInfo.getSubscriptionsAmount());
            userContract.setUser(user);
            userContract.setContractStatus(refsService.findContractStatus(userContractInfo.getContractStatusId()));
            userContract.setContractType(refsService.findContractType(userContractInfo.getContractTypeId()));
            ifNotNull(userContractInfo.getModifyReasonId(), mdid -> userContract.setModifyReason(refsService.findModifyReason(mdid)));
            userContract.setEditNote(userContractInfo.getEditNote());

            log.debug("try 2 creating/update contract: {}", userContract);

            ifTrue(isContractNew, () -> savePreviousUserSubscriptionsToContractIfExist(userContractRepository.save(userContract)));

            createdUserContract.setContractId(userContract.getContractId());

            log.debug("created/update contract: {}", createdUserContract);

        });
    }

    public void savePreviousUserSubscriptionsToContractIfExist(UserContract newContract) {

        findPreviousUserSubscriptions(newContract.getUser())
                .stream()
                .limit(newContract.getSubscriptionsAmount())
                .forEach(
                        subscription
                                -> userSubscriptionsService.saveUserSubscription(
                                userSubscriptionsService.createSubscriptionBasedOnNewContract(subscription, newContract), BOOLEAN_TRUE
                        )
                );
    }

    @Transactional
    public Collection<UserSubscription> findPreviousUserSubscriptions(User user) {

        return findAllUserContracts(user)
                .stream()
                .filter(
                        contract -> contract.getContractStatus().getContractStatusId().equals(CS_CLOSED)
                                || contract.getContractStatus().getContractStatusId().equals(CS_CANCELLED))
                .sorted(
                        (UserContract contract1, UserContract contract2)
                                -> contract2.getEndDate().compareTo(contract1.getEndDate()))
                .findFirst()
                .map(userSubscriptionsService::findByUserContract)
                .orElseGet(ServiceFuncs::createCollection);
    }

    @Transactional
    public CreatedUserContract createOrUpdateFutureTrialContract(FutureTrialUserContractsInfo futureTrialUserContractsInfo) {

        return create(CREATED_USER_CONTRACT_CLASS, createdUserContract -> {

            log.debug("create future trial contract from {}", futureTrialUserContractsInfo);

            final var errors = futureTrialContractValidator.validate(futureTrialUserContractsInfo);

            ifTrue(!errors.isEmpty(), () -> {
                createdUserContract.setContractId(0);
                createdUserContract.setAnswerCode(-100);
                createdUserContract.setNote(errors.stream().findFirst().orElseThrow().getErrorMsg());

                log.warn("can't create future trial contract: {}, [{}: {}]",
                        createdUserContract,
                        createdUserContract.getAnswerCode(),
                        createdUserContract.getNote());

            }, () -> {

                final var userContract = findOrCreateUserContract(futureTrialUserContractsInfo.getContractId());

                // copy contract 2 history
                saveUserContractHist(userContract);

                // device 4 plan
                final var deviceType = ofNullable(futureTrialUserContractsInfo.getDeviceTypeId())
                        .map(refsService::findDeviceType)
                        .orElse(null);

                final var tariffPlan4trial = tariffPlan4Trial.apply(deviceType);

                ifNull(userContract.getCreateDate(), () -> userContract.setCreateDate(now()));
                userContract.setActualDate(now());
                userContract.setBeginDate(userContract.getActualDate().plusHours(futureTrialWaiting));
                userContract.setCancelDate(LOCALDATETIME_NULL);
                userContract.setEndDate(userContract.getBeginDate().plusHours(futureTrialLength));
                userContract.setSubscriptionsAmount(tariffPlan4trial.getSubscriptionsAmount());
                userContract.setUser(usersService.findUserByLoginToken(futureTrialUserContractsInfo.getLoginToken()));
                userContract.setContractStatus(refsService.findContractStatus(CS_FUTURED));
                userContract.setContractType(refsService.findContractType(CT_TRIAL));

                log.debug("try 2 creating/update future trial contract: {}", userContract);

                saveContract(userContract);

                // fill old subscriptions
                fillContractsWithDeprecatedSubscriptions(userContract);

                createdUserContract.setContractId(userContract.getContractId());

                log.debug("created/update contract: {}", createdUserContract);
            });
        });
    }
    //--------------------------------------------------------------------------

    public UserContract createTrialContract(User user, DeviceType deviceType, LocalDateTime actualDate, LocalDateTime beginDate) {

        log.debug("create trial contract 4 user: {}, beginDate: {}", user.getUserId(), beginDate);

        // tariffPlan 4 trialContract
        final var tariffPlan4trial = tariffPlan4Trial.apply(deviceType);

        return create(USER_CONTRACT_CLASS, contract -> {
            contract.setCreateDate(now());
            contract.setActualDate(actualDate);
            contract.setBeginDate(beginDate);
            contract.setEndDate(beginDate.plusHours(tariffPlan4trial.getDurationHours()));
            contract.setUser(user);
            contract.setContractType(refsService.findContractType(CT_TRIAL));
            contract.setContractStatus(refsService.findContractStatus(CS_ACTUAL));
            contract.setSubscriptionsAmount(tariffPlan4trial.getSubscriptionsAmount());

            // fill old subscriptions
            fillContractsWithDeprecatedSubscriptions(contract);

        });
    }

    final Function<DeviceType, TariffPlan> tariffPlan4Trial = deviceType -> {

        final var allDevices = StmtProcessor.isNull(deviceType);
        final var deviceTypeId = allDevices ? INTEGER_NULL : deviceType.getDeviceTypeId();
        final var allPlanStatuses = BOOLEAN_FALSE;
        final var planStatusId = TPS_ACTIVE;

        // find tariffPlan 4 trialContract
        return getTariffPlansService().findTariffPlans(allDevices, deviceTypeId, allPlanStatuses, planStatusId)
                .stream()
                .filter(isTariffPlanContractTrial)
                .sorted(latestTariffPlanActualDateSort)
                .limit(1)
                .findFirst()
                .orElseGet(getTariffPlansService().buildDefaultTariffPlan(deviceType));
    };

    //------------------------------------------------------------------------------
    @Transactional
    public CreatedUserContract createContractFromPayment(UserContractFromPaymentInfo userContractFromPaymentInfo) {

        return create(CreatedUserContract.class, cuc -> {

            log.debug("create contract payment {}", userContractFromPaymentInfo);

            final var errors = paymentContractValidator.validate(userContractFromPaymentInfo);
            final var user = usersService.findUserByLoginToken(userContractFromPaymentInfo.getLoginToken());

            ifTrue(!errors.isEmpty(), () -> {
                cuc.setContractId(0);
                cuc.setAnswerCode(-100);
                cuc.setNote(errors.stream().findFirst().orElseThrow().getErrorMsg());

                log.error("can't create contract: {}, [{}: {}]", cuc, cuc.getAnswerCode(), cuc.getNote());

            }, () -> {

                // create contract
                final var contractType = refsService.findContractType(userContractFromPaymentInfo.getContractTypeId());

                // find latest userContract
                final var userContract = findAllUserContracts(user)
                        .stream()
                        .sorted(latestContractEndDateSort)
                        .findFirst()
                        .orElseGet(() -> create(USER_CONTRACT_CLASS, contract -> contract.setActualDate(now())));

                // update or restore oldContract
                final var isOldContract = StmtProcessor.notNull(userContract.getContractId());

                // store history
                ifTrue(isOldContract, () -> {
                    saveUserContractHist(userContract);

                    // update subscriptions of trial/futured contract
                    ifTrue(isFutured.test(userContract), () ->
                            // refresh status of exist subscription
                            userSubscriptionsService.findActualSubscriptions(userContract)
                                    .stream()
                                    .filter(isCreatedSubsription)
                                    .forEach(userSubscription -> {
                                        // store hist
                                        userSubscriptionsService.saveUserSubscriptionHist(userSubscription);
                                        userSubscription.setActualDate(now());
                                        // notify kafka
                                        userSubscriptionsService.saveUserSubscription(userSubscription, BOOLEAN_TRUE);

                                    })
                    );

                    // new assigned newBeginContractDate
                    final var newBeginContractDate = long2LocalDateTime(userContractFromPaymentInfo.getBeginDate());

                    ifTrue(userContract.getBeginDate().compareTo(newBeginContractDate) > INTEGER_ZERO,
                            () -> userContract.setBeginDate(newBeginContractDate));

                    // new assigned newEndContractDate
                    final var newEndContractDate = long2LocalDateTime(userContractFromPaymentInfo.getEndDate());

                    ifTrue(userContract.getEndDate().compareTo(newEndContractDate) < INTEGER_ZERO,
                            () -> userContract.setEndDate(newEndContractDate));

                    // when downgrade, truncate rest of subscriptions
                    ifTrue(userContract.getSubscriptionsAmount().compareTo(userContractFromPaymentInfo.getSubscriptionsAmount()) > 0,
                            () ->
                                    userSubscriptionsService.findByUserContract(userContract)
                                            .stream()
                                            .peek(subscription -> ifTrue(phoneNotExistsSubsription.test(subscription),
                                                    () -> userSubscriptionsService.closeSubscription(subscription)))
                                            .filter(isConfirmedSubsription)
                                            .sorted(earlySubscriptionsOrder)
                                            .skip(userContractFromPaymentInfo.getSubscriptionsAmount())
                                            .forEach(userSubscriptionsService::closeSubscription));

                }, () -> {
                    userContract.setBeginDate(long2LocalDateTime(userContractFromPaymentInfo.getBeginDate()));
                    userContract.setUser(user);
                });

                // update
                ifNull(userContract.getCreateDate(), () -> userContract.setCreateDate(now()));
                userContract.setActualDate(now());
                userContract.setEndDate(long2LocalDateTime(userContractFromPaymentInfo.getEndDate()));
                userContract.setSubscriptionsAmount(userContractFromPaymentInfo.getSubscriptionsAmount());
                userContract.setContractType(contractType);
                userContract.setContractStatus(refsService.findContractStatus(CS_ACTUAL));

                // fill old subscriptions
                fillContractsWithDeprecatedSubscriptions(userContract);

                // save Contract
                saveContract(userContract);

                cuc.setContractId(userContract.getContractId());

                log.debug("create/update contract_id: {}, [{}: {}]", cuc, cuc.getAnswerCode(), cuc.getNote());
            });
        });
    }

    @Transactional
    public CreatedUserContract modifyContract(ModifyContractInfo modifyContractInfo) {

        return create(CreatedUserContract.class, cuc -> {

            log.debug("manual modify user contract: {}", modifyContractInfo);

            final var errors = modifyContractValidator.validate(modifyContractInfo);

            ifTrue(!errors.isEmpty(), () -> {
                cuc.setContractId(INTEGER_ZERO);
                cuc.setAnswerCode(-100);
                cuc.setNote(errors.stream().findFirst().orElseThrow().getErrorMsg());

                log.error("can't update contract: {}, [{}: {}]", cuc, cuc.getAnswerCode(), cuc.getNote());

            }, () -> {

                // modified contract
                final var userContract = findUserContract(modifyContractInfo.getContractId());

                saveUserContractHist(userContract);

                // days amount
                ifNotNull(modifyContractInfo.getDaysAmount(),
                        () -> {
                            userContract.setBeginDate(NLS.minLdt(now(), userContract.getBeginDate()));
                            userContract.setEndDate(NLS.maxLdt(now(), userContract.getEndDate()).plusDays(modifyContractInfo.getDaysAmount()));
                        }, () -> {
                            // optional beginDate
                            ifNotNull(modifyContractInfo.getBeginDate(), beginDate -> userContract.setBeginDate(long2LocalDateTime(beginDate)));
                            // optional endDate
                            ifNotNull(modifyContractInfo.getEndDate(), endDate -> userContract.setEndDate(NLS.maxLdt(long2LocalDateTime(endDate), userContract.getEndDate())));
                        });

                // subscriptionAmount
                ifNotNull(modifyContractInfo.getSubscriptionAmount(), () -> userContract.setSubscriptionsAmount(modifyContractInfo.getSubscriptionAmount()));

                // contractTypeId
                ifNotNull(modifyContractInfo.getContractTypeId(), () -> userContract.setContractType(refsService.findContractType(modifyContractInfo.getContractTypeId())));

                // contract status always set as ACTUAL
                userContract.setContractStatus(refsService.findContractStatus(CS_ACTUAL));
                ifNull(userContract.getCreateDate(), () -> userContract.setCreateDate(now()));

                // actualDate
                userContract.setActualDate(now());

                // editNote
                userContract.setEditNote(modifyContractInfo.getEditNote());

                // ReasonId
                userContract.setModifyReason(refsService.findModifyReason(modifyContractInfo.getModifyReasonId()));

                // fill old subscriptions
                fillContractsWithDeprecatedSubscriptions(userContract);

                // save Contract
                saveContract(userContract);

                cuc.setContractId(userContract.getContractId());

                log.debug("create/update contract_id: {}, [{}: {}]", cuc, cuc.getAnswerCode(), cuc.getNote());
            });
        });
    }

    @Transactional
    public CreatedUserContract modifyContractBySupport(ModifyContractBySupportInfo modifyContractBySupportInfo, String loginToken) {

        return create(CreatedUserContract.class, cuc -> {

            log.debug("manual modify user contract: {}", modifyContractBySupportInfo);

            final var errors = modifyContractBySupportValidator.validate(modifyContractBySupportInfo);
            final var activeContracts = ServiceFuncs.<UserContract>createCollection();

            // loginToken
            ifNull(loginToken, () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, LOGIN_TOKEN, "LoginToken not defined")),
                    () -> usersService.findOptionalUserByLoginToken(loginToken).ifPresentOrElse(user -> {

                        // find all user contracts
                        findAllUserContracts(user)
                                .stream()
                                .sorted(latestContractEndDateSort)
                                .limit(1)
                                .findFirst()
                                .ifPresent(activeContracts::add);

                        ifTrue(activeContracts.isEmpty(), () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, LOGIN_TOKEN, String.format("Cant't find any user contract (loginToken=%s)", loginToken))));

                    }, () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, LOGIN_TOKEN, String.format("LoginToken is unknown or not exists (%s)", loginToken)))));


            ifTrue(!errors.isEmpty(), () -> {
                cuc.setContractId(INTEGER_ZERO);
                cuc.setAnswerCode(-100);
                cuc.setNote(errors.stream().findFirst().orElseThrow().getErrorMsg());

                log.error("can't update contract: {}, [{}: {}]", cuc, cuc.getAnswerCode(), cuc.getNote());

            }, () -> {

                // modified contract
                final var userContract = activeContracts.iterator().next();

                saveUserContractHist(userContract);

                // contractTypeId
                ifNotNull(modifyContractBySupportInfo.getContractTypeId(), () -> userContract.setContractType(refsService.findContractType(modifyContractBySupportInfo.getContractTypeId())));

                // subscriptionAmount
                // (from TariffPlans by user deviceTypeId)

                // latest user device
                final var userDevice = userDevicesService
                        .findAllUserDevices(userContract.getUser())
                        .stream()
                        .sorted(latestDeviceSort)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Can't find any user device"));


                final var tariffPlan = tariffPlansService.findActualTariffPlan(userDevice.getDeviceType(),
                        refsService.findContractType(modifyContractBySupportInfo.getContractTypeId()));

                ifNull(userContract.getCreateDate(), () -> userContract.setCreateDate(now()));
                // subscription amount
                userContract.setSubscriptionsAmount(tariffPlan.getSubscriptionsAmount());

                // contract status always set as ACTUAL
                userContract.setContractStatus(refsService.findContractStatus(CS_ACTUAL));

                // actualDate
                userContract.setActualDate(now());

                // new endDate
                userContract.setEndDate(NLS.maxLdt(now(), userContract.getEndDate()).plusDays(modifyContractBySupportInfo.getDaysAmount()));

                // editNote
                userContract.setEditNote(modifyContractBySupportInfo.getEditNote());

                // ReasonId
                userContract.setModifyReason(refsService.findModifyReason(modifyContractBySupportInfo.getModifyReasonId()));

                // save Contract
                saveContract(userContract);

                // fill old subscriptions
                fillContractsWithDeprecatedSubscriptions(userContract);

                cuc.setContractId(userContract.getContractId());
                cuc.setNote(String.format("update contractId: %d, new endDate: is %s", userContract.getContractId(),
                        userContract.getEndDate()));

                log.debug(cuc.getNote());
            });
        });
    }

    @Transactional
    public CreatedUserContract modifyContractEndDate(ModifyContractEndDateInfo modifyContractEndDateInfo) {

        return create(CreatedUserContract.class, cuc -> {

            log.debug("manual modify user contract endDate: {}", modifyContractEndDateInfo);

            final var errors = modifyContractEndDateValidator.validate(modifyContractEndDateInfo);

            ifTrue(!errors.isEmpty(), () -> {
                cuc.setContractId(INTEGER_ZERO);
                cuc.setAnswerCode(-100);
                cuc.setNote(errors.stream().findFirst().orElseThrow().getErrorMsg());

                log.error("can't update contract: {}, [{}: {}]", cuc, cuc.getAnswerCode(), cuc.getNote());

            }, () -> {

                // modified contract
                final var userContract = findAllUserContracts(modifyContractEndDateInfo.getLoginToken())
                        .stream()
                        .max((a, b) -> a.getEndDate().compareTo(b.getEndDate())).orElseThrow();

                saveUserContractHist(userContract);

                // days amount
                ifNotNull(modifyContractEndDateInfo.getDaysAmount(), () -> userContract.setEndDate(userContract.getEndDate().plusDays(modifyContractEndDateInfo.getDaysAmount())));

                // contract status always set as ACTUAL
                userContract.setContractStatus(refsService.findContractStatus(CS_ACTUAL));

                // fill old subscriptions
                fillContractsWithDeprecatedSubscriptions(userContract);

                // save Contract
                saveContract(userContract);

                cuc.setContractId(userContract.getContractId());
                cuc.setNote(String.format("ContractId: %d, new endDate: is %s", userContract.getContractId(),
                        userContract.getEndDate()));

                log.debug("create/update contract_id: {}, [{}: {}]", cuc, cuc.getAnswerCode(), cuc.getNote());
            });
        });
    }

    //==================================================================================================================

    final Function<Collection<UserContract>, Integer> findPrevContractId = subscriptions -> {

        log.debug("findPrevContractId - contracts: {}",
                subscriptions.stream().map(v -> String.format("[c: %d],", v.getContractId()))
                        .collect(Collectors.toList()));

        return subscriptions
                .stream()
                .map(UserContract::getContractId)
                .max(Integer::compare)
                .orElse(INTEGER_ZERO);
    };

    protected void fillContractsWithDeprecatedSubscriptions(UserContract userContract) {

        final var existSubcriptionsAmount = ofNullable(userContract.getContractId())
                .map(contractId -> userSubscriptionsService.findActualSubscriptionsAmount(userContract))
                .orElse(INTEGER_ZERO);

        log.debug("restore old subscriptions: contractId: {}, exists: {}, limit: {}",
                userContract.getContractId(), existSubcriptionsAmount, userContract.getSubscriptionsAmount());

        ifTrue(existSubcriptionsAmount < userContract.getSubscriptionsAmount(),
                () -> {

                    final var allDeprecatedUserSubscriptions = userSubscriptionsService
                            .findAllDeprecatedUserSubscriptions(userContract.getUser().getLoginToken());

                    // deprecated subsriptions from current contract
                    final var allowedDeprecatedUserSubscriptions = allDeprecatedUserSubscriptions
                            .stream()
                            .filter(s -> s.getUserContract().getContractId().equals(userContract.getContractId()))
                            .collect(Collectors.toList());

                    log.debug("found {} deprecated subscription(s) 4 contractId: {}",
                            allowedDeprecatedUserSubscriptions.size(), userContract.getContractId());

                    // no closed subs
                    ifTrue(allowedDeprecatedUserSubscriptions.isEmpty(), () -> {

                        // previous contract
                        final var prevContractId = findPrevContractId.apply(
                                findAllUserContracts(userContract.getUser())
                                        .stream()
                                        .filter(c -> !c.getContractId().equals(userContract.getContractId()))
                                        .collect(Collectors.toList()));

                        allDeprecatedUserSubscriptions
                                .stream()
                                .filter(s -> s.getUserContract().getContractId().equals(prevContractId))
                                .forEach(allowedDeprecatedUserSubscriptions::add);

                        log.debug("found {} deprecated subscription(s) 4 contractId: {}, prevContractId: {}",
                                allowedDeprecatedUserSubscriptions.size(), userContract.getContractId(), prevContractId);

                    });

                    log.debug("fill unused subscriptions from history, contractId: {}: actual: {}, limit: {}, deprecated: {}",
                            userContract.getContractId(), existSubcriptionsAmount, userContract.getSubscriptionsAmount(), allDeprecatedUserSubscriptions.size());

                    StmtProcessor.processCollection(allowedDeprecatedUserSubscriptions, ads -> {

                        final var existsSubscriptions = userSubscriptionsService.findActualSubscriptions(userContract);
                        final var addSubscription = ServiceFuncs.<UserSubscription>createCollection();

                        final var diff = userContract.getSubscriptionsAmount() - existSubcriptionsAmount;

                        ads.stream()
                                .filter(subscription -> existsSubscriptions.stream().noneMatch(exists -> exists.getPhoneNum().equals(subscription.getPhoneNum())))
                                .sorted(earlySubscriptionsOrder)
                                .limit(diff)
                                .forEach(userSubscription -> {
                                    addSubscription.add(userSubscription);
                                    log.debug("assign unused subscription to contract [s:{} -> c:{}]",
                                            userSubscription.getSubscriptionId(), userContract.getContractId());
                                });

                        log.debug("add {} unused subsription(s) from history, 4 contractId: {}",
                                addSubscription.size(), userContract.getContractId());

                        StmtProcessor.processCollection(addSubscription, addedSubs -> addedSubs.forEach(subscription -> {
                            userSubscriptionsService.saveUserSubscriptionHist(subscription);
                            subscription.setAgent(agentsService.validateOrReplaceAgent(subscription.getAgent()));
                            subscription.setUserContract(userContract);
                            subscription.setSubscriptionStatus(refsService.findSubscriptionStatus(SS_CREATED));
                            subscription.setActualDate(now());
                            userSubscriptionsService.saveUserSubscription(subscription, !isFutured.test(userContract));
                        }));
                    });
                });
    }

    private Collection<ContractStatus> getActualContractStatuses() {

        return ServiceFuncs.createCollection(c -> {
            c.add(refsService.findContractStatus(CS_ACTUAL));
            c.add(refsService.findContractStatus(CS_FUTURED));
        });
    }

    private Collection<ContractStatus> getFuturedContractStatuses() {

        return ServiceFuncs.createCollection(c -> c.add(refsService.findContractStatus(CS_FUTURED)));
    }

    @Transactional(readOnly = true)
    public UserContractInfoCollection getActualUserContractsInfo(String userLoginToken) {

        final var ldt = now();

        return create(UserContractInfoCollection.class, ucic
                -> findActualUserContracts(usersService.findUserByLoginToken(userLoginToken))
                .stream()
                .filter(contract -> contract.getEndDate().compareTo(ldt) > 0)
                .forEach(uc -> ucic.getUserContracts().add(create(UserContractInfo.class, uci -> uci.assign(uc))))
        );
    }

    public Collection<UserContract> findAllUserContracts(String userLoginToken) {

        return findAllUserContracts(usersService.findUserByLoginToken(userLoginToken));
    }

    public Collection<UserContract> findActualUserContracts(String userLoginToken) {

        return findActualUserContracts(usersService.findUserByLoginToken(userLoginToken));
    }

    //@Cacheable(CACHE_USER_CONTRACTS)
    public Collection<UserContract> findActualUserContracts(User user) {

        return userContractRepository.findAll(auc.getSpecification(user, getActualContractStatuses()));
    }

    /**
     * Method allows find all user contracts (contracts with all statuses:
     * active, non-active etc.)
     */
    public Collection<UserContract> findUserContracts(String userLoginToken, ContractStatus contractStatus) {

        return userContractRepository.findByUserAndContractStatus(usersService.findUserByLoginToken(userLoginToken), contractStatus);
    }

    public Collection<UserContract> findAllUserContracts(User user) {

        return userContractRepository.findByUser(user);
    }

    //==========================================================================
    final ActualUserContracts auc = (user, cs) -> (r, cq, cb) -> {

        final var predicate = cb.conjunction();

        predicate.getExpressions().add(cb.equal(r.get("user"), user));
        predicate.getExpressions().add(r.get("contractStatus").in(cs));

        return predicate;
    };

    final DeprecatedUserContracts duc = cs -> (r, cq, cb) -> {

        final var predicate = cb.conjunction();

        predicate.getExpressions().add(r.get("contractStatus").in(cs));
        predicate.getExpressions().add(cb.lessThan(r.get("endDate"), cb.currentTimestamp()));

        return predicate;
    };

    final FuturedUserContracts fuc = cs -> (r, cq, cb) -> {

        final var predicate = cb.conjunction();

        predicate.getExpressions().add(r.get("contractStatus").in(cs));
        predicate.getExpressions().add(cb.lessThan(r.get("beginDate"), cb.currentTimestamp()));

        return predicate;
    };

    public void saveContract(UserContract userContract) {
        userContractRepository.save(userContract);
    }

    public UserContract createUserContract() {
        return create(USER_CONTRACT_CLASS, a -> a.setActualDate(now()));
    }

    public UserContract findUserContract(Integer contractId) {

        return findOptionalUserContract(contractId)
                .orElseThrow(() -> new UserContractIsNotFound(String.format("contractId not found (%d)", contractId)));
    }

    public Optional<UserContract> findOptionalUserContract(Integer contractId) {

        return userContractRepository
                .findById(contractId);
    }

    public UserContract findOrCreateUserContract(Integer contractId) {
        return (ofNullable(contractId)
                .orElseGet(() -> 0) > 0)
                ? findUserContract(contractId)
                : createUserContract();
    }

    public void saveUserContractHist(UserContractHist userContractHist) {

        userContractHistRepository.save(userContractHist);
    }

    public void saveUserContractHist(UserContract userContract) {

        ofNullable(userContract.getContractId())
                .ifPresent(id -> saveUserContractHist(create(USER_CONTRACT_HIST_CLASS, userContractHist -> {

                    userContractHist.setContractId(id);
                    userContractHist.setActualDate(userContract.getActualDate());
                    userContractHist.setBeginDate(userContract.getBeginDate());
                    userContractHist.setCancelDate(userContract.getCancelDate());
                    userContractHist.setEndDate(userContract.getEndDate());
                    userContractHist.setContractStatus(userContract.getContractStatus());
                    userContractHist.setContractType(userContract.getContractType());
                    userContractHist.setSubscriptionsAmount(userContract.getSubscriptionsAmount());
                    userContractHist.setUser(userContract.getUser());
                    userContractHist.setEditNote(userContract.getEditNote());
                    userContractHist.setModifyReason(userContract.getModifyReason());
                })));
    }

    public void saveUserContract(UserContract userContract) {
        userContractRepository.save(userContract);
    }

    @Transactional
    @Scheduled(fixedRateString = "${config.wa.contract.deprecated.update:3600000}")
    public void closeDeprecatedContracts() {

        final var deprecatedContracts = userContractRepository.findAll(duc.getSpecification(getActualContractStatuses()));

        final var stopWatcher = StopWatcher.createWhen(!deprecatedContracts.isEmpty(), "Close deprecated contracts");

        deprecatedContracts.forEach(contract -> {
            final var uc = findUserContract(contract.getContractId());

            log.debug("close deprecated contract = {}", uc.getContractId());

            saveUserContractHist(uc);

            uc.setContractStatus(refsService.findContractStatus(CS_CLOSED));
            uc.setActualDate(now());

            ifTrue(!uc.getSubscriptions().isEmpty(), () -> {
                // move existing subscriptions to new contracts
                log.debug("there {} subscription(s) on closed contract = {}", uc.getSubscriptions().size(), uc.getContractId());

                // to do
                // find actual contract
                uc.getSubscriptions()
                        .forEach(userSubscriptionsService::closeSubscription);
            });

            saveUserContract(uc);
        });

        ifNotNull(stopWatcher, () -> log.debug("{}", stopWatcher.getStringExecutionTime()));
    }

    @Transactional
    @Scheduled(fixedRateString = "${config.wa.contract.futured.update:60000}")
    public void validateFuturedContracts() {

        // update futures contracts
        final var futuredContracts = userContractRepository.findAll(fuc.getSpecification(getFuturedContractStatuses()));

        final var stopWatcher = StopWatcher.createWhen(!futuredContracts.isEmpty(), "Validate future contracts");

        StmtProcessor.processCollection(futuredContracts, fc -> {

            log.debug("there are {} future contract(s) to start processing", fc.size());

            final var agents = agentsService.findLeastLoadedAgents(fc.size()).iterator();

            futuredContracts.forEach(contract -> {
                final var uc = findUserContract(contract.getContractId());

                log.info("update futured contract = {}", uc.getContractId());

                saveUserContractHist(uc);

                uc.setContractStatus(refsService.findContractStatus(CS_ACTUAL));
                uc.setActualDate(now());

                ifTrue(!uc.getSubscriptions().isEmpty(), () -> {
                    // move existing subscriptions to new contracts
                    var newAgent = agents.hasNext() ? agents.next() : agentsService.findLeastLoadedAgent();

                    log.debug("there {} subscription(s) on futured contract = {}, assign new agent = {}",
                            uc.getSubscriptions().size(), uc.getContractId(), newAgent.getAgentId());

                    uc.getSubscriptions()
                            .forEach(userSubscription -> {
                                userSubscriptionsService.saveUserSubscriptionHist(userSubscription);
                                userSubscription.setAgent(newAgent);
                                userSubscription.setActualDate(now());
                                userSubscriptionsService.saveUserSubscription(userSubscription, BOOLEAN_TRUE);
                            });
                });

                saveUserContract(uc);
            });

            ifNotNull(stopWatcher, () -> log.debug("{}", stopWatcher.getStringExecutionTime()));
        });
    }
}
