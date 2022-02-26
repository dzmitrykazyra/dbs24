package org.dbs24.tik.assist.dao;

import org.dbs24.tik.assist.entity.domain.*;

import java.util.List;

public interface OrderActionDao {

    OrderAction saveOrderAction(OrderAction orderActionToSave);
    List<OrderAction> changeOrderActionsToInProgress(List<OrderAction> orderActionsToStart);
    List<OrderAction> changeOrderActionsToNeedRemake(List<OrderAction> orderActionsToRemake);
    List<OrderAction> saveOrderActions(List<OrderAction> orderActionsToSave);

    List<OrderAction> findByUserOrder(UserOrder userOrder);

    List<OrderAction> findInProgressOrderActions();
    List<OrderAction> findExecutionErrorOrderActions();
    List<OrderAction> findActiveOrderActionsByOrderAndQuantity(UserOrder userOrder, Integer orderActionsQuantity);
    List<OrderAction> findWaitingForBotOrderActions();

    List<OrderAction> findNotStartedByUserOrder(UserOrder userOrder);
    List<OrderAction> findNotStartedByUserPlan(UserPlan userPlan);
    List<OrderAction> findNotStartedByUserSubscription(UserSubscription userSubscription);
}
