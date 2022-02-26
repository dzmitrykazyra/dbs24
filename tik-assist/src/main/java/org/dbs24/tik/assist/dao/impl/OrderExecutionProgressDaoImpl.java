package org.dbs24.tik.assist.dao.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.dao.OrderActionDao;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.dao.UserOrderDao;
import org.dbs24.tik.assist.dao.OrderExecutionProgressDao;
import org.dbs24.tik.assist.entity.domain.*;
import org.dbs24.tik.assist.repo.OrderExecutionProgressRepo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
public class OrderExecutionProgressDaoImpl implements OrderExecutionProgressDao {

    private final UserOrderDao userOrderDao;
    private final OrderActionDao orderActionDao;
    private final ReferenceDao referenceDao;

    private final OrderExecutionProgressRepo orderExecutionProgressRepo;

    public OrderExecutionProgressDaoImpl(UserOrderDao userOrderDao, OrderActionDao orderActionDao, ReferenceDao referenceDao, OrderExecutionProgressRepo orderExecutionProgressRepo) {

        this.userOrderDao = userOrderDao;
        this.orderActionDao = orderActionDao;
        this.referenceDao = referenceDao;
        this.orderExecutionProgressRepo = orderExecutionProgressRepo;
    }

    @Override
    public OrderExecutionProgress increment(UserOrder userOrder) {

        OrderExecutionProgress progressToIncrement = findProgressByUserOrder(userOrder);

        int doneActionsQuantity = progressToIncrement.getDoneActionsQuantity();
        doneActionsQuantity++;

        progressToIncrement.setDoneActionsQuantity(doneActionsQuantity);

        return saveProgress(progressToIncrement);
    }

    @Override
    public OrderAction finishOrderAction(OrderAction orderActionToFinish) {

        UserOrder userOrder = orderActionToFinish.getUserOrder();

        increment(userOrder);

        orderActionToFinish.setFinishDate(LocalDateTime.now());
        orderActionToFinish.setOrderActionResult(referenceDao.findFinishedOrderActionResult());
        OrderAction finishedOrderAction = orderActionDao.saveOrderAction(orderActionToFinish);

        if (isOrderFinished(userOrder)) {
            userOrderDao.changeOrderStatusToFinished(userOrder);
        }

        return finishedOrderAction;
    }

    private boolean isOrderFinished(UserOrder userOrder) {

        OrderExecutionProgress progress = findProgressByUserOrder(userOrder);

        return progress.getUserOrder().getActionsAmount()
                .equals(progress.getDoneActionsQuantity());
    }

    @Override
    public OrderExecutionProgress saveProgress(OrderExecutionProgress orderExecutionProgress) {

        return orderExecutionProgressRepo.save(orderExecutionProgress);
    }

    @Override
    public OrderExecutionProgress saveZeroProgressByUserOrder(UserOrder userOrder) {

        return orderExecutionProgressRepo.save(createFreshOrderExecutionProgress(userOrder));
    }

    @Override
    public List<OrderExecutionProgress> saveZeroProgressesByUserOrders(List<UserOrder> userOrders) {

        return orderExecutionProgressRepo.saveAll(
                userOrders
                        .stream()
                        .map(this::createFreshOrderExecutionProgress)
                        .collect(Collectors.toList()));
    }

    private OrderExecutionProgress createFreshOrderExecutionProgress(UserOrder userOrder) {

        return OrderExecutionProgress.builder()
                .userOrder(userOrder)
                .doneActionsQuantity(0)
                .build();
    }

    /**
     * Method allows find progress entity by OrderAction entity
     * or create new progress entity in case it does not exist(empty progress entity has 0 performed actions)
     */
    @Override
    public OrderExecutionProgress findProgressByUserOrder(UserOrder userOrder) {

        return orderExecutionProgressRepo
                .findByUserOrder(userOrder)
                .orElseThrow(() -> new RuntimeException("No progress stored for order"));
    }

    @Override
    public List<OrderExecutionProgress> findActiveOrdersProgressesByTiktokAccount(TiktokAccount tiktokAccount) {

        return userOrderDao
                .findActiveOrdersByTiktokAccount(tiktokAccount)
                .stream()
                .map(
                        userOrder -> orderExecutionProgressRepo.findByUserOrder(userOrder)
                                .orElseThrow(() -> new RuntimeException("No progress by such user order"))
                )
                .collect(Collectors.toList());
    }
}
