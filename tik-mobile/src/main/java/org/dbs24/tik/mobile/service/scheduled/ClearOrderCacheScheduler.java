package org.dbs24.tik.mobile.service.scheduled;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.mobile.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@EnableAsync
@Service
public class ClearOrderCacheScheduler {
    private final OrderService orderService;

    @Autowired
    public ClearOrderCacheScheduler(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(fixedDelayString = "${config.scheduling.expired-order.delay}")
    public void clearOrdersInCache() {
        orderService.removeExpiredOrders();
    }
}