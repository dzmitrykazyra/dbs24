package org.dbs24.proxy.core.entity.dto.request;

import lombok.Data;
import org.dbs24.spring.core.api.RequestBody;

@Data
public class GetApplicationByNetworkRequest implements RequestBody {

    private String applicationNetworkName;
}