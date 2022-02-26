package org.dbs24.tik.assist.service.scheduled;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.dao.OrderActionDao;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.dao.UserPlanDao;
import org.dbs24.tik.assist.entity.domain.OrderAction;
import org.dbs24.tik.assist.entity.domain.UserPlan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
@EnableAsync
public class PlanExpirationExecutor {

    private final OrderActionExecutor orderActionExecutor;

    private final UserPlanDao userPlanDao;
    private final ReferenceDao referenceDao;
    private final OrderActionDao orderActionDao;

    public PlanExpirationExecutor(OrderActionExecutor orderActionExecutor, UserPlanDao userPlanDao, ReferenceDao referenceDao, OrderActionDao orderActionDao) {

        this.orderActionExecutor = orderActionExecutor;
        this.userPlanDao = userPlanDao;
        this.referenceDao = referenceDao;
        this.orderActionDao = orderActionDao;
    }

    @Transactional
    @Scheduled(
            fixedRateString = "${config.scheduled.plan.expiration.refresh-delay}",
            initialDelayString = "${config.scheduled.plan.expiration.initial-delay}"
    )
    public void refreshExpiredPlans() {

        List<UserPlan> expiredUserPlans = userPlanDao
                .findExpiredUserPlans()
                .stream()
                .peek(userPlan -> userPlan.setPlanStatus(referenceDao.findDonePlanStatus()))
                .collect(Collectors.toList());

        List<UserPlan> updatedUserPlans = userPlanDao.updateUserPlans(expiredUserPlans);

        List<OrderAction> orderActionsRest = updatedUserPlans
                .stream()
                .map(orderActionDao::findNotStartedByUserPlan)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        log.info("FOUND EXPIRED PLANS SIZE : {}; ORDER ACTIONS LEFT FOR THESE ORDERS: {}", updatedUserPlans.size(), orderActionsRest.size());

        orderActionExecutor.sendOrderActionsToExecute(orderActionsRest);
    }
}
