package org.dbs24.tik.mobile.dao.impl;

import org.dbs24.tik.mobile.dao.OrderDao;
import org.dbs24.tik.mobile.dao.OrderExecutionProgressDao;
import org.dbs24.tik.mobile.dao.OrderStatusDao;
import org.dbs24.tik.mobile.entity.domain.Order;
import org.dbs24.tik.mobile.entity.domain.OrderHist;
import org.dbs24.tik.mobile.entity.domain.OrderStatus;
import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.repo.OrderHistRepo;
import org.dbs24.tik.mobile.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDaoImpl implements OrderDao {

    @Value("${constraint.orders.history.page-size}")
    private Integer amountOrdersInPage;

    private final OrderRepo orderRepo;
    private final OrderStatusDao orderStatusDao;
    private final OrderExecutionProgressDao orderExecutionProgressDao;
    private final OrderHistRepo orderHistRepo;

    @Autowired
    public OrderDaoImpl(OrderRepo orderRepo,
                        OrderStatusDao orderStatusDao,
                        OrderExecutionProgressDao orderExecutionProgressDao,
                        OrderHistRepo orderHistRepo) {

        this.orderRepo = orderRepo;
        this.orderStatusDao = orderStatusDao;
        this.orderExecutionProgressDao = orderExecutionProgressDao;
        this.orderHistRepo = orderHistRepo;
    }

    public List<Order> findAllAvailableOrders(Integer userId) {

        Integer inProgressOrderStatusId = orderStatusDao.findInProgressOrderStatus().getOrderStatusId();
        return orderRepo.findAllAvailableOrders(userId, LocalDateTime.now(), inProgressOrderStatusId);
    }

    @Override
    public Order findOrderById(Integer orderId) {

        return orderRepo.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Failed to find order with id = " + orderId));
    }

    @Override
    public Order update(Order order) {
        return orderRepo.save(order);
    }

    @Override
    public Order saveNewOrder(Order order) {
        Order savedOrder = orderRepo.save(order);
        orderExecutionProgressDao.saveZeroExecutionProgressByOrder(savedOrder);
        return savedOrder;
    }

    @Override
    public Integer findUserOrdersAmount(User user) {
        return orderRepo.countAllByUser(user);
    }

    @Override
    public List<Order> findAllActiveOrdersByUser(User user, Integer actionTypeId) {

        return orderRepo.findAllOrdersByUserAndOrderStatus(
                user.getId(),
                orderStatusDao.findInProgressOrderStatus().getOrderStatusId(),
                actionTypeId,
                Pageable.unpaged()
        );
    }

    @Override
    public List<Order> findAllCompleteOrdersByPage(User user, Integer pageNum, Integer actionTypeId) {

        return orderRepo.findAllOrdersByUserAndOrderStatus(
                user.getId(),
                orderStatusDao.findFinishedOrderStatus().getOrderStatusId(),
                actionTypeId,
                PageRequest.of(pageNum, amountOrdersInPage)
        );
    }

    @Override
    public void updateOrderStatusToComplete(Order order) {
        updateOrder(order, orderStatusDao.findFinishedOrderStatus());
    }

    @Override
    public Order updateOrderStatusToHistoryCleared(Integer orderToClearHistoryId) {

        Order orderToClearHistory = findOrderById(orderToClearHistoryId);
        orderToClearHistory.setOrderStatus(orderStatusDao.findHistoryClearedOrderStatus());

        return updateOrder(orderToClearHistory, orderStatusDao.findHistoryClearedOrderStatus());
    }

    @Override
    public Order updateOrderStatusToInvalid(Integer orderToInvalidateId) {
        Order orderToInvalidate = findOrderById(orderToInvalidateId);
        orderHistRepo.save(OrderHist.of(orderToInvalidate));

        orderToInvalidate.setOrderStatus(orderStatusDao.findInvalidOrderStatus());
        orderToInvalidate.setActualDate(LocalDateTime.now());

        return updateOrder(orderToInvalidate, orderStatusDao.findInvalidOrderStatus());
    }

    @Override
    public List<Order> updateOrdersStatusesToInvalid(List<Integer> ordersToInvalidateIdList) {
        List<Order> ordersToInvalidate = ordersToInvalidateIdList
                .stream()
                .map(this::findOrderById)
                .collect(Collectors.toList());

        return updateOrders(ordersToInvalidate, orderStatusDao.findInvalidOrderStatus());
    }

    @Override
    public List<Order> findAllActiveOrders() {

        return orderRepo.findByOrderStatus(
                orderStatusDao.findInProgressOrderStatus()
        );
    }

    private List<Order> updateOrders(List<Order> orders, OrderStatus newOrderStatus) {
        orderHistRepo.saveAll(orders.stream()
                .map(OrderHist::of)
                .collect(Collectors.toList())
        );

        return orderRepo.saveAll(orders.stream()
                .peek(order -> {
                    order.setActualDate(LocalDateTime.now());
                    order.setOrderStatus(newOrderStatus);
                })
                .collect(Collectors.toList()));

    }


    private Order updateOrder(Order orderToUpdate, OrderStatus newOrderStatus) {
        orderHistRepo.save(OrderHist.of(orderToUpdate));

        orderToUpdate.setActualDate(LocalDateTime.now());
        orderToUpdate.setOrderStatus(newOrderStatus);

        return orderRepo.save(orderToUpdate);
    }
}
