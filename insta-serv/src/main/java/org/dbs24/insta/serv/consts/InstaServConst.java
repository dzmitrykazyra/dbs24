/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.serv.consts;

import static org.dbs24.consts.RestHttpConsts.URI_API;

public final class InstaServConst {

    //==========================================================================
    // Account processing
    public static class Uri {

        // user
        public static final String URI_CREATE_SERVICE_TASK = URI_API.concat("/createServiceTask");

    }

    public static class RestQueryParams {

        public static final String QP_ACTUAL_DATE = "actualDate";
    }

}
