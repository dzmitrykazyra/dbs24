package org.dbs24.entity.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AgentLatestMessageDto {

    @EqualsAndHashCode.Include
    private String agentPhone;
    private Long agentCreateDate;
    private Long createDate;

}
