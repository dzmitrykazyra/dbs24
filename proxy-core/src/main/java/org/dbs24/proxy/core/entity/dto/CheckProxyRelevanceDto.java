package org.dbs24.proxy.core.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.dbs24.proxy.core.entity.domain.Country;
import org.dbs24.proxy.core.entity.domain.Proxy;
import org.dbs24.spring.core.api.EntityInfo;
import org.dbs24.stmt.StmtProcessor;


@Data
public class CheckProxyRelevanceDto implements EntityInfo {

    private Integer proxyId;
    private String url;
    private Integer port;
    private Integer socksPort;
    private String login;
    private String password;
    private String urlIpChange;
    private Country country;
    private boolean isActual;

    public static CheckProxyRelevanceDto of(Proxy proxy, Boolean isActual) {

        return StmtProcessor.create(CheckProxyRelevanceDto.class, checkProxyRelevanceDto -> {
            checkProxyRelevanceDto.setProxyId(proxy.getProxyId());
            checkProxyRelevanceDto.setCountry(proxy.getCountry());
            checkProxyRelevanceDto.setLogin(proxy.getLogin());
            checkProxyRelevanceDto.setPassword(proxy.getPass());
            checkProxyRelevanceDto.setUrl(proxy.getUrl());
            checkProxyRelevanceDto.setUrlIpChange(proxy.getUrlIpChange());
            checkProxyRelevanceDto.setPort(proxy.getPort());
            checkProxyRelevanceDto.setSocksPort(proxy.getSocksPort());
            checkProxyRelevanceDto.setActual(isActual);
        });
    }

}
