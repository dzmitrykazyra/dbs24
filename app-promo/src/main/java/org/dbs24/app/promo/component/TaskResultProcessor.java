package org.dbs24.app.promo.component;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.google.api.OrderActionResult;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static org.dbs24.application.core.service.funcs.ServiceFuncs.createConcurencyCollection;
import static org.dbs24.google.api.OrderActionsConsts.Consumers.PRODUCER_GROUP_ID;
import static org.dbs24.google.api.OrderActionsConsts.KafkaTopics.KAFKA_ACTION_RESULT;

@Getter
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "app-promo")
public class TaskResultProcessor extends AbstractApplicationService {

    final Collection<OrderActionResult> actionResults = createConcurencyCollection();
    final OrdersActionsProcessor ordersActionsProcessor;

    public TaskResultProcessor(OrdersActionsProcessor ordersActionsProcessor) {
        this.ordersActionsProcessor = ordersActionsProcessor;
    }

    @KafkaListener(id = KAFKA_ACTION_RESULT, groupId = PRODUCER_GROUP_ID, topics = KAFKA_ACTION_RESULT)
    public void receiveTask(Collection<OrderActionResult> actionResults) {

        log.debug("{}: receive tasks results: {}", KAFKA_ACTION_RESULT, actionResults.size());
        // add new tasks 4 research
        this.actionResults.addAll(actionResults);

    }

    //==================================================================================================================
    @Scheduled(fixedRateString = "${config.kafka.processing-interval:3000}", cron = "${config.kafka.processing-cron:}")
    public void processTasksResults() {

        StmtProcessor.ifTrue(!actionResults.isEmpty(),
                () -> actionResults
                        .stream()
                        .parallel()
                        .forEach(this::applyAction));
    }

    protected void applyAction(OrderActionResult orderActionResult) {

        actionResults.remove(orderActionResult);
        getOrdersActionsProcessor().processTaskResult(orderActionResult);

    }
}
