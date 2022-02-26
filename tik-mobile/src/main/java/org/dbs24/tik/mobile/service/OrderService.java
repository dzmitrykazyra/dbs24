package org.dbs24.tik.mobile.service;

import org.dbs24.tik.mobile.entity.dto.action.ExecutedActionResponseDto;
import org.dbs24.tik.mobile.entity.dto.action.OrderActionDto;
import org.dbs24.tik.mobile.entity.dto.action.OrderActionTypeDtoList;
import org.dbs24.tik.mobile.entity.dto.order.actual.ActualOrderDtoList;
import org.dbs24.tik.mobile.entity.dto.order.OrderDto;
import org.dbs24.tik.mobile.entity.dto.order.OrderIdDto;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface OrderService {

    Mono<ActualOrderDtoList> getActualOrders(Integer userId, Optional<String> actionTypeId);

    Mono<ExecutedActionResponseDto> completeOrderAction(Mono<OrderActionDto> orderActionDtoMono, Integer userId);

    Mono<OrderIdDto> createOrder(Integer userId, Mono<OrderDto> orderDtoMono);

    Mono<OrderIdDto> skipOrder(Mono<OrderIdDto> orderIdDtoMono);

    Mono<OrderActionTypeDtoList> getAllOrderActionTypes();

    void removeExpiredOrders();
}
