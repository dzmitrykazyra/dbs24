/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.rest.dto.device;

import lombok.Data;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.spring.core.api.PostRequestBody;

@Data
public class CreateDeviceRequest implements PostRequestBody {

    private SimpleActionInfo entityAction;
    private DeviceInfo entityInfo;

}
