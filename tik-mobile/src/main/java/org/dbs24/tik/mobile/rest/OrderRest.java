package org.dbs24.tik.mobile.rest;

import org.dbs24.tik.mobile.constant.RequestQueryParam;
import org.dbs24.tik.mobile.entity.dto.action.OrderActionDto;
import org.dbs24.tik.mobile.entity.dto.action.OrderActionTypeDtoList;
import org.dbs24.tik.mobile.entity.dto.order.actual.ActualOrderDtoList;
import org.dbs24.tik.mobile.entity.dto.order.OrderDto;
import org.dbs24.tik.mobile.entity.dto.order.OrderIdDto;
import org.dbs24.tik.mobile.service.OrderService;
import org.dbs24.tik.mobile.service.TokenHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class OrderRest {

    private final OrderService orderService;
    private final TokenHolder tokenHolder;

    @Autowired
    public OrderRest(OrderService orderService, TokenHolder tokenHolder) {
        this.orderService = orderService;
        this.tokenHolder = tokenHolder;
    }

    @ResponseStatus
    public Mono<ServerResponse> getAllAvailableOrders(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        orderService.getActualOrders(tokenHolder.extractUserIdFromServerRequest(request),
                                request.queryParam(RequestQueryParam.QP_ACTION_TYPE_ID)),
                        ActualOrderDtoList.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> completeOrderAction(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        orderService.completeOrderAction(
                                request.bodyToMono(OrderActionDto.class),
                                tokenHolder.extractUserIdFromServerRequest(request)
                        ),
                        OrderIdDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> createOrder(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        orderService.createOrder(
                                tokenHolder.extractUserIdFromServerRequest(request),
                                request.bodyToMono(OrderDto.class)),
                        OrderIdDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> skipOrder(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        orderService.skipOrder(request.bodyToMono(OrderIdDto.class)),
                        OrderIdDto.class
                );
    }

    @ResponseStatus
    public Mono<ServerResponse> getAllActionType(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        orderService.getAllOrderActionTypes(),
                        OrderActionTypeDtoList.class
                );
    }

}