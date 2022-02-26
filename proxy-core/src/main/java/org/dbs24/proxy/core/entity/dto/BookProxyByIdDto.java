package org.dbs24.proxy.core.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.spring.core.api.EntityInfo;
import org.dbs24.spring.core.api.RequestBody;

@Data
public class BookProxyByIdDto implements EntityInfo {

    private Integer proxyId;
    private Integer bookingTimeMillis;
    private String applicationName;
}