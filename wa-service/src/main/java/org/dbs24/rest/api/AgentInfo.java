/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.entity.Agent;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class AgentInfo {

    private Integer agent_id;
    private String phone_num;
    private String payload;
    private Long actual_date;
    private String str_actual_date;
    private Byte agent_status_id;

    public void assign(Agent agent) {
        this.agent_id = agent.getAgentId();
        this.phone_num = agent.getPhoneNum();
        this.payload = agent.getPayload();
        this.actual_date = NLS.localDateTime2long(agent.getActualDate());
        this.str_actual_date = actual_date.toString();
        this.agent_status_id = agent.getAgentStatus().getAgentStatusId();

    }
}
