package org.dbs24.tik.mobile.dao.impl;

import org.dbs24.tik.mobile.dao.OrderExecutionProgressDao;
import org.dbs24.tik.mobile.entity.domain.Order;
import org.dbs24.tik.mobile.entity.domain.OrderExecutionProgress;
import org.dbs24.tik.mobile.repo.OrderExecutionProgressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderExecutionProgressDaoImpl implements OrderExecutionProgressDao {

    private final OrderExecutionProgressRepo orderExecutionProgressRepo;

    @Autowired
    public OrderExecutionProgressDaoImpl(OrderExecutionProgressRepo orderExecutionProgressRepo) {
        this.orderExecutionProgressRepo = orderExecutionProgressRepo;
    }

    @Override
    public void saveZeroExecutionProgressByOrder(Order order) {
        orderExecutionProgressRepo.save(OrderExecutionProgress.builder()
                .order(order)
                .doneActionsQuantity(0)
                .build()
        );
    }

    @Override
    public OrderExecutionProgress findExecutionProgressByOrder(Order order) {
        return orderExecutionProgressRepo.findOrderExecutionProgressByOrder(order)
                .orElseThrow(() -> new RuntimeException("Cannot find order execution progress with order id = " + order.getOrderId()));
    }

    @Override
    public OrderExecutionProgress incrementOrderProgress(Order order) {
        OrderExecutionProgress progressToIncrement = findExecutionProgressByOrder(order);

        int doneActionsQuantity = progressToIncrement.getDoneActionsQuantity() + 1;

        progressToIncrement.setDoneActionsQuantity(doneActionsQuantity);

        return orderExecutionProgressRepo.save(progressToIncrement);
    }
}