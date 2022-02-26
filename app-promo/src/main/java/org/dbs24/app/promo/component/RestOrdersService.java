/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.component;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.entity.Order;
import org.dbs24.app.promo.rest.dto.order.CreateOrderRequest;
import org.dbs24.app.promo.rest.dto.order.CreatedOrder;
import org.dbs24.app.promo.rest.dto.order.CreatedOrderResponse;
import org.dbs24.app.promo.rest.dto.order.OrderInfo;
import org.dbs24.app.promo.rest.dto.order.validator.OrderInfoValidator;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.BiFunction;

import static org.dbs24.app.promo.component.OrdersService.createNewOrder;
import static org.dbs24.consts.SysConst.CURRENT_LOCALDATETIME;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_INVALID_ENTITY_ATTRS;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;

@Getter
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "app-promo")
public class RestOrdersService extends AbstractRestApplicationService {

    final PackagesService packagesService;
    final RefsService refsService;
    final BatchTemplateService batchTemplateService;
    final OrderInfoValidator orderInfoValidator;
    final OrdersService ordersService;
    final BotsService botsService;

    public RestOrdersService(RefsService refsService, OrderInfoValidator orderInfoValidator, PackagesService packagesService, BatchTemplateService batchTemplateService, OrdersService ordersService, BotsService botsService) {
        this.refsService = refsService;
        this.orderInfoValidator = orderInfoValidator;
        this.packagesService = packagesService;
        this.batchTemplateService = batchTemplateService;
        this.ordersService = ordersService;
        this.botsService = botsService;
    }

    @FunctionalInterface
    interface OrdersHistBuilder {
        void buildOrdersHist(Order order);
    }

    final BiFunction<OrderInfo, Order, Order> assignDto = (orderInfo, order) -> {

        order.setBatchTemplate(getBatchTemplateService().findBatchTemplate(orderInfo.getBatchTemplateId()));
        order.setActualDate(Optional.ofNullable(orderInfo.getActualDate())
                .map(NLS::long2LocalDateTime)
                .orElseGet(CURRENT_LOCALDATETIME));
        order.setOrderNote(orderInfo.getOrderNote());
        order.setOrderName(orderInfo.getOrderName());
        order.setAppPackage(getPackagesService().findPackage(orderInfo.getAppPackageId()));
        order.setOrderStatus(getRefsService().findOrderStatus(orderInfo.getOrderStatusId()));
        order.setExecFinishDate(NLS.long2LocalDateTime(orderInfo.getExecFinishDate()));
        order.setExecStartDate(NLS.long2LocalDateTime(orderInfo.getExecStartDate()));
        order.setOrderedBatchesAmount(orderInfo.getOrderBatchesAmount());
        order.setExecLastDate(NLS.long2LocalDateTime(orderInfo.getExecLastDate()));
        order.setOrderNote(orderInfo.getOrderNote());
        order.setSuccessBatchesAmount(orderInfo.getSuccessBatchesAmount());
        order.setFailBatchesAmount(orderInfo.getFailBatchesAmount());
        return order;
    };

    final BiFunction<OrderInfo, RestOrdersService.OrdersHistBuilder, Order> assignOrderInfo = (orderInfo, ordersHistBuilder) -> {

        final Order order = Optional.ofNullable(orderInfo.getOrderId())
                .map(getOrdersService()::findOrder)
                .orElseGet(createNewOrder);

        // store history
        Optional.ofNullable(order.getOrderId()).ifPresent(borId -> ordersHistBuilder.buildOrdersHist(order));

        assignDto.apply(orderInfo, order);

        return order;
    };

    //==========================================================================
    @Transactional
    public CreatedOrderResponse createOrUpdateOrder(Mono<CreateOrderRequest> monoRequest) {

        return this.<CreatedOrder, CreatedOrderResponse>createAnswer(CreatedOrderResponse.class,
                (responseBody, createdOrder) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(orderInfoValidator.validateConditional(request.getEntityInfo(), orderInfo
                        -> {

                    final SimpleActionInfo simpleActionInfo = request.getEntityAction();

                    //log.info("simpleActionInfo =  {}", simpleActionInfo);

                    log.debug("create/update order: {}", orderInfo);

                    //StmtProcessor.assertNotNull(String.class, orderInfo.getPackageName(), "packageName name is not defined");

                    final Order order = findOrCreateOrder(orderInfo, orderHist -> getOrdersService().saveOrderHist(getOrdersService().createOrderHist(orderHist)));

                    final Boolean isNewSetting = StmtProcessor.isNull(order.getOrderId());

                    getOrdersService().saveOrder(order);

                    final String finalMessage = String.format("Order is %s (orderId=%d)",
                            isNewSetting ? "created" : "updated",
                            order.getOrderId());

                    createdOrder.setCreatedOrderId(order.getOrderId());
                    responseBody.complete();

                    log.debug(finalMessage);

                    responseBody.setCode(OC_OK);
                    responseBody.setMessage(finalMessage);
                }, errorInfos -> {
                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                }))));
    }

    public Order findOrCreateOrder(OrderInfo orderInfo, RestOrdersService.OrdersHistBuilder ordersHistBuilder) {
        return assignOrderInfo.apply(orderInfo, ordersHistBuilder);
    }
}
