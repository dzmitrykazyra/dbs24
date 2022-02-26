package org.dbs24.tik.assist.constant.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.PlanTemplateStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum PlanTemplateStatusDefine {

    PTS_ACTIVE(1, "PTS.ACTIVE"),
    PTS_NON_ACTIVE(2, "PTS.NONACTIVE");

    private final Integer id;
    private final String planTemplateStatusValue;

    public static List<PlanTemplateStatus> getAll() {
        return Arrays.stream(PlanTemplateStatusDefine.values()).map(
                planTemplateStatusEnum -> StmtProcessor.create(
                        PlanTemplateStatus.class,
                        planTemplateStatus -> {
                            planTemplateStatus.setPlanTemplateStatusId(planTemplateStatusEnum.getId());
                            planTemplateStatus.setPlanTemplateStatusName(planTemplateStatusEnum.getPlanTemplateStatusValue());
                        }
                )
        ).collect(Collectors.toList());
    }
}
