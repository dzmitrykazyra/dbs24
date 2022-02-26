package org.dbs24.proxy.core.entity.dto.response.body;

import lombok.Data;
import org.dbs24.proxy.core.entity.domain.ProxyUsageError;
import org.dbs24.spring.core.api.EntityInfo;

@Data
public class CreatedProxyUsageError implements EntityInfo {

    private Integer errorId;
    private String errorTypeName;
    private String applicationName;

    /**
     * Method allows filling DTO fields by not null
     * {@link org.dbs24.proxy.core.entity.domain.ProxyUsageError} fields values
     */
    public void assign(ProxyUsageError proxyUsageError) {

        this.errorId = proxyUsageError.getErrorId();
        this.errorTypeName = proxyUsageError.getErrorType().getErrorTypeName();
        this.applicationName = proxyUsageError.getApplication().getName();
    }
}
