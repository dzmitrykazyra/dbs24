/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.rest.api.dto.OperationResult;

import java.util.Collection;

@Data
@EqualsAndHashCode
public class RemoveDeviceResult extends OperationResult {

    private Collection<String> deletedDevices = ServiceFuncs.createCollection();
}
