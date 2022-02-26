package org.dbs24.tik.assist.service.hierarchy;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.dao.UserOrderDao;
import org.dbs24.tik.assist.dao.OrderExecutionProgressDao;
import org.dbs24.tik.assist.dao.UserDao;
import org.dbs24.tik.assist.entity.dto.order.OrderPagesQuantityDto;
import org.dbs24.tik.assist.entity.dto.statistics.OrderStatisticsDto;
import org.dbs24.tik.assist.entity.dto.statistics.OrderStatisticsDtoList;
import org.dbs24.tik.assist.service.tiktok.resolver.TiktokInteractor;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class OrderExecutionProgressService {

    private final TiktokInteractor tiktokInteractor;

    private final UserDao userDao;
    private final UserOrderDao userOrderDao;
    private final OrderExecutionProgressDao orderExecutionProgressDao;

    public OrderExecutionProgressService(TiktokInteractor tiktokInteractor, UserOrderDao userOrderDao, UserDao userDao, OrderExecutionProgressDao orderExecutionProgressDao) {

        this.tiktokInteractor = tiktokInteractor;
        this.userOrderDao = userOrderDao;
        this.userDao = userDao;
        this.orderExecutionProgressDao = orderExecutionProgressDao;
    }

    public OrderStatisticsDtoList getActiveOrderProgressesByTiktokUsernameAndUserId(String tiktokUsername, Integer userId) {

        return OrderStatisticsDtoList
                .orderProgressesToDto(
                        orderExecutionProgressDao.findActiveOrdersProgressesByTiktokAccount(
                                tiktokInteractor.getTiktokAccount(tiktokUsername, userDao.findUserById(userId))
                        )
                );
    }

    public OrderStatisticsDtoList getOrdersHistoryByTiktokUsernameAndUserId(String tiktokUsername, Integer pageNumber, Integer userId) {

        return OrderStatisticsDtoList
                .finishedOrdersToDto(
                        userOrderDao.findClosedOrdersByPageAndTiktokAccount(
                                pageNumber,
                                tiktokInteractor.getTiktokAccount(tiktokUsername, userDao.findUserById(userId))
                        )
                );
    }

    public OrderStatisticsDtoList clearOrdersHistoryByTiktokUsernameAndUserId(String tiktokUsername, Integer userId) {

        return OrderStatisticsDtoList
                .finishedOrdersToDto(
                        userOrderDao.changeClosedOrdersStatusToHistoryClearByTiktokAccount(
                                tiktokInteractor.getTiktokAccount(tiktokUsername, userDao.findUserById(userId))
                        )
                );
    }

    public OrderStatisticsDto clearOrderHistoryById(Integer orderId) {

        return OrderStatisticsDto
                .fromFinishedOrderToDto(
                        userOrderDao.changeUserOrderStatusToHistoryClearById(orderId)
                );
    }

    public OrderPagesQuantityDto getOrdersHistoryPagesQuantityByTiktokUsername(String tiktokUsername, Integer userId) {

        return OrderPagesQuantityDto.of(
                userOrderDao.findClosedOrdersPagesQuantityByTiktokAccount(
                        tiktokInteractor.getTiktokAccount(tiktokUsername, userDao.findUserById(userId))
                )
        );
    }
}
