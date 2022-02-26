package org.dbs24.tik.assist.entity.dto.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FacebookVerificationResponse {

    @JsonProperty("json")
    private FacebookData facebookData;
}
