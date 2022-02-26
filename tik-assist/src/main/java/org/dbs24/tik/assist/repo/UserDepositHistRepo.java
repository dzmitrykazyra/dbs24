package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.UserDepositHist;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDepositHistRepo extends ApplicationJpaRepository<UserDepositHist, Integer>, JpaSpecificationExecutor<UserDepositHist>, PagingAndSortingRepository<UserDepositHist, Integer> {

}