package org.dbs24.tik.mobile.rest;

import org.dbs24.tik.mobile.constant.RequestQueryParam;
import org.dbs24.tik.mobile.entity.dto.order.OrderIdDto;
import org.dbs24.tik.mobile.entity.dto.order.details.OrderDetailsDto;
import org.dbs24.tik.mobile.entity.dto.order.details.OrderDetailsDtoList;
import org.dbs24.tik.mobile.entity.dto.order.statistic.StatisticOrderHistDtoList;
import org.dbs24.tik.mobile.service.OrderStatisticService;
import org.dbs24.tik.mobile.service.TokenHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class OrderStatisticRest {

    private final OrderStatisticService orderStatisticService;
    private final TokenHolder tokenHolder;

    @Autowired
    public OrderStatisticRest(OrderStatisticService orderStatisticService, TokenHolder tokenHolder) {
        this.orderStatisticService = orderStatisticService;
        this.tokenHolder = tokenHolder;
    }

    public Mono<ServerResponse> getOderInfo(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        orderStatisticService.getOrderInfoById(Integer.valueOf(request.queryParam(RequestQueryParam.QP_ORDER_ID).get())),
                        OrderDetailsDto.class
                );
    }

    public Mono<ServerResponse> getActiveOrdersByUser(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        orderStatisticService.getAllActiveOrdersByUser(
                                tokenHolder.extractUserIdFromServerRequest(request),
                                request.queryParam(RequestQueryParam.QP_ACTION_TYPE_ID)
                        ),
                        OrderDetailsDtoList.class
                );
    }

    public Mono<ServerResponse> getOrdersHistory(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        orderStatisticService.getOrdersHistory(
                                tokenHolder.extractUserIdFromServerRequest(request),
                                Integer.valueOf(request.queryParam(RequestQueryParam.QP_PAGE_NUM).get()),
                                request.queryParam(RequestQueryParam.QP_ACTION_TYPE_ID)),
                        StatisticOrderHistDtoList.class
                );
    }

    public Mono<ServerResponse> clearOrderHistory(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        orderStatisticService.clearOrderHistory(Integer.valueOf(request.queryParam(RequestQueryParam.QP_ORDER_ID).get())),
                        OrderIdDto.class
                );
    }

    public Mono<ServerResponse> invalidateUserOrder(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        orderStatisticService.invalidateOrderById(Integer.valueOf(request.queryParam(RequestQueryParam.QP_ORDER_ID).get())),
                        OrderIdDto.class
                );
    }
}
