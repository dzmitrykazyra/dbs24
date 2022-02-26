package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.service.funcs.ServiceFuncs;

import java.util.Collection;

@Data
@EqualsAndHashCode
public class AllTariffPlans {
    private Collection<TariffPlanInfo> plans = ServiceFuncs.<TariffPlanInfo>createCollection();
}
