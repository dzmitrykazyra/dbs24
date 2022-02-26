/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.kafka;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.google.api.ExecOrderAction;
import org.dbs24.kafka.AbstractKafkaService;
import org.dbs24.kafka.api.KafkaMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.dbs24.google.api.OrderActionsConsts.KafkaTopics.KAFKA_ORDER_ACTION;

@Service
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "app-promo")
public class KafkaService extends AbstractKafkaService {

    @Bean
    public KafkaAdmin.NewTopics kafkaTopics() {

        return new KafkaAdmin.NewTopics(
                TopicBuilder.name(KAFKA_ORDER_ACTION)
                        .partitions(5)
                        .build());
    }

    public void sendAction2Google(ExecOrderAction execOrderAction) {
        getProducer().send(KAFKA_ORDER_ACTION, execOrderAction);
    }


    @Override
    protected Map<String, Class<? extends KafkaMessage>> mappings() {
        return ServiceFuncs.createMap(m -> {
            m.put(ExecOrderAction.class.getSimpleName(), ExecOrderAction.class);
        });
    }
}
