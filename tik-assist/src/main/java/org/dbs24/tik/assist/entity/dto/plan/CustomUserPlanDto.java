package org.dbs24.tik.assist.entity.dto.plan;

import lombok.Data;

@Data
public class CustomUserPlanDto {

    private String accountUsername;

    private String promocodeValue;

    private CustomPlanConstraint customPlanConstraint;
}
