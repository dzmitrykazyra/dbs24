package org.dbs24.google.entity.dto.proxy;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

@Data
public class BookProxiesDto implements EntityInfo {
    private Integer amount;
    private String proxyTypeName;
    private String providerName;
    private String countryName;
    private String applicationName;
    private String algorithmName;
    private Integer bookingTimeMillis;
}
