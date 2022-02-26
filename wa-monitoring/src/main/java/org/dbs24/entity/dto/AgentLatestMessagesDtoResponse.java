package org.dbs24.entity.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.rest.api.dto.OperationResult;

import java.util.Collection;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AgentLatestMessagesDtoResponse extends OperationResult {

    @EqualsAndHashCode.Include
    private Collection<AgentLatestMessageDto> agentLatestMessageDtos;

}
