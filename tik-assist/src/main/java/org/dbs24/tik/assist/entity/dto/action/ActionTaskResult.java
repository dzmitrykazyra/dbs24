/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.dto.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ActionTaskResult {

    @JsonProperty("task_id")   
    private Long actionId;
    
    @JsonProperty("task_status")   
    private Integer actionResultId;
    
    @JsonProperty("agent_id")   
    private Integer agentId;
    
    @JsonProperty("fulfil_time")
    private Long finishDate;
    
    @JsonProperty("error")
    private String errMsg;
}
