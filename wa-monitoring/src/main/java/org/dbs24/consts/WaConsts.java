/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.consts;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.entity.*;
import org.dbs24.entity.dto.AgentPayloadInfo;
import org.dbs24.rest.api.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

import static org.dbs24.consts.RestHttpConsts.URI_API;

public final class WaConsts {

    //==========================================================================

    public static class Common {
        public static final String APP_NAME = "wa-monitoring";
    }

    // Account processing
    public static class Uri {

        // agent
        public static final String URI_CREATE_AGENT = URI_API + ("/createAgent");
        public static final String URI_GET_AGENTS_LIST = URI_API + ("/getAgentsList");
        public static final String URI_GET_AGENTS_LIST_BY_ACTUAL_DATE = URI_API + ("/getAgentsListByActulDate");
        public static final String URI_GET_AGENT_HISTORY = URI_API + ("/getAgentHistory");
        public static final String URI_CREATE_OR_UPDATE_AGENT_MESSAGE = URI_API + ("/createOrUpdateAgentMessage");
        public static final String URI_GET_AGENT_MESSAGE = URI_API + ("/getAgentMessage");
        public static final String URI_GET_ACTUAL_MESSAGES_COUNT = URI_API + ("/getActualMessagesCount");
        public static final String URI_GET_MESSAGING_SUBSCRIPTION = URI_API + ("/getMessagingSubscriptions");
        public static final String URI_GET_MESSAGING_LAST_CASHE = URI_API + ("/getMessagingLastMessageCache");

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
        public static final String URI_PREPARE_ACTUAL_SUBSCRIPTIONS = URI_API + ("/prepareActualSubscriptions");
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

    public static class SwaggerTags {
        public static final String TAG_USERS = "Users activities";
        public static final String TAG_CONTRACTS = "Users contracts activities";
        public static final String TAG_TARIFFS = "Tariffs activities";
        public static final String TAG_AGENTS = "Agents activities";
        public static final String TAG_SUBSCRIPTIONS = "Subscriptions managements activities";
        public static final String TAG_AVATARS = "Avatars activities";
        public static final String TAG_DEVICES = "Devices activities";
        public static final String TAG_ACTIVITIES = "Subscriptions activities";
        public static final String TAG_SETTINGS = "Application setting activities";
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
        public static final String QP_AGENT_NOTE = "agentNote";
        public static final String QP_DEVICE_TYPE = "deviceType";
        public static final String QP_DEVICE_UID = "deviceUid";
        public static final String QP_TARIFF_PLAN_STATUS = "tariffPlanStatus";
        public static final String QP_AMOUNT = "amount";
        public static final String QP_ACTIVITY_LIMIT = "activityLimit";
        public static final String QP_PAGE_SIZE = "pageSize";
    }

    //==========================================================================
    public static class Classes {

        // users        
        public static final Class<CreatedUser> CREATED_USER_CLASS = CreatedUser.class;
        public static final Class<UserInfo> USER_INFO_CLASS = UserInfo.class;
        public static final Class<UserAndroidInfo> USER_ANDROID_INFO_CLASS = UserAndroidInfo.class;
        //public static final Class<GeneralUserInfo> GENERAL_USER_INFO_CLASS = GeneralUserInfo.class;        
        public static final Class<UserAttrsInfo> USER_ATTRS_INFO_CLASS = UserAttrsInfo.class;
        public static final Class<User> USER_CLASS = User.class;
        public static final Class<UserHist> USER_HIST_CLASS = UserHist.class;
        public static final Class<AppUserInfoCollection> USER_INFO_COLLECTION_CLASS = AppUserInfoCollection.class;
        public static final Class<AppUserIdInfoCollection> USER_ID_INFO_COLLECTION_CLASS = AppUserIdInfoCollection.class;
        public static final Class<LoginTokenInfo> LOGIN_TOKEN_INFO_CLASS = LoginTokenInfo.class;

        // agents
        public static final Class<AgentInfo> AGENT_INFO_CLASS = AgentInfo.class;
        public static final Class<AgentHist> AGENT_HIST_CLASS = AgentHist.class;
        public static final Class<AgentInfoCollection> AGENT_INFO_COLLECTION_CLASS = AgentInfoCollection.class;
        public static final Class<Agent> AGENT_CLASS = Agent.class;
        public static final Class<CreatedAgent> CREATED_AGENT_CLASS = CreatedAgent.class;
        // contracts
        public static final Class<UserContractInfo> USER_CONTRACT_INFO_CLASS = UserContractInfo.class;
        public static final Class<UserContract> USER_CONTRACT_CLASS = UserContract.class;
        public static final Class<UserContractHist> USER_CONTRACT_HIST_CLASS = UserContractHist.class;
        public static final Class<CreatedUserContract> CREATED_USER_CONTRACT_CLASS = CreatedUserContract.class;
        public static final Class<UserContractInfoCollection> USER_CONTRACT_INFO_COLLECTION_CLASS = UserContractInfoCollection.class;
        public static final Class<ShortUserContractInfo> SHORT_USER_CONTRACT_INFO_CLASS = ShortUserContractInfo.class;
        public static final Class<ShortUserContractInfoCollection> SHORT_USER_CONTRACT_INFO_COLLECTION_CLASS = ShortUserContractInfoCollection.class;
        // subsriptions
        public static final Class<UserSubscriptionInfoCollection> USER_SUBSCRIPTION_INFO_COLLECTION_CLASS = UserSubscriptionInfoCollection.class;
        public static final Class<UserSubscriptionInfo> USER_SUBSCRIPTION_INFO_CLASS = UserSubscriptionInfo.class;
        public static final Class<UserSubscription> USER_SUBSCRIPTION_CLASS = UserSubscription.class;
        public static final Class<UserSubscriptionHist> USER_SUBSCRIPTION_HIST_CLASS = UserSubscriptionHist.class;
        public static final Class<CreatedUserSubscription> CREATED_USER_SUBSCRIPTION_CLASS = CreatedUserSubscription.class;
        public static final Class<UserSubscriptionNotifyStatusInfo> USER_SUBSCRIPTION_NOTIFY_STATUS_INFO_CLASS = UserSubscriptionNotifyStatusInfo.class;
        public static final Class<UserSubscriptionNotifyStatusExtInfo> USER_SUBSCRIPTION_NOTIFY_STATUS_INFO_EXT_CLASS = UserSubscriptionNotifyStatusExtInfo.class;
        public static final Class<UserSubscriptionNotifyStatusExtInfoCollection> USER_SUBSCRIPTION_NOTIFY_STATUS_EXT_INFO_COLLECTION_CLASS = UserSubscriptionNotifyStatusExtInfoCollection.class;
        public static final Class<UserSubscriptionAgentInfoCollection> USER_SUBSCRIPTION_AGENT_INFO_COLLECTION_CLASS = UserSubscriptionAgentInfoCollection.class;

        // activities
        public static final Class<ActivityInfo> ACTIVITY_INFO_CLASS = ActivityInfo.class;
        public static final Class<ActivitiesRequest> ACTIVITIES_REQUEST_CLASS = ActivitiesRequest.class;
        public static final Class<SubscriptionActivity> SUBSCRIPTION_ACTIVITY_CLASS = SubscriptionActivity.class;
        public static final Class<CreatedActivity> CREATED_ACTIVITY_CLASS = CreatedActivity.class;

        // other
        public static final Class<LicenseAgreementInfo> LICENSE_AGREEMENT_INFO_CLASS = LicenseAgreementInfo.class;
        public static final Class<AppSettingsInfo> APP_SETTINGS_INFO_CLASS = AppSettingsInfo.class;
        public static final Class<LastSessionInfo> LAST_SESSION_INFO_CLASS = LastSessionInfo.class;
        public static final Class<AgentPayloadInfo> AGENT_PAYLOAD_INFO_CLASS = AgentPayloadInfo.class;

        // devices
        public static final Class<UserDeviceInfo> USER_DEVICE_INFO_CLASS = UserDeviceInfo.class;
        public static final Class<UserDeviceShortInfo> USER_DEVICE_SHORT_INFO_CLASS = UserDeviceShortInfo.class;
        public static final Class<UserDevice> USER_DEVICE_CLASS = UserDevice.class;
        public static final Class<UserDeviceAndroid> USER_DEVICE_ANDROID_CLASS = UserDeviceAndroid.class;
        public static final Class<UserDeviceHist> USER_DEVICE_HIST_CLASS = UserDeviceHist.class;
        public static final Class<CreatedUserDevice> CREATED_USER_DEVICE_CLASS = CreatedUserDevice.class;
        public static final Class<UserDevicesInfoCollection> USER_DEVICES_INFO_COLLECTION_CLASS = UserDevicesInfoCollection.class;

        public static final Class<DeviceSessionInfo> DEVICE_SESSION_INFO_CLASS = DeviceSessionInfo.class;
        public static final Class<DeviceSession> DEVICE_SESSION_CLASS = DeviceSession.class;
        public static final Class<CreatedDeviceSession> CREATED_DEVICE_SESSION_CLASS = CreatedDeviceSession.class;

        public static final Class<UserTokenInfo> USER_TOKEN_INFO_CLASS = UserTokenInfo.class;
        public static final Class<UserToken> USER_TOKEN_CLASS = UserToken.class;
        public static final Class<CreatedUserToken> CREATED_USER_TOKEN_CLASS = CreatedUserToken.class;

        public static final Class<AuthKeyInfo> AUTH_KEY_INFO_CLASS = AuthKeyInfo.class;
        public static final Class<AuthKeyCollection> AUTH_KEY_COLLECTION_CLASS = AuthKeyCollection.class;

        public static final Class<MigrationScriptInfo> MIGRATION_SCRIPT_CLASS = MigrationScriptInfo.class;

        // references
        public static final Class<ActionResult> ACTION_RESULT_CLASS = ActionResult.class;
        public static final Class<Country> COUNTRY_CLASS = Country.class;
        public static final Class<ModifyReason> CURRENCY_CLASS = ModifyReason.class;
        public static final Class<AgentStatus> AGENT_STATUS_CLASS = AgentStatus.class;
        public static final Class<ContractType> CONTRACT_TYPE_CLASS = ContractType.class;
        public static final Class<ContractStatus> CONTRACT_STATUS_CLASS = ContractStatus.class;
        public static final Class<SubscriptionStatus> SUBSCRIPTION_STATUS_CLASS = SubscriptionStatus.class;
        public static final Class<DeviceType> DEVICE_TYPE_CLASS = DeviceType.class;

        public static final Class<SubscriptionPhoneInfo> SPI_CLASS = SubscriptionPhoneInfo.class;
        public static final Class<CreatedGooglePayment> CREATED_GP_CLASS = CreatedGooglePayment.class;
        public static final Class<VisitNoteInfo> VISIT_NOTE_INFO_CLASS = VisitNoteInfo.class;

        // attributes
        public static final Class<AndroidAttrs> ANDROID_ATTRS_CLASS = AndroidAttrs.class;
        public static final Class<IosAttrs> IOS_ATTRS_CLASS = IosAttrs.class;

    }

    public static class References {

        public static final Byte AS_RESERVE = Byte.valueOf("1");
        public static final Byte AS_SUPPORT = Byte.valueOf("2");
        public static final Byte AS_TRACKNG = Byte.valueOf("3");
        public static final Byte AS_BANNED = Byte.valueOf("4");
        public static final Byte AS_QUARANTINE = Byte.valueOf("5");
        public static final Byte AS_MESSAGING = Byte.valueOf("6");
        public static final Byte AS_INSURANCE = Byte.valueOf("0");

        //==============================================================================================================
        public static final Byte OS_BASIC = Byte.valueOf("1");
        public static final Byte OS_BUSINESS = Byte.valueOf("2");

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
        public static final String CACHE_AGENT_OS_TYPE_REF = "agentOsTypeRef";
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
        public static final int OC_OPERATION_NOT_ALLOWED = -5;
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
        public static final String KAFKA_ACTUAL_SUBSCRIPTION = "wa_actualSubscrptions_02";

    }
}
