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
public class AndroidAttrs {
    private String gsfId;
    private String secureId;
    private Integer versionSdkInt;
    private String versionRelease;
    private String board;
    private String fingerprint;
    private String manufacturer;
    private String model;
    private String product;
    private String supportedAbis;
    private String androidId;
    private String gcmToken;
}
