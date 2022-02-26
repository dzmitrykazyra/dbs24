/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.consts;

import org.dbs24.application.core.service.funcs.ServiceFuncs;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

import static org.dbs24.consts.RestHttpConsts.URI_API;

public final class WaConsts {

    //==========================================================================
    // Account processing
    public static class Uri {

        // agent
        public static final String URI_CREATE_AGENT = URI_API + ("/createAgent");
        public static final String URI_GET_AGENTS_LIST = URI_API + ("/getAgentsList");
        public static final String URI_GET_AGENTS_LIST_BY_ACTUAL_DATE = URI_API + ("/getAgentsListByActulDate");
        public static final String URI_GET_AGENT_HISTORY = URI_API + ("/getAgentHistory");
        // contract
        public static final String URI_CREATE_CONTRACT = URI_API + ("/createContract");
        public static final String URI_CREATE_OR_UPDATE_FUTUTE_TRIAL_CONTRACT = URI_API + ("/createOrUpdateFutureTrialContract");
        public static final String URI_GET_USER_CONTRACTS = URI_API + ("/getUserContracts");
        public static final String URI_GET_CREATE_CONTRACT_FROM_PAYMENT = URI_API + ("/createContractFromPayment");
        public static final String URI_MODIFY_CONTRACT = URI_API + ("/modifyContract");
        public static final String URI_MODIFY_CONTRACT_BY_SUPPORT = URI_API + ("/modifyContractBySupport");
        public static final String URI_GET_ACTUAL_USER_CONTRACTS = URI_API + ("/getActualUserContracts");
        public static final String URI_GET_CHECK_USER_CONTRACTS_VALIDITY = URI_API + ("/checkUserContractValidity");
        public static final String URI_GET_CHECK_ALL_USER_CONTRACTS_VALIDITY = URI_API + ("/checkAllUserContractValidity");
        public static final String URI_MODIFY_CONTRACT_END_DATE = URI_API + ("/modifyContractEndDate");
        // tariff plans
        public static final String URI_CREATE_OR_UPDATE_TARIFF_PLAN = URI_API + ("/createOrUpdateTariffPlan");
        public static final String URI_GET_TARIFF_PLANS = URI_API + ("/getTariffPlans");
        // subscription
        public static final String URI_CREATE_OR_UPDATE_SUBSCRIPTION = URI_API + ("/createOrUpdateSubscription");
        public static final String URI_UPDATE_SUBSCRIPTION_STATUS = URI_API + ("/updateSubscriptionStatus");
        public static final String URI_GET_ALL_SUBSCRIPTIONS = URI_API + ("/getAllSubscriptions");
        public static final String URI_GET_MODIFIED_SUBSCRIPTIONS = URI_API + ("/getModifiedSubscriptions");
        public static final String URI_GET_MODIFIED_DEVICES = URI_API + ("/getModifiedDevices");
        public static final String URI_UPDATE_DEVICE_ATTRS = URI_API + ("/updateDeviceAttrs");
        public static final String URI_GET_ALL_USER_DEVICES = URI_API + ("/getAllUserDevices");
        public static final String URI_GET_EXISTS_DEVICE = URI_API + ("/getExistsDevice");
        public static final String URI_GET_SUBSCRIPTION = URI_API + ("/getSubscription");
        public static final String URI_UPDATE_AGENT_STATUS = URI_API + ("/updateAgentStatus");
        public static final String URI_REBALANCE_AGENTS = URI_API + ("/rebalanceAgents");
        public static final String URI_GET_AGENT_SUBSCRIPTIONS = URI_API + ("/getAgentSubscriptions");
        @Deprecated
        public static final String URI_CREATE_ACTIVITIES = URI_API + ("/createActivities");
        public static final String URI_GET_ACTIVITIES = URI_API + ("/getActivities");
        @Deprecated
        public static final String URI_GET_LAST_ACTIVITIES = URI_API + ("/getLastActivities");
        public static final String URI_GET_LATEST_ACTIVITIES = URI_API + ("/getLatestActivities");
        public static final String URI_GET_SUBSCRIPTIONS_LATEST_ACTIVITIES = URI_API + ("/getSubscriptionsLatestActivities");
        public static final String URI_REMOVE_DEVICE_BY_ID = URI_API + ("/removeDeviceById");
        public static final String URI_CREATE_DEVICE = URI_API + ("/createDevice");
        public static final String URI_CREATE_DEVICE_SESSION = URI_API + ("/createDeviceSession");
        public static final String URI_CREATE_TOKEN = URI_API + ("/createToken");
        public static final String URI_CHECK_TOKEN_VALIDITY = URI_API + ("/checkTokenValidity");
        public static final String URI_CREATE_OR_UPDATE_MP_USER = URI_API + ("/createOrUpdateMultiPlatformUser");
        public static final String URI_GET_SETTINGS = URI_API + ("/getSettings");
        public static final String URI_GET_SUBSCRIPTIONS_NOTIFY_STATUS = URI_API + ("/getSubscriptionNotifyStatus");
        public static final String URI_GET_ALL_USER_SUBSCRIPTIONS_NOTIFY_STATUS = URI_API + ("/getAllUserSubscriptions");
        public static final String URI_GET_INVALID_SUBSCRIPTIONS = URI_API + ("/getInvalidSubscriptions");
        public static final String URI_UPDATE_INVALID_SUBSCRIPTIONS = URI_API + ("/updateInvalidSubscriptions");
        public static final String URI_GET_INVALID_ACTIVITY_SUBSCRIPTIONS = URI_API + ("/getInvalidActivitySubscriptions");
        public static final String URI_GENERATE_TEST_USERS = URI_API + ("/generateTestUsers");
        // avatar
        public static final String URI_CREATE_OR_UPDATE_AVATAR = URI_API + ("/createOrUpdateAvatar");
        public static final String URI_CREATE_OR_UPDATE_CUSTOM_AVATAR = URI_API + ("/createOrUpdateCustomAvatar");
        public static final String URI_GET_AVATAR = URI_API + ("/getAvatar");
        public static final String URI_GET_CUSTOM_AVATAR = URI_API + ("/getCustomAvatar");
        // testing
        public static final String URI_CREATE_TEST_SUBSCRIPTIONS = URI_API + ("/createTestSubscriptions");
        // fireBase
        public static final String URI_CREATE_OR_UPDATE_FIREBASE_APPLICATION = URI_API + ("/createOrUpdateFirebaseApp");
        public static final String URI_ALL_FIREBASE_APPLICATION = URI_API + ("/getFirebaseApps");
        // app settings
        @Deprecated
        public static final String URI_CREATE_OR_UPDATE_PACKAGE_DETAILS = URI_API + ("/createOrUpdatePackageDetails");
        public static final String URI_ALL_PACKAGE_DETAILS = URI_API + ("/getAllPackageDetails");
        public static final String URI_GET_LICENSE_AGREEMENT = URI_API + ("/getLicenseAgreement");
        public static final String URI_CREATE_OR_UPDATE_APP_SETTING = URI_API + ("/createOrUpdateAppSetting");
    }

    public static class RestQueryParams {

        public static final String QP_ACTUAL_DATE = "actualDate";
        public static final String QP_LOGIN_TOKEN = "loginToken";
        public static final String QP_USER_ID = "userId";
        public static final String QP_SUBSCRIPTION_ID = "subscriptionId";
        public static final String QP_HOURS = "hours";
        public static final String QP_IS_NEW = "isNew";
        public static final String QP_SUBSCRIPTION_STATUS = "subscriptionStatus";
        public static final String QP_PHONE = "phone";
        public static final String QP_PHONE_MASK = "phoneMask";
        public static final String QP_CONTRACT_STATUS = "contractStatus";
        public static final String QP_PACKAGE_NAME = "packageName";
        public static final String QP_AGENT_PHONE = "agentPhone";
        public static final String QP_ACTUAL_ONLY = "actualOnly";
        public static final String QP_FRESH_HOURS = "freshHours";
        public static final String QP_D1 = "d1";
        public static final String QP_D2 = "d2";
        public static final String QP_AGENT_ID = "agentId";
        public static final String QP_AGENT_STATUS = "agentStatus";
        public static final String QP_DEVICE_TYPE = "deviceType";
        public static final String QP_DEVICE_UID = "deviceUid";
        public static final String QP_TARIFF_PLAN_STATUS = "tariffPlanStatus";
        public static final String QP_AMOUNT = "amount";
        public static final String QP_ACTIVITY_LIMIT = "activityLimit";
    }

    //==========================================================================
    public static class References {

        public static final Byte AS_RESERVE = Byte.valueOf("1");
        public static final Byte AS_SUPPORT = Byte.valueOf("2");
        public static final Byte AS_TRACKNG = Byte.valueOf("3");
        public static final Byte AS_BANNED = Byte.valueOf("4");
        public static final Byte AS_QUARANTINE = Byte.valueOf("5");

        //==============================================================================================================
        public static final Integer CT_TRIAL = Integer.valueOf("10");
        public static final Integer CT_BASIC = Integer.valueOf("20");
        public static final Integer CT_STANDART = Integer.valueOf("30");
        public static final Integer CT_PREMIUM = Integer.valueOf("40");
        public static final Integer CT_GIFT = Integer.valueOf("100");

        public static final String[][] ALL_CONTRACT_TYPES = new String[][]{
                {String.valueOf(CT_TRIAL), "Trial"},
                {String.valueOf(CT_BASIC), "Basic"},
                {String.valueOf(CT_STANDART), "Standart"},
                {String.valueOf(CT_PREMIUM), "Premium"},
                {String.valueOf(CT_GIFT), "Gift"}
        };

        public static final Collection<Integer> ALL_CONTRACT_TYPES_IDS = ServiceFuncs.createCollection(cp -> Arrays.stream(ALL_CONTRACT_TYPES).map(t -> Integer.valueOf(t[0])).forEach(ref -> cp.add(ref)));

        //==============================================================================================================
        public static final Byte CS_ACTUAL = Byte.valueOf("0");
        public static final Byte CS_CLOSED = Byte.valueOf("1");
        public static final Byte CS_FUTURED = Byte.valueOf("9");
        public static final Byte CS_CANCELLED = Byte.valueOf("-1");

        public static final String[][] ALL_CONTRACT_STATUSES = new String[][]{
                {String.valueOf(CS_ACTUAL), "Actual"},
                {String.valueOf(CS_CANCELLED), "Cancelled"},
                {String.valueOf(CS_CLOSED), "Closed"},
                {String.valueOf(CS_FUTURED), "Futured"}
        };

        //==============================================================================================================
        public static final Byte SS_CREATED = Byte.valueOf("0");
        public static final Byte SS_CONFIRMED = Byte.valueOf("1");
        public static final Byte SS_CLOSED = Byte.valueOf("2");
        public static final Byte SS_CANCELLED = Byte.valueOf("-1");
        public static final Byte SS_PHONE_NOT_EXISTS = Byte.valueOf("-2");

        public static final String[][] ALL_SUBSCRIPTION_STATUSES = new String[][]{
                {String.valueOf(SS_CREATED), "Created"},
                {String.valueOf(SS_CONFIRMED), "Confirmed"},
                {String.valueOf(SS_CLOSED), "Closed"},
                {String.valueOf(SS_CANCELLED), "Cancelled"},
                {String.valueOf(SS_PHONE_NOT_EXISTS), "Phone not exists"}
        };

        public static final Collection<Byte> ALL_SUBSCRIPTION_STATUSES_IDS = ServiceFuncs.createCollection(cp -> Arrays.stream(ALL_SUBSCRIPTION_STATUSES).map(t -> Byte.valueOf(t[0])).forEach(ref -> cp.add(ref)));

        //==============================================================================================================

        public static final Integer DT_ANDROID = Integer.valueOf("0");
        public static final Integer DT_IOS = Integer.valueOf("1");

        public static final Predicate<Integer> isAndroid = deviceTypeId -> deviceTypeId.equals(DT_ANDROID);
        public static final Predicate<Integer> isIos = deviceTypeId -> deviceTypeId.equals(DT_IOS);

        public static final String[][] ALL_DEVICE_TYPES = new String[][]{
                {String.valueOf(DT_ANDROID), "Android"},
                {String.valueOf(DT_IOS), "Ios"}
        };


        public static final Collection<Integer> ALL_DEVICE_TYPES_IDS = ServiceFuncs.createCollection(cp -> Arrays.stream(ALL_DEVICE_TYPES).map(t -> Integer.valueOf(t[0])).forEach(ref -> cp.add(ref)));
        //==============================================================================================================

        public static final Integer PMT_GOOGLE = Integer.valueOf("10");
        public static final Integer PMT_PAYPAL = Integer.valueOf("20");
        public static final Integer PMT_STRIP = Integer.valueOf("30");

        public static final Byte PMT_ST_CREATED = Byte.valueOf("0");
        public static final Byte PMT_ST_CONFIRMED = Byte.valueOf("1");
        public static final Byte PMT_ST_CANCELLED = Byte.valueOf("2");

        public static final Integer TPS_ACTIVE = Integer.valueOf("1");
        public static final Integer TPS_CLOSED = Integer.valueOf("10");

        //==============================================================================================================

        public static final String[][] ALL_TARIFF_PLAN_STATUSES = new String[][]{
                {String.valueOf(TPS_ACTIVE), "Active"},
                {String.valueOf(TPS_CLOSED), "Closed"}
        };

        public static final Collection<Integer> ALL_TARIFF_PLAN_STATUSES_IDS = ServiceFuncs.createCollection(cp -> Arrays.stream(ALL_TARIFF_PLAN_STATUSES).map(t -> Integer.valueOf(t[0])).forEach(ref -> cp.add(ref)));

        //==============================================================================================================

        public static final Integer MRT_GIFT = Integer.valueOf("100");

        public static final String[][] ALL_MODIFY_REASONS_TYPES = new String[][]{
                {String.valueOf(MRT_GIFT), "Gift contract"}
        };

        public static final Collection<Integer> ALL_MODIFY_REASONS_TYPES_IDS = ServiceFuncs.createCollection(cp -> Arrays.stream(ALL_MODIFY_REASONS_TYPES).map(t -> Integer.valueOf(t[0])).forEach(ref -> cp.add(ref)));

        //==============================================================================================================

    }

    public static class Caches {

        //==========================================================================
        public static final String CACHE_USER_LOGIN_TOKEN = "userLoginToken";
        public static final String CACHE_USER_LOGIN_TOKEN_SILENT = "userLoginTokenSilent";
        public static final String CACHE_USER_CONTRACTS = "userContracts";
        public static final String CACHE_ACTUAL_USER_CONTRACTS = "userActualContracts";
        public static final String CACHE_USER_LOGIN_TOKEN_SS_PHONE = "loginTokenAndSubscriptionPhone";
        public static final String CACHE_USER_LOGIN_TOKEN_SUBSCRIPTIONS = "loginTokenSubscriptions";
        public static final String CACHE_USER_LOGIN_TOKEN_ACTUAL_SUBSCRIPTIONS = "loginTokenActualSubscriptions";
        public static final String CACHE_USER_SUBSCRIPTIONS = "userSubscriptions";
        public static final String CACHE_USER_SUBSRIPTION_BY_ID = "subscriptionById";
        public static final String CACHE_USER_SUBSRIPTION_BY_PHONE = "subscriptionByPhone";
        public static final String CACHE_SUBSRIPTION_STATUS_REF = "subscriptionStatusRef";
        public static final String CACHE_AGENT_BY_PHONE_NUM = "agentByPhoneNum";
        public static final String CACHE_USER_SUBSCRIPTION = "subscriptions";
        public static final String CACHE_DEVICE_BY_USER = "deviceByUser";
        public static final String CACHE_ANDROID_DEVICE = "androidDevice";
        public static final String CACHE_IOS_DEVICE = "iosDevice";
        public static final String CACHE_DEVICE_TYPE = "deviceTypeRef";
        public static final String CACHE_COUNTRY = "countryRef";
        public static final String CACHE_MODIFY_REASON = "modifyReasonRef";
        public static final String CACHE_AGENT_STATUS_REF = "agentStatusRef";
        public static final String CACHE_PAYMENT_STATUS_REF = "paymentStatusRef";
        public static final String CACHE_PAYMENT_SERVICE_REF = "paymentServiceRef";
        public static final String CACHE_DEVICE_SILENT = "deviceSilent";
        public static final String CACHE_CONTRACT_STATUS = "contractStatusRef";
        public static final String CACHE_CONTRACT_TYPE = "contractTypeRef";
        public static final String CACHE_TARIFF_PLAN_STATUS_REF = "tariffPlanStatusRef";

        public static final String CACHE_ANDROID_DEVICE_BY_GSFID = "androidDeviceByGsfId";
        public static final String CACHE_IOS_DEVICE_BY_IFV = "iosDeviceIfv";

    }

    public static class OperCode {

        public static final int OC_LIMIT_REACHED = -1;
        public static final int OC_NO_ACTIVE_CONTRACT = -2;
        public static final int OC_CANT_UPDATE_DELETED_SUBSCRIPTION = -3;
        public static final int OC_SUBSCRIPTION_ALRERADY_EXISTS = -4;
        //======================================================================
        public static final int NO_FCM_TOKEN_CHANGED = 101;
        public static final int NO_IDENTIFIER_FOR_VENDOR_CHANGED = 102;
        public static final int OC_UNKNOWN_DEVICE = -100;
        public static final int OC_FCM_TOKEN_NOT_SPECIFIED = -101;
        public static final int OC_CHILD_DEVICES_NOT_FOUND = -110;

        public static final int OC_INVALID_NUM = -1000;
        public static final int OC_INVALID_MASK = -1001;

        public static final String NO_FCM_TOKEN_CHANGED_STR = "no fcm token changed";
        public static final String NO_IDENTIFIER_FOR_VENDOR_CHANGED_STR = "no identifier for vendor changed";
        public static final String OC_UNKNOWN_DEVCE_STR = "Device not found";
        public static final String OC_FCM_TOKEN_NOT_SPECIFIED_STR = "Fcm token is not specified";

    }

    // Kafka topics
    public static class Kafka {

        public static final String KAFKA_MODIFIED_SUBSCRIPTIONS = "wa_modifiedSubscriptions";
        @Deprecated
        public static final String KAFKA_MODIFIED_DEVICES = "wa_modifiedDevices";
        public static final String KAFKA_MODIFIED_DEVICES_ANDROID = "wa_modifiedDevicesAndroid";
        public static final String KAFKA_MODIFIED_DEVICES_IOS = "wa_modifiedDevicesIos";
        public static final String KAFKA_SUBSCRIPTION_ACTIVITIES = "wa_subscrptionActivities";

    }
}
