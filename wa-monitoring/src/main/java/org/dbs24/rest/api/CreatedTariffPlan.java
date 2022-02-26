package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.rest.api.dto.OperationResult;

@Data
@EqualsAndHashCode
public class CreatedTariffPlan extends OperationResult {
    private Integer tariffPlanId;
}
