package org.dbs24.proxy.core.entity.dto.response;

import lombok.Data;
import org.dbs24.proxy.core.entity.dto.response.body.BookedProxyList;
import org.dbs24.proxy.core.rest.api.proxy.ProxyInfo;
import org.dbs24.rest.api.ResponseBody;

@Data
public class ProxyListResponse extends ResponseBody<BookedProxyList> {

}
