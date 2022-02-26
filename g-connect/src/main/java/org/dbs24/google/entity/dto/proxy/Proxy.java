package org.dbs24.google.entity.dto.proxy;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Proxy {
    private Integer proxyId;
    private LocalDateTime actualDate;
    private ProxyProvider proxyProvider;
    private String url;
    private Integer socksPort;
    private Integer port;
    private String login;
    private String pass;
    private String urlIpChange;
    private Country country;
    private String externalIpAddress;
    private ProxyStatus proxyStatus;
    private ProxyType proxyType;
    private LocalDateTime dateBegin;
    private LocalDateTime dateEnd;
    private SocksAuthMethod socksAuthMethod;
    private byte[] socksClientData;
    private Integer traffic;
}
