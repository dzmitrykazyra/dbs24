package org.dbs24.proxy.core.entity.dto.response.body;

import lombok.Data;
import org.dbs24.proxy.core.entity.domain.Proxy;
import org.dbs24.spring.core.api.EntityInfo;

import java.util.List;

@Data
public class BookedProxyList implements EntityInfo {

    private Integer requestId;
    private List<Proxy> bookedProxyList;
}
