/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.crw.kafka;

import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.insta.api.*;
import org.dbs24.kafka.AbstractKafkaService;
import org.dbs24.kafka.api.KafkaMessage;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

import static org.dbs24.insta.api.consts.InstaConsts.Topics.*;

@Log4j2
@Service
@EnableScheduling
@EnableAsync
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-crw")
public class KafkaCrowlerService<T extends KafkaMessage> extends AbstractKafkaService {

    @Bean
    public KafkaAdmin.NewTopics kafkaTopics() {
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name(IFS_ACCOUNT)
                        .partitions(5)
                        .build(),
                TopicBuilder.name(IFS_IMAGE)
                        .partitions(5)
                        .build(),
                TopicBuilder.name(IFS_TASK_BUILDER)
                        .partitions(5)
                        .build(),
                TopicBuilder.name(IFS_ACCOUNT_IS_CREATED)
                        .partitions(5)
                        .build(),
                TopicBuilder.name(IFS_CREATE_FOLLOWERS_LIST)
                        .partitions(5)
                        .build());
    }

//    @KafkaListener(id = IFS_SERVICE_TASK, groupId = CON_GROUP_ID, topics = IFS_SERVICE_TASK)
//    public void receiveTask(Collection<IfsServiceTask> serviceTasks) {
////        log.info("{}: receive kaffka msg: {}",
////                IFS_SERVICE_TASK, serviceTasks.size());
//
//        log.debug("{}: receive kaffka msg: {}", IFS_SERVICE_TASK, serviceTasks.size());
//
//    }
    @Override
    protected Map<String, Class<T>> mappings() {
        return ServiceFuncs.createMap(m -> {
            m.put(IFS_ACCOUNT, (Class<T>) IfsAccount.class);
            m.put(IFS_SERVICE_TASK, (Class<T>) IfsServiceTask.class);
        });
    }

    public void sendConfirmTaskCreation(IfsAccountIsCreated ifsAddNewAccountsTask) {
        //log.debug("{}: send {} record", IFS_ACCOUNT_IS_CREATED, ifsAddNewAccountsTask);
        getProducer().send(IFS_ACCOUNT_IS_CREATED, (T) ifsAddNewAccountsTask);
    }

    public void sendImgTask(T ii) {
        log.debug("{}: send {} record", IFS_IMAGE, ii);
        getProducer().send(IFS_IMAGE, ii);
    }

    public void buildGeneralTask(T ft) {
        ///log.debug("{}: send {} record", IFS_TASK_BUILDER, ft);
        getProducer().send(IFS_TASK_BUILDER, ft);
    }

    public void createFollowersList(IfsCreatedFollowers ifsCreatedFollowers) {
        //log.debug("{}: send {} records", IFS_CREATE_FOLLOWERS_LIST, ifsCreatedFollowers.getFollowers().size());
        getProducer().send(IFS_CREATE_FOLLOWERS_LIST, (T) ifsCreatedFollowers);
    }
    
    //@Scheduled(fixedRateString = "${config.kafka.processing-interval:2000}", cron = "${config.kafka.processing-cron:}")
    private void perform() {

        sendImgTask((T) StmtProcessor.create(IfsImg.class, st -> {

            st.setPathToImg(LocalDateTime.now().toString());

            //log.info("send record: {}", st.messageId());
        }));
    }

}
