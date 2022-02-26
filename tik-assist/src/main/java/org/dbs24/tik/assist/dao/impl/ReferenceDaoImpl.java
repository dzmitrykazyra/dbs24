package org.dbs24.tik.assist.dao.impl;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.tik.assist.constant.*;
import org.dbs24.tik.assist.constant.reference.*;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.entity.domain.*;
import org.dbs24.tik.assist.repo.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Log4j2
@Component
public class ReferenceDaoImpl extends DaoAbstractApplicationService implements ReferenceDao {

    final UserStatusRepo userStatusRepo;
    final CountryRepo countryRepo;
    final OrderActionResultRepo orderActionResultRepo;
    final DepositPaymentTypeRepo depositPaymentTypeRepo;
    final PhoneStatusRepo phoneStatusRepo;
    final BotStatusRepo botStatusRepo;
    final BotRegistrationTypeRepo botRegistrationTypeRepo;
    final ActionTypeRepo actionTypeRepo;
    final OrderStatusRepo orderStatusRepo;
    final CurrencyRepo currencyRepo;
    final PlanStatusRepo planStatusRepo;
    final PlanTemplateStatusRepo planTemplateStatusRepo;
    final UserSubscriptionStatusRepo userSubscriptionStatusRepo;

    public ReferenceDaoImpl(
            UserStatusRepo userStatusRepo,
            CountryRepo countryRepo,
            OrderActionResultRepo orderActionResultRepo,
            BotStatusRepo botStatusRepo,
            ActionTypeRepo actionTypeRepo,
            OrderStatusRepo orderStatusRepo,
            CurrencyRepo currencyRepo,
            DepositPaymentTypeRepo depositPaymentTypeRepo,
            BotRegistrationTypeRepo botRegistrationTypeRepo,
            PhoneStatusRepo phoneStatusRepo,
            PlanStatusRepo planStatusRepo,
            PlanTemplateStatusRepo planTemplateStatusRepo,
            UserSubscriptionStatusRepo userSubscriptionStatusRepo) {

        this.userStatusRepo = userStatusRepo;
        this.countryRepo = countryRepo;
        this.orderActionResultRepo = orderActionResultRepo;
        this.depositPaymentTypeRepo = depositPaymentTypeRepo;
        this.botStatusRepo = botStatusRepo;
        this.botRegistrationTypeRepo = botRegistrationTypeRepo;
        this.actionTypeRepo = actionTypeRepo;
        this.orderStatusRepo = orderStatusRepo;
        this.currencyRepo = currencyRepo;
        this.phoneStatusRepo = phoneStatusRepo;
        this.planStatusRepo = planStatusRepo;
        this.planTemplateStatusRepo = planTemplateStatusRepo;
        this.userSubscriptionStatusRepo = userSubscriptionStatusRepo;
    }

    @Override
    @Cacheable(CacheKey.CACHE_ACTIVE_USS)
    public UserSubscriptionStatus findActiveUserSubscriptionStatus() {

        return userSubscriptionStatusRepo
                .findByUserSubscriptionStatusName(ACTIVE_USER_SUBSCRIPTION_STATUS_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find active user subscription status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_WAITING_FOR_PAYMENT_USS)
    public UserSubscriptionStatus findWaitingForPaymentUserSubscriptionStatus() {

        return userSubscriptionStatusRepo
                .findByUserSubscriptionStatusName(WAITING_FOR_PAYMENT_USER_SUBSCRIPTION_STATUS_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find waiting for payment user subscription status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.NON_CACHE_ACTIVE_USS)
    public UserSubscriptionStatus findNonActiveUserSubscriptionStatus() {

        return userSubscriptionStatusRepo
                .findByUserSubscriptionStatusName(NON_ACTIVE_USER_SUBSCRIPTION_STATUS_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find non activet user subscription status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_ACTIVE_PS)
    public PlanStatus findActivePlanStatus() {

        return planStatusRepo
                .findByPlanStatusName(ACTIVE_PLAN_STATUS_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find active plan status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_DONE_PS)
    public PlanStatus findDonePlanStatus() {

        return planStatusRepo
                .findByPlanStatusName(DONE_PLAN_STATUS_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find done plan status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_FINISHED_OS)
    public OrderStatus findClosedOrderStatus() {

        return orderStatusRepo
                .findByOrderStatusName(CLOSED_ORDER_STATUS_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find closed order status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_HISTORY_CLEAR_OS)
    public OrderStatus findHistoryClearOrderStatus() {

        return orderStatusRepo
                .findByOrderStatusName(HISTORY_DELETED_ORDER_STATUS_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find history clear order status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_ACTUAL_OS)
    public OrderStatus findActualOrderStatus() {

        return orderStatusRepo
                .findByOrderStatusName(ACTUAL_ORDER_STATUS_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find actual order status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_ACTIVE_BS)
    public BotStatus findActiveBotStatus() {

        return botStatusRepo
                .findByBotStatusName(ACTIVE_BOT_STATUS_STATUS_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find active bot status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_EXPIRED_BS)
    public BotStatus findExpiredBotStatus() {

        return botStatusRepo
                .findByBotStatusName(EXPIRED_BOT_STATUS_STATUS_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find expired bot status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_BANNED_BS)
    public BotStatus findBannedBotStatus() {

        return botStatusRepo
                .findByBotStatusName(BANNED_BOT_STATUS_STATUS_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find banned bot status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_NOT_ACTIVE_PTS)
    public PlanTemplateStatus findNotActivePlanTemplateStatus() {

        return planTemplateStatusRepo
                .findByPlanTemplateStatusName(NOT_ACTIVE_PLAN_TEMPLATE_STATUS_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find not active plan template status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_ACTIVE_PTS)
    public PlanTemplateStatus findActivePlanTemplateStatus() {

        return planTemplateStatusRepo
                .findByPlanTemplateStatusName(ACTIVE_PLAN_TEMPLATE_STATUS_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find active plan template status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_ACTIVE_US)
    public UserStatus findActiveUserStatus() {

        return userStatusRepo
                .findByUserStatusName(ACTIVE_USER_STATUS_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find active user status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_ACTIVE_GOOGLE_US)
    public UserStatus findActiveGoogleUserStatus() {

        return userStatusRepo
                .findByUserStatusName(ACTIVE_GOOGLE_USER_STATUS_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find active google user status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_ACTIVE_FACEBOOK_US)
    public UserStatus findActiveFacebookUserStatus() {

        return userStatusRepo
                .findByUserStatusName(ACTIVE_FACEBOOK_USER_STATUS_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find active facebook user status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_NOT_ACTIVE_US)
    public UserStatus findNotActiveUserStatus() {

        return userStatusRepo
                .findByUserStatusName(NOT_ACTIVE_USER_STATUS_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find not active user status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_ACTIVE_OS)
    public List<OrderStatus> findActiveOrderStatuses() {

        return ACTIVE_ORDER_STATUS_NAMES
                .stream()
                .map(
                        orderStatusName -> orderStatusRepo
                                .findByOrderStatusName(orderStatusName)
                                .orElseThrow(
                                        () -> new RuntimeException("Cannot find actual order status records in repo")
                                )
                )
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(CacheKey.CACHE_NOT_STARTED_OAR)
    public List<OrderActionResult> findNotStartedOrderActionResults() {

        return NOT_STARTED_ACTION_RESULT_NAMES
                .stream()
                .map(
                        orderActionResultName -> orderActionResultRepo
                                .findByOrderActionResultName(orderActionResultName)
                                .orElseThrow(
                                        () -> new RuntimeException("Cannot find in progress order action result records in repo")
                                )
                )
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(CacheKey.CACHE_CREATED_OAR)
    public OrderActionResult findCreatedOrderActionResult() {

        return orderActionResultRepo
                .findByOrderActionResultName(CREATED_ACTION_RESULT_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find created order action result records in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_IN_PROGRESS_OAR)
    public OrderActionResult findInProgressOrderActionResult() {

        return orderActionResultRepo
                .findByOrderActionResultName(IN_PROGRESS_ACTION_RESULT_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find in progress order action result records in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_FINISHED_OAR)
    public OrderActionResult findFinishedOrderActionResult() {

        return orderActionResultRepo
                .findByOrderActionResultName(FINISHED_ACTION_RESULT_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find finished order action result records in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_WAITING_FOR_BOT_OAR)
    public OrderActionResult findWaitingForBotOrderActionResult() {

        return orderActionResultRepo
                .findByOrderActionResultName(WAITING_FOR_BOT_ACTION_RESULT_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find waiting for bot order action result records in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_BOT_IS_EXPIRED_OAR)
    public OrderActionResult findBotIsExpiredOrderActionResult() {

        return orderActionResultRepo
                .findByOrderActionResultName(BOT_IS_EXPIRED_ACTION_RESULT_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find expired bot order action result records in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_NEED_REMAKE_ACTION_RESULT)
    public OrderActionResult findNeedRemakeOrderActionResult() {

        return orderActionResultRepo
                .findByOrderActionResultName(NEED_REMAKE_ACTION_RESULT_NAME)
                .orElseThrow(() -> new RuntimeException("Cannot find need remake order action result records in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_EXECUTION_ERROR_ACTION_RESULTS)
    public List<OrderActionResult> findExecutionErrorOrderActionResults() {

        return EXECUTION_ERROR_ACTION_RESULT_NAMES
                .stream()
                .map(
                        orderActionResultName -> orderActionResultRepo
                                .findByOrderActionResultName(orderActionResultName)
                                .orElseThrow(
                                        () -> new RuntimeException("Cannot find execution error order action result records in repo")
                                )
                )
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(CacheKey.CACHE_VIDEO_ACTION_TYPES)
    public List<ActionType> findVideoActionTypes() {

        return VIDEO_ORDER_ACTION_TYPE_NAMES
                .stream()
                .map(
                        videoActionTypeName -> actionTypeRepo.findByActionTypeName(videoActionTypeName)
                                .orElseThrow(
                                        () -> new RuntimeException("Cannot find video action type records in repo")
                                )
                )
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(CacheKey.CACHE_ACCOUNT_ACTION_TYPES)
    public List<ActionType> findAccountActionTypes() {

        return ACCOUNT_ORDER_ACTION_TYPE_NAMES
                .stream()
                .map(
                        accountActionTypeName -> actionTypeRepo.findByActionTypeName(accountActionTypeName)
                                .orElseThrow(
                                        () -> new RuntimeException("Cannot find account action type records in repo")
                                )
                )
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(CacheKey.CACHE_BS_BY_ID)
    public BotStatus findBotStatusById(Integer botStatusId) {

        return botStatusRepo
                .findById(botStatusId)
                .orElseThrow(() -> new RuntimeException("Cannot find bot status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_PHS_BY_ID)
    public PhoneStatus findPhoneStatusById(Integer phoneStatusId) {

        return phoneStatusRepo
                .findById(phoneStatusId)
                .orElseThrow(() -> new RuntimeException("Cannot find phone status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_BRT_BY_ID)
    public BotRegistrationType findBotRegistrationTypeById(Integer botRegistryTypeId) {

        return botRegistrationTypeRepo
                .findById(botRegistryTypeId)
                .orElseThrow(() -> new RuntimeException("Cannot find bot registry type record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_COUNTRY)
    public Country findCountryById(Integer countryId) {

        return countryRepo
                .findById(countryId)
                .orElseThrow(() -> new RuntimeException("Cannot find country record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_USER_STATUS)
    public UserStatus findUserStatusById(Integer userStatusId) {

        return userStatusRepo
                .findById(userStatusId)
                .orElseThrow(() -> new RuntimeException("Cannot find user status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_ORDER_STATUS)
    public OrderStatus findOrderStatusById(Integer orderStatusId) {

        return orderStatusRepo
                .findById(orderStatusId)
                .orElseThrow(() -> new RuntimeException("Cannot find order status record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_AT_BY_NAME)
    public ActionType findActionTypeByName(String actionTypeName) {

        return actionTypeRepo
                .findByActionTypeName(actionTypeName)
                .orElseThrow(() -> new RuntimeException("Cannot find order type record in repo by name"));
    }
    @Override
    @Cacheable(CacheKey.CACHE_AT_BY_ID)
    public ActionType findActionTypeById(Integer actionTypeId) {

        return actionTypeRepo
                .findById(actionTypeId)
                .orElseThrow(() -> new RuntimeException("Cannot find order type record in repo by id"));
    }


    @Override
    @Cacheable(CacheKey.CACHE_C_BY_ISO)
    public Currency findCurrencyByIso(String currencyIso) {

        return currencyRepo
                .findById(currencyIso)
                .orElseThrow(() -> new RuntimeException("Cannot find currency record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_C_BY_ID)
    public Currency findCurrencyById(String currencyIso) {

        return currencyRepo
                .findById(currencyIso)
                .orElseThrow(() -> new RuntimeException("Cannot find currency record in repo"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_AR_BY_ID)
    public OrderActionResult findActionResultById(Integer actionResultId) {

        return orderActionResultRepo
                .findById(actionResultId)
                .orElseThrow(() -> new RuntimeException("Cannot find action result record in repo" + actionResultId));
    }

    @Override
    @Cacheable(CacheKey.CACHE_PT_BY_ID)
    public DepositPaymentType findPaymentTypeById(Integer paymentStatusId) {

        return depositPaymentTypeRepo
                .findById(paymentStatusId)
                .orElseThrow(() -> new RuntimeException("Cannot find payment status record in repo"));
    }

    @Override
    @Transactional
    public void saveAllReferences() {

        List<UserStatus> userStatuses = UserStatusDefine.getAll();
        List<Country> countries = CountryDefine.getAll();
        List<OrderActionResult> orderActionResults = OrderActionResultDefine.getAll();
        List<DepositPaymentType> depositPaymentTypes = PaymentTypeDefine.getAll();
        List<BotStatus> botStatuses = BotStatusDefine.getAll();
        List<BotRegistrationType> botRegistrationTypes = BotRegistrationTypeDefine.getAll();
        List<PhoneStatus> phoneStatuses = PhoneStatusDefine.getAll();
        List<ActionType> actionTypes = ActionTypeDefine.getAll();
        List<OrderStatus> orderStatuses = OrderStatusDefine.getAll();
        List<PlanTemplateStatus> planTemplateStatuses = PlanTemplateStatusDefine.getAll();
        List<PlanStatus> planStatuses = PlanStatusDefine.getAll();
        List<UserSubscriptionStatus> userSubscriptionStatuses = UserSubscriptionStatusDefine.getAll();
        List<Currency> currencies = CurrencyDefine.getAll();

        userStatusRepo.saveAll(userStatuses);
        countryRepo.saveAll(countries);
        orderActionResultRepo.saveAll(orderActionResults);
        depositPaymentTypeRepo.saveAll(depositPaymentTypes);
        botStatusRepo.saveAll(botStatuses);
        botRegistrationTypeRepo.saveAll(botRegistrationTypes);
        phoneStatusRepo.saveAll(phoneStatuses);
        actionTypeRepo.saveAll(actionTypes);
        orderStatusRepo.saveAll(orderStatuses);
        planStatusRepo.saveAll(planStatuses);
        planTemplateStatusRepo.saveAll(planTemplateStatuses);
        userSubscriptionStatusRepo.saveAll(userSubscriptionStatuses);
        currencyRepo.saveAll(currencies);
    }
}
