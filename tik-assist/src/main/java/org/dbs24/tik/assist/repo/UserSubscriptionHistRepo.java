package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.UserSubscriptionHist;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserSubscriptionHistRepo extends ApplicationJpaRepository<UserSubscriptionHist, Integer>, JpaSpecificationExecutor<UserSubscriptionHist>, PagingAndSortingRepository<UserSubscriptionHist, Integer> {

}