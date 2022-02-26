/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.entity.UserDevice;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserDeviceShortInfo {

    @EqualsAndHashCode.Include
    private Integer deviceId;
    private Long actualDate;
    private Integer userId;
    private String gcmToken;
    private String appName;
    private String appVersion;

    public void assign(UserDevice userDevice) {
        this.deviceId = userDevice.getDeviceId();
        this.actualDate = NLS.localDateTime2long(userDevice.getActualDate());
        this.userId = userDevice.getUser().getUserId();
        this.appName = userDevice.getAppName();
        this.appVersion = userDevice.getAppVersion();
    }
}
