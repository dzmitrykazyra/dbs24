/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.img.kafka;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.insta.api.IfsImg;
import org.dbs24.insta.api.IfsVector;
import static org.dbs24.insta.api.consts.InstaConsts.Consumers.CON_GROUP_ID;
import static org.dbs24.insta.api.consts.InstaConsts.Topics.IFS_IMAGE;
import static org.dbs24.insta.api.consts.InstaConsts.Topics.IFS_VECTOR;
import org.dbs24.kafka.AbstractKafkaService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;
import org.dbs24.kafka.api.KafkaMessage;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Log4j2
@Service
@EnableScheduling
@EnableAsync
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-img")
public class KafkaImgService extends AbstractKafkaService {

    @Bean
    public KafkaAdmin.NewTopics kafkaTopics() {
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name(IFS_VECTOR)
                        .partitions(15)
                        .build());
    }

    @KafkaListener(id = IFS_IMAGE, groupId = CON_GROUP_ID, topics = IFS_IMAGE)
    public void receiveTask(Collection<IfsImg> images) {
//        log.info("{}: receive kaffka msg: {}",
//                IFS_SERVICE_TASK, serviceTasks.size());

        log.debug("{}: receive kaffka msg: {}", IFS_IMAGE, images.size());

    }
//    public void notifyModSub(UserSubscriptionInfo usi) {
//        log.info("{}: send {} record", KAFKA_MODIFIED_SUBSCRIPTIONS, usi);
//        getProducer().send(KAFKA_MODIFIED_SUBSCRIPTIONS, usi);
//    }

    @Override
    protected Map<String, Class<? extends KafkaMessage>> mappings() {
        return ServiceFuncs.createMap(m -> {
            m.put(IFS_VECTOR, IfsVector.class);
            m.put(IFS_IMAGE, IfsImg.class);
        });
    }

    public void sendVector(IfsVector vector) {
        log.debug("{}: send {} record", IFS_VECTOR, vector);
        getProducer().send(IFS_VECTOR, vector.getMsgId(), vector);
    }

//    public void sendVector2(IfsVector vector) {
//        log.debug("{}: 2 send {} record", IFS_VECTOR, vector);
//        getProducer().send(IFS_VECTOR, 10, vector.getMsgId(), vector);
//    }
    @Scheduled(fixedRateString = "${config.kafka.processing-interval:2000}", cron = "${config.kafka.processing-cron:}")
    private void perform() {

        sendVector(StmtProcessor.create(IfsVector.class, st -> {

            st.setVector("Send vector image: " + LocalDateTime.now().toString());
        }));

//        sendVector2(StmtProcessor.create(IfsVector.class, st -> {
//
//            st.setVector("2: " + LocalDateTime.now().toString());
//        }));
    }
}
