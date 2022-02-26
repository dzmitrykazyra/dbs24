/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.dto.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RepairBotTask {
    @JsonProperty("task_id")
    private Long taskId;
    @JsonProperty("agent_id")
    private Integer agentId;
    @JsonProperty("error")
    private String error;
}
