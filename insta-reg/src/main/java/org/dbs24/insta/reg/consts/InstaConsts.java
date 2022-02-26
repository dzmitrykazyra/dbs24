/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.consts;

import static org.dbs24.consts.RestHttpConsts.URI_API;

/**
 *
 * @author kdg
 */
public final class InstaConsts {

    // AccountStatuses
    public static class AccountStatuses {

        public static final Byte AS_JUST_CREATED = Byte.valueOf("1");
        public static final Byte AS_IN_PROGRESS = Byte.valueOf("2");
        public static final Byte AS_POSTPONED = Byte.valueOf("3");
        public static final Byte AS_WAIT_4_CODE = Byte.valueOf("4");
        public static final Byte AS_READY = Byte.valueOf("5");
        public static final Byte AS_BANNED = Byte.valueOf("-1");
        public static final Byte AS_CHECK_POINT_REQUIRED = Byte.valueOf("-2");
        public static final Byte AS_REJECTED = Byte.valueOf("-3");
        public static final Byte AS_EMAIL_SHARING_LIMIT = Byte.valueOf("-4");
        public static final Byte AS_MARKED_AS_OPEN_PROXY = Byte.valueOf("-5");
        public static final Byte AS_ANOTHER_ACCOUNT_USING = Byte.valueOf("-6");

    }

    // Batches
    public static class Bathches {

        public static final Integer BH_DEFUALT = Integer.valueOf("1");
        public static final Integer BH_ADVANCED = Integer.valueOf("2");
    }

    // EmailStatuses
    public static class EmailStatuses {

        public static final Byte ES_ACTUAL = Byte.valueOf("1");
        public static final Byte ES_CLOSED = Byte.valueOf("2");
        public static final Byte ES_BANNED = Byte.valueOf("3");
        public static final Byte ES_RESERVED = Byte.valueOf("4");

    }

    // ProxyStatuses
    public static class ProxyStatuses {

        public static final Byte PS_ACTUAL = Byte.valueOf("1");
        public static final Byte PS_CLOSED = Byte.valueOf("2");
        public static final Byte PS_TEMPORARY = Byte.valueOf("3");
        public static final Byte PS_BANNED = Byte.valueOf("4");
    }

    // ProxyTypes
    public static class ProxyTypes {

        public static final Byte PT_STANDARD = Byte.valueOf("1");
        public static final Byte PT_MOBILE = Byte.valueOf("2");
        public static final Byte PS_SERVER = Byte.valueOf("3");

    }

    // Actions
    public static class ActionCodes {

        public static final int ACT_CHANEG_IP = 3;
        public static final int ACT_VALIDAATE_EMAIL = 5;
        public static final int ACT_GET_MID = 15;
        public static final int ACT_GENERATE_CSRF = 20;
        public static final int ACT_GET_FIRST = 25;
        public static final int ACT_BATCH_FETCH_WEB = 30;
        public static final int ACT_EMAIL_SIGN_UP = 35;
        public static final int ACT_CREATE_ACCOUNT_FIRST = 40;
        public static final int ACT_CREATE_ACCOUNT_SECOND = 45;
        public static final int ACT_CREATE_ACCOUNT_THIRD = 50;
        public static final int ACT_CHECK_AGES = 55;
        public static final int ACT_CREATE_ACCOUNT_FOURTH = 60;
        public static final int ACT_SEND_VERIFY_EMAIL_OPTION = 65;
        public static final int ACT_SEND_VERIFY_EMAIL = 70;
        public static final int ACT_GET_VALIDATION_CODE_FROM_EMAIL = 75;
        public static final int ACT_CHECK_CONFIRM_CODE_OPT = 80;
        public static final int ACT_CHECK_CONFIRM_CODE = 85;
        public static final int ACT_WEB_CREATE = 90;
    }

    // Account processing
    public static class AccountProcessing {

        public static final String EMAIL_SHARING_LIMIT = "email_sharing_limit";
        public static final String CHECKPOINT_REQ = "checkpoint_required";
        public static final String OPEN_PROXY = "open proxy";
        public static final String ANOTHER_ACCOUNT = "Another account is using";
        public static final String TO_MANY_ACCOUNTS = "Too many accounts are using";
    }
    
    
    // Account processing
    public static class UriConsts {

        public static final String URI_GET_FAKED_MAIL = URI_API.concat("/getFakedMail");
        public static final String URI_GET_LATEST_EXCEPTIONS = URI_API.concat("/getLatestExceptions");

    }    
}
