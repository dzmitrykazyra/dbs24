package org.dbs24.email.spammer.constant;

public class ApiPath {

    private static final String API = "/api";
    private static final String V1 = "/v1";
    private static final String SPAMMERS = "/spammers";
    private static final String SUBSCRIBERS = "/subscribers";
    private static final String APPLICATIONS = "/applications";

    public static final String GET_SPAMMER_BY_EMAIL = API + V1 + SPAMMERS;
    public static final String CREATE_SPAMMER = API + V1 + SPAMMERS;

    public static final String REGISTER_SUBSCRIBER = API + V1 + SUBSCRIBERS;

    public static final String GET_ALL_APPLICATIONS = API + V1 + APPLICATIONS;
}
