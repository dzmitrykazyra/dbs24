package org.dbs24.tik.assist.dao;

import org.dbs24.tik.assist.entity.domain.*;

import java.util.List;

public interface UserOrderDao {

    UserOrder saveOrder(UserOrder userOrder);
    List<UserOrder> saveOrders(List<UserOrder> userOrders);
    UserOrder updateOrder(UserOrder userOrder);
    List<UserOrder> updateOrders(List<UserOrder> userOrders);

    Integer findClosedOrdersPagesQuantityByTiktokAccount(TiktokAccount tiktokAccount);
    UserOrder findOrderById(Integer orderId);
    List<UserOrder> findActualUserOrders();
    List<UserOrder> findUserOrdersByOrderStatus(OrderStatus orderStatus);
    List<UserOrder> findActiveOrdersByTiktokAccount(TiktokAccount tiktokAccount);
    List<UserOrder> findClosedOrdersByTiktokAccount(TiktokAccount tiktokAccount);
    List<UserOrder> findClosedOrdersByPageAndTiktokAccount(Integer pageNumber, TiktokAccount tiktokAccount);
    List<UserOrder> findExpiredUserOrders();

    UserOrder changeOrderStatusToFinished(UserOrder userOrder);
    List<UserOrder> changeClosedOrdersStatusToHistoryClearByTiktokAccount(TiktokAccount tiktokAccount);
    UserOrder changeUserOrderStatusToHistoryClearById(Integer orderId);

    void saveOrderHistoryByUserOrder(UserOrder userOrder);
}
