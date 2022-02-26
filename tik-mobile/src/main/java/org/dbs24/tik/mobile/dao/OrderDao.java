package org.dbs24.tik.mobile.dao;

import org.dbs24.tik.mobile.entity.domain.Order;
import org.dbs24.tik.mobile.entity.domain.User;

import java.util.List;

public interface OrderDao {

    List<Order> findAllAvailableOrders(Integer userId);

    Order findOrderById(Integer orderId);

    Order saveNewOrder(Order order);

    Integer findUserOrdersAmount(User user);

    List<Order> findAllActiveOrdersByUser(User user, Integer actionTypeId);

    List<Order> findAllCompleteOrdersByPage(User user, Integer pageNum, Integer actionTypeId);

    void updateOrderStatusToComplete(Order order);

    Order updateOrderStatusToHistoryCleared(Integer orderToClearHistoryId);

    Order updateOrderStatusToInvalid(Integer orderToInvalidateId);

    Order update(Order order);

    List<Order> updateOrdersStatusesToInvalid(List<Integer> ordersToInvalidateIdList);

    List<Order> findAllActiveOrders();
}
