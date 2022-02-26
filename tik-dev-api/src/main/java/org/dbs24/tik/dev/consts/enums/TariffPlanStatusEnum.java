package org.dbs24.tik.dev.consts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.tik.dev.entity.reference.TariffPlanStatus;

import java.util.Arrays;
import java.util.Collection;

import static org.dbs24.stmt.StmtProcessor.create;


@Getter
@AllArgsConstructor
public enum TariffPlanStatusEnum {

    TP_ACTIVE(100, "TP.ACTIVE"),
    TP_PREPARE(101, "TP.PREPARE"),
    TP_CLOSED(1000, "TP.CLOSED");

    public static final Collection<TariffPlanStatus> TP_STATUSES_LIST = ServiceFuncs.<TariffPlanStatus>createCollection(cp -> Arrays.stream(TariffPlanStatusEnum.values())
            .map(stringRow -> create(TariffPlanStatus.class, ref -> {
                ref.setTariffPlanStatusId(stringRow.getTariffPlanStatusId());
                ref.setTariffPlanStatusName(stringRow.getTariffPlanStatusName());
            })).forEach(cp::add)
    );
    public static final Collection<Integer> TP_STATUSES_IDS = ServiceFuncs.<Integer>createCollection(cp -> TP_STATUSES_LIST.forEach(ref -> cp.add(ref.getTariffPlanStatusId())));
    private final Integer tariffPlanStatusId;
    private final String tariffPlanStatusName;

}