package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.DepositPaymentType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DepositPaymentTypeRepo extends ApplicationJpaRepository<DepositPaymentType, Integer>, JpaSpecificationExecutor<DepositPaymentType>, PagingAndSortingRepository<DepositPaymentType, Integer> {

}