package org.dbs24.tik.mobile.entity.dto.action;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.tik.mobile.entity.domain.OrderActionType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderActionTypeDto {
    private Integer actionTypeId;
    private String actionTypeName;

    public static OrderActionTypeDto of(OrderActionType orderActionType) {
        return OrderActionTypeDto.builder()
                .actionTypeId(orderActionType.getOrderActionTypeId())
                .actionTypeName(orderActionType.getOrderActionTypeName())
                .build();
    }
}
