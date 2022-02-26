package org.dbs24.google.entity.dto.proxy;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

import java.util.List;

@Data
public class BookedProxyList implements EntityInfo {
    private Integer requestId;
    private List<Proxy> bookedProxyList;
}
