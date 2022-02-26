/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.rest.api.request;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

@Data
public class ProxyRequestInfo implements EntityInfo {
    
    private Integer requestId;
    private Long requestDate;
    private Integer proxyId;
    private Integer proxyProviderId;
    private String application;
    private String answer;
    private Integer countryId;
    private Integer algId;    
}
