/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.google.service.impl;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.google.api.OrderActionResult;
import org.dbs24.kafka.AbstractKafkaService;
import org.dbs24.kafka.api.KafkaMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.dbs24.google.api.OrderActionsConsts.KafkaTopics.KAFKA_ACTION_RESULT;

@Service
public class KafkaService extends AbstractKafkaService {

    @Bean
    public KafkaAdmin.NewTopics kafkaTopics() {

        return new KafkaAdmin.NewTopics(
                TopicBuilder.name(KAFKA_ACTION_RESULT)
                        .partitions(5)
                        .build());
    }

    public void sendActionResult(OrderActionResult orderActionResult) {

        getProducer().send(KAFKA_ACTION_RESULT, orderActionResult);
    }

    @Override
    protected Map<String, Class<? extends KafkaMessage>> mappings() {
        return ServiceFuncs.createMap(m -> {
            m.put(OrderActionResult.class.getSimpleName(), OrderActionResult.class);
        });
    }
}