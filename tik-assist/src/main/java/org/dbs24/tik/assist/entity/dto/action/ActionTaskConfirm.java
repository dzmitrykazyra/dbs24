/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.dto.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ActionTaskConfirm {

    @JsonProperty("status")   
    private Boolean isConfirmed;
    
    @JsonProperty("error")   
    private String error;
    
    @JsonProperty("task_id")   
    private Integer taskId;
}
