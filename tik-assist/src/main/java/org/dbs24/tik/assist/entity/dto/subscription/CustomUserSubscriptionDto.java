package org.dbs24.tik.assist.entity.dto.subscription;

import lombok.Data;
import org.dbs24.tik.assist.entity.dto.plan.CustomPlanConstraint;

@Data
public class CustomUserSubscriptionDto extends AbstractUserSubscriptionDto {

    private CustomPlanConstraint customPlanConstraint;
}
