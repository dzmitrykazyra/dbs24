package org.dbs24.proxy.core.entity.dto.response.body;

import lombok.Data;
import org.dbs24.proxy.core.entity.domain.Proxy;
import org.dbs24.spring.core.api.EntityInfo;

@Data
public class BookedProxy implements EntityInfo {

    private Integer requestId;
    private Proxy bookedProxy;
}
