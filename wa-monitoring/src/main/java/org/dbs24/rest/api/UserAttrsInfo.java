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
public class UserAttrsInfo {

    @EqualsAndHashCode.Include
    private String loginToken;
    @EqualsAndHashCode.Include
    private Long actualDate;
    @EqualsAndHashCode.Include
    private String appName;
    private String appVersion;
    private String makAddr;
    @EqualsAndHashCode.Include
    private String devicePlatform;
    // remove in future
    private AndroidAttrs androidAttrs;
    private IosAttrs iosAttrs;

}
