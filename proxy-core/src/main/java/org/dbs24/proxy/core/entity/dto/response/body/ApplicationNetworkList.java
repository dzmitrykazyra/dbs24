package org.dbs24.proxy.core.entity.dto.response.body;

import lombok.Data;
import org.dbs24.proxy.core.entity.domain.ApplicationNetwork;
import org.dbs24.spring.core.api.EntityInfo;

import java.util.List;

@Data
public class ApplicationNetworkList implements EntityInfo {

    private List<ApplicationNetwork> applicationNetworkList;
}
