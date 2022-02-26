/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.consts;

import org.dbs24.rest.api.ShutdownRequest;
import org.dbs24.rest.api.StandardResponse;

public final class RestHttpConsts {

    public static final String APP_JSON = "application/json;";
    public static final String APP_XML = "application/xml";
    public static final String CONTENT_TYPE = "application/json; charset=utf-8;";
    public static final String SRV_READ_TIMEOUT = "30000";
    public static final String SRV_CONNECT_TIMEOUT = "10000";
    public static final String GZIP_ACCEPT_ENCODING = "gzip";
    public static final String IDENTITY_CONTENT_ENCODING = "identity";
    public static final Integer HTTP_200_OK = 200;
    public static final String HTTP_200_STRING = "200";
    public static final Integer HTTP_204_NO_CONTENT = 204;
    public static final Integer HTTP_500_INTERNAL_ERROR = 500;
    public static final Boolean USE_GZIP_ENCODING = Boolean.TRUE;
    public static final Boolean NO_GZIP_ENCODING = Boolean.FALSE;
    public static final String HTTP_200_OK_STRING = "OK";

    public static final String URI_API = "/api";
    public static final String URI_STARTED = URI_API.concat("/started");
    public static final String URI_LIVENESS = URI_API.concat("/liveness");
    public static final String URI_READINESS = URI_API.concat("/readiness");
    public static final String URI_HEALH_Z = URI_API.concat("/healhZ");
    public static final String URI_SHUTDOWN = URI_API.concat("/shutdown");
    public static final String URI_CAN_SHUTDOWN = URI_API.concat("/canShutdown");
    public static final String URI_KILL_9 = URI_API.concat("/kill9");
    public static final String URI_LOGIN = URI_API.concat("/login");
    public static final String URI_REGISTER_TOKEN = URI_API.concat("/token/create");
    public static final String URI_CREATE_USER = URI_API.concat("/auth/create/user");
    public static final String URI_CREATE_ROLE = URI_API.concat("/auth/create/role");
    public static final String URI_SWAGGER_MAIN = "/swagger-ui.html";
    public static final String URI_SWAGGER_LINKS = "/webjars/swagger-ui/**";
    public static final String URI_SWAGGER_WEBJARS_LINKS = "/swagger-ui/**";
    public static final String URI_SWAGGER_API_DOCS = "/v3/api-docs/**";
    public static final String URI_SPRING_BOOT_ACTUATOR = "/actuator/**";

    public static final Class<ShutdownRequest> SHUTDOWN_REQUEST_CLASS = ShutdownRequest.class;
    public static final Class<StandardResponse> STANDARD_RESPONSE_CLASS = StandardResponse.class;
}
