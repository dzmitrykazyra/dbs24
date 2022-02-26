/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.service.scheduled;

import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.tik.assist.constant.reference.RepairTaskResultDefine;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.service.bot.BotService;
import org.dbs24.tik.assist.service.scheduled.resolver.BotAdjustInteractor;
import org.springframework.scheduling.annotation.EnableAsync;
import java.util.Collection;

import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.dto.bot.RepairBotTask;
import org.dbs24.tik.assist.entity.dto.bot.RepairBotTaskResult;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@EnableAsync
public class BotRepairService extends AbstractApplicationService {

    private final BotService botService;
    private final BotAdjustInteractor botAdjustInteractor;

    private final ReferenceDao referenceDao;


    public BotRepairService(BotService botService, ReferenceDao referenceDao, BotAdjustInteractor botAdjustInteractor) {

        this.botService = botService;
        this.referenceDao = referenceDao;
        this.botAdjustInteractor = botAdjustInteractor;
    }

    final Collection<RepairBotTaskResult> repairTasks = ServiceFuncs.<RepairBotTaskResult>createConcurencyCollection();

    @Scheduled(fixedRateString = "${config.tik-connector.agent.update:5000}", cron = "${config.tik-connector.agent.processing-cron:}")
    public void perform() {

        checkTasksInProgress();
        addNewRepairTasks();
        updateRepairTaskStatuses();
    }

    private void checkTasksInProgress() {

        log.debug("checkTasksInProgress");

        repairTasks.removeIf(task -> task.getTaskResult().equals(RepairTaskResultDefine.TR_FINISHED.getId()) || task.getTaskResult().equals(RepairTaskResultDefine.TR_AGENT_IS_BANNED.getId()));

        final int actionsAmount = repairTasks.size();

        log.debug("threre are {} repair task(s) in progress ", actionsAmount);

    }

    private void addNewRepairTasks() {

        botService
                .findExpiredBots()
                .stream()
                .filter(task -> repairTasks
                        .stream()
                        .filter(activeTask -> activeTask.getAgentId().equals(task.getBotId()))
                        .findAny()
                        .isEmpty())
                .forEach(task -> {

                    final Long newTaskId = System.currentTimeMillis();

                    RepairBotTask repairBotTask = StmtProcessor.create(
                            RepairBotTask.class,
                            repairTask -> {
                                repairTask.setAgentId(task.getBotId());
                                repairTask.setTaskId(newTaskId);
                            });

                    botAdjustInteractor.addRepairTask(task, repairBotTask).ifPresent(repairTasks::add);
                });
    }

    private void updateRepairTaskStatuses() {

        repairTasks
                .stream()
                .filter(task -> !(task.getTaskResult().equals(RepairTaskResultDefine.TR_FINISHED.getId()) || task.getTaskResult().equals(RepairTaskResultDefine.TR_AGENT_IS_BANNED.getId())))
                .forEach(task -> {
                    log.debug("Checking status of repair task. Task id:{}. Bot id:{}. Task result:{}", task.getTaskId(), task.getAgentId(), task.getTaskResult());

                    RepairBotTaskResult updatedRepairBotResult = botAdjustInteractor.updateRepairTaskStatus(task);
                    StmtProcessor.ifNotNull(
                            updatedRepairBotResult,
                            taskResult -> task.setTaskResult(taskResult.getTaskResult())
                    );

                    log.info("Result of bot repair process check {}", updatedRepairBotResult.toString());

                    if (updatedRepairBotResult.getTaskResult().equals(RepairTaskResultDefine.TR_FINISHED.getId())) {

                        botService.updateBotStatus(task.getAgentId(), referenceDao.findActiveBotStatus());
                    }

                    if (updatedRepairBotResult.getTaskResult().equals(RepairTaskResultDefine.TR_AGENT_IS_BANNED.getId())) {

                        botService.updateBotStatus(task.getAgentId(), referenceDao.findBannedBotStatus());
                    }
                });
    }
}
