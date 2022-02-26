package org.dbs24.kafka;

import lombok.EqualsAndHashCode;
import org.dbs24.kafka.api.KafkaMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Map;

import static org.dbs24.application.core.service.funcs.ServiceFuncs.createMap;

@Deprecated
//@Service
@ConditionalOnMissingBean(type = "kafkaService")
@EqualsAndHashCode(callSuper = true)
public class KafkaService extends AbstractKafkaService {
    @Bean
    public KafkaAdmin.NewTopics kafkaFakedTopic() {

        return new KafkaAdmin.NewTopics(
                TopicBuilder.name("FAKED_TOPIC")
                        .partitions(1)
                        .build());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Map<String, Class<? extends KafkaMessage>> mappings() {
        return createMap(m -> {
        });
    }
}
