package org.dbs24.tik.assist.dao.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.dao.OrderActionDao;
import org.dbs24.tik.assist.dao.OrderExecutionProgressDao;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.dao.UserOrderDao;
import org.dbs24.tik.assist.entity.domain.*;
import org.dbs24.tik.assist.repo.OrderActionRepo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Log4j2
public class OrderActionDaoImpl implements OrderActionDao {

    private final ReferenceDao referenceDao;

    private final OrderActionRepo orderActionRepo;

    public OrderActionDaoImpl(ReferenceDao referenceDao, OrderActionRepo orderActionRepo) {

        this.referenceDao = referenceDao;
        this.orderActionRepo = orderActionRepo;
    }

    @Override
    public OrderAction saveOrderAction(OrderAction orderActionToSave) {

        return orderActionRepo.save(orderActionToSave);
    }

    @Override
    public List<OrderAction> changeOrderActionsToInProgress(List<OrderAction> orderActionsToStart) {

        OrderActionResult inProgressOrderActionResult = referenceDao.findInProgressOrderActionResult();

        List<OrderAction> inProgressOrderActions = orderActionsToStart
                .stream()
                .peek(
                        orderAction -> {
                            orderAction.setOrderActionResult(inProgressOrderActionResult);
                            orderAction.setStartDate(LocalDateTime.now());
                        }
                )
                .collect(Collectors.toList());

        return orderActionRepo.saveAll(inProgressOrderActions);
    }

    @Override
    public List<OrderAction> changeOrderActionsToNeedRemake(List<OrderAction> orderActionsToRemake) {

        OrderActionResult needRemakeOrderActionResult = referenceDao.findNeedRemakeOrderActionResult();

        List<OrderAction> needRemakeOrderActions = orderActionsToRemake
                .stream()
                .peek(orderAction -> orderAction.setOrderActionResult(needRemakeOrderActionResult))
                .collect(Collectors.toList());

        return orderActionRepo.saveAll(needRemakeOrderActions);
    }

    @Override
    public List<OrderAction> saveOrderActions(List<OrderAction> orderActionsToSave) {

        return orderActionRepo.saveAllAndFlush(orderActionsToSave);
    }

    @Override
    public List<OrderAction> findByUserOrder(UserOrder userOrder) {

        return orderActionRepo.findByUserOrder(userOrder);
    }

    @Override
    public List<OrderAction> findInProgressOrderActions() {

        return orderActionRepo.findByOrderActionResult(referenceDao.findInProgressOrderActionResult());
    }

    @Override
    public List<OrderAction> findExecutionErrorOrderActions() {

        return referenceDao
                .findExecutionErrorOrderActionResults()
                .stream()
                .map(orderActionRepo::findByOrderActionResult)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderAction> findActiveOrderActionsByOrderAndQuantity(UserOrder userOrder, Integer orderActionsQuantity) {

        return referenceDao
                .findNotStartedOrderActionResults()
                .stream()
                .map(
                        orderActionResult -> orderActionRepo.findByOrderActionIdLimit(
                                userOrder.getOrderId(),
                                orderActionResult.getOrderActionResultId(),
                                orderActionsQuantity)
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderAction> findWaitingForBotOrderActions() {

        return orderActionRepo.findByBotAndOrderActionResult(
                null,
                referenceDao.findWaitingForBotOrderActionResult()
        );
    }

    @Override
    public List<OrderAction> findNotStartedByUserOrder(UserOrder userOrder) {

        return referenceDao
                .findNotStartedOrderActionResults()
                .stream()
                .map(actionResult -> orderActionRepo.findByOrderActionResultAndUserOrder(actionResult, userOrder))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderAction> findNotStartedByUserPlan(UserPlan userPlan) {

        return referenceDao
                .findNotStartedOrderActionResults()
                .stream()
                .map(actionResult -> orderActionRepo.findByOrderActionResultIdAndUserPlanId(actionResult.getOrderActionResultId(), userPlan.getPlanId()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderAction> findNotStartedByUserSubscription(UserSubscription userSubscription) {

        return referenceDao
                .findNotStartedOrderActionResults()
                .stream()
                .map(actionResult -> orderActionRepo.findByOrderActionResultIdAndUserSubscriptionId(actionResult.getOrderActionResultId(), userSubscription.getUserSubscriptionId()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
