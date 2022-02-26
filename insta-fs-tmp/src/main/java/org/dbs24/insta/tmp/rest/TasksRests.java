/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.insta.tmp.component.TasksService;
import org.dbs24.insta.tmp.rest.api.AllTasks;
import org.dbs24.insta.tmp.rest.api.BotInfo;
import org.dbs24.insta.tmp.rest.api.TaskInfo;
import org.dbs24.insta.tmp.rest.api.CreatedTask;
import org.dbs24.rest.api.ReactiveRestProcessor;

import static org.dbs24.consts.SysConst.BOOLEAN_FALSE;
import static org.dbs24.consts.SysConst.BOOLEAN_TRUE;
import static org.dbs24.insta.tmp.consts.IfsConst.References.BotStatuses.BS_ACTUAL;
import static org.dbs24.insta.tmp.consts.IfsConst.RestQueryParams.*;
import static org.dbs24.rest.api.ReactiveRestProcessor.httpOk;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Optional;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-tmp")
public class TasksRests extends ReactiveRestProcessor {

    final TasksService tasksService;

    public TasksRests(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdateTask(ServerRequest request) {

        return this.<TaskInfo, CreatedTask>processServerRequest(request, TaskInfo.class,
                taskInfo -> tasksService.createOrUpdateTask(taskInfo));
    }

    //==========================================================================
    public Mono<ServerResponse> getTasksList(ServerRequest request) {

        final Integer taskResultId = getOptionalIntegerFromParam(request, QP_TASK_RESULT_ID);
        final Boolean allTasks = Optional.ofNullable(taskResultId).map(status -> BOOLEAN_FALSE).orElse(BOOLEAN_TRUE);

        return this.<AllTasks>processServerRequest(request, () -> tasksService.getAllTasks(allTasks, taskResultId));

    }
}
