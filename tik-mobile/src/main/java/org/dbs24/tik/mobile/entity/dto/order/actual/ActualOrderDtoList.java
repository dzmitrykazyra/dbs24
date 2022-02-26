package org.dbs24.tik.mobile.entity.dto.order.actual;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;

import java.util.List;

@Data
public class ActualOrderDtoList {
    private List<ActualOrderDto> actualOrders;

    public static ActualOrderDtoList of(List<ActualOrderDto> actualOrders) {
        return StmtProcessor.create(ActualOrderDtoList.class,
                actualOrderList -> actualOrderList.setActualOrders(actualOrders)
        );
    }
}
