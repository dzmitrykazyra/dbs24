/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.consts;

public final class ServicePeriodsConsts {

    public static class Routes {

        public static final String ROUTE_OUT_OF_SERVICE = "/api/out-of-service";
        public static final String ROUTE_OUT_OF_SERVICE_SET = ROUTE_OUT_OF_SERVICE + "/set";
        public static final String ROUTE_OUT_OF_SERVICE_GET = ROUTE_OUT_OF_SERVICE + "/get";

    }

    public static class Caches {

        public static final String CACHE_OUT_OF_SERVICE = "outOfService";

    }
}
