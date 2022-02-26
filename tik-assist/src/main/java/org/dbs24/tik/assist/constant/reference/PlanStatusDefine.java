package org.dbs24.tik.assist.constant.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.PlanStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum PlanStatusDefine {

    PS_ACTIVE(1, "PS.ACTIVE"),
    PS_DONE(2, "PS.DONE");

    private final Integer id;
    private final String planStatusValue;

    public static List<PlanStatus> getAll() {
        return Arrays.stream(PlanStatusDefine.values()).map(
                planStatusEnum -> StmtProcessor.create(
                        PlanStatus.class,
                        planStatus -> {
                            planStatus.setPlanStatusId(planStatusEnum.getId());
                            planStatus.setPlanStatusName(planStatusEnum.getPlanStatusValue());
                        }
                )
        ).collect(Collectors.toList());
    }
}