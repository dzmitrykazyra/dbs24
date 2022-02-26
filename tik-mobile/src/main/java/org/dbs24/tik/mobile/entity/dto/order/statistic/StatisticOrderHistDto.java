package org.dbs24.tik.mobile.entity.dto.order.statistic;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.tik.mobile.entity.domain.Order;

import java.time.LocalDate;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class StatisticOrderHistDto {
    private Integer orderId;
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate startDate;
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate finishDate;
    private Integer actionsAmount;
    private Integer totalCost;

    public static StatisticOrderHistDto of(Order order) {
        return StatisticOrderHistDto.builder()
                .withOrderId(order.getOrderId())
                .withActionsAmount(order.getActionsAmount())
                .withStartDate(order.getStartDate().toLocalDate())
                .withFinishDate(order.getEndDate().toLocalDate())
                .withActionsAmount(order.getActionsAmount())
                .withTotalCost(order.getOrderSum())
                .build();
    }


}
