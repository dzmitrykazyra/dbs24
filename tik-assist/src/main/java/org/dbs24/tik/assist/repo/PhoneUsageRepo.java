/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.PhoneUsage;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Collection;
import org.springframework.data.jpa.repository.Query;

public interface PhoneUsageRepo extends ApplicationJpaRepository<PhoneUsage, Integer>, JpaSpecificationExecutor<PhoneUsage>, PagingAndSortingRepository<PhoneUsage, Integer> {

    @Query(value = "select phone_usage_id, phone_id, actual_date, is_success, null error_message  from (\n"
            + "select pu.*,\n"
            + "       row_number() over ( partition by phone_id order by actual_date ) lv\n"
            + "  from tik_phone_usages pu\n"
            + "  where phone_id in (select phone_id from tik_phones where phone_status_id=1)) pus\n"
            + "  where pus.lv=1\n"
            + "  order by actual_date", nativeQuery = true)
    public Collection<PhoneUsage> findActualPhoneUsages();
}
