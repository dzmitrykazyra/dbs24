package org.dbs24.proxy.core.entity.dto;

import lombok.Data;
import org.dbs24.proxy.core.entity.domain.ProxyUsageBan;
import org.dbs24.proxy.core.entity.domain.ProxyUsageError;
import org.dbs24.spring.core.api.EntityInfo;
import org.dbs24.stmt.StmtProcessor;

import java.time.LocalDateTime;

@Data
public class ProxyUsageBanDto implements EntityInfo {

    private Integer proxyUsageId;
    private String log;
    private String applicationNetworkName;
    private Integer banTimeMillis;

    public ProxyUsageBan toProxyUsageBan(ProxyUsageError proxyUsageError) {

        return StmtProcessor.create(
                ProxyUsageBan.class,
                proxyUsageBan -> {
                    proxyUsageBan.setProxyUsageErrorId(proxyUsageError.getErrorId());
                    proxyUsageBan.setProxy(proxyUsageError.getProxyUsage().getProxy());
                    proxyUsageBan.setBannedUntilDate(LocalDateTime.now().plusSeconds(banTimeMillis / 1000));
                    proxyUsageBan.setApplicationNetwork(proxyUsageError.getApplication().getApplicationNetwork());
                }
        );
    }
}
