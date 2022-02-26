/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.ad.server.wa;

import static org.dbs24.consts.RestHttpConsts.URI_API;

public final class WaAdConsts {

    public static final String APP_TAG_NAME = "WhatsApp ads actions ";
    public static final String URI_WA_CONST = "wa";
    public static final String URI_WA_CREATE_OR_UPDATE_ADS_SETTINGS = URI_API + "/" + URI_WA_CONST + ("/createOrUpdateAdsSettings");
    public static final String URI_WA_GET_ADS_SETTINGS = URI_API + "/" + URI_WA_CONST + ("/getAdsSettings");

}
