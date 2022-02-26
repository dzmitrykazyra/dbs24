package org.dbs24.proxy.core.entity.dto.request;

import lombok.Data;
import org.dbs24.proxy.core.entity.dto.ProxyUsageErrorDto;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.spring.core.api.PostRequestBody;

@Data
public class CreateProxyUsageErrorRequest implements PostRequestBody {

    private SimpleActionInfo entityAction;
    private ProxyUsageErrorDto entityInfo;
}

