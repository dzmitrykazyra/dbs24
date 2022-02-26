package org.dbs24.app.promo.rest.dto.proxy.refs;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

@Data
public class BookedProxy implements EntityInfo {
    private Integer requestId;
    private Proxy bookedProxy;
}
