package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class SubscriptionSessionInfo extends LastSessionInfo {

    private Integer subscriptionId;
    private String phoneNum;

}
