package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.UserDeposit;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserDepositRepo extends ApplicationJpaRepository<UserDeposit, Integer>, JpaSpecificationExecutor<UserDeposit>, PagingAndSortingRepository<UserDeposit, Integer> {

    Optional<UserDeposit> findByUserId(Integer userId);
}