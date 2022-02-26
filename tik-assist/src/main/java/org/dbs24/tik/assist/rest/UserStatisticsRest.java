package org.dbs24.tik.assist.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.dbs24.tik.assist.constant.RequestQueryParam;
import org.dbs24.tik.assist.entity.dto.order.OrderPagesQuantityDto;
import org.dbs24.tik.assist.entity.dto.order.UserAccountOrderDetailsDto;
import org.dbs24.tik.assist.entity.dto.order.UserVideoOrderDetailsDto;
import org.dbs24.tik.assist.entity.dto.statistics.OrderStatisticsDto;
import org.dbs24.tik.assist.entity.dto.statistics.OrderStatisticsDtoList;
import org.dbs24.tik.assist.service.hierarchy.OrderExecutionProgressService;
import org.dbs24.tik.assist.service.hierarchy.UserOrderService;
import org.dbs24.tik.assist.service.security.AuthenticationService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class UserStatisticsRest extends ReactiveRestProcessor {

    private final UserOrderService userOrderService;
    private final AuthenticationService authenticationService;
    private final OrderExecutionProgressService orderExecutionProgressService;

    public UserStatisticsRest(UserOrderService userOrderService, AuthenticationService authenticationService, OrderExecutionProgressService orderExecutionProgressService) {

        this.userOrderService = userOrderService;
        this.authenticationService = authenticationService;
        this.orderExecutionProgressService = orderExecutionProgressService;
    }

    public Mono<ServerResponse> getActiveOrderProgresses(ServerRequest serverRequest) {

        return this.<OrderStatisticsDtoList>createResponse(
                serverRequest,
                OrderStatisticsDtoList.class,
                () -> orderExecutionProgressService.getActiveOrderProgressesByTiktokUsernameAndUserId(
                        serverRequest.queryParam(RequestQueryParam.QP_TIKTOK_USERNAME).orElse(""),
                        authenticationService.extractUserIdFromServerRequest(serverRequest)
                ));
    }

    public Mono<ServerResponse> getOrdersHistory(ServerRequest serverRequest) {

        return this.<OrderStatisticsDtoList>createResponse(
                serverRequest,
                OrderStatisticsDtoList.class,
                () -> orderExecutionProgressService.getOrdersHistoryByTiktokUsernameAndUserId(
                        serverRequest.queryParam(RequestQueryParam.QP_TIKTOK_USERNAME).orElse(""),
                        Integer.valueOf(serverRequest.queryParam(RequestQueryParam.QP_PAGE_NUMBER).orElse("0")),
                        authenticationService.extractUserIdFromServerRequest(serverRequest)
                ));
    }

    public Mono<ServerResponse> clearOrdersHistory(ServerRequest serverRequest) {

        return this.<OrderStatisticsDtoList>createResponse(
                serverRequest,
                OrderStatisticsDtoList.class,
                () -> orderExecutionProgressService.clearOrdersHistoryByTiktokUsernameAndUserId(
                        serverRequest.queryParam(RequestQueryParam.QP_TIKTOK_USERNAME).orElse(""),
                        authenticationService.extractUserIdFromServerRequest(serverRequest)
                ));
    }

    public Mono<ServerResponse> getVideoOrderDetails(ServerRequest serverRequest) {

        return this.<UserVideoOrderDetailsDto>createResponse(
                serverRequest,
                UserVideoOrderDetailsDto.class,
                () -> userOrderService.getVideoUserOrderDetails(
                        Integer.valueOf(serverRequest.queryParam(RequestQueryParam.QP_ORDER_ID).orElse("-1"))
                ));
    }

    public Mono<ServerResponse> getAccountOrderDetails(ServerRequest serverRequest) {

        return this.<UserAccountOrderDetailsDto>createResponse(
                serverRequest,
                UserAccountOrderDetailsDto.class,
                () -> userOrderService.getAccountUserOrderDetails(
                        Integer.valueOf(serverRequest.queryParam(RequestQueryParam.QP_ORDER_ID).orElse("-1"))
                ));
    }

    public Mono<ServerResponse> clearOrderHistoryById(ServerRequest serverRequest) {

        return this.<OrderStatisticsDto>createResponse(
                serverRequest,
                OrderStatisticsDto.class,
                () -> orderExecutionProgressService.clearOrderHistoryById(
                        Integer.valueOf(serverRequest.queryParam(RequestQueryParam.QP_ORDER_ID).orElse("-1"))
                ));
    }

    public Mono<ServerResponse> getOrdersHistoryPagesQuantity(ServerRequest serverRequest) {

        return this.<OrderPagesQuantityDto>createResponse(
                serverRequest,
                OrderPagesQuantityDto.class,
                () -> orderExecutionProgressService.getOrdersHistoryPagesQuantityByTiktokUsername(
                        serverRequest.queryParam(RequestQueryParam.QP_TIKTOK_USERNAME).orElse(""),
                        authenticationService.extractUserIdFromServerRequest(serverRequest)
                ));
    }
}
