/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.sale.config.kafka;

import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.insta.api.IfsSearchTask;
import static org.dbs24.insta.api.consts.InstaConsts.Topics.IFS_CLIENT_SEARCH;
import org.dbs24.kafka.AbstractKafkaService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;
import org.dbs24.kafka.api.KafkaMessage;

@Log4j2
@Service
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-sale")
public class KafkaSaleService extends AbstractKafkaService {

    @Bean
    public KafkaAdmin.NewTopics kafkaTopics() {
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name(IFS_CLIENT_SEARCH)
                        .partitions(2)
                        .build());
    }

    @Override
    protected Map<String, Class<? extends KafkaMessage>> mappings() {
        return ServiceFuncs.createMap(m -> m.put(IFS_CLIENT_SEARCH, IfsSearchTask.class));
    }
}
