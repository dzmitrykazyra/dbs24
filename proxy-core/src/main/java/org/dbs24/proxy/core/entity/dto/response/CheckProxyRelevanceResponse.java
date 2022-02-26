package org.dbs24.proxy.core.entity.dto.response;

import lombok.Data;
import org.dbs24.proxy.core.entity.dto.response.body.CheckProxyRelevanceDtoList;
import org.dbs24.rest.api.ResponseBody;

@Data
public class CheckProxyRelevanceResponse extends ResponseBody<CheckProxyRelevanceDtoList> {
}
