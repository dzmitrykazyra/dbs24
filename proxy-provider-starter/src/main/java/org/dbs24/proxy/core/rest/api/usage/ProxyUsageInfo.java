/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.rest.api.usage;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

@Data
public class ProxyUsageInfo implements EntityInfo {

    private Integer usageId;
    private Integer proxyRequestId;
    private Integer proxyId;
    private Long sessionStart;
    private Long sessionFinish;
    private Boolean isSuccess;
    private String error;
    private String usedIp;

}
