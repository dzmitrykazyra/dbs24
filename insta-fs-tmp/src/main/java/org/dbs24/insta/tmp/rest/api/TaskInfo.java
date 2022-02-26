/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.rest.api;

import lombok.Data;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.insta.tmp.entity.Task;
import org.dbs24.stmt.StmtProcessor;

@Data
public class TaskInfo {

    private Long taskId;
    private Long parentTaskId;
    private Long accountId;
    private Long instaId;
    private Long actualDate;
    private Integer taskTypeId;
    private Integer taskResultId;
    private Integer botId;
    private Long taskStartDate;
    private Long taskFinishDate;
    private String error;
    private String requestPaginationId;

    public void assign(Task task) {
        setTaskId(task.getTaskId());
        setAccountId(task.getAccount().getAccountId());
        setActualDate(NLS.localDateTime2long(task.getActualDate()));
        setBotId(task.getBot().getBotId());
        setError(task.getError());
        setInstaId(task.getAccount().getInstaId());
        setRequestPaginationId(task.getRequestPaginationId());
        setTaskFinishDate(NLS.localDateTime2long(task.getTaskFinishDate()));
        StmtProcessor.ifNotNull(task.getParentTaskId(), () -> setParentTaskId(task.getParentTaskId().getTaskId()));
        setTaskTypeId(task.getTaskType().getTaskTypeId());
        setTaskStartDate(NLS.localDateTime2long(task.getTaskStartDate()));
        setTaskResultId(task.getTaskResult().getTaskResultId());
    }
}
