package org.dbs24.auth.server.entity.tik_assist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FacebookVerificationResponse {

    @JsonProperty("json")
    private FacebookData facebookData;
}
