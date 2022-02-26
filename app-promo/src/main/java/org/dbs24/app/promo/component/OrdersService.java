package org.dbs24.app.promo.component;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.dao.OrderDao;
import org.dbs24.app.promo.entity.Order;
import org.dbs24.app.promo.entity.OrderHist;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.function.Supplier;

import static org.dbs24.app.promo.consts.AppPromoutionConsts.OrderStatusEnum.OS_FINISHED;

@Getter
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "app-promo")
public class OrdersService extends AbstractApplicationService {

    final OrderDao orderDao;
    final PackagesService packagesService;
    final RefsService refsService;
    final BatchTemplateService batchTemplateService;

    public static final Supplier<Order> createNewOrder = () -> StmtProcessor.create(Order.class, order -> order.setActualDate(LocalDateTime.now()));

    public OrdersService(RefsService refsService, OrderDao orderDao, PackagesService packagesService, BatchTemplateService batchTemplateService) {
        this.refsService = refsService;
        this.orderDao = orderDao;
        this.packagesService = packagesService;
        this.batchTemplateService = batchTemplateService;
    }

    public OrderHist createOrderHist(Order order) {
        return StmtProcessor.create(OrderHist.class, orderHist -> {

            orderHist.setOrderNote(order.getOrderNote());
            orderHist.setActualDate(order.getActualDate());
            orderHist.setOrderId(order.getOrderId());
            orderHist.setOrderStatus(order.getOrderStatus());
            orderHist.setOrderName(order.getOrderName());
            orderHist.setBatchTemplate(order.getBatchTemplate());
            orderHist.setAppPackage(order.getAppPackage());
            orderHist.setExecFinishDate(order.getExecFinishDate());
            orderHist.setExecStartDate(order.getExecStartDate());
            orderHist.setExecLastDate(order.getExecLastDate());
            orderHist.setSuccessBatchesAmount(order.getSuccessBatchesAmount());
            orderHist.setFailBatchesAmount(order.getFailBatchesAmount());
            orderHist.setOrderNote(order.getOrderNote());
            orderHist.setOrderedBatchesAmount(order.getOrderedBatchesAmount());
        });
    }

    public void saveOrder(Order order) {
        getOrderDao().saveOrder(order);
    }

    public void saveOrderHist(OrderHist orderHist) {
        getOrderDao().saveOrderHist(orderHist);
    }

    public void saveOrderHist(Order order) {
        getOrderDao().saveOrderHist(createOrderHist(order));
    }

    public Order findOrder(Integer orderId) {
        return getOrderDao().findOrder(orderId);
    }

    @Transactional
    public void closeOrder(Order order) {
        saveOrderHist(order);
        order.setOrderStatus(refsService.findOrderStatus(OS_FINISHED));
        getOrderDao().saveOrder(order);
    }

    //==================================================================================================================

    public Collection<Order> loadActualOrders() {
        return getOrderDao().loadActualOrders();
    }

    public Collection<Order> findInvalidOrders() {

        return getOrderDao().findInvalidOrders();

    }
}
