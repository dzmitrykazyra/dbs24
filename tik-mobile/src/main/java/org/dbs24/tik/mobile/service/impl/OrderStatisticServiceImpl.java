package org.dbs24.tik.mobile.service.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.dao.OrderDao;
import org.dbs24.tik.mobile.dao.OrderExecutionProgressDao;
import org.dbs24.tik.mobile.dao.UserDao;
import org.dbs24.tik.mobile.entity.domain.Order;
import org.dbs24.tik.mobile.entity.domain.OrderExecutionProgress;
import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.dto.order.OrderIdDto;
import org.dbs24.tik.mobile.entity.dto.order.details.OrderDetailsDto;
import org.dbs24.tik.mobile.entity.dto.order.details.OrderDetailsDtoList;
import org.dbs24.tik.mobile.entity.dto.order.statistic.StatisticOrderHistDtoList;
import org.dbs24.tik.mobile.service.OrderStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Log4j2
public class OrderStatisticServiceImpl implements OrderStatisticService {

    private final OrderDao orderDao;
    private final UserDao userDao;
    private final OrderExecutionProgressDao orderExecutionProgressDao;

    @Autowired
    public OrderStatisticServiceImpl(OrderDao orderDao, UserDao userDao, OrderExecutionProgressDao orderExecutionProgressDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.orderExecutionProgressDao = orderExecutionProgressDao;
    }

    @Override
    @Transactional
    public Mono<OrderDetailsDto> getOrderInfoById(Integer orderId) {
        Order order = orderDao.findOrderById(orderId);
        OrderExecutionProgress orderProgress = orderExecutionProgressDao.findExecutionProgressByOrder(order);

        return Mono.just(OrderDetailsDto.of(order, orderProgress));
    }

    @Override
    @Transactional
    public Mono<OrderDetailsDtoList> getAllActiveOrdersByUser(Integer userId, Optional<String> actionTypeId) {
        User user = userDao.findById(userId);
        List<OrderDetailsDto> orderDetailsList = orderDao.findAllActiveOrdersByUser(user,
                        actionTypeId.map(Integer::valueOf)
                                .orElse(null))
                .stream()
                .map(order ->
                        OrderDetailsDto.of(order,
                                orderExecutionProgressDao.findExecutionProgressByOrder(order)))
                .collect(Collectors.toList());

        return Mono.just(
                StmtProcessor.create(
                        OrderDetailsDtoList.class,
                        orderDetailsDtoList -> orderDetailsDtoList.setOrderDetailsDtoList(orderDetailsList)
                )
        );
    }

    @Override
    @Transactional
    public Mono<StatisticOrderHistDtoList> getOrdersHistory(Integer userId, Integer pageNum, Optional<String> actionTypeId) {

        return Mono.just(
                StatisticOrderHistDtoList.finishedOrdersToDto(
                        orderDao.findAllCompleteOrdersByPage(
                                userDao.findById(userId),
                                pageNum,
                                actionTypeId.map(Integer::valueOf).orElse(null)
                        )
                )
        );
    }

    @Override
    public Mono<OrderIdDto> clearOrderHistory(Integer orderToClearHistoryId) {

        return Mono.just(
                OrderIdDto.of(
                        orderDao.updateOrderStatusToHistoryCleared(orderToClearHistoryId)
                )
        );
    }

    @Override
    public Mono<OrderIdDto> invalidateOrderById(Integer orderToInvalidateId) {

        return Mono.just(
                OrderIdDto.of(
                        orderDao.updateOrderStatusToInvalid(orderToInvalidateId)
                )
        );
    }
}