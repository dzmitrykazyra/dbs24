package org.dbs24.tik.assist.constant.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.OrderStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum OrderStatusDefine {

    OS_ACTUAL(1, "Actual"),
    OS_CLOSED(2, "Closed"),
    OS_ROLLOVER(3, "Rollover"),
    OS_CANCELLED(4, "Cancelled"),
    OS_TRIAL(5, "Trial"),
    OS_HISTORY_CLEARED(6, "History cleared");

    private final Integer id;
    private final String orderStatusValue;

    public static List<OrderStatus> getAll() {
        return Arrays.stream(OrderStatusDefine.values()).map(
                orderStatusEnum -> StmtProcessor.create(
                        OrderStatus.class,
                        orderStatus -> {
                            orderStatus.setOrderStatusId(orderStatusEnum.getId());
                            orderStatus.setOrderStatusName(orderStatusEnum.getOrderStatusValue());
                        }
                )
        ).collect(Collectors.toList());
    }
}

