package org.dbs24.tik.mobile.entity.dto.order;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.domain.Order;


@Data
public class OrderIdDto {

    private Integer orderId;

    public static OrderIdDto of(Order order) {

        return StmtProcessor.create(
                OrderIdDto.class,
                orderIdDto -> orderIdDto.setOrderId(order.getOrderId())
        );
    }
}
