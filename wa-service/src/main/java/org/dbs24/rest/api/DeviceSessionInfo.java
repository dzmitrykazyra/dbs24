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
public class DeviceSessionInfo {

    private Long sessionId;
    private Integer deviceId;
    private Long actualDate;
    private String ipAddress;
    private String note;
}
