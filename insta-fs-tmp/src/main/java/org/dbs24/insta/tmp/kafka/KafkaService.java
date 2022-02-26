/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.kafka;

import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.kafka.AbstractKafkaService;
import org.dbs24.kafka.api.KafkaMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;
import static org.dbs24.insta.tmp.consts.IfsConst.Kafka.*;
import org.dbs24.insta.tmp.kafka.api.*;

@Log4j2
@Service
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-tmp")
public class KafkaService extends AbstractKafkaService {

    @Bean
    public KafkaAdmin.NewTopics kafkaTopics() {

        return new KafkaAdmin.NewTopics(
                TopicBuilder.name(KAFKA_ACCOUNTS)
                        .partitions(5)
                        .build(),
                TopicBuilder.name(KAFKA_POSTS)
                        .partitions(5)
                        .build(),
                TopicBuilder.name(KAFKA_SOURCES)
                        .partitions(5)
                        .build(),
                TopicBuilder.name(KAFKA_SOURCES_TASKS)
                        .partitions(5)
                        .build(),
                TopicBuilder.name(KAFKA_VECTORS)
                        .partitions(5)
                        .build(),
                TopicBuilder.name(KAFKA_PICTURES)
                        .partitions(5)
                        .build());
    }

    @Override
    protected Map<String, Class<? extends KafkaMessage>> mappings() {
        return ServiceFuncs.createMap(m -> {
            m.put(IgAccount.class.getSimpleName(), IgAccount.class);
            m.put(IgPost.class.getSimpleName(), IgPost.class);
            m.put(IgSource.class.getSimpleName(), IgSource.class);
            m.put(IgSourceTask.class.getSimpleName(), IgSourceTask.class);
            m.put(IgVector.class.getSimpleName(), IgVector.class);
            m.put(IgPicture.class.getSimpleName(), IgPicture.class);
        });
    }

    public void sendSourceTask(IgSourceTask igSourceTask) {
        getProducer().send(KAFKA_SOURCES_TASKS, igSourceTask);
    }

    // 4 Junit test only
    public void sendSourceTest(IgSource igSource) {
        getProducer().send(KAFKA_SOURCES, igSource);
    }
}
