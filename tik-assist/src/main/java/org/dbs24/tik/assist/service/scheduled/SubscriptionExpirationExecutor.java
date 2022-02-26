package org.dbs24.tik.assist.service.scheduled;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.dao.OrderActionDao;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.dao.UserSubscriptionDao;
import org.dbs24.tik.assist.entity.domain.OrderAction;
import org.dbs24.tik.assist.entity.domain.UserSubscription;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
@EnableAsync
public class SubscriptionExpirationExecutor {

    private final OrderActionExecutor orderActionExecutor;

    private final ReferenceDao referenceDao;
    private final OrderActionDao orderActionDao;
    private final UserSubscriptionDao userSubscriptionDao;

    public SubscriptionExpirationExecutor(OrderActionExecutor orderActionExecutor, ReferenceDao referenceDao, OrderActionDao orderActionDao, UserSubscriptionDao userSubscriptionDao) {

        this.orderActionExecutor = orderActionExecutor;
        this.referenceDao = referenceDao;
        this.orderActionDao = orderActionDao;
        this.userSubscriptionDao = userSubscriptionDao;
    }

    @Transactional
    @Scheduled(
            fixedRateString = "${config.scheduled.subscription.expiration.refresh-delay}",
            initialDelayString = "${config.scheduled.subscription.expiration.initial-delay}"
    )
    public void refreshExpiredSubscriptions() {

        List<UserSubscription> expiredUserSubscriptions = userSubscriptionDao
                .findExpiredUserSubscriptions()
                .stream()
                .peek(userSubscription -> userSubscription.setUserSubscriptionStatus(referenceDao.findWaitingForPaymentUserSubscriptionStatus()))
                .collect(Collectors.toList());

        List<UserSubscription> updatedUserSubscriptions = userSubscriptionDao.updateUserSubscriptions(expiredUserSubscriptions);

        List<OrderAction> orderActionsRest = updatedUserSubscriptions
                .stream()
                .map(orderActionDao::findNotStartedByUserSubscription)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        log.info("FOUND EXPIRED SUBSCRIPTIONS SIZE : {}; ORDER ACTIONS LEFT FOR THESE ORDERS: {}", expiredUserSubscriptions.size(), orderActionsRest.size());

        orderActionExecutor.sendOrderActionsToExecute(orderActionsRest);
    }
}
