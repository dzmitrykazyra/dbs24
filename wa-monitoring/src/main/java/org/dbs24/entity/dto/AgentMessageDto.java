/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AgentMessageDto {

    @EqualsAndHashCode.Include
    private String agentPhoneNum;
    @EqualsAndHashCode.Include
    private String subscriptionPhoneNum;
    private Boolean isTrackingAllowed;
    private String messageNote;

}
