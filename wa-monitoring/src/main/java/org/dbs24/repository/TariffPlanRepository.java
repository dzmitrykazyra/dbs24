package org.dbs24.repository;

import org.dbs24.entity.TariffPlan;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface TariffPlanRepository extends ApplicationJpaRepository<TariffPlan, Integer> {

    @Query(value = "select *\n" +
            "from wa_tariffs_plans\n" +
            "where (1 = :ALL_DEVICES OR device_type_id = :DEVICE_TYPE)\n" +
            "  and (1 = :ALL_STATUSES OR tariff_plan_status_id = :PLAN_STATUS)", nativeQuery = true)
    Collection<TariffPlan> findTariffPlans(
            @Param("ALL_DEVICES") Integer allDevices,
            @Param("DEVICE_TYPE") Integer deviceTypeId,
            @Param("ALL_STATUSES") Integer allStatuses,
            @Param("PLAN_STATUS") Integer planStatusId);

}
