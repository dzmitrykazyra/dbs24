package org.dbs24.tik.mobile.dao;

import org.dbs24.tik.mobile.entity.domain.OrderStatus;

public interface OrderStatusDao {

    OrderStatus findInProgressOrderStatus();
    OrderStatus findFinishedOrderStatus();
    OrderStatus findHistoryClearedOrderStatus();
    OrderStatus findInvalidOrderStatus();

    OrderStatus findOrderStatusById(Integer orderStatusId);
}
