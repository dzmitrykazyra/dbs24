package org.dbs24.tik.mobile.service;

import org.dbs24.tik.mobile.entity.dto.order.OrderIdDto;
import org.dbs24.tik.mobile.entity.dto.order.details.OrderDetailsDto;
import org.dbs24.tik.mobile.entity.dto.order.details.OrderDetailsDtoList;
import org.dbs24.tik.mobile.entity.dto.order.statistic.StatisticOrderHistDtoList;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface OrderStatisticService {
    Mono<OrderDetailsDto> getOrderInfoById(Integer orderId);

    Mono<OrderDetailsDtoList> getAllActiveOrdersByUser(Integer userId, Optional<String> actionTypeId);

    Mono<StatisticOrderHistDtoList> getOrdersHistory(Integer userId, Integer pageNum, Optional<String> actionTypeId);

    Mono<OrderIdDto> clearOrderHistory(Integer orderToClearHistoryId);

    Mono<OrderIdDto> invalidateOrderById(Integer orderToInvalidateId);
}