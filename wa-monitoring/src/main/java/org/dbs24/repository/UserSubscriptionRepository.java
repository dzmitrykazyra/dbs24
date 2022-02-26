/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.entity.SubscriptionStatus;
import org.dbs24.entity.User;
import org.dbs24.entity.UserContract;
import org.dbs24.entity.UserSubscription;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface UserSubscriptionRepository extends ApplicationJpaRepository<UserSubscription, Integer>, PagingAndSortingRepository<UserSubscription, Integer>, JpaSpecificationExecutor<UserSubscription> {

    @Query(value = "select a.* from wa_users_subscriptions a where subscription_id = (select max(subscription_id) from wa_users_subscriptions)", nativeQuery = true)
    UserSubscription findLastUserSubscription();

    Optional<UserSubscription> findByTmpOraId(Integer oraId);

    @Query(value = "select us.* from wa_users_subscriptions us\n"
            + "  where us.subscription_status_id in (0,1)\n"
            + "    and us.agent_id in (select agent_id from wa_agents where agent_status_id<>3)", nativeQuery = true)
    Collection<UserSubscription> findInvalidSubscriptions();

    @Query(value = "with c_actSubss as (select subscription_id from (\n"
            + "                                select us.subscription_id, COALESCE(create_date, actual_date) create_date\n"
            + "                                from (\n"
            + "                                         select us.subscription_id,\n"
            + "                                                us.actual_date,\n"
            + "                                                (select min(ush.actual_date)\n"
            + "                                                 from wa_users_subscriptions_hist ush\n"
            + "                                                 where ush.subscription_id = us.subscription_id) create_date\n"
            + "                                         from wa_users_subscriptions us\n"
            + "                                         where us.subscription_status_id = 1) us\n"
            + "                            ) us\n"
            + "where us.create_date <=  :L),\n"
            + "     c_validSubs as ( select distinct sa.subscription_id from wa_uss_activities sa, c_actSubss a where sa.subscription_id=a.subscription_id and sa.actual_date > :L ),\n"
            + "     c_invalidSubs as ( select a.subscription_id from c_actSubss a WHERE a.subscription_id not in (select subscription_id from c_validSubs ))\n"
            + "select * from wa_users_subscriptions where subscription_id in (select subscription_id from c_invalidSubs order by subscription_id)", nativeQuery = true)
    Collection<UserSubscription> findInvalidActivitySubscriptions(@Param("L") LocalDateTime limit);

    Collection<UserSubscription> findByUserAndPhoneNumAndSubscriptionStatus(User user, String phoneNum, SubscriptionStatus subscriptionStatus);

    Collection<UserSubscription> findByUserAndPhoneNum(User user, String phoneNum);

    Collection<UserSubscription> findByUser(User user);

    Collection<UserSubscription> findByActualDateGreaterThan(LocalDateTime sinceDate);

    @Query(value = "SELECT * FROM wa_users_subscriptions s WHERE s.agent_id= :A and (:AC = 0 OR s.subscription_status_id in (0,1))", nativeQuery = true)
    Collection<UserSubscription> findByAgent(@Param("A") Integer agentId, @Param("AC") Integer actualOnly);

    Collection<UserSubscription> findByPhoneNumStartingWith(String mask);

    Collection<UserSubscription> findByUserContract(UserContract userContract);

    @Query(value = "SELECT COUNT(s) FROM wa_users_subscriptions s WHERE s.contract_id =:C and s.subscription_status_id in (0,1,-2)", nativeQuery = true)
    Integer findActualSubscriptionsAmount(@Param("C") Integer contractId);

    @Query(value = "SELECT * FROM wa_users_subscriptions s WHERE s.contract_id =:C and s.subscription_status_id in (0,1,-2)", nativeQuery = true)
    Collection<UserSubscription> findActualSubscriptions(@Param("C") Integer contractId);

    @Query(value = "select us.* from wa_users_subscriptions us, wa_users_contracts uc\n" +
            "  where us.contract_id = uc.contract_id\n" +
            "    and uc.contract_status_id = 0\n" +
            "    and us.subscription_status_id = 0", nativeQuery = true)
    Page<UserSubscription> findUnhandledSubscriptions(Pageable pageable);

    @Query(value = "select s.* from wa_users_subscriptions s, wa_agents a\n" +
            "  where s.agent_id = a.agent_id and a.agent_status_id = 0\n" +
            "  order by s.actual_date asc  limit :L ", nativeQuery = true)
    Collection<UserSubscription> findInsuranceSubscriptions(@Param("L") Integer limit);
}
