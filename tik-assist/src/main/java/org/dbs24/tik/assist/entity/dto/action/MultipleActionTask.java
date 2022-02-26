package org.dbs24.tik.assist.entity.dto.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.OrderAction;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MultipleActionTask {

    @JsonProperty("tasks")
    private List<ActionTask> actionTasks;

    public static MultipleActionTask toDto(List<OrderAction> orderActions) {

        return StmtProcessor.create(
                MultipleActionTask.class,
                multipleActionTask -> multipleActionTask.setActionTasks(
                        orderActions
                                .stream()
                                .map(ActionTask::toActionTask)
                                .collect(Collectors.toList()))
        );
    }
}
