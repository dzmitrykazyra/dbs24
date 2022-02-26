package org.dbs24.google.api.dto;

import lombok.Data;

@Data
public class ProxyInfo {
    private String url;
    private Integer port;
    private String login;
    private String pass;
}
