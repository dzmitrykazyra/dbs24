/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AgentInfo {

    private Integer agent_id;
    private String phone_num;
    private String payload;
    private Long actual_date;
    private Byte agent_status_id;
}
