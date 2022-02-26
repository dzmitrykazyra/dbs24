package org.dbs24.tik.assist.entity.dto.statistics;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.OrderExecutionProgress;
import org.dbs24.tik.assist.entity.domain.UserOrder;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderStatisticsDtoList {

    private List<OrderStatisticsDto> orderStatisticsDtoList;

    public static OrderStatisticsDtoList finishedOrdersToDto(List<UserOrder> userOrders) {

        return StmtProcessor.create(
                OrderStatisticsDtoList.class,
                orderStatisticsDtoList -> orderStatisticsDtoList.setOrderStatisticsDtoList(
                        userOrders
                                .stream()
                                .map(OrderStatisticsDto::fromFinishedOrderToDto)
                                .collect(Collectors.toList())
                )
        );
    }

    public static OrderStatisticsDtoList orderProgressesToDto(List<OrderExecutionProgress> executionProgresses) {

        return StmtProcessor.create(
                OrderStatisticsDtoList.class,
                orderStatisticsDtoList -> orderStatisticsDtoList.setOrderStatisticsDtoList(
                        executionProgresses
                                .stream()
                                .map(OrderStatisticsDto::fromProgressToDto)
                                .collect(Collectors.toList())
                )
        );
    }
}
