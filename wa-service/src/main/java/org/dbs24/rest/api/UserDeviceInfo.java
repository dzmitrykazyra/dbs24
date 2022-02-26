/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class UserDeviceInfo {

    private Integer deviceId;
    private Long actualDate;
    private Integer userId;
    private Integer deviceTypeId;
    private String cpuId;
    private String gcmToken;
    private String deviceFingerprint;
    private String appName;
    private String appVersion;
    private String secureId;
    private String gsfId;
    private String iosKey;
    private String ipAddress;
    
    }
    
