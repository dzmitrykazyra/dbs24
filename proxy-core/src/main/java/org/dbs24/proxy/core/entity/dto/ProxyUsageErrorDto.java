package org.dbs24.proxy.core.entity.dto;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;
import org.dbs24.stmt.StmtProcessor;

@Data
public class ProxyUsageErrorDto implements EntityInfo {

    private Integer proxyUsageId;
    private String errorTypeName;
    private String log;
}
