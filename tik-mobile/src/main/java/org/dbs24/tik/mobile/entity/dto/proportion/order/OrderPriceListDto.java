package org.dbs24.tik.mobile.entity.dto.proportion.order;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.domain.OrderPrice;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderPriceListDto {

    private List<OrderPriceDto> orderCostDtoList;

    public static OrderPriceListDto of(List<OrderPrice> orderPriceList) {

        return StmtProcessor.create(
                OrderPriceListDto.class,
                orderCostListDto -> orderCostListDto.setOrderCostDtoList(
                        orderPriceList.stream()
                                .map(OrderPriceDto::of)
                                .collect(Collectors.toList())
                )
        );
    }
}
