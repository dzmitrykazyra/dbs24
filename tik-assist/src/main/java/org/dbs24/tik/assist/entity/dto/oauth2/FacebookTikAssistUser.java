package org.dbs24.tik.assist.entity.dto.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FacebookTikAssistUser {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("input_token")
    private String inputToken;
}

