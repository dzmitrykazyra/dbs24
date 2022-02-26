/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.kafka.api.KafkaMessage;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserSubscriptionInfo extends KafkaMessage {

    @EqualsAndHashCode.Include
    private Integer subscriptionId;
    private Integer agentId;
    private Integer userId;
    private Long createDate;
    private Long actualDate;
    private String subscriptionName;
    private String phoneNum;
    private Long avatarId;
    private Byte subscriptionStatusId;
    private Boolean onlineNotify;
    private Long avatarModifyDate;
    private boolean isCustomAvatar;
}
