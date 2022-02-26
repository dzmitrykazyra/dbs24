package org.dbs24.repository;

import org.dbs24.entity.SubscriptionPhoneActivity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;

public interface ActivityPhoneRepository extends ActivityRepository {

    @Query(value = "select a.*, s.phone_num, s.actual_date subscription_actual_date, c.actual_date contract_actual_date\n" +
            "  from wa_uss_activities a, wa_users_subscriptions s, wa_users_contracts c, wa_users u\n" +
            "  where a.subscription_id = s.subscription_id\n" +
            "    and s.contract_id = c.contract_id\n" +
            "    and c.user_id = u.user_id\n" +
            "    and u.login_token = :L\n" +
            "    and a.actual_date > :AD", nativeQuery = true)
    Collection<SubscriptionPhoneActivity> findLatestSubscriptionsActivities(@Param("L") String loginToken, @Param("AD") LocalDateTime localDateTime);

}
