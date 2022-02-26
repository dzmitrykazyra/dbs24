package org.dbs24.tik.mobile.entity.dto.action;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.domain.OrderActionType;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderActionTypeDtoList {
    private List<OrderActionTypeDto> orderActionTypeDtoList;

    public static OrderActionTypeDtoList actionTypesToList(List<OrderActionType> actionTypes) {
        return StmtProcessor.create(OrderActionTypeDtoList.class,
                orderActionTypeDtoList -> {
                    orderActionTypeDtoList.setOrderActionTypeDtoList(
                            actionTypes.stream()
                                    .map(OrderActionTypeDto::of)
                                    .collect(Collectors.toList())
                    );
                });
    }
}
