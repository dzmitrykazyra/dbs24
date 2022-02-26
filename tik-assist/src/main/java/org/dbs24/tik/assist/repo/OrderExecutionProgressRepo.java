package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.OrderExecutionProgress;
import org.dbs24.tik.assist.entity.domain.UserOrder;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface OrderExecutionProgressRepo extends ApplicationJpaRepository<OrderExecutionProgress, Integer>, JpaSpecificationExecutor<OrderExecutionProgress>, PagingAndSortingRepository<OrderExecutionProgress, Integer> {

    Optional<OrderExecutionProgress> findByUserOrder(UserOrder userOrder);
}