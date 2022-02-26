/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.fs.kafka;

import java.util.Collection;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.insta.api.IfsVector;
import static org.dbs24.insta.api.consts.InstaConsts.Consumers.CON_GROUP_ID;
import static org.dbs24.insta.api.consts.InstaConsts.Topics.IFS_VECTOR;
import org.dbs24.kafka.AbstractKafkaService;
import org.dbs24.kafka.api.KafkaMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-fs")
public class KafkaService extends AbstractKafkaService {

    @KafkaListener(id = IFS_VECTOR, groupId = CON_GROUP_ID, topics = IFS_VECTOR)
    public void receiveTask(Collection<IfsVector> vectors) {
        log.debug("{}: receive kaffka msg: {}", IFS_VECTOR, vectors.stream().count());
    }
//    @KafkaListener(id = IFS_VECTOR, groupId = CON_GROUP_ID, topics = IFS_VECTOR,
//            topicPartitions = {
//                @TopicPartition(topic = IFS_VECTOR, partitions = {"10"})})
//    public void receiveTask10(Collection<IfsVector> vectors) {
//        log.debug("{}: 10 - receive kaffka msg: {}", IFS_VECTOR, vectors.stream().count());
//
//    }

    @Override
    protected Map<String, Class<? extends KafkaMessage>> mappings() {
        return ServiceFuncs.createMap(m -> {
            m.put(IFS_VECTOR, IfsVector.class);
            //m.put("image", IfsImg.class);
        });
    }
}
