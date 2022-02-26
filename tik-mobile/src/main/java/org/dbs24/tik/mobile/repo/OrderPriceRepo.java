package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.OrderPrice;
import org.dbs24.tik.mobile.entity.domain.OrderActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderPriceRepo extends JpaRepository<OrderPrice, Integer> {

    List<OrderPrice> findByOrderActionTypeAndUpToActionsQuantityGreaterThanEqual(OrderActionType orderActionType, Integer actionsQuantity);
}
