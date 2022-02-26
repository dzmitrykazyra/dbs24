package org.dbs24.auth.server.entity.tik_assist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleVerificationResponse {

    @JsonProperty("issued_to")
    private String issuedTo;

    @JsonProperty("audience")
    private String audience;

    @JsonProperty("user_id")
    private String googleUserId;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("email")
    private String email;

    @JsonProperty("verified_email")
    private Boolean verifiedEmail;

    @JsonProperty("access_type")
    private String accessType;
}
