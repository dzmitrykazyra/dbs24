package org.dbs24.tik.mobile.constant.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.domain.OrderStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum OrderStatusDefine {
    OS_IN_PROGRESS(1, "OS.IN_PROGRESS"),
    OS_FINISHED(2, "OS.FINISHED"),
    OS_HISTORY_CLEARED(3, "OS.HISTORY_CLEARED"),
    OS_INVALID(4, "OS.INVALIID");

    private final Integer id;
    private final String orderStatusName;

    public static List<OrderStatus> getAll() {
        return Arrays.stream(OrderStatusDefine.values()).map(
                orderStatusEnum -> StmtProcessor.create(
                        OrderStatus.class,
                        orderStatus -> {
                            orderStatus.setOrderStatusId(orderStatusEnum.getId());
                            orderStatus.setOrderStatusName(orderStatusEnum.getOrderStatusName());
                        }
                )
        ).collect(Collectors.toList());
    }
}
