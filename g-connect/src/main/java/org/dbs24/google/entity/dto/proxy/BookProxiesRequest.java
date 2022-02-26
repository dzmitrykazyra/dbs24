package org.dbs24.google.entity.dto.proxy;

import lombok.Data;
import org.dbs24.rest.api.action.SimpleActionInfo;

@Data
public class BookProxiesRequest {
    private SimpleActionInfo entityAction;
    private BookProxiesDto entityInfo;
}
