package org.dbs24.tik.dev.consts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.tik.dev.entity.reference.TariffPlanType;

import java.util.Arrays;
import java.util.Collection;

import static org.dbs24.stmt.StmtProcessor.create;


@Getter
@AllArgsConstructor
public enum TariffPlanTypeEnum {

    TPT_MONTH(1000, "TPT_MONTH"),
    TPT_QUARTER(1100, "TPT_QUARTER"),
    TPT_YEAR(1200, "TPT_YEAR"),
    TPT_CUSTOM(2000, "TPT_CUSTOM");

    public static final Collection<TariffPlanType> TP_TYPES_LIST = ServiceFuncs.<TariffPlanType>createCollection(cp -> Arrays.stream(TariffPlanTypeEnum.values())
            .map(stringRow -> create(TariffPlanType.class, ref -> {
                ref.setTariffPlanTypeId(stringRow.getTariffPlanTypeId());
                ref.setTariffPlanTypeName(stringRow.getTariffPlanTypeName());
            })).forEach(cp::add)
    );
    public static final Collection<Integer> TP_TYPES_LIST_IDS = ServiceFuncs.<Integer>createCollection(cp -> TP_TYPES_LIST.forEach(ref -> cp.add(ref.getTariffPlanTypeId())));
    private final Integer tariffPlanTypeId;
    private final String tariffPlanTypeName;

}