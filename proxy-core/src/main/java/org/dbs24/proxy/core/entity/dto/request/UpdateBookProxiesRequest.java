package org.dbs24.proxy.core.entity.dto.request;

import lombok.Data;
import org.dbs24.proxy.core.entity.dto.UpdateBookedProxiesDto;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.spring.core.api.PostRequestBody;

@Data
public class UpdateBookProxiesRequest implements PostRequestBody {

    private SimpleActionInfo entityAction;
    private UpdateBookedProxiesDto entityInfo;
}