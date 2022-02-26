package org.dbs24.app.promo.rest.dto.proxy.response;

import lombok.Data;
import org.dbs24.app.promo.rest.dto.proxy.refs.BookedProxyList;
import org.dbs24.rest.api.ResponseBody;

@Data
public class ProxyListResponse extends ResponseBody<BookedProxyList> {
}
