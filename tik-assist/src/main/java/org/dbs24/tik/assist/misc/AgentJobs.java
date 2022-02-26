/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.misc;

import lombok.Data;
import org.dbs24.tik.assist.entity.domain.ActionType;
import org.dbs24.tik.assist.entity.domain.Bot;

@Data
public class AgentJobs {
    private Bot bot;
    private ActionType actionType;
    private Integer count;
}
