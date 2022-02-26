package org.dbs24.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ModifyContractBySupportInfo {
    @JsonProperty("modify_reason_id")
    private Integer modifyReasonId;
    @JsonProperty("edit_note")
    private String editNote;
    @JsonProperty("days_amount")
    private Integer daysAmount;
    @JsonProperty("contract_type_id")
    private Integer contractTypeId;
//    @JsonProperty("subscription_amount")
//    private Integer subscriptionAmount;
}
