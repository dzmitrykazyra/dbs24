package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.Order;
import org.dbs24.tik.mobile.entity.domain.OrderExecutionProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderExecutionProgressRepo extends JpaRepository<OrderExecutionProgress, Integer> {
    Optional<OrderExecutionProgress> findOrderExecutionProgressByOrder(Order order);
}
