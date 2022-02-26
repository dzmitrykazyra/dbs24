package org.dbs24.tik.assist.entity.dto.subscription;

import lombok.Data;
import org.dbs24.tik.assist.entity.dto.plan.CustomPlanConstraint;

@Data
public class CalculateSubscriptionCostDto {

    private CustomPlanConstraint customPlanConstraint;
    private int accountsQuantity;
}
