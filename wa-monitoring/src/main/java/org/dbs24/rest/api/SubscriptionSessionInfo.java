package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class SubscriptionSessionInfo extends LastSessionInfo {

    @EqualsAndHashCode.Include
    private Integer subscriptionId;
    private String phoneNum;

}
