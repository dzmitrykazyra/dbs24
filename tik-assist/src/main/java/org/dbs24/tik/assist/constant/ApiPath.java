package org.dbs24.tik.assist.constant;

import static org.dbs24.consts.RestHttpConsts.URI_API;

public class ApiPath {
    private static final String API = "/api";
    private static final String V1 = "/v1";

    private static final String USERS = "/users";
    private static final String ACTIVATION = "/activation";
    private static final String AUTH = "/auth";
    private static final String PASSWORD = "/password";
    private static final String FORGOT  = "/forgot";
    private static final String CHANGE = "/change";
    private static final String AUTHENTICATED = "/authenticated";
    private static final String GOOGLE = "/google";
    private static final String FACEBOOK = "/facebook";
    private static final String LOGIN = "/login";
    private static final String LOGOUT = "/logout";
    private static final String REGISTRATION = "/registration";
    private static final String EMAIL_RESEND = "/email_resend";
    private static final String PROFILES = "/profiles";
    private static final String PLAN_TEMPLATES = "/plan_templates";
    private static final String DEACTIVATE = "/deactivate";
    private static final String ALL = "/all";
    private static final String STATISTICS = "/statistics";
    private static final String ORDERS = "/orders";
    private static final String VIDEO = "/video";
    private static final String ACCOUNTS = "/accounts";
    private static final String ALL_INFO = "/all_info";
    private static final String PLAN_INFO = "/plan_info";
    private static final String HISTORY = "/history";
    private static final String ACTIVE = "/active";
    private static final String CLEAR = "/clear";
    private static final String PLANS = "/plans";
    private static final String PLAN = "/plan";
    private static final String CUSTOM = "/custom";
    private static final String FOLLOWERS = "/followers";
    private static final String LIKES = "/likes";
    private static final String VIEWS = "/views";
    private static final String SUBSCRIPTIONS = "/subscriptions";
    private static final String BY_TEMPLATE = "/by_template";
    private static final String PROPORTIONS = "/proportions";
    private static final String ACTIONS = "/actions";
    private static final String CONSTRAINTS = "/constraints";
    private static final String PAYMENTS = "/payments";
    private static final String DEPOSITS = "/deposits";
    private static final String INCREASE = "/increase";
    private static final String KEY_SET = "/key_set";
    private static final String VERIFY = "/verify";
    private static final String LAST_SELECTED = "/last_selected";
    private static final String ACCOUNT_ACTIONS = "/account_actions";
    private static final String VIDEO_ACTIONS = "/video_actions";
    private static final String INFO = "/info";
    private static final String PAGES_QUANTITY = "/pages_quantity";
    private static final String CALCULATE_COST = "/calculate_cost";
    private static final String CARTS = "/carts";
    private static final String PAY = "/pay";
    private static final String PROMOCODES = "/promocodes";
    private static final String MAILING = "/mailing";

    public static final String URI_CREATE_OR_UPDATE_ACTIONS_PROPORTION = API + V1 + PROPORTIONS + ACTIONS;
    public static final String URI_CREATE_OR_UPDATE_ACCOUNTS_PROPORTION = API + V1 + PROPORTIONS + ACCOUNTS;
    public static final String URI_GET_ALL_ACTIONS_PROPORTIONS = API + V1 + PROPORTIONS + ACTIONS;
    public static final String URI_GET_ALL_ACCOUNTS_PROPORTIONS = API + V1 + PROPORTIONS + ACCOUNTS;
    public static final String URI_GET_CUSTOM_PLAN_MAX_CONSTRAINTS = API + V1 + PROPORTIONS + CONSTRAINTS;

    public static final String URI_INCREASE_DEPOSIT = API + V1 + PAYMENTS + DEPOSITS + INCREASE;
    public static final String URI_PAY_FOR_SUBSCRIPTION = API + V1 + PAYMENTS  + SUBSCRIPTIONS;
    public static final String URI_GET_DEPOSIT_BALANCE = API + V1 + PAYMENTS + DEPOSITS;

    public static final String URI_CREATE_USER_SUBSCRIPTION_BY_TEMPLATE = API + V1 + SUBSCRIPTIONS + BY_TEMPLATE;
    public static final String URI_CREATE_USER_SUBSCRIPTION_CUSTOM = API + V1 + SUBSCRIPTIONS + CUSTOM;
    public static final String URI_UPDATE_USER_SUBSCRIPTION_BY_TEMPLATE = API + V1 + SUBSCRIPTIONS + BY_TEMPLATE;
    public static final String URI_UPDATE_USER_SUBSCRIPTION_CUSTOM = API + V1 + SUBSCRIPTIONS + CUSTOM;
    public static final String URI_CALCULATE_SUBSCRIPTION_SUM = API + V1 + SUBSCRIPTIONS + CALCULATE_COST;
    public static final String URI_UNDO_SUBSCRIPTION = API + V1 + SUBSCRIPTIONS;

    public static final String URI_CREATE_OR_UPDATE_ORDER = API + V1 + ORDERS;
    public static final String URI_CREATE_LIKES_ORDER = API + V1 + ORDERS + LIKES;
    public static final String URI_CREATE_FOLLOWERS_ORDER = API + V1 + ORDERS + FOLLOWERS;
    public static final String URI_CREATE_VIEWS_ORDER = API + V1 + ORDERS + VIEWS;
    public static final String URI_VERIFY_LIKES_ORDER_DATA = API + V1 + ORDERS + LIKES + VERIFY;
    public static final String URI_VERIFY_FOLLOWERS_ORDER_DATA = API + V1 + ORDERS + FOLLOWERS + VERIFY;
    public static final String URI_VERIFY_VIEWS_ORDER_DATA = API + V1 + ORDERS + VIEWS + VERIFY;

    public static final String URI_CREATE_CUSTOM_PLAN = API + V1 + PLANS + CUSTOM;

    public static final String URI_GET_ACTIVE_USER_SUBSCRIPTION = API + V1 + USERS + STATISTICS + SUBSCRIPTIONS + ACTIVE;
    public static final String URI_CLEAR_USER_ORDER_HISTORY_BY_ID = API + V1 + USERS + STATISTICS + ORDERS + CLEAR;
    public static final String URI_CLEAR_USER_ORDERS_HISTORY = API + V1 + USERS + STATISTICS + ORDERS + CLEAR + ALL;
    public static final String URI_GET_ACCOUNT_ORDER_DETAILS_BY_ID = API + V1 + USERS + STATISTICS + ORDERS + ACCOUNT_ACTIONS + INFO;
    public static final String URI_GET_ACTIVE_USER_ORDERS_PROGRESSES = API + V1 + USERS + STATISTICS + ORDERS + ACTIVE;
    public static final String URI_GET_USER_ORDERS_HISTORY = API + V1 + USERS + STATISTICS + ORDERS + HISTORY;
    public static final String URI_GET_USER_ORDERS_HISTORY_PAGES_QUANTITY = API + V1 + USERS + STATISTICS + ORDERS + HISTORY + PAGES_QUANTITY;
    public static final String URI_GET_VIDEO_ORDER_DETAILS_BY_ID = API + V1 + USERS + STATISTICS + ORDERS + VIDEO_ACTIONS + INFO;

    public static final String URI_REGISTER = API + V1 + USERS + REGISTRATION;
    public static final String URI_RESEND_ACTIVATION_EMAIL = API + V1 + USERS + ACTIVATION + EMAIL_RESEND;
    public static final String URI_AUTH = API + V1 + USERS + AUTH;
    public static final String URI_LOGIN = API + V1 + USERS + AUTH + LOGIN;
    public static final String URI_LOGIN_WITH_FACEBOOK = API + V1 + USERS + AUTH + FACEBOOK;
    public static final String URI_LOGIN_WITH_GOOGLE = API + V1 + USERS + AUTH + GOOGLE;
    public static final String URI_ACTIVATE_USER = API + V1 + USERS + ACTIVATION;
    public static final String URI_CHANGE_PASSWORD = API + V1 + USERS + PASSWORD + CHANGE;
    public static final String URI_FORGOT_PASSWORD = API + V1 + USERS + PASSWORD + FORGOT;
    public static final String URI_FORGOT_PASSWORD_AUTHENTICATED = API + V1 + USERS + PASSWORD + FORGOT + AUTHENTICATED;
    public static final String URI_CHANGE_FORGOTTEN_PASSWORD = API + V1 + USERS + PASSWORD + FORGOT + CHANGE;
    public static final String URI_VERIFY_KEY_SET = API + V1 + USERS + PASSWORD + FORGOT + KEY_SET + VERIFY;
    public static final String URI_LOGOUT = API + V1 + USERS + AUTH + LOGOUT;

    public static final String URI_ADD_TIKTOK_ACCOUNT_TO_PROFILE = API + V1 + PROFILES + ACCOUNTS;
    public static final String URI_GET_USER_BOUNDED_ACCOUNTS = API + V1 + PROFILES + ACCOUNTS + ALL_INFO;
    public static final String URI_GET_USER_ACCOUNTS_SUBSCRIPTION = API + V1 + PROFILES + ACCOUNTS + PLAN_INFO;
    public static final String URI_GET_LAST_SELECTED_USER_BOUNDED_ACCOUNT = API + V1 + PROFILES + ACCOUNTS + LAST_SELECTED;
    public static final String URI_REMOVE_TIKTOK_ACCOUNT_FROM_PROFILE = API + V1 + PROFILES + ACCOUNTS;
    public static final String URI_FIND_PLAN_TIKTOK_ACCOUNT = API + V1 + PROFILES + ACCOUNTS + PLAN;

    public static final String URI_CREATE_OR_UPDATE_PLAN_TEMPLATE =  API + V1 + PLAN_TEMPLATES;
    public static final String URI_GET_PLAN_TEMPLATE_BY_NAME =  API + V1 + PLAN_TEMPLATES;
    public static final String URI_GET_ALL_PLAN_TEMPLATES =  API + V1 + PLAN_TEMPLATES + ALL;
    public static final String URI_CHANGE_PLAN_TEMPLATE_TO_NOT_ACTIVE =  API + V1 + PLAN_TEMPLATES + DEACTIVATE;

    public static final String URI_PAY_FOR_CART =  API + V1 + CARTS + PAY;

    public static final String URI_SEND_MAILING_PROMOCODE =  API + V1 + PROMOCODES + MAILING;

    public static final String URI_CREATE_OR_UPDATE_BOT = URI_API + "/createOrUpdateBot";
    public static final String URI_GET_BOT_BY_ID = URI_API + "/getBotById";
    public static final String URI_GET_AVAILABLE_BOTS_BY_AWEME_ID = URI_API + "/getAvailableBotsByAwemeId";
    public static final String URI_GET_AVAILABLE_BOTS_BY_SEC_USER_ID = URI_API + "/getAvailableBotsSecUserId";
    public static final String URI_GET_ACTIVE_BOTS_IDS = URI_API + "/getActiveBotsIds";

    public static final String URI_CREATE_OR_UPDATE_PHONE = URI_API + "/createOrUpdatePhone";
    public static final String URI_CREATE_OR_UPDATE_PHONE_USAGE = URI_API + "/createOrUpdatePhoneUsage";
    public static final String URI_GET_PHONE = URI_API + "/getPhone";
    public static final String URI_GET_AVAILABLE_PHONE = URI_API + "/getAvailablePhone";

    public static class EmuTest {

        public static final String URI_CREATE_TASK_4_TIK_CONNECTOR = URI_API + "/createAction4Test";
        public static final String URI_GET_TASK_STATUS_4_TIK_CONNECTOR = URI_API + "/getActionStatus4Test";
        public static final String URI_CREATE_AGENT_TASK_4_TIK_CONNECTOR = URI_API + "/repairAgent4Test";
        public static final String URI_GET_AGENT_STATUS_4_TIK_CONNECTOR = URI_API + "/getAgentStatus4Test";
    }
}
