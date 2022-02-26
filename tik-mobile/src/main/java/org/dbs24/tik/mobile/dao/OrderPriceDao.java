package org.dbs24.tik.mobile.dao;

import org.dbs24.tik.mobile.entity.domain.OrderPrice;
import org.dbs24.tik.mobile.entity.domain.OrderActionType;

import java.util.List;

public interface OrderPriceDao {
    
    OrderPrice save(OrderPrice costToQuantityToSave);

    Integer findByActionTypeAndActionsQuantity(OrderActionType orderActionType, Integer actionsQuantity);

    List<OrderPrice> findAll();
}
