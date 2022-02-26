package org.dbs24.tik.mobile.constant;

public class ApiPath {

    private static final String API = "/api";
    private static final String V1 = "/v1";
    private static final String USERS = "/users";
    private static final String REGISTRATION = "/registration";
    private static final String LOGIN = "/login";
    private static final String LOGOUT = "/logout";
    private static final String PASSWORDS = "/passwords";
    private static final String FORGOT = "/forgot";
    private static final String AUTH = "/auth";
    private static final String DEPOSITS = "/deposits";
    private static final String ORDERS = "/orders";
    private static final String EXECUTION = "/execution";
    private static final String PROPORTIONS = "/proportions";
    private static final String HURTS_COSTS = "/hurts_costs";
    private static final String ORDERS_COSTS = "/orders_costs";
    private static final String STATISTICS = "/statistics";
    private static final String DONE = "/done";
    private static final String PAGES = "/pages";
    private static final String SINGLE = "/single";
    private static final String TIKTOK_ACCOUNT = "/tiktok_account";
    private static final String VIDEOS = "/videos";
    private static final String COMPLETE = "/complete";
    private static final String BOUND = "/bound";
    private static final String REQUEST = "/request";
    private static final String EMAIL = "/email";
    private static final String CONFIRM = "/confirm";
    private static final String BY_HEARTS_QUANTITY = "/by_hearts_quantity";
    private static final String BY_LINK = "/by_link";
    private static final String BY_USER = "/by_user";
    private static final String SKIP = "/skip";
    private static final String TYPES = "/types";
    private static final String ALL = "/all";
    private static final String DOWNLOADS = "/downloads";
    private static final String SETTINGS = "/settings";
    private static final String FIREBASE = "/firebase";
    private static final String DEVICE = "/device";
    private static final String ATTRIBUTE = "/attribute";

    public static final String USER_REGISTER =                  API + V1 + USERS + REGISTRATION;
    public static final String USER_LOGIN =                     API + V1 + USERS + LOGIN;
    public static final String USER_LOGOUT =                    API + V1 + USERS + LOGOUT;
    public static final String USER_IS_EMAIL_BOUNDED =          API + V1 + USERS + EMAIL;
    public static final String USER_BOUND_EMAIL =               API + V1 + USERS + EMAIL + BOUND + REQUEST;
    public static final String USER_CONFIRM_EMAIL_BY_KEY =      API + V1 + USERS + EMAIL + BOUND + CONFIRM;
    public static final String USER_FORGOT_PASSWORD =           API + V1 + USERS + PASSWORDS + FORGOT + REQUEST;
    public static final String USER_CHANGE_FORGOTTEN_PASSWORD = API + V1 + USERS + PASSWORDS + FORGOT + CONFIRM;
    public static final String USER_AUTH =                      API + V1 + USERS + AUTH;

    public static final String DEPOSITS_INCREASE =              API + V1 + DEPOSITS;
    public static final String DEPOSITS_GET_CURRENT_BALANCE =   API + V1 + DEPOSITS;

    public static final String ORDERS_CREATE =                  API + V1 + ORDERS + EXECUTION;
    public static final String ORDERS_GET_TO_EXECUTE =          API + V1 + ORDERS + EXECUTION;
    public static final String ORDERS_EXECUTION_COMPLETE =      API + V1 + ORDERS + EXECUTION + COMPLETE;
    public static final String ORDERS_SKIP =                    API + V1 + ORDERS + SKIP;
    public static final String ORDERS_GET_ALL_ACTION_TYPES =    API + V1 + ORDERS + TYPES + ALL;

    public static final String HURTS_TO_CURRENCY_PROPORTIONS_CREATE =                      API + V1 + PROPORTIONS + HURTS_COSTS;
    public static final String HURTS_TO_CURRENCY_PROPORTIONS_GET =                         API + V1 + PROPORTIONS + HURTS_COSTS;
    public static final String HURTS_COST_BY_HEARTS_QUANTITY =                             API + V1 + PROPORTIONS + HURTS_COSTS + BY_HEARTS_QUANTITY;

    public static final String HURTS_TO_ACTION_TYPE_PROPORTIONS_CREATE =                   API + V1 + PROPORTIONS + ORDERS_COSTS;
    public static final String HURTS_TO_ACTION_TYPE_PROPORTIONS_GET =                      API + V1 + PROPORTIONS + ORDERS_COSTS;
    public static final String HURTS_TO_ACTION_TYPE_PROPORTIONS_GET_BY_TYPE_AND_QUANTITY = API + V1 + PROPORTIONS + ORDERS_COSTS + SINGLE;

    public static final String STATISTICS_GET_ACTIVE_ORDERS_PROGRESSES =                   API + V1 + STATISTICS + ORDERS;
    public static final String STATISTICS_INVALIDATE_ACTIVE_ORDER =                        API + V1 + STATISTICS + ORDERS;
    public static final String STATISTICS_GET_ALL_DONE_ORDER_ACTIONS_PAGES_QUANTITY =      API + V1 + STATISTICS + ORDERS + DONE + PAGES;
    public static final String STATISTICS_GET_ALL_DONE_ORDER_ACTIONS  =                    API + V1 + STATISTICS + DONE;
    public static final String STATISTICS_GET_SINGLE_ORDER_DETAILS =                       API + V1 + STATISTICS + ORDERS + DONE + SINGLE;
    public static final String STATISTICS_CLEAR_SINGLE_ORDER_HISTORY =                     API + V1 + STATISTICS + ORDERS + DONE + SINGLE;

    public static final String TIKTOK_ACCOUNT_GET_LAST_VIDEOS =     API + V1 + TIKTOK_ACCOUNT + VIDEOS;
    public static final String TIKTOK_ACCOUNT_GET_DETAILS =         API + V1 + TIKTOK_ACCOUNT;

    public static final String DOWNLOADS_GET_ALL_VIDEO_HISTORY =    API + V1 + DOWNLOADS + VIDEOS;
    public static final String DOWNLOADS_FIND_VIDEO_BY_LINK =       API + V1 + DOWNLOADS + VIDEOS + BY_LINK;
    public static final String DOWNLOADS_VIDEO_DOWNLOAD =           API + V1 + DOWNLOADS + VIDEOS;

    public static final String SETTINGS_GET_BY_PACKAGE_NAME =                 API + V1 + SETTINGS;
    public static final String SETTINGS_CREATE_OR_UPDATE_APP_SETTINGS =       API + V1 + SETTINGS;

    public static final String FIREBASE_GET_ALL_FIREBASE_APPS =               API + V1 + FIREBASE;
    public static final String FIREBASE_CREATE_OR_UPDATE_APP_FIREBASE =       API + V1 + FIREBASE;

    public static final String DEVICE_GET_ALL_BY_USER =                       API + V1 + DEVICE;
    public static final String DEVICE_REMOVE_BY_ID =                          API + V1 + DEVICE;
    public static final String DEVICE_ATTRIBUTES_CREATE_OR_UPDATE =           API + V1 + DEVICE + ATTRIBUTE;

}