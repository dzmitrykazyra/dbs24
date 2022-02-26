/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.component;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.admin.NewTopic;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.kafka.AbstractKafkaService;
import org.dbs24.kafka.api.KafkaMessage;
import static org.dbs24.kafka.consts.KafkaConsts.RLC.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@EnableScheduling
@EnableAsync
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-reg")
public class InstaRegKafkaService extends AbstractKafkaService {

    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name(TP_TOPIC2)
                .partitions(10)
                .replicas(10)
                .build();
    }

    @Scheduled(fixedRateString = "${config.kafka.processing-interval:10000}", cron = "${config.kafka.processing-cron:}")
    public void perform() {

        final String msg4receivers = "Hello from " + this.getClass().getTypeName() + ": " + LocalDateTime.now().toString();

        log.info(msg4receivers);

        // anonim class 4 test
        final KafkaMessage km = new KafkaMessage() {

            public String getMessage() {
                return msg4receivers;
            }
        };

        getProducer().send(TP_TOPIC2, km);
    }

    @Override
    protected Map<String, Class<? extends KafkaMessage>> mappings() {
        return ServiceFuncs.createMap(m -> {});
    }
}
