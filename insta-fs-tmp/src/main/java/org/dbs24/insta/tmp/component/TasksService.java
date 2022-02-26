/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.insta.tmp.entity.Account;
import org.dbs24.insta.tmp.entity.Task;
import org.dbs24.insta.tmp.repo.TaskRepo;
import org.dbs24.insta.tmp.rest.api.AllTasks;
import org.dbs24.insta.tmp.rest.api.TaskInfo;
import org.dbs24.insta.tmp.rest.api.CreatedTask;

import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-tmp")
public class TasksService extends AbstractApplicationService {

    final TaskRepo taskRepo;
    final RefsService refsService;
    final BotsService botsService;
    final AccountsService accountsService;

    public TasksService(TaskRepo taskRepo, RefsService refsService, BotsService botsService, AccountsService accountsService) {
        this.taskRepo = taskRepo;
        this.refsService = refsService;
        this.botsService = botsService;
        this.accountsService = accountsService;
    }
    //==========================================================================

    @Transactional
    public CreatedTask createOrUpdateTask(TaskInfo taskInfo) {

        final Task task = findOrCreateTask(taskInfo.getTaskId());

        Assert.notNull(taskInfo.getInstaId(), String.format("%s: taskInfo.getInstaId() is null!",
                TaskInfo.class.getCanonicalName()));

        final Account accountInternal = accountsService.findOrCreateAccount(taskInfo.getAccountId());

        final Account account = StmtProcessor.notNull(accountInternal.getInstaId()) ? accountInternal : accountsService.findAccountByInstaId(taskInfo.getInstaId());

        // create future account
        StmtProcessor.ifNull(account.getUserName(), () -> {

            account.setInstaId(taskInfo.getInstaId());

            accountsService.buildFutureAccount(account);
            accountsService.saveAccount(account);

        });

        task.setActualDate((LocalDateTime) StmtProcessor.nvl(NLS.long2LocalDateTime(taskInfo.getActualDate()), LocalDateTime.now()));
        task.setBot(StmtProcessor.notNull(taskInfo.getBotId()) ? botsService.findBot(taskInfo.getBotId()) : null);
        task.setAccount(account);
        task.setError(taskInfo.getError());
        task.setParentTaskId(StmtProcessor.notNull(taskInfo.getParentTaskId()) ? this.findTask(taskInfo.getParentTaskId()) : null);
        task.setRequestPaginationId(taskInfo.getRequestPaginationId());
        task.setTaskFinishDate(NLS.long2LocalDateTime(taskInfo.getTaskFinishDate()));
        task.setTaskResult(refsService.findTaskResult(taskInfo.getTaskResultId()));
        task.setTaskStartDate(NLS.long2LocalDateTime(taskInfo.getTaskStartDate()));
        task.setTaskType(refsService.findTaskType(taskInfo.getTaskTypeId()));

        saveTask(task);

        return StmtProcessor.create(CreatedTask.class, ca -> {

            ca.setTaskId(task.getTaskId());

            log.debug("try 2 create/update task: {}", task.getTaskId());

        });
    }

    //==================================================================================================================
    @Transactional(readOnly = true)
    public AllTasks getAllTasks(Boolean allTasks, Integer taskResultId) {

        log.debug("get all tasks: {}/{}", allTasks, taskResultId);

        return StmtProcessor.create(AllTasks.class, atp -> findAllTasks(allTasks, taskResultId)
                .stream()
                .forEach(task -> atp.getTasks().add(StmtProcessor.create(TaskInfo.class, taskInfo -> taskInfo.assign(task)))));
    }

    //==================================================================================================================
    public Collection<Task> findAllTasks(Boolean allTasks, Integer taskResultId) {
        return taskRepo.findTasks(allTasks.compareTo(false), taskResultId);
    }


    public Task createTask() {
        return StmtProcessor.create(Task.class, a -> {
            a.setActualDate(LocalDateTime.now());
        });
    }

    public Task findTask(Long taskId) {

        return taskRepo
                .findById(taskId)
                .orElseThrow(() -> new RuntimeException(String.format("taskId not found (%d)", taskId)));
    }

    public Task findOrCreateTask(Long taskId) {
        return (Optional.ofNullable(taskId)
                .orElseGet(() -> Long.valueOf("0")) > 0)
                ? findTask(taskId)
                : createTask();
    }

    public void saveTask(Task task) {
        taskRepo.save(task);
    }
}
