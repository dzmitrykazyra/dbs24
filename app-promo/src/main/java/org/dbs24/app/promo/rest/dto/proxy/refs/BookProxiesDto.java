package org.dbs24.app.promo.rest.dto.proxy.refs;

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
    private Integer proxyId;
}
