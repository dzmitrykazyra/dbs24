package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.UserSubscriptionStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserSubscriptionStatusRepo extends ApplicationJpaRepository<UserSubscriptionStatus, Integer>, JpaSpecificationExecutor<UserSubscriptionStatus>, PagingAndSortingRepository<UserSubscriptionStatus, Integer> {

    Optional<UserSubscriptionStatus> findByUserSubscriptionStatusName(String userSubscriptionStatusName);
}