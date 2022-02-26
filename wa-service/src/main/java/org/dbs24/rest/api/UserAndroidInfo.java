/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;

@Data
@Deprecated
public class UserAndroidInfo {
//    private Integer deviceId;
    private String loginToken;
    private String gcmToken;
    private String appName;
    private String appVersion;
    private String androidSecureID;
    private String deviceFingerprint;
    private String gsfId;
    private String countryCode;
    private String ipAddress;    
    private String macAddress;    
}
