package org.dbs24.tik.assist.entity.dto.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.OrderAction;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderActionIdList {

    @JsonProperty("task_ids")
    private List<Integer> taskIdList;

    public static OrderActionIdList toDto(List<OrderAction> orderActions) {

        return StmtProcessor.create(
                OrderActionIdList.class,
                orderActionIdList -> orderActionIdList.setTaskIdList(orderActions
                        .stream()
                        .map(OrderAction::getOrderActionId)
                        .collect(Collectors.toList()))
        );
    }
}
