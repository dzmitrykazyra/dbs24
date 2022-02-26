package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderStatusRepo extends JpaRepository<OrderStatus, Integer> {
    Optional<OrderStatus> findByOrderStatusName(String orderStatusName);

    Optional<OrderStatus> findByOrderStatusId(Integer orderStatusId);
}
