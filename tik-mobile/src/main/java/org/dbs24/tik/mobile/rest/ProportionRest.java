package org.dbs24.tik.mobile.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.mobile.constant.RequestQueryParam;
import org.dbs24.tik.mobile.entity.dto.proportion.heart.HeartPriceDto;
import org.dbs24.tik.mobile.entity.dto.proportion.heart.HeartPriceListDto;
import org.dbs24.tik.mobile.entity.dto.proportion.order.OrderPriceDto;
import org.dbs24.tik.mobile.service.HeartPriceService;
import org.dbs24.tik.mobile.service.OrderPriceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class ProportionRest {

    private final HeartPriceService heartPriceService;
    private final OrderPriceService orderPriceService;

    public ProportionRest(HeartPriceService heartPriceService, OrderPriceService orderPriceService) {

        this.heartPriceService = heartPriceService;
        this.orderPriceService = orderPriceService;
    }

    public Mono<ServerResponse> createHeartPriceRecord(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        heartPriceService.create(request.bodyToMono(HeartPriceDto.class)),
                        HeartPriceDto.class
                );
    }

    public Mono<ServerResponse> getHeartPriceByHeartsAmount(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        heartPriceService.getHeartPriceDtoByHeartsAmount(Integer.valueOf(request.queryParam(RequestQueryParam.QP_HEARTS_AMOUNT).get())),
                        HeartPriceDto.class
                );
    }

    public Mono<ServerResponse> getAllHeartPrices(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        heartPriceService.getHeartPrices(),
                        HeartPriceListDto.class
                );
    }

    public Mono<ServerResponse> createOrderRecord(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        orderPriceService.create(request.bodyToMono(OrderPriceDto.class)),
                        OrderPriceDto.class
                );
    }

    public Mono<ServerResponse> getAllOrderPrices(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        orderPriceService.getOrderPrices(),
                        HeartPriceDto.class
                );
    }

    public Mono<ServerResponse> getOrderPrice(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        orderPriceService.getOrderPriceDtoByActionsQuantityAndActionTypeId(
                                Integer.valueOf(request.queryParam(RequestQueryParam.QP_ACTIONS_AMOUNT).get()),
                                Integer.valueOf(request.queryParam(RequestQueryParam.QP_ACTION_TYPE_ID).get())
                        ),
                        HeartPriceListDto.class
                );
    }
}
