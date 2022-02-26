package org.dbs24.proxy.core.entity.dto.request;

import lombok.Data;
import org.dbs24.proxy.core.entity.dto.ApplicationDto;
import org.dbs24.proxy.core.rest.api.proxy.ProxyInfo;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.spring.core.api.PostRequestBody;

@Data
public class CreateApplicationRequest implements PostRequestBody {

    private SimpleActionInfo entityAction;
    private ApplicationDto entityInfo;
}