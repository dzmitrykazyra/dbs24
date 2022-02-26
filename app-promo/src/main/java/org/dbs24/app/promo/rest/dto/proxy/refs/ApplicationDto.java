package org.dbs24.app.promo.rest.dto.proxy.refs;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

@Data
public class ApplicationDto implements EntityInfo {
    private Integer applicationId;
    private String name;
    private String description;
    private String applicationNetworkName;
}
