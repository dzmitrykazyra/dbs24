package org.dbs24.tik.assist.entity.dto.plan.response;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.UserPlan;

@Data
public class CreatedCustomUserPlanDto {

    private Integer createdPlanId;

    public static CreatedCustomUserPlanDto toDto(UserPlan userPlan) {

        return StmtProcessor.create(
                CreatedCustomUserPlanDto.class,
                createdCustomUserPlanDto -> createdCustomUserPlanDto.setCreatedPlanId(userPlan.getPlanId())
        );
    }
}
