package org.dbs24.tik.assist.entity.dto.constraint;

import lombok.Data;

import java.util.Map;

@Data
public class CustomPlanConstraintsDto {

    private Map<String, Integer> constraintNameToValue;
}
