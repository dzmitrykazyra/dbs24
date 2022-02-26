/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.auth.server.wa;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.auth.server.api.WaUserSessionInfo;

@Data
@EqualsAndHashCode(callSuper = true)
public class SessionInfo extends WaUserSessionInfo {

    private Boolean added;

    public void assign(WaUserSessionInfo waUserSessionInfo, Boolean added) {
        this.setLoginToken(waUserSessionInfo.getLoginToken());
        this.setGsfId(waUserSessionInfo.getGsfId());
        this.setIdentifierForVendor(waUserSessionInfo.getIdentifierForVendor());
        this.setAppleId(waUserSessionInfo.getAppleId());
        this.setDeviceId(waUserSessionInfo.getDeviceId());
        this.setAppVersion(waUserSessionInfo.getAppVersion());
        this.setAppName(waUserSessionInfo.getAppName());
        this.setFcmToken(waUserSessionInfo.getFcmToken());
        this.setAdded(added);
    }
}
