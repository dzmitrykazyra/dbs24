package org.dbs24.google.service.impl;

import com.github.yeriomin.playstoreapi.exception.account.BannedException;
import com.github.yeriomin.playstoreapi.exception.application.AppNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.dbs24.google.api.ExecOrderAction;
import org.dbs24.google.api.OrderActionResult;
import org.dbs24.google.api.OrderActionsConsts;
import org.dbs24.google.dao.InProgressTasksStorage;
import org.dbs24.google.dao.NewTasksStorage;
import org.dbs24.google.service.ActionTaskExecutorService;
import org.dbs24.google.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Log4j2
@Service
@EnableAsync
@EnableScheduling
public class TaskServiceImpl implements TaskService {

    private final ActionTaskExecutorService executor;
    private final KafkaService kafkaService;
    private final NewTasksStorage newTasksStorage;
    private final InProgressTasksStorage inProgressTasksStorage;

    @Autowired
    public TaskServiceImpl(ActionTaskExecutorService executor,
                           KafkaService kafkaService,
                           NewTasksStorage newTasksStorage,
                           InProgressTasksStorage inProgressTasksStorage) {

        this.executor = executor;
        this.kafkaService = kafkaService;
        this.newTasksStorage = newTasksStorage;
        this.inProgressTasksStorage = inProgressTasksStorage;
    }

    @Scheduled(fixedRateString = "${config.storage.reboot:1000}")
    public void perform() {
        List<ExecOrderAction> actionsToExecute = newTasksStorage.getAll();
        int tasksToExecuteQuantity = actionsToExecute.size();

        inProgressTasksStorage.addAll(actionsToExecute);

        if (tasksToExecuteQuantity > 0) {
            log.info("Got {} tasks to execute during scheduling", tasksToExecuteQuantity);

            IntStream.range(0, tasksToExecuteQuantity)
                    .mapToObj(counter -> execute(actionsToExecute.get(counter)))
                    .forEach(kafkaService::sendActionResult);
        }
        newTasksStorage.removeAll(actionsToExecute);
    }

    @Override
    public OrderActionResult execute(ExecOrderAction taskToExecute) {
        OrderActionsConsts.ActionEnum actionType = defineActionType(taskToExecute);
        try {

            return switch (actionType) {
                case USER_SEARCH -> executor.simulateSearch(taskToExecute);

                case INSTALL_ACTION -> executor.install(taskToExecute);

                case RATE_COMMENT_ACTION -> executor.rateComment(taskToExecute);

                case REVIEW_ACTION -> executor.review(taskToExecute);

                case FLAG_CONTENT -> executor.flagContent(taskToExecute);

                case APPLICATION_ACTIVITY -> executor.applicationActivity(taskToExecute);
            };

        } catch (NullPointerException e) {
            log.error("Exception: action type code = {} does not exist", taskToExecute.getActRefId());

            return OrderActionResult.of(taskToExecute, OrderActionsConsts.ActionResultEnum.FAIL_UNKNOWN_ACTION);

        } catch (AppNotFoundException e) {
            log.error("Cannot find application - {}", taskToExecute.getAppPackage());

            return OrderActionResult.of(taskToExecute, OrderActionsConsts.ActionResultEnum.FAIL_URI_NOT_EXIST);

        } catch (BannedException e) {
            log.error("Account - {} banned", taskToExecute.getGmailAccountInfo().getEmail());

            return OrderActionResult.of(taskToExecute, OrderActionsConsts.ActionResultEnum.FAIL_AGENT_IS_EXPIRED);

        } catch (Exception e) {
            log.error("Exception during {} action type execution. Cause : {}. Message : {}", taskToExecute.getActRefId(), e.getCause(), e.getMessage());

            return OrderActionResult.of(taskToExecute, OrderActionsConsts.ActionResultEnum.FAIL_GENERAL);
        }
    }

    private OrderActionsConsts.ActionEnum defineActionType(ExecOrderAction taskToExecute) {

        return Arrays.stream(OrderActionsConsts.ActionEnum.values())
                .filter(action -> taskToExecute.getActRefId().equals(action.getCode()))
                .findFirst()
                .orElse(null);
    }
}
