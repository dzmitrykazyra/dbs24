/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.service.bot;

import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.constant.reference.OrderActionResultDefine;
import org.dbs24.tik.assist.entity.dto.action.ActionTaskConfirm;
import org.dbs24.tik.assist.entity.dto.action.ActionTask;
import org.dbs24.tik.assist.entity.dto.bot.RepairBotTask;
import org.dbs24.tik.assist.entity.dto.bot.RepairBotTaskConfirm;
import org.dbs24.tik.assist.entity.dto.bot.RepairBotTaskResult;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.tik.assist.entity.dto.action.ActionTaskResult;
import org.springframework.stereotype.Service;

@Log4j2
@Deprecated
@Service
public class TikConnectorEmuService extends AbstractApplicationService {

    final Collection<ActionTask> actions4work = ServiceFuncs.<ActionTask>createConcurencyCollection();
    final Collection<RepairBotTask> expiredAgents = ServiceFuncs.<RepairBotTask>createConcurencyCollection();
    final Random rand = new Random();

    public ActionTaskConfirm createTikAction(ActionTask actionTask) {
        return StmtProcessor.create(ActionTaskConfirm.class, atc -> {

            actions4work.add(actionTask);
            log.info("createTikAction: receive task: actionId: {}, actions pool size: {}", actionTask, actions4work.size());
            atc.setIsConfirmed(Boolean.TRUE);
        });
    }

    public RepairBotTaskConfirm createRepairTaskAction(RepairBotTask actionTask) {
        return StmtProcessor.create(RepairBotTaskConfirm.class, atc -> {

            expiredAgents.add(actionTask);
            log.info("createRepairTaskAction: receive repair task: actionId: {}, actions pool size: {}", actionTask, expiredAgents.size());
            atc.setIsConfirmed(Boolean.TRUE);
            atc.setTaskId(actionTask.getTaskId());
            atc.setAgentId(actionTask.getAgentId());
        });
    }

    //===========================================================================
    public ActionTaskResult getTikActionStatus(Long actionId) {

        log.info("getTikActionStatus: request 4 actionId = {}", actionId);

        return StmtProcessor.create(ActionTaskResult.class, atr -> {

            actions4work
                    .stream()
                    .filter(at -> at.getActionId().equals(actionId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("getTikActionStatus: actionId is not found: " + actionId.toString()));

            final Integer actionResultId = generateRandomActionResultStatuses(
                    OrderActionResultDefine.AR_OK.getId(),
                    OrderActionResultDefine.AR_FAIL.getId(),
                    OrderActionResultDefine.AR_WARN.getId(),
                    OrderActionResultDefine.AR_PAGE_NOT_EXIST.getId(),
                    OrderActionResultDefine.AR_IN_PROGRESS.getId());

            atr.setActionId(actionId);
            atr.setActionResultId(actionResultId);

            if ((actionResultId.equals(OrderActionResultDefine.AR_FAIL) || actionResultId.equals(OrderActionResultDefine.AR_WARN) || actionResultId.equals(OrderActionResultDefine.AR_PAGE_NOT_EXIST))) {
                atr.setErrMsg("SomeTestErrMsg".concat(UUID.randomUUID().toString().concat(UUID.randomUUID().toString()).replaceAll("-", "").substring(0, 20).toUpperCase()));/*
                atr.setNote("SomeTestNote".concat(UUID.randomUUID().toString().concat(UUID.randomUUID().toString()).replaceAll("-", "").substring(0, 20).toUpperCase()));*/
            }

            if (!actionResultId.equals(OrderActionResultDefine.AR_IN_PROGRESS)) {
                atr.setFinishDate(NLS.localDateTime2long(LocalDateTime.now()));
            }

            log.debug("getTikActionStatus: {}: new status is {}", actionId, atr);
        });
    }

    //===========================================================================
    public RepairBotTaskResult getAgentRepairActionStatus(Long taskId) {

        log.info("getAgentRepairActionStatus: request 4 taskId = {}", taskId);

        return StmtProcessor.create(RepairBotTaskResult.class, atr -> {

            atr.setAgentId(
                    expiredAgents
                            .stream()
                            .filter(at -> at.getTaskId().equals(taskId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("getAgentRepairActionStatus: taskId is not found: " + taskId.toString()))
                            .getAgentId());

            //atr.setTaskResult(OrderActionResultDefine.TR_FINISHED.getId());
            atr.setTaskId(taskId);

            log.debug("getTikActionStatus: {}: new status is {}", taskId, atr);
        });
    }

    //==========================================================================
    private Integer generateRandomActionResultStatuses(Integer... values) {
        return values[rand.nextInt(values.length)];
    }
}
