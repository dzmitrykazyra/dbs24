/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.entity.SubscriptionActivity;
import org.dbs24.entity.UserSubscription;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;

public interface ActivityRepository extends ApplicationJpaRepository<SubscriptionActivity, Long>, JpaSpecificationExecutor<SubscriptionActivity> {

    Collection<SubscriptionActivity> findByUserSubscriptionAndActualDateBetween(UserSubscription subscription, LocalDateTime d1, LocalDateTime d2);

    @Query(value = "select * from wa_uss_activities where subscription_id=:S order by activity_id desc limit :L", nativeQuery = true)
    Collection<SubscriptionActivity> findLatestActivities(@Param("S") UserSubscription subscription, @Param("L") Integer limit);

    Collection<SubscriptionActivity> findByActualDateBefore(LocalDateTime localDateTime);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "call wa_delete_deprecated_activities(:arcdate, :deprecateddate)", nativeQuery = true)
    void deleteDeprecatedActivities(@Param("arcdate") LocalDateTime arcDate, @Param("deprecateddate") LocalDateTime deprecatedDate);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "call wa_delete_deprecated_hist(:deprecateDate)", nativeQuery = true)
    void deleteDeprecatedHistData(@Param("deprecateDate") LocalDateTime deprecatedDate);

}
