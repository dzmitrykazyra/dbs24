/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.rest.api.proxy;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

@Data
public class ProxyInfo implements EntityInfo {

    private Integer proxyId;
    private Long actualDate;
    private Integer proxyProviderId;
    private Long dateBegin;
    private Long dateEnd;
    private Integer countryId;
    private Integer proxyStatusId;
    private Integer proxyTypeId;
    private Integer socksAuthMethodId;
    private byte[] socksAuthData;
    private byte[] socksClientData;
    private String url;
    private String urlIpChange;
    private String login;
    private String pass;
    private Integer port;
    private Integer traffic;

}
