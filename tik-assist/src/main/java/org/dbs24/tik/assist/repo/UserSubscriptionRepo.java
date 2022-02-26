package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.User;
import org.dbs24.tik.assist.entity.domain.UserSubscription;
import org.dbs24.tik.assist.entity.domain.UserSubscriptionStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserSubscriptionRepo extends ApplicationJpaRepository<UserSubscription, Integer>, JpaSpecificationExecutor<UserSubscription>, PagingAndSortingRepository<UserSubscription, Integer> {

    List<UserSubscription> findByUserAndUserSubscriptionStatus(User user, UserSubscriptionStatus userSubscriptionStatus);

    List<UserSubscription> findByUserSubscriptionStatusAndEndDateBefore(
            UserSubscriptionStatus userSubscriptionStatus,
            LocalDateTime currentDate
    );

    List<UserSubscription> findByUser(User user);

    Optional<UserSubscription> findByUserAndUserSubscriptionId(User user, Integer userSubscriptionId);

    void removeByUserSubscriptionId(Integer userSubscriptionId);
}