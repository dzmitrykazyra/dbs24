package org.dbs24.tik.assist.service.scheduled;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.dao.OrderActionDao;
import org.dbs24.tik.assist.entity.domain.OrderAction;
import org.dbs24.tik.assist.service.scheduled.resolver.OrderActionSelector;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
@EnableAsync
public class ErrorTaskRemakeExecutor {

    private final OrderActionSelector orderActionSelector;

    private final OrderActionDao orderActionDao;

    public ErrorTaskRemakeExecutor(OrderActionSelector orderActionSelector, OrderActionDao orderActionDao) {

        this.orderActionSelector = orderActionSelector;
        this.orderActionDao = orderActionDao;
    }

    //@Scheduled(fixedRateString = "${config.scheduled.error-action.remake.refresh-delay}")
    @Transactional
    public void checkErrorTasks() {

        List<OrderAction> executionErrorOrderActions = orderActionDao.findExecutionErrorOrderActions();

        List<OrderAction> reCreatedOrderActions = executionErrorOrderActions
                .stream()
                .map(orderActionSelector::reCreateOrderAction)
                .collect(Collectors.toList());

        orderActionDao.saveOrderActions(reCreatedOrderActions);
        orderActionDao.changeOrderActionsToNeedRemake(executionErrorOrderActions);
    }
}
