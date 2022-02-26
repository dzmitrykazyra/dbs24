package org.dbs24.tik.mobile.entity.dto.order.statistic;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class StatisticOrderHistDtoList {
    private List<StatisticOrderHistDto> statisticOrderHistDtoList;

    public static StatisticOrderHistDtoList finishedOrdersToDto(List<Order> orders) {
        return StmtProcessor.create(StatisticOrderHistDtoList.class,
                statisticOrderHistDtoList -> statisticOrderHistDtoList.setStatisticOrderHistDtoList(
                        orders
                                .stream()
                                .map(StatisticOrderHistDto::of)
                                .collect(Collectors.toList())
                )
        );
    }
}
