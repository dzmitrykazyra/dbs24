/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.rest.dto.device;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

@Data
public class DeviceInfo implements EntityInfo {

    private Long deviceId;
    private Long actualDate;
    private Integer deviceStatusId;
    private String deviceIdStr;
    private String installId;
    private byte[] apkAttrs;
    private String apkHashId;

}
