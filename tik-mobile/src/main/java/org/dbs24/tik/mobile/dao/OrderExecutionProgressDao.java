package org.dbs24.tik.mobile.dao;

import org.dbs24.tik.mobile.entity.domain.Order;
import org.dbs24.tik.mobile.entity.domain.OrderExecutionProgress;

public interface OrderExecutionProgressDao {

    void saveZeroExecutionProgressByOrder(Order order);

    OrderExecutionProgress findExecutionProgressByOrder(Order order);

    OrderExecutionProgress incrementOrderProgress(Order order);
}
