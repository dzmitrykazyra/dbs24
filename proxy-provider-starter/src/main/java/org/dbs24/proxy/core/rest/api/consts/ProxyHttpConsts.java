/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.rest.api.consts;

import static org.dbs24.consts.RestHttpConsts.URI_API;

public final class ProxyHttpConsts {

    public static class UriConsts {

        //todo remove URIs below
        // Proxy
        public static final String URI_CREATE_OR_UPDATE_PROXY = URI_API.concat("/createOrUpdateProxy");
        public static final String URI_GET_PROXY = URI_API.concat("/getProxy");
        // Request
        public static final String URI_CREATE_OR_UPDATE_PROXY_REQUEST = URI_API.concat("/createOrUpdateProxyRequest");
        public static final String URI_CREATE_OR_UPDATE_PROXY_USAGE = URI_API.concat("/createOrUpdateProxyUsage");
    }

    public static class RestQueryParams {

        public static final String QP_REQUEST_ID = "requestId";
        public static final String QP_APPLICATION_ID = "applicationId";
        public static final String QP_APPLICATION_NETWORK_NAME = "applicationNetworkName";
        public static final String QP_PROXY_ID = "proxyId";
        public static final String QP_TOKEN = "token";
        public static final String QP_BOOKING_TIME_MILLIS = "bookingTime";
        public static final String QP_APPLICATION_NAME = "applicationName";
    }
}
