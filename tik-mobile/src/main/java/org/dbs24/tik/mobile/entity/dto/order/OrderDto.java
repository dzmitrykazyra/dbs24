package org.dbs24.tik.mobile.entity.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Integer orderTypeId;
    private String tiktokUri;
    private Integer actionsAmount;
    private Integer orderDuration;
    private String orderName;
}
