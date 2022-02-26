package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.entity.TariffPlan;
import org.dbs24.stmt.StmtProcessor;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TariffPlanInfo {

    @EqualsAndHashCode.Include
    private Integer tariffPlanId;
    private Long actualDate;
    @EqualsAndHashCode.Include
    private Integer contractTypeId;
    private Integer tariffPlanStatusId;
    private Integer deviceTypeId;
    private String sku;
    private Integer durationHours;
    private Integer subscriptionsAmount;

    public void assign(TariffPlan tariffPlan) {
        setTariffPlanId(tariffPlan.getTariffPlanId());
        StmtProcessor.ifNotNull(tariffPlan.getContractType(), contractType -> setContractTypeId(contractType.getContractTypeId()));
        StmtProcessor.ifNotNull(tariffPlan.getTariffPlanStatus(), tariffPlanStatus -> setTariffPlanStatusId(tariffPlanStatus.getTariffPlanStatusId()));
        setDurationHours(tariffPlan.getDurationHours());
        setSku(tariffPlan.getSku());
        setSubscriptionsAmount(tariffPlan.getSubscriptionsAmount());
        StmtProcessor.ifNotNull(tariffPlan.getDeviceType(), deviceType -> setDeviceTypeId(deviceType.getDeviceTypeId()));
        setActualDate(NLS.localDateTime2long(tariffPlan.getActualDate()));
    }
}
