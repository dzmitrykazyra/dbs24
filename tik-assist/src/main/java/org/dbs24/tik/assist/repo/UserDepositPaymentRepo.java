package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.UserDepositPayment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDepositPaymentRepo extends ApplicationJpaRepository<UserDepositPayment, Integer>, JpaSpecificationExecutor<UserDepositPayment>, PagingAndSortingRepository<UserDepositPayment, Integer> {

}
