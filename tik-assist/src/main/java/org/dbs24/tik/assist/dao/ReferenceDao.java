package org.dbs24.tik.assist.dao;

import org.dbs24.tik.assist.constant.reference.*;
import org.dbs24.tik.assist.entity.domain.*;

import java.util.List;

public interface ReferenceDao {

    String ACTIVE_USER_SUBSCRIPTION_STATUS_NAME = UserSubscriptionStatusDefine.USS_ACTIVE.getUserSubscriptionStatusValue();
    String WAITING_FOR_PAYMENT_USER_SUBSCRIPTION_STATUS_NAME = UserSubscriptionStatusDefine.USS_WAITING_FOR_PAYMENT.getUserSubscriptionStatusValue();
    String NON_ACTIVE_USER_SUBSCRIPTION_STATUS_NAME = UserSubscriptionStatusDefine.USS_NON_ACTIVE.getUserSubscriptionStatusValue();
    String DONE_PLAN_STATUS_NAME = PlanStatusDefine.PS_DONE.getPlanStatusValue();
    String ACTIVE_PLAN_STATUS_NAME = PlanStatusDefine.PS_ACTIVE.getPlanStatusValue();
    String CLOSED_ORDER_STATUS_NAME = OrderStatusDefine.OS_CLOSED.getOrderStatusValue();
    String HISTORY_DELETED_ORDER_STATUS_NAME = OrderStatusDefine.OS_HISTORY_CLEARED.getOrderStatusValue();
    String ACTUAL_ORDER_STATUS_NAME = OrderStatusDefine.OS_ACTUAL.getOrderStatusValue();
    String ACTIVE_BOT_STATUS_STATUS_NAME = BotStatusDefine.BS_TRACKING.getBotStatusValue();
    String EXPIRED_BOT_STATUS_STATUS_NAME = BotStatusDefine.BS_EXPIRED.getBotStatusValue();
    String BANNED_BOT_STATUS_STATUS_NAME = BotStatusDefine.BS_BANNED.getBotStatusValue();
    String NOT_ACTIVE_PLAN_TEMPLATE_STATUS_NAME = PlanTemplateStatusDefine.PTS_NON_ACTIVE.getPlanTemplateStatusValue();
    String ACTIVE_PLAN_TEMPLATE_STATUS_NAME = PlanTemplateStatusDefine.PTS_ACTIVE.getPlanTemplateStatusValue();
    String ACTIVE_USER_STATUS_NAME = UserStatusDefine.US_ACTIVE.getStatusValue();
    String ACTIVE_GOOGLE_USER_STATUS_NAME = UserStatusDefine.US_ACTIVE_GOOGLE.getStatusValue();
    String ACTIVE_FACEBOOK_USER_STATUS_NAME = UserStatusDefine.US_ACTIVE_FACEBOOK.getStatusValue();
    String NOT_ACTIVE_USER_STATUS_NAME = UserStatusDefine.US_NOT_ACTIVE.getStatusValue();
    List<String> ACTIVE_ORDER_STATUS_NAMES = List.of(
            OrderStatusDefine.OS_ACTUAL.getOrderStatusValue(),
            OrderStatusDefine.OS_ROLLOVER.getOrderStatusValue(),
            OrderStatusDefine.OS_TRIAL.getOrderStatusValue()
    );
    List<String> NOT_STARTED_ACTION_RESULT_NAMES = List.of(
            OrderActionResultDefine.AR_CREATED.getResultValue(),
            OrderActionResultDefine.AR_NOT_STARTED.getResultValue()
    );
    List<String> EXECUTION_ERROR_ACTION_RESULT_NAMES = List.of(
            OrderActionResultDefine.AR_FAIL.getResultValue(),
            OrderActionResultDefine.AR_WARN.getResultValue(),
            OrderActionResultDefine.AR_PAGE_NOT_EXIST.getResultValue(),
            OrderActionResultDefine.AR_UNKNOWN_ACTION.getResultValue(),
            OrderActionResultDefine.AR_BOT_IS_EXPIRED.getResultValue(),
            OrderActionResultDefine.AR_TASK_NOT_EXISTS.getResultValue()
    );
    String IN_PROGRESS_ACTION_RESULT_NAME = OrderActionResultDefine.AR_IN_PROGRESS.getResultValue();
    String FINISHED_ACTION_RESULT_NAME = OrderActionResultDefine.AR_OK.getResultValue();
    String CREATED_ACTION_RESULT_NAME = OrderActionResultDefine.AR_CREATED.getResultValue();
    String WAITING_FOR_BOT_ACTION_RESULT_NAME = OrderActionResultDefine.AR_WAITING_FOR_BOT.getResultValue();
    String BOT_IS_EXPIRED_ACTION_RESULT_NAME = OrderActionResultDefine.AR_BOT_IS_EXPIRED.getResultValue();
    String NEED_REMAKE_ACTION_RESULT_NAME = OrderActionResultDefine.NEED_REMAKE.getResultValue();
    List<String> VIDEO_ORDER_ACTION_TYPE_NAMES = List.of(
            ActionTypeDefine.AT_GET_LIKES.getActionTypeValue(),
            ActionTypeDefine.AT_GET_VIEWS.getActionTypeValue(),
            ActionTypeDefine.AT_GET_COMMENTS.getActionTypeValue(),
            ActionTypeDefine.AT_GET_REPOSTS.getActionTypeValue()
    );
    List<String> ACCOUNT_ORDER_ACTION_TYPE_NAMES = List.of(
            ActionTypeDefine.AT_GET_FOLLOWERS.getActionTypeValue()
    );

    void saveAllReferences();

    UserSubscriptionStatus findActiveUserSubscriptionStatus();
    UserSubscriptionStatus findWaitingForPaymentUserSubscriptionStatus();
    UserSubscriptionStatus findNonActiveUserSubscriptionStatus();
    PlanStatus findActivePlanStatus();
    PlanStatus findDonePlanStatus();
    OrderStatus findClosedOrderStatus();
    OrderStatus findHistoryClearOrderStatus();
    OrderStatus findActualOrderStatus();
    BotStatus findActiveBotStatus();
    BotStatus findExpiredBotStatus();
    BotStatus findBannedBotStatus();
    PlanTemplateStatus findNotActivePlanTemplateStatus();
    PlanTemplateStatus findActivePlanTemplateStatus();
    UserStatus findActiveUserStatus();
    UserStatus findActiveGoogleUserStatus();
    UserStatus findActiveFacebookUserStatus();
    UserStatus findNotActiveUserStatus();
    List<OrderStatus> findActiveOrderStatuses();
    OrderActionResult findCreatedOrderActionResult();
    OrderActionResult findFinishedOrderActionResult();
    OrderActionResult findInProgressOrderActionResult();
    OrderActionResult findWaitingForBotOrderActionResult();
    OrderActionResult findBotIsExpiredOrderActionResult();
    OrderActionResult findNeedRemakeOrderActionResult();
    List<OrderActionResult> findNotStartedOrderActionResults();
    List<OrderActionResult> findExecutionErrorOrderActionResults();
    List<ActionType> findVideoActionTypes();
    List<ActionType> findAccountActionTypes();

    BotStatus findBotStatusById(Integer agentStatusId);

    PhoneStatus findPhoneStatusById(Integer phoneStatusId);

    BotRegistrationType findBotRegistrationTypeById(Integer agentRegistryTypeId);

    Country findCountryById(Integer countryId);

    UserStatus findUserStatusById(Integer userStatusId);

    OrderStatus findOrderStatusById(Integer contractStatusId);

    ActionType findActionTypeById(Integer actionTypeId);
    ActionType findActionTypeByName(String actionTypeName);

    Currency findCurrencyByIso(String currencyIso);
    Currency findCurrencyById(String currencyIso);

    OrderActionResult findActionResultById(Integer actionResultId);

    DepositPaymentType findPaymentTypeById(Integer paymentStatusId);
}
