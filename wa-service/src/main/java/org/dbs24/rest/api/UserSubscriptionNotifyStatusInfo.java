/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class UserSubscriptionNotifyStatusInfo {
    
    private Long actualDate;
    private String phoneNum;
    private Byte subscriptionStatusId;
    private Boolean onlineNotify;    
    private String assignedName;    
}
