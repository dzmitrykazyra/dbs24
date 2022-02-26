/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.consts;

import static org.dbs24.consts.RestHttpConsts.URI_API;
import org.dbs24.rest.api.*;
import org.dbs24.entity.*;

public final class WaConsts {

    //==========================================================================
    public static final String URI_CREATE_APP_USER = URI_API.concat("/createAppUser");
    public static final String URI_CREATE_USER = URI_API.concat("/createUser");
    public static final String URI_CREATE_AGENT = URI_API.concat("/createAgent");
    public static final String URI_CREATE_CONTRACT = URI_API.concat("/createContract");
    public static final String URI_CREATE_SUBSCRIPTION = URI_API.concat("/createSubscription");
    public static final String URI_CREATE_ACTIVITY = URI_API.concat("/createActivity");
    public static final String URI_CREATE_AUTH_KEY = URI_API.concat("/createAuthKey");
    public static final String URI_CREATE_SP = URI_API.concat("/createSubscriptionPhone");
    public static final String URI_CREATE_VISIT_NOTE = URI_API.concat("/createVisitNote");
    public static final String URI_CREATE_PAYMENT = URI_API.concat("/createPayment");
    public static final String URI_HILOAD_CREATE_VISIT_NOTE = URI_API.concat("/createHiLoadVisitNote");
    public static final String URI_GET_AGENTS = URI_API.concat("/getAgents");
    public static final String URI_GET_APP_USERS = URI_API.concat("/getUsers");
    public static final String URI_GET_ACTUAL_APP_USERS = URI_API.concat("/getActualUsers");
    public static final String URI_GET_ALL_SUBSCRIPTIONS = URI_API.concat("/getSubscriptions");
    //==========================================================================
    public static final Class<CreatedUser> CREATED_USER_CLASS = CreatedUser.class;
    public static final Class<AppUserInfo> USER_INFO_CLASS = AppUserInfo.class;
    public static final Class<AppUser> APP_USER_CLASS = AppUser.class;
    public static final Class<AppUserCollection> APP_USER_COLLECTION_CLASS = AppUserCollection.class;
    public static final Class<AppUserIdCollection> APP_USER_ID_COLLECTION_CLASS = AppUserIdCollection.class;
    
    public static final Class<AgentInfo> AGENT_INFO_CLASS = AgentInfo.class;
    public static final Class<CreatedAgent> CREATED_AGENT_CLASS = CreatedAgent.class;

    public static final Class<UserContractInfo> USER_CONTRACT_INFO_CLASS = UserContractInfo.class;
    public static final Class<CreatedUserContract> CREATED_USER_CONTRACT_CLASS = CreatedUserContract.class;    
    
    public static final Class<SubscriptionPhoneInfo> SUBSCRIPTION_PHONE_INFO_CLASS = SubscriptionPhoneInfo.class;
    public static final Class<UserSubscriptionInfo> USER_SUBSCRIPTION_INFO_CLASS = UserSubscriptionInfo.class;
    public static final Class<CreatedUserSubscription> CREATED_USER_SUBSCRIPTION_CLASS = CreatedUserSubscription.class;
    public static final Class<SubscriptionPhoneCollection> SUBSCRIPTION_PHONE_COLLECTION_CLASS = SubscriptionPhoneCollection.class;
    
    
    public static final Class<ActivityInfo> ACTIVITY_INFO_CLASS = ActivityInfo.class;
    public static final Class<CreatedActivity> CREATED_ACTIVITY_CLASS = CreatedActivity.class;
    
    public static final Byte AS_RESERVE = Byte.valueOf("0");
    public static final Byte AS_SUPPORT = Byte.valueOf("1");
    public static final Byte AS_TRACKNG = Byte.valueOf("3");
    public static final Byte AS_BANNED = Byte.valueOf("4");
    public static final Byte AS_QUARANTINE = Byte.valueOf("5");
    
    public static final Integer CT_4 = Integer.valueOf("4");
    public static final Integer CT_10 = Integer.valueOf("10");
    
    public static final Byte CS_ACTUAL = Byte.valueOf("0");
    public static final Byte CS_CLOSED = Byte.valueOf("1");
    public static final Byte CS_ROLLOVER = Byte.valueOf("2");
    public static final Byte CS_CANCELLED = Byte.valueOf("-1");
    public static final Byte CS_TRIAL = Byte.valueOf("9");
  
    public static final Byte SS_ACTUAL = Byte.valueOf("0");
    public static final Byte SS_CLOSED = Byte.valueOf("1");    
    public static final Byte SS_CANCELLED = Byte.valueOf("-1");   
    public static final Class<SubscriptionStatus> SUBSCRIPTION_STATUS_CLASS = SubscriptionStatus.class;

    public static final Class<AuthKey> AUTH_KEY_CLASS = AuthKey.class;
    public static final Class<AuthKeyInfo> AUTH_KEY_INFO_CLASS = AuthKeyInfo.class;
    public static final Class<CreatedAuthKey> CREATED_AUTH_KEY_CLASS = CreatedAuthKey.class;
    public static final Class<AuthKeyCollection> AUTH_KEY_COLLECTION_CLASS = AuthKeyCollection.class;

    public static final Class<SubscriptionPhone> SP_CLASS = SubscriptionPhone.class;
    public static final Class<SubscriptionPhoneInfo> SPI_CLASS = SubscriptionPhoneInfo.class;
    public static final Class<CreatedSubscriptionPhone> CREATED_SP_CLASS = CreatedSubscriptionPhone.class;

    public static final Class<Payment> GP_CLASS = Payment.class;
    public static final Class<PaymentInfo> GPI_CLASS = PaymentInfo.class;
    public static final Class<CreatedGooglePayment> CREATED_GP_CLASS = CreatedGooglePayment.class;

    public static final Class<VisitNote> VISIT_NOTE_CLASS = VisitNote.class;
    public static final Class<VisitNoteInfo> VISIT_NOTE_INFO_CLASS = VisitNoteInfo.class;
    public static final Class<CreatedVisitNote> CREATED_VISIT_NOTE_CLASS = CreatedVisitNote.class;

    //==========================================================================
    public static final int WC_TASK = 10100;

    public static final int ES_TASK_IN_PROGRESS = 0;
    public static final int ES_TASK_IN_READY = 1;
    public static final int ES_TASK_IN_CANCELLED = -1;

}
