/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.auth.server.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class WaUserSessionInfo {

    private String loginToken;
    private Integer deviceId;
    private String fcmToken;
    private String gsfId;
    private String appleId;
    private String identifierForVendor;
    private String packageName;
    private String appName;
    private String appVersion;
}
