package org.dbs24.proxy.core.entity.dto.response.body;

import lombok.Data;
import org.dbs24.proxy.core.entity.dto.CheckProxyRelevanceDto;
import org.dbs24.spring.core.api.EntityInfo;

import java.util.List;

@Data
public class CheckProxyRelevanceDtoList implements EntityInfo {

    private Integer actualProxyAmount;
    private Integer totalProxyAmount;
    private List<CheckProxyRelevanceDto> proxyInfoList;
}
