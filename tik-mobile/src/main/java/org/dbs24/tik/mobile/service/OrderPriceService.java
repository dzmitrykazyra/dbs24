package org.dbs24.tik.mobile.service;

import org.dbs24.tik.mobile.entity.domain.OrderActionType;
import org.dbs24.tik.mobile.entity.dto.proportion.order.OrderPriceDto;
import org.dbs24.tik.mobile.entity.dto.proportion.order.OrderPriceListDto;
import reactor.core.publisher.Mono;

public interface OrderPriceService {

    Mono<OrderPriceDto> create(Mono<OrderPriceDto> heartPriceToCreate);

    Mono<OrderPriceDto> getOrderPriceDtoByActionsQuantityAndActionTypeId(Integer actionsQuantity, Integer actionTypeId);

    Integer getOrderCostByActionsAmountAndActionType(Integer actionsQuantity, OrderActionType orderActionType);

    Integer getActionCost(OrderActionType orderActionType);

    Mono<OrderPriceListDto> getOrderPrices();
}
