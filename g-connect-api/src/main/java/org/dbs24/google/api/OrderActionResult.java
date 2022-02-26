package org.dbs24.google.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.google.api.OrderActionsConsts.ActionResultEnum;
import org.dbs24.kafka.api.KafkaMessage;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OrderActionResult extends KafkaMessage {

    private Integer orderId;
    private Integer actionId;
    private ActionResultEnum actionResultEnum;
    private Long actualDate;

    public OrderActionResult(Integer actionId, Integer orderId,ActionResultEnum actionResultEnum, LocalDateTime actualDate) {

        this.actionId = actionId;
        this.orderId = orderId;
        this.actionResultEnum = actionResultEnum;
        this.actualDate = NLS.localDateTime2long(actualDate);
    }

    public static OrderActionResult ok(ExecOrderAction task) {

        return new OrderActionResult(
                task.getActionId(),
                task.getOrderId(),
                OrderActionsConsts.ActionResultEnum.OK_FINISHED,
                LocalDateTime.now());
    }

    public static OrderActionResult of(ExecOrderAction task, OrderActionsConsts.ActionResultEnum actionResult) {

        return new OrderActionResult(
                task.getActionId(),
                task.getOrderId(),
                actionResult,
                LocalDateTime.now());
    }
}
