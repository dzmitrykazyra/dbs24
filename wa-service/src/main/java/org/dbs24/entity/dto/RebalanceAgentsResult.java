/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.rest.api.dto.OperationResult;

import static org.dbs24.consts.SysConst.INTEGER_ZERO;

@Data
@EqualsAndHashCode
public class RebalanceAgentsResult extends OperationResult {

    private Integer agentAmount = INTEGER_ZERO;
    private Integer subscriptionAmount = INTEGER_ZERO;
    private Integer workLoad = INTEGER_ZERO;
}
