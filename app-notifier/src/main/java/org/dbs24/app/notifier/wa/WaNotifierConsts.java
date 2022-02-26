/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.notifier.wa;

import static org.dbs24.consts.RestHttpConsts.URI_API;

public final class WaNotifierConsts {

    public static final String URI_WA_CONST = "wa";
    public static final String URI_WA_CREATE_OR_UPDATE_MESSAGE = URI_API + "/" + URI_WA_CONST + ("/createOrUpdateMessage");
    public static final String URI_WA_GET_MESSAGES = URI_API + "/" + URI_WA_CONST + ("/getMessages");

}
