/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dbs24.application.core.service.funcs.ServiceFuncs;

import java.util.Collection;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class UserDevicesInfoCollection {

    private Collection<UserDeviceShortInfo> userDevices = ServiceFuncs.<UserDeviceShortInfo>createCollection();
}
