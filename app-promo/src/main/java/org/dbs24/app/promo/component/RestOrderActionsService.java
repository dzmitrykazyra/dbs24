/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.component;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.entity.OrderAction;
import org.dbs24.app.promo.rest.dto.action.CreateOrderActionRequest;
import org.dbs24.app.promo.rest.dto.action.CreatedOrderAction;
import org.dbs24.app.promo.rest.dto.action.CreatedOrderActionResponse;
import org.dbs24.app.promo.rest.dto.action.OrderActionInfo;
import org.dbs24.app.promo.rest.dto.action.validator.OrderActionInfoValidator;
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

import static org.dbs24.app.promo.component.OrdersActionsService.createNewOrderAction;
import static org.dbs24.consts.SysConst.CURRENT_LOCALDATETIME;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_INVALID_ENTITY_ATTRS;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;

@Getter
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "app-promo")
public class RestOrderActionsService extends AbstractRestApplicationService {

    final RefsService refsService;
    final OrdersService ordersService;
    final OrdersActionsService ordersActionsService;
    final BotsService botsService;
    final BatchSetupService batchSetupService;
    final OrderActionInfoValidator orderActionInfoValidator;

    public RestOrderActionsService(RefsService refsService, OrderActionInfoValidator orderActionInfoValidator, OrdersService ordersService, BotsService botsService, BatchSetupService batchSetupService, OrdersActionsService ordersActionsService) {
        this.refsService = refsService;
        this.orderActionInfoValidator = orderActionInfoValidator;
        this.ordersService = ordersService;
        this.botsService = botsService;
        this.batchSetupService = batchSetupService;
            this.ordersActionsService = ordersActionsService;
    }

    @FunctionalInterface
    interface OrderActionsHistBuilder {
        void buildOrderActionsHist(OrderAction orderAction);
    }

    final BiFunction<OrderActionInfo, OrderAction, OrderAction> assignDto = (orderActionInfo, orderAction) -> {

        orderAction.setActualDate(Optional.ofNullable(orderActionInfo.getActualDate()).map(NLS::long2LocalDateTime).orElseGet(CURRENT_LOCALDATETIME));
        orderAction.setOrder(getOrdersService().findOrder(orderActionInfo.getOrderId()));
        orderAction.setBatchSetup(getBatchSetupService().findBatchSetup(orderActionInfo.getBatchSetupId()));
        orderAction.setBot(getBotsService().findBot(orderActionInfo.getBotId()));
        orderAction.setAction(getRefsService().findAction(orderActionInfo.getActRefId()));
        orderAction.setActionResult(getRefsService().findActionResult(orderActionInfo.getActionResultId()));
        orderAction.setActionStartDate(NLS.long2LocalDateTime(orderActionInfo.getActionStartDate()));
        orderAction.setActionFinishDate(NLS.long2LocalDateTime(orderActionInfo.getActionFinishDate()));
        orderAction.setErrMsg(orderActionInfo.getErrMsg());
        orderAction.setExecutionOrder(orderActionInfo.getExecutionOrder());
        orderAction.setUsedIp(orderActionInfo.getUsedIp());

        return orderAction;
    };

    final BiFunction<OrderActionInfo, RestOrderActionsService.OrderActionsHistBuilder, OrderAction> assignOrderActionsInfo = (orderActionInfo, orderActionsHistBuilder) -> {

        final OrderAction orderAction = Optional.ofNullable(orderActionInfo.getActionId())
                .map(getOrdersActionsService()::findOrderAction)
                .orElseGet(createNewOrderAction);

        // store history
        Optional.ofNullable(orderAction.getActionId()).ifPresent(borId -> orderActionsHistBuilder.buildOrderActionsHist(orderAction));

        assignDto.apply(orderActionInfo, orderAction);

        return orderAction;
    };

    //==========================================================================
    @Transactional
    public CreatedOrderActionResponse createOrUpdateOrderAction(Mono<CreateOrderActionRequest> monoRequest) {

        return this.<CreatedOrderAction, CreatedOrderActionResponse>createAnswer(CreatedOrderActionResponse.class,
                (responseBody, createdOrderAction) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(orderActionInfoValidator.validateConditional(request.getEntityInfo(), orderActionInfo
                        -> {

                    final SimpleActionInfo simpleActionInfo = request.getEntityAction();

                    log.debug("create/update orderAction: {}", orderActionInfo);

                    //StmtProcessor.assertNotNull(String.class, orderActionInfo.getPackageName(), "packageName name is not defined");

                    final OrderAction orderAction = findOrCreateOrderActions(orderActionInfo, orderActionHist -> getOrdersActionsService().saveOrderActionHist(getOrdersActionsService().createOrderActionHist(orderActionHist)));

                    final Boolean isNewSetting = StmtProcessor.isNull(orderAction.getActionId());

                    getOrdersActionsService().saveOrderAction(orderAction);

                    final String finalMessage = String.format("OrderAction is %s (OrderActionId=%d)",
                            isNewSetting ? "created" : "updated",
                            orderAction.getActionId());

                    createdOrderAction.setCreatedActionId(orderAction.getActionId());
                    responseBody.complete();

                    log.debug(finalMessage);

                    responseBody.setCode(OC_OK);
                    responseBody.setMessage(finalMessage);
                }, errorInfos -> {
                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                }))));
    }

    public OrderAction findOrCreateOrderActions(OrderActionInfo orderActionInfo, RestOrderActionsService.OrderActionsHistBuilder orderActionsHistBuilder) {
        return assignOrderActionsInfo.apply(orderActionInfo, orderActionsHistBuilder);
    }

}
