package org.dbs24.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ModifyContractEndDateInfo {
    @JsonProperty("login_token")
    private String loginToken;
    @JsonProperty("days_amount")
    private Integer daysAmount;
}
