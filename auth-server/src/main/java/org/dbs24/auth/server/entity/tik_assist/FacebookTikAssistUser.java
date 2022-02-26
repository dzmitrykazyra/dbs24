package org.dbs24.auth.server.entity.tik_assist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FacebookTikAssistUser {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("input_token")
    private String inputToken;
}
