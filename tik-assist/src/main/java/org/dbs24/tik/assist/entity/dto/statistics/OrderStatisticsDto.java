package org.dbs24.tik.assist.entity.dto.statistics;

import lombok.Data;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.OrderExecutionProgress;
import org.dbs24.tik.assist.entity.domain.UserOrder;

import java.math.BigDecimal;

@Data
public class OrderStatisticsDto {

    private Integer orderId;
    private Integer actionTypeId;
    private Integer actionsAmount;
    private Long beginDateMillis;
    private Long endDate;
    private BigDecimal totalCost;
    private Integer progressPercents;

    public static OrderStatisticsDto fromProgressToDto(OrderExecutionProgress orderExecutionProgress) {

        UserOrder userOrder = orderExecutionProgress.getUserOrder();

        OrderStatisticsDto orderStatisticsDto = defaultDtoFromOrder(userOrder);
        orderStatisticsDto.setProgressPercents(
                100 * orderExecutionProgress.getDoneActionsQuantity() / userOrder.getActionsAmount()
        );

        return orderStatisticsDto;
    }

    public static OrderStatisticsDto fromFinishedOrderToDto(UserOrder userOrder) {

        OrderStatisticsDto orderStatisticsDto = defaultDtoFromOrder(userOrder);
        orderStatisticsDto.setProgressPercents(100);

        return orderStatisticsDto;
    }

    /**
     * Method creates OrderStatisticsDto object with 0 progressPercents value
     */
    private static OrderStatisticsDto defaultDtoFromOrder(UserOrder userOrder) {

        return StmtProcessor.create(
                OrderStatisticsDto.class,
                orderStatisticsDto -> {
                    orderStatisticsDto.setOrderId(userOrder.getOrderId());
                    orderStatisticsDto.setActionTypeId(userOrder.getActionType().getActionTypeId());
                    orderStatisticsDto.setActionsAmount(userOrder.getActionsAmount());
                    orderStatisticsDto.setBeginDateMillis(NLS.localDateTime2long(userOrder.getBeginDate()));
                    orderStatisticsDto.setEndDate(NLS.localDateTime2long(userOrder.getEndDate()));
                    orderStatisticsDto.setTotalCost(userOrder.getOrderSum());
                    orderStatisticsDto.setProgressPercents(100);
                }
        );
    }
}
