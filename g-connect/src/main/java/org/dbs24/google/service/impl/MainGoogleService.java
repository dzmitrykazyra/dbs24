package org.dbs24.google.service.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.google.api.ExecOrderAction;
import org.dbs24.google.dao.InProgressTasksStorage;
import org.dbs24.google.dao.NewTasksStorage;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.dbs24.google.api.OrderActionsConsts.Consumers.PRODUCER_GROUP_ID;
import static org.dbs24.google.api.OrderActionsConsts.KafkaTopics.KAFKA_ORDER_ACTION;

@Log4j2
@Service
//@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "g-connect")
public class MainGoogleService extends AbstractApplicationService {
    private final InProgressTasksStorage inProgressTasksStorage;
    private final NewTasksStorage newTasksStorage;

    public MainGoogleService(InProgressTasksStorage inProgressTasksStorage, NewTasksStorage newTasksStorage) {

        this.inProgressTasksStorage = inProgressTasksStorage;
        this.newTasksStorage = newTasksStorage;
    }

    @KafkaListener(id = KAFKA_ORDER_ACTION, groupId = PRODUCER_GROUP_ID, topics = KAFKA_ORDER_ACTION)
    public void receiveTask(List<ExecOrderAction> serviceTasks) {

        log.debug("{}: receive new tasks: {}", KAFKA_ORDER_ACTION, serviceTasks.size());

        log.info("KAFKA RECEIVE: {}", serviceTasks);

//        newTasksStorage.addAll(serviceTasks);
    }
}
