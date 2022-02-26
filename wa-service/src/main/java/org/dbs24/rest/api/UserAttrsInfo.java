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
public class UserAttrsInfo {

    private String loginToken;
    private Long actualDate;    
    private String appName;
    private String appVersion;
    private String makAddr;
    private String devicePlatform;
    // remove in future
    private AndroidAttrs androidAttrs;
    private IosAttrs iosAttrs;

}
