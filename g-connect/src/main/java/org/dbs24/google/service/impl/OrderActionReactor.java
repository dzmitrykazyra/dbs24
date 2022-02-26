package org.dbs24.google.service.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.google.api.ExecOrderAction;
import org.dbs24.google.api.OrderActionResult;
import org.dbs24.google.service.TaskService;
import org.dbs24.reactor.AbstractHotSubscriber;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.dbs24.google.api.OrderActionsConsts.ActionResultEnum.OK_FINISHED;

@Log4j2
@Service
public class OrderActionReactor extends AbstractHotSubscriber<ExecOrderAction> {

    private final KafkaService kafkaService;

    public OrderActionReactor(KafkaService kafkaService) {

        this.kafkaService = kafkaService;
    }

    @Override
    protected void processEvent(ExecOrderAction execOrderAction) {
        log.info("process new order: {}", execOrderAction);

        //taskService.execute(execOrderAction);
        kafkaService.sendActionResult(
                new OrderActionResult(
                        execOrderAction.getActionId(),
                        execOrderAction.getOrderId(),
                        OK_FINISHED,
                        LocalDateTime.now()
                )
        );
    }
}