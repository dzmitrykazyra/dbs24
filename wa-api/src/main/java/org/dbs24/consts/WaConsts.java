/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.consts;


import static org.dbs24.consts.RestHttpConsts.URI_API;

public final class WaConsts {

    //==========================================================================
    // Account processing
    public static class Uri {

        // agent

        public static final String URI_GET_LICENSE_AGREEMENT = URI_API + ("/getLicenseAgreement");
        public static final String URI_CREATE_OR_UPDATE_APP_SETTING = URI_API + ("/createOrUpdateAppSetting");
    }

}
