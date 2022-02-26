package org.dbs24.app.promo.rest.dto.proxy.request;

import lombok.Data;
import org.dbs24.app.promo.rest.dto.proxy.refs.BookProxiesDto;
import org.dbs24.rest.api.action.SimpleActionInfo;

@Data
public class BookProxiesRequest {
    private SimpleActionInfo entityAction;
    private BookProxiesDto entityInfo;
}
