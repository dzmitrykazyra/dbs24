/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.dao;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.entity.Order;
import org.dbs24.app.promo.entity.OrderHist;
import org.dbs24.app.promo.repo.OrderHistRepo;
import org.dbs24.app.promo.repo.OrderRepo;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.spring.core.data.PageLoader;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.Collection;
import java.util.Optional;

import static org.dbs24.app.promo.consts.AppPromoutionConsts.Caches.CACHE_ORDER;
import static org.dbs24.app.promo.consts.AppPromoutionConsts.OrderStatusEnum.OS_CREATED;

@Getter
@Log4j2
@Component
public class OrderDao extends DaoAbstractApplicationService {

    final OrderRepo orderRepo;
    final OrderHistRepo orderHistRepo;

    public OrderDao(OrderRepo orderRepo, OrderHistRepo orderHistRepo) {
        this.orderRepo = orderRepo;
        this.orderHistRepo = orderHistRepo;
    }

    //==========================================================================
    public Optional<Order> findOptionalOrder(Integer orderId) {
        return orderRepo.findById(orderId);
    }

    @Cacheable(CACHE_ORDER)
    public Order findOrder(Integer orderId) {
        log.warn("load from db: orderId: {}".toUpperCase(), orderId);
        return findOptionalOrder(orderId).orElseThrow();
    }

    public void saveOrderHist(OrderHist orderHist) {
        orderHistRepo.save(orderHist);
    }

    @CacheEvict(value = {CACHE_ORDER}, beforeInvocation = true, key = "#order.orderId", condition = "#order.orderId>0")
    public void saveOrder(Order order) {
        log.warn("reset cache: orderId: {}".toUpperCase(), order.getOrderId());
        orderRepo.save(order);
    }
    //==================================================================================================================

    public Collection<Order> loadActualOrders() {

        final Collection<Order> orders = ServiceFuncs.createConcurencyCollection();

        (new PageLoader<Order>() {
            @Override
            public void addPage(Collection<Order> pageOrders) {
                log.debug("add actual orders: {} records(s)", pageOrders.size());
                orders.addAll(pageOrders);
            }
        }).loadRecords(orderRepo, () -> (r, cq, cb) -> {

            final Predicate predicate = cb.conjunction();

            predicate.getExpressions().add(cb.equal(r.get("orderStatus"), OS_CREATED.getCode()));

            return predicate;

        });

        return orders;
    }

    public Collection<Order> findInvalidOrders() {
        return getOrderRepo().findInvalidOrders();
    }
}
