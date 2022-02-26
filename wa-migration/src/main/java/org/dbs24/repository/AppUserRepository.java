/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.entity.AppUser;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

public interface AppUserRepository extends ApplicationJpaRepository<AppUser, Integer>, JpaSpecificationExecutor<AppUser> {

    @Query(value = "select *\n"
            + "from wa_tracker.appuser\n"
            + "where id in (\n"
            + "    select distinct user_id\n"
            + "    from wa_tracker.subscriptionphone s\n"
            + "    where s.user_id in\n"
            + "          (select distinct user_id from (select p.user_id from wa_tracker.payment p where p.valid_until > current_date and subs_amount>1))\n"
            + "      and is_removed = 0\n"
            + "      and is_valid = 1)", nativeQuery = true)
    public Collection<AppUser> findActualUAppsers();

//    @Query(value = "select u.* from WA_TRACKER.appUser u, WA_TRACKER.subscriptionphone s  WHERE s.user_id=u.id AND s.id = TO_NUMBER(:id) ", nativeQuery = true)
//    Optional<AppUser> findBySubscriptionId(@Param("id") Integer phoneId);

}
