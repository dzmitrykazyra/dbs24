package org.dbs24.tik.assist.service.scheduled;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.dao.OrderActionDao;
import org.dbs24.tik.assist.dao.UserOrderDao;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.entity.domain.OrderAction;
import org.dbs24.tik.assist.entity.domain.UserOrder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
@EnableAsync
public class OrderExpirationExecutor {

    private final OrderActionExecutor orderActionExecutor;

    private final UserOrderDao userOrderDao;
    private final ReferenceDao referenceDao;
    private final OrderActionDao orderActionDao;

    public OrderExpirationExecutor(OrderActionExecutor orderActionExecutor, UserOrderDao userOrderDao, ReferenceDao referenceDao, OrderActionDao orderActionDao) {

        this.orderActionExecutor = orderActionExecutor;
        this.userOrderDao = userOrderDao;
        this.referenceDao = referenceDao;
        this.orderActionDao = orderActionDao;
    }

    @Transactional
    @Scheduled(
            fixedRateString = "${config.scheduled.order.expiration.refresh-delay}",
            initialDelayString = "${config.scheduled.order.expiration.initial-delay}"
    )
    public void refreshExpireOrders() {

        List<UserOrder> expiredUserOrders = userOrderDao
                .findExpiredUserOrders()
                .stream()
                .peek(userOrder -> userOrder.setOrderStatus(referenceDao.findClosedOrderStatus()))
                .collect(Collectors.toList());

        List<UserOrder> updatedUserOrders = userOrderDao.updateOrders(expiredUserOrders);

        List<OrderAction> orderActionsRest = updatedUserOrders
                .stream()
                .map(orderActionDao::findNotStartedByUserOrder)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        log.info("FOUND EXPIRED ORDERS SIZE : {}; ORDER ACTIONS LEFT FOR THESE ORDERS: {}", updatedUserOrders.size(), orderActionsRest.size());

        orderActionExecutor.sendOrderActionsToExecute(orderActionsRest);
    }
}
