package org.dbs24.app.promo.component;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.dao.OrderActionDao;
import org.dbs24.app.promo.entity.BatchSetup;
import org.dbs24.app.promo.entity.Order;
import org.dbs24.app.promo.entity.OrderAction;
import org.dbs24.app.promo.entity.OrderActionHist;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.dbs24.app.promo.component.BatchSetupService.latestBatchSetupExecOrder;
import static org.dbs24.consts.SysConst.INTEGER_ZERO;

@Getter
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "app-promo")
public class OrdersActionsService extends AbstractApplicationService {

    final OrderActionDao orderActionDao;
    final RefsService refsService;
    final OrdersService ordersService;
    final BotsService botsService;
    final BatchSetupService batchSetupService;

    public static final Supplier<OrderAction> createNewOrderAction = () -> StmtProcessor.create(OrderAction.class, oa -> oa.setActualDate(LocalDateTime.now()));
    public static final Predicate<OrderAction> isReadyToExecute = orderAction -> LocalDateTime.now().compareTo(orderAction.getActionStartDate()) > 0;

    public OrdersActionsService(RefsService refsService, OrderActionDao orderActionDao, OrdersService ordersService, BotsService botsService, BatchSetupService batchSetupService) {
        this.refsService = refsService;
        this.orderActionDao = orderActionDao;
        this.ordersService = ordersService;
        this.botsService = botsService;
        this.batchSetupService = batchSetupService;
    }

    public OrderActionHist createOrderActionHist(OrderAction orderAction) {
        return StmtProcessor.create(OrderActionHist.class, orderActionHist -> {
            orderActionHist.setActualDate(orderAction.getActualDate());
            orderActionHist.setActionId(orderAction.getActionId());
            orderActionHist.setAction(orderAction.getAction());
            orderActionHist.setBatchSetup(orderAction.getBatchSetup());
            orderActionHist.setOrder(orderAction.getOrder());
            orderActionHist.setBot(orderAction.getBot());
            orderActionHist.setActionFinishDate(orderAction.getActionFinishDate());
            orderActionHist.setActionStartDate(orderAction.getActionStartDate());
            orderActionHist.setActionResult(orderAction.getActionResult());
            orderActionHist.setErrMsg(orderAction.getErrMsg());
            orderActionHist.setUsedIp(orderAction.getUsedIp());
            orderActionHist.setExecutionOrder(orderAction.getExecutionOrder());
        });
    }

    public void saveOrderActionHist(OrderActionHist orderActionHist) {
        getOrderActionDao().saveOrderActionHist(orderActionHist);
    }

    public void saveOrderActionHist(OrderAction orderAction) {
        getOrderActionDao().saveOrderActionHist(createOrderActionHist(orderAction));
    }

    public void saveOrderAction(OrderAction orderAction) {
        orderAction.setActualDate(LocalDateTime.now());
        getOrderActionDao().saveOrderAction(orderAction);
    }

    public OrderAction findOrderAction(Integer orderActionId) {
        return orderActionDao.findOrderAction(orderActionId);
    }

    public Collection<OrderAction> loadFuturedActions() {
        return getOrderActionDao().loadFuturedActions();
    }

    public Collection<OrderAction> findOrderActions(Order order) {
        return orderActionDao.findOrderActions(order);
    }

//    public Collection<OrderAction> findOrderActions(Order order, ActionResult actionResult) {
//        return orderActionDao.findOrderActions(order, actionResult);
//    }

    public Optional<BatchSetup> getNextBatchSetup(Order order) {

        log.debug("build next action 4 order: {}", order.getOrderId());

        final Collection<BatchSetup> batchSetup = batchSetupService.findByBatchTemplate(order.getBatchTemplate());

        log.debug("batchSetup size: {}, templateId: {}", batchSetup.size(), order.getBatchTemplate().getBatchTemplateId());

        final Collection<OrderAction> actions = findOrderActions(order);

        log.debug("order actions: {}, order: {}", actions.size(), order.getOrderId());

        final Integer lastExecuted =
                actions
                        .stream()
                        .max(Comparator.comparing(OrderAction::getExecutionOrder))
                        .map(OrderAction::getExecutionOrder)
                        .orElse(INTEGER_ZERO);

        final Predicate<BatchSetup> batchSetupPredicate = bs -> bs.getExecutionOrder() > lastExecuted;

        return batchSetup
                .stream()
                .filter(batchSetupPredicate)
                .sorted(latestBatchSetupExecOrder)
                .findFirst();

    }
}
