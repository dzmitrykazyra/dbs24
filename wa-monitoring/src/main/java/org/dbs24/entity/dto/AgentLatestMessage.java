package org.dbs24.entity.dto;


import java.time.LocalDateTime;

public interface AgentLatestMessage {

    String getAgentPhone();
    LocalDateTime getCreateDate();
    LocalDateTime getAgentCreateDate();

}
