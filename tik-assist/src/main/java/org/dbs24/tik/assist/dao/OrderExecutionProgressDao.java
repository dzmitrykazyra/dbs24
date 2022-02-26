package org.dbs24.tik.assist.dao;

import org.dbs24.tik.assist.entity.domain.*;

import java.util.List;

public interface OrderExecutionProgressDao {

    OrderExecutionProgress increment(UserOrder userOrder);
    OrderAction finishOrderAction(OrderAction orderActionToFinish);
    OrderExecutionProgress saveProgress(OrderExecutionProgress orderExecutionProgress);
    OrderExecutionProgress saveZeroProgressByUserOrder(UserOrder userOrder);
    List<OrderExecutionProgress> saveZeroProgressesByUserOrders(List<UserOrder> userOrders);

    OrderExecutionProgress findProgressByUserOrder(UserOrder userOrder);
    List<OrderExecutionProgress> findActiveOrdersProgressesByTiktokAccount(TiktokAccount tiktokAccount);
}
