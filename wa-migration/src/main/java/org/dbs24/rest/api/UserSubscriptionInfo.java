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
public class UserSubscriptionInfo {

    private Integer subscriptionId;
    private Long actualDate;
    private String subscriptionName;
    private String phoneNum;
    private Integer userId;
    private Integer agentId;
    private Integer contractId;
    private Byte subscriptionStatusId;
    private Boolean onlineNotify;
}
