package org.dbs24.tik.assist.entity.dto.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class FacebookData {

    @JsonProperty("app_id")
    private String appId;

    @JsonProperty("application")
    private String application;

    @JsonProperty("expires_at")
    private Long expiresAt;

    @JsonProperty("is_valid")
    private Boolean isValid;

    @JsonProperty("issued_at")
    private Boolean issuedAt;

    @JsonProperty("metadata")
    private Map<String, String> metadata;

    @JsonProperty("scopes")
    private String scopes;

    @JsonProperty("user_id")
    private String facebookUserId;
}
