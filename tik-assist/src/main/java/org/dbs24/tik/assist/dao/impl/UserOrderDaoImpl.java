package org.dbs24.tik.assist.dao.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.tik.assist.dao.OrderActionDao;
import org.dbs24.tik.assist.dao.UserOrderDao;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.entity.domain.*;
import org.dbs24.tik.assist.repo.OrderHistRepo;
import org.dbs24.tik.assist.repo.OrderRepo;
import org.dbs24.tik.assist.service.exception.UserOrderNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Component
public class UserOrderDaoImpl extends DaoAbstractApplicationService implements UserOrderDao {

    private final ReferenceDao referenceDao;
    private final OrderActionDao orderActionDao;

    private final OrderRepo orderRepo;
    private final OrderHistRepo orderHistRepo;

    @Value("${constraint.user.statistics.page-size}")
    private int userHistoryPageSize;

    public UserOrderDaoImpl(ReferenceDao referenceDao, OrderActionDao orderActionDao, OrderRepo orderRepo, OrderHistRepo orderHistRepo) {

        this.referenceDao = referenceDao;
        this.orderActionDao = orderActionDao;
        this.orderRepo = orderRepo;
        this.orderHistRepo = orderHistRepo;
    }

    @Override
    public List<UserOrder> changeClosedOrdersStatusToHistoryClearByTiktokAccount(TiktokAccount tiktokAccount) {

        List<UserOrder> closedUserOrders = findClosedOrdersByTiktokAccount(tiktokAccount);
        OrderStatus historyClearOrderStatus = referenceDao.findHistoryClearOrderStatus();

        List<UserOrder> ordersWithClearedHistoryStatuses = closedUserOrders
                .stream()
                .peek(userOrder -> userOrder.setOrderStatus(historyClearOrderStatus))
                .collect(Collectors.toList());

        return orderRepo.saveAllAndFlush(ordersWithClearedHistoryStatuses);
    }

    @Override
    public UserOrder changeUserOrderStatusToHistoryClearById(Integer orderId) {

        return orderRepo
                .save(
                        orderRepo.findById(orderId)
                                .map(
                                        userOrder -> {
                                            userOrder.setOrderStatus(referenceDao.findHistoryClearOrderStatus());
                                            return userOrder;
                                        })
                                .orElseThrow(
                                        () -> new UserOrderNotFoundException(HttpStatus.BAD_REQUEST))
                );
    }

    @Override
    public UserOrder saveOrder(UserOrder userOrder) {

        return orderRepo.saveAndFlush(userOrder);
    }

    @Override
    public List<UserOrder> saveOrders(List<UserOrder> userOrders) {

        return orderRepo.saveAllAndFlush(userOrders);
    }

    @Override
    public List<UserOrder> updateOrders(List<UserOrder> userOrders) {

        saveUserOrderHistsByUserOrders(userOrders);
        return saveOrders(userOrders);
    }

    @Override
    public UserOrder updateOrder(UserOrder userOrder) {

        saveOrderHistoryByUserOrder(userOrder);
        userOrder.setActualDate(LocalDateTime.now());
        return saveOrder(userOrder);
    }

    public List<UserOrderHist> saveUserOrderHistsByUserOrders(List<UserOrder> userOrders) {

        return orderHistRepo.saveAll(userOrders.stream().map(UserOrderHist::toUserOrderHist).collect(Collectors.toList()));
    }

    @Override
    public Integer findClosedOrdersPagesQuantityByTiktokAccount(TiktokAccount tiktokAccount) {

        return orderRepo.findByOrderStatusAndUserPlanAndTiktokAccount(
                referenceDao.findClosedOrderStatus(),
                null,
                tiktokAccount,
                PageRequest.of(0, userHistoryPageSize)
        ).getTotalPages();
    }

    @Override
    public List<UserOrder> findActualUserOrders() {

        return findUserOrdersByOrderStatus(referenceDao.findActualOrderStatus());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserOrder> findUserOrdersByOrderStatus(OrderStatus orderStatus) {

        return orderRepo.findByOrderStatus(orderStatus);
    }

    @Override
    public List<UserOrder> findActiveOrdersByTiktokAccount(TiktokAccount tiktokAccount) {

        return orderRepo.findByTiktokAccountAndOrderStatusAndUserPlan(
                tiktokAccount,
                referenceDao.findActualOrderStatus(),
                null);
    }

    @Override
    public List<UserOrder> findClosedOrdersByTiktokAccount(TiktokAccount tiktokAccount) {

        return orderRepo.findByOrderStatusAndUserPlanAndTiktokAccount(
                referenceDao.findClosedOrderStatus(),
                null,
                tiktokAccount
        );
    }

    @Override
    public List<UserOrder> findClosedOrdersByPageAndTiktokAccount(Integer pageNumber, TiktokAccount tiktokAccount) {

        return orderRepo.findByOrderStatusAndUserPlanAndTiktokAccount(
                referenceDao.findClosedOrderStatus(),
                null,
                tiktokAccount,
                PageRequest.of(pageNumber, userHistoryPageSize)
        ).getContent();
    }

    @Override
    public UserOrder changeOrderStatusToFinished(UserOrder userOrder) {

        userOrder.setOrderStatus(referenceDao.findClosedOrderStatus());

        return orderRepo.save(userOrder);
    }

    @Override
    public List<UserOrder> findExpiredUserOrders() {

        return orderRepo.findByOrderStatusAndEndDateBefore(
                referenceDao.findActualOrderStatus(),
                LocalDateTime.now()
        );
    }

    @Override
    public UserOrder findOrderById(Integer orderId) {

        return orderRepo
                .findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order with such id not found: " + orderId));
    }

    @Override
    public void saveOrderHistoryByUserOrder(UserOrder userOrder) {

        orderHistRepo.save(UserOrderHist.toUserOrderHist(userOrder));
    }
}
