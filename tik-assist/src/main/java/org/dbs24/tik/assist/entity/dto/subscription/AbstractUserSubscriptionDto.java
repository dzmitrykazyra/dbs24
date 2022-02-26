package org.dbs24.tik.assist.entity.dto.subscription;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public abstract class AbstractUserSubscriptionDto {

    @JsonIgnore
    protected String promocodeValue;

    private String tiktokUsername;

    @JsonIgnore
    protected String currencyIso;

    @JsonIgnore
    protected String[] subscriptionAccountUrls;
}
