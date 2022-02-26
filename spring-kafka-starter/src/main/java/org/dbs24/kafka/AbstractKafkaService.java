/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.kafka;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.kafka.api.KafkaMessage;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.BatchMessagingMessageConverter;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;

import java.util.Map;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;

@Log4j2
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractKafkaService extends AbstractApplicationService {

    @Getter
    private KafkaTemplate<Object, Object> producer;

    @Bean
    public KafkaTemplate<Object, Object> kafkaTemplate(ProducerFactory<Object, Object> kafkaProducerFactory, ProducerListener<Object, Object> kafkaProducerListener, ObjectProvider<RecordMessageConverter> messageConverter) {
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate(kafkaProducerFactory);
        messageConverter.ifUnique(kafkaTemplate::setMessageConverter);
        kafkaTemplate.setProducerListener(kafkaProducerListener);
        kafkaTemplate.setDefaultTopic("DEFAUL_TOPIC");
        producer = kafkaTemplate;
        return kafkaTemplate;
    }

    @Bean
    public BatchMessagingMessageConverter batchConverter() {
        return new BatchMessagingMessageConverter(converter());
    }

    @Bean
    public RecordMessageConverter converter() {
        final ByteArrayJsonMessageConverter converter = new ByteArrayJsonMessageConverter();
        final DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        typeMapper.addTrustedPackages(ALL_PACKAGES + ".*");
        typeMapper.setIdClassMapping(kafkaMappings());
        converter.setTypeMapper(typeMapper);

        return converter;
    }

    private Map<String, Class<?>> kafkaMappings() {

        return ServiceFuncs.createMap(map
                -> mappings()
                .entrySet()
                .forEach(es -> {
                    map.put(es.getKey(), es.getValue());
                    log.info("Registry kafka '{}' [{}] mapping type ", es.getValue(), es.getKey());
                })
        );
    }

    //==========================================================================
    protected <T extends KafkaMessage> void send(String topic, T message) {

        log.debug("{}/{}: send = {} ", topic, message.getClass().getSimpleName(), message.getMsgId());

        getProducer().send(topic, message);

    }

    //==========================================================================
    protected abstract <T extends KafkaMessage> Map<String, Class<T>> mappings();

}
