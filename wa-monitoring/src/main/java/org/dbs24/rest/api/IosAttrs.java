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
public class IosAttrs {

    private String systemVersion;
    private String model;
    @EqualsAndHashCode.Include
    private String appleId;
    @EqualsAndHashCode.Include
    private String identifierForVendor;
    private String ustnameRelease;
    private String ustnameVersion;
    private String ustnameMachine;
    private String gcmToken;
}
