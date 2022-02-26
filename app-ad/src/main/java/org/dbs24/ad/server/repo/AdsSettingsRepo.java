/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.ad.server.repo;

import org.dbs24.ad.server.entity.AdsSettings;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AdsSettingsRepo extends ApplicationJpaRepository<AdsSettings, Integer> {

    @Query(value = "select * from ads_common_settings cs, (select setting_id, row_number() over (ORDER BY modify_date desc ) rn\n" +
            "  from ads_common_settings where modify_date < :D and is_actual = true and packages like :PKG) ptc\n" +
            "  where cs.setting_id =  ptc.setting_id and ptc.rn=1", nativeQuery = true)
    Optional<AdsSettings> findActualSettings(@Param("D") LocalDateTime sinceDate, @Param("PKG") String appPackages);

}
