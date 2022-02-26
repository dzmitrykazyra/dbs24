/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.rest.dto.endpoint;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

@Data
public class EndpointActionInfo implements EntityInfo {

    private Long endpointActionId;
    private Integer endpointId;
    private Long executionDate;
    private Long contractId;
    private Integer endpointResultId;
    private Long tikAccountId;
    private Long deviceId;
    private Integer endpointResponse;
    private String body;
    private Integer usedBytes;
    private String headers;
    private String queryParams;
    private String ipAddress;
    private String errLog;

}
