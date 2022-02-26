package org.dbs24.tik.assist.service.scheduled;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.dao.UserOrderDao;
import org.dbs24.tik.assist.service.bot.BotService;
import org.dbs24.tik.assist.service.hierarchy.OrderActionService;
import org.dbs24.tik.assist.dao.OrderActionDao;
import org.dbs24.tik.assist.dao.OrderExecutionProgressDao;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.entity.domain.OrderAction;
import org.dbs24.tik.assist.entity.domain.OrderExecutionProgress;
import org.dbs24.tik.assist.entity.domain.OrderActionResult;
import org.dbs24.tik.assist.entity.domain.UserOrder;
import org.dbs24.tik.assist.entity.dto.action.ActionTaskResult;
import org.dbs24.tik.assist.service.scheduled.resolver.TiktokActionInteractor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@EnableAsync
@Service
public class OrderActionExecutor {

    private final UserOrderDao              userOrderDao;
    private final ReferenceDao              referenceDao;
    private final OrderActionDao            orderActionDao;
    private final OrderExecutionProgressDao orderExecutionProgressDao;

    private final BotService                botService;
    private final OrderActionService        orderActionService;
    private final TiktokActionInteractor    tiktokActionInteractor;

    public OrderActionExecutor(ReferenceDao referenceDao,
                               OrderActionDao orderActionDao,
                               OrderExecutionProgressDao orderExecutionProgressDao,
                               UserOrderDao userOrderDao,
                               BotService botService,
                               OrderActionService orderActionService,
                               TiktokActionInteractor tiktokActionInteractor) {
        this.referenceDao = referenceDao;
        this.orderActionDao = orderActionDao;
        this.orderExecutionProgressDao = orderExecutionProgressDao;
        this.userOrderDao = userOrderDao;
        this.botService = botService;
        this.orderActionService = orderActionService;
        this.tiktokActionInteractor = tiktokActionInteractor;
    }

    @Transactional
    //@Scheduled(fixedRateString = "${config.tik-connector.action.update:5000}", cron = "${config.tik-connector.action.processing-cron:}")
    public void executeOrderTasks() {

        List<UserOrder> actualOrders = userOrderDao.findActualUserOrders();
        List<OrderAction> allInProgressOrderActions = orderActionDao.findInProgressOrderActions();

        log.info("THERE IS {} ACTIVE ORDERS", actualOrders.size());

        actualOrders
                .forEach(order -> {
                    OrderExecutionProgress orderProgress = orderExecutionProgressDao.findProgressByUserOrder(order);

                    Integer tasksQuantityToExecute = calculateActionsToExecuteQuantity(orderProgress);
                    log.info("TASKS QUANTITY TO EXECUTE: {} FOR ORDER : {}" , tasksQuantityToExecute, orderProgress.getUserOrder().getOrderId());

                    List<OrderAction> orderActionsToExecute = orderActionDao.findActiveOrderActionsByOrderAndQuantity(order, tasksQuantityToExecute);

                    List<OrderAction> inProgressOrderActions = orderActionDao.changeOrderActionsToInProgress(orderActionsToExecute);

                    sendOrderActionsToExecute(inProgressOrderActions);
                });

        if (allInProgressOrderActions.size() > 0) {
            updateTasksExecutionStatuses(allInProgressOrderActions);
        }

        setAvailableBotsForWaitingForBotTasks();
    }

    private void updateTasksExecutionStatuses(List<OrderAction> activeOrderActions) {

        List<OrderAction> orderActionsWithNewStatus = tiktokActionInteractor
                .getMultipleTaskResult(activeOrderActions)
                .getActionTaskResults()
                .stream()
                .map(
                        actionTaskResult -> activeOrderActions.stream()
                                .filter(
                                        orderAction -> (Long.valueOf(orderAction.getOrderActionId()).equals(actionTaskResult.getActionId()))
                                                && !orderAction.getOrderActionResult().getOrderActionResultId().equals(actionTaskResult.getActionResultId()))
                                .findAny()
                                .map(
                                        orderAction -> {
                                            orderAction.setOrderActionResult(referenceDao.findActionResultById(actionTaskResult.getActionResultId()));
                                            orderAction.setErrorMessage(actionTaskResult.getErrMsg());

                                            return orderAction;
                                        }
                                )
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .peek(
                        orderAction -> {
                            if (referenceDao.findFinishedOrderActionResult().getOrderActionResultId()
                                    .equals(orderAction.getOrderActionResult().getOrderActionResultId())) {

                                orderExecutionProgressDao.finishOrderAction(orderAction);
                            } else if (referenceDao.findBotIsExpiredOrderActionResult().getOrderActionResultId()
                                    .equals(orderAction.getOrderActionResult().getOrderActionResultId())) {

                                botService.updateBotStatus(orderAction.getBot().getBotId(), referenceDao.findExpiredBotStatus());
                            }
                        }
                )
                .collect(Collectors.toList());

        orderActionDao.saveOrderActions(orderActionsWithNewStatus);
    }

    private Integer calculateActionsToExecuteQuantity(OrderExecutionProgress orderExecutionProgress) {

        int totalActionsQuantity = Long.valueOf(orderExecutionProgress.getUserOrder().getActionsAmount()).intValue();
        int doneActionsQuantity = Long.valueOf(orderExecutionProgress.getDoneActionsQuantity()).intValue();

        LocalDateTime actualDate = orderExecutionProgress.getUserOrder().getActualDate();
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime endDate = orderExecutionProgress.getUserOrder().getEndDate();

        Integer totalExecutionHours = Long.valueOf(actualDate.until(endDate, ChronoUnit.HOURS)).intValue();
        Integer passedExecutionHours = Long.valueOf(actualDate.until(currentDateTime, ChronoUnit.HOURS)).intValue();

        //todo REMOVE TEST CALCULATION
        return 3/*Math.abs((totalActionsQuantity - doneActionsQuantity) / (totalExecutionHours - passedExecutionHours))*/;
    }

    public void sendOrderActionsToExecute(List<OrderAction> orderActions) {

        tiktokActionInteractor.sendMultipleTaskToExecute(orderActions);
    }

    private void setAvailableBotsForWaitingForBotTasks() {

        orderActionService.setValidBotsToWaitingForBotTasks(orderActionDao.findWaitingForBotOrderActions());
    }
}
