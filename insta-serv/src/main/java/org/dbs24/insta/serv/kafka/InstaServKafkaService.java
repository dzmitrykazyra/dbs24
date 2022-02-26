/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.serv.kafka;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.insta.api.IfsServiceTask;
import org.dbs24.insta.api.IfsGetFollowersTask;
import static org.dbs24.insta.api.consts.InstaConsts.Topics.IFS_GET_FOLLOWERS_TASK;
import static org.dbs24.insta.api.consts.InstaConsts.Topics.IFS_SERVICE_TASK;
import org.dbs24.kafka.AbstractKafkaService;
import org.dbs24.kafka.api.KafkaMessage;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import static org.dbs24.insta.api.consts.InstaConsts.Topics.IFS_CREATE_FOLLOWERS_LIST;

@Log4j2
@Service
@EnableScheduling
@EnableAsync
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-serv")
public class InstaServKafkaService extends AbstractKafkaService {

    @Bean
    public KafkaAdmin.NewTopics kafkaTopics() {
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name(IFS_SERVICE_TASK)
                        .partitions(2)
                        .build(),
                TopicBuilder.name(IFS_GET_FOLLOWERS_TASK)
                        .partitions(5)
                        .build());
    }

    @Override
    protected Map<String, Class<? extends KafkaMessage>> mappings() {
        return ServiceFuncs.createMap(m -> m.put(IFS_SERVICE_TASK, IfsServiceTask.class));
    }

    public void sendCreateAccountTask(IfsServiceTask ifsServiceTask) {
        send(IFS_SERVICE_TASK, ifsServiceTask);
    }

    public void sendGetFollwersTask(IfsGetFollowersTask st) {
        send(IFS_GET_FOLLOWERS_TASK, st);
    }

    //@Scheduled(fixedRateString = "${config.kafka.processing-interval:2000}", cron = "${config.kafka.processing-cron:}")
    private void perform() {

        sendCreateAccountTask(StmtProcessor.create(IfsServiceTask.class, st -> {

            st.setInstaUserName(LocalDateTime.now().toString());

        }));
    }
}
