/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AndroidAttrs {
    @EqualsAndHashCode.Include
    private String gsfId;
    @EqualsAndHashCode.Include
    private String secureId;
    private Integer versionSdkInt;
    private String versionRelease;
    private String board;
    private String fingerprint;
    private String manufacturer;
    private String model;
    private String product;
    private String supportedAbis;
    @EqualsAndHashCode.Include
    private String androidId;
    private String gcmToken;
}
