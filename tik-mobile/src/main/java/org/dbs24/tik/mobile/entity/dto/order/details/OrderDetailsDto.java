package org.dbs24.tik.mobile.entity.dto.order.details;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.tik.mobile.entity.domain.Order;
import org.dbs24.tik.mobile.entity.domain.OrderExecutionProgress;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class OrderDetailsDto {
    private String orderName;
    private String orderStatusName;
    private long startDate;
    private long finishDate;
    private Integer doneActionsAmount;
    private Integer actionsAmount;
    private Integer cost;
    private String tiktokUri;
    private Integer actionTypeId;

    public static OrderDetailsDto of(Order order, OrderExecutionProgress orderExecutionProgress) {
        return OrderDetailsDto.builder()
                .withOrderName(order.getOrderName())
                .withOrderStatusName(order.getOrderStatus().getOrderStatusName())
                .withTiktokUri(order.getTiktokUri())
                .withCost(order.getOrderSum())
                .withActionsAmount(order.getActionsAmount())
                .withStartDate(getTimestampFromLocalDateTime(order.getStartDate()))
                .withFinishDate(getTimestampFromLocalDateTime(order.getEndDate()))
                .withDoneActionsAmount(orderExecutionProgress.getDoneActionsQuantity())
                .withActionTypeId(order.getOrderActionType().getOrderActionTypeId())
                .build();
    }

    private static long getTimestampFromLocalDateTime(LocalDateTime localDateTime){
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        return timestamp.getTime();
    }
}
