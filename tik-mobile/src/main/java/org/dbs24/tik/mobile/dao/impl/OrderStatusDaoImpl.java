package org.dbs24.tik.mobile.dao.impl;

import org.dbs24.tik.mobile.constant.CacheKey;
import org.dbs24.tik.mobile.constant.reference.OrderStatusDefine;
import org.dbs24.tik.mobile.dao.OrderStatusDao;
import org.dbs24.tik.mobile.entity.domain.OrderStatus;
import org.dbs24.tik.mobile.repo.OrderStatusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusDaoImpl implements OrderStatusDao {

    private final OrderStatusRepo orderStatusRepo;

    @Autowired
    public OrderStatusDaoImpl(OrderStatusRepo orderStatusRepo) {

        this.orderStatusRepo = orderStatusRepo;
    }

    @Override
    @Cacheable(CacheKey.CACHE_IN_PROGRESS_ORDER)
    public OrderStatus findInProgressOrderStatus() {
        return orderStatusRepo
                .findByOrderStatusName(OrderStatusDefine.OS_IN_PROGRESS.getOrderStatusName())
                .orElseThrow(() -> new RuntimeException("Cannot find in progress order status"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_FINISHED_ORDER)
    public OrderStatus findFinishedOrderStatus() {
        return orderStatusRepo
                .findByOrderStatusName(OrderStatusDefine.OS_FINISHED.getOrderStatusName())
                .orElseThrow(() -> new RuntimeException("Cannot find finished order status"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_HISTORY_CLEARED_ORDER)
    public OrderStatus findHistoryClearedOrderStatus() {
        return orderStatusRepo
                .findByOrderStatusName(OrderStatusDefine.OS_HISTORY_CLEARED.getOrderStatusName())
                .orElseThrow(() -> new RuntimeException("Cannot find history cleared order status"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_INVALID_ORDER)
    public OrderStatus findInvalidOrderStatus() {
        return orderStatusRepo
                .findByOrderStatusName(OrderStatusDefine.OS_INVALID.getOrderStatusName())
                .orElseThrow(() -> new RuntimeException("Cannot find invalid order status"));
    }

    @Override
    @Cacheable(CacheKey.CACHE_ORDER_STATUS_BY_ID)
    public OrderStatus findOrderStatusById(Integer orderStatusId) {
        return orderStatusRepo.findByOrderStatusId(orderStatusId)
                .orElseThrow(() -> new RuntimeException("Cannot find order status with id = " + orderStatusId));
    }
}
