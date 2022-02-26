package org.dbs24.tik.mobile.service.scheduled;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.mobile.constant.reference.ActionTypeDefine;
import org.dbs24.tik.mobile.dao.OrderDao;
import org.dbs24.tik.mobile.service.TiktokAccountService;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@EnableAsync
@Service
public class InvalidUserPostScheduler {

    private final TiktokAccountService tiktokAccountService;

    private final OrderDao orderDao;

    public InvalidUserPostScheduler(TiktokAccountService tiktokAccountService, OrderDao orderDao) {
        this.tiktokAccountService = tiktokAccountService;

        this.orderDao = orderDao;
    }

    @Transactional
    //@Scheduled(fixedRateString = "${config.scheduling.invalid-post.delay}")
    public void executeOrderTasks() {

        List<Integer> ordersToInvalidateIdList = orderDao.findAllActiveOrders().stream()
                .filter(
                        order -> !order.getOrderActionType().getOrderActionTypeId()
                                .equals(ActionTypeDefine.AT_FOLLOWERS.getOrderActionTypeId())
                ).collect(Collectors.toMap(
                                order -> order.getOrderId(),
                                order -> tiktokAccountService.searchSinglePostByLink(order.getTiktokUri())
                        )
                )
                .entrySet()
                .stream()
                .filter(
                        orderIdToPost -> orderIdToPost.getValue()
                                .toProcessor().block().getTiktokUserPostDto().getAwemeId() == null
                )
                .map(orderIdToPost -> orderIdToPost.getKey())
                .collect(Collectors.toList());

        orderDao.updateOrdersStatusesToInvalid(ordersToInvalidateIdList);
    }
}
