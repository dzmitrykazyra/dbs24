/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.kafka;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.WaMonitoring;
import org.dbs24.kafka.api.KafkaMessage;
import org.dbs24.rest.api.UserSubscriptionInfo;
import org.dbs24.validator.ApplicationValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Map;

import static java.lang.String.format;
import static org.dbs24.application.core.locale.NLS.localDateTime2long;
import static org.dbs24.application.core.locale.NLS.long2LocalDateTime;
import static org.dbs24.application.core.service.funcs.ServiceFuncs.createMap;
import static org.dbs24.consts.WaConsts.Kafka.*;
import static org.dbs24.impl.api.MultiApiConsts.KafkaTopics.KAFKA_REGISTRY_APPLICATION;
import static org.dbs24.impl.api.MultiApiConsts.KafkaTopics.KAFKA_SHUTDOWN_APPLICATION;
import static org.dbs24.stmt.StmtProcessor.assertNotNull;
import static org.dbs24.stmt.StmtProcessor.create;

@Log4j2
@Service
@EqualsAndHashCode(callSuper = true)
public class KafkaService extends AbstractKafkaService {

    @Value("${server.port}")
    public Integer serverPort;

    @Value("${server.ssl.enabled:false}")
    public Boolean sslEnabled;

    @Value("${server.host-country:BY}")
    public String hostCountry;

    @Value("${server.users-capacity:100}")
    public Integer usersCapacity;

    private String fullAddress;

    final ApplicationValidator applicationValidator;

    public KafkaService(ApplicationValidator applicationValidator) {
        this.applicationValidator = applicationValidator;
    }

    @Bean
    public KafkaAdmin.NewTopics kafkaTopics() {

        return new KafkaAdmin.NewTopics(
                TopicBuilder.name(KAFKA_MODIFIED_SUBSCRIPTIONS)
                        .partitions(5)
                        .build(),
                TopicBuilder.name(KAFKA_MODIFIED_DEVICES_ANDROID)
                        .partitions(5)
                        .build(),
                TopicBuilder.name(KAFKA_MODIFIED_DEVICES_IOS)
                        .partitions(5)
                        .build(),
                TopicBuilder.name(KAFKA_SUBSCRIPTION_ACTIVITIES)
                        .partitions(5)
                        .build(),
                TopicBuilder.name(KAFKA_REGISTRY_APPLICATION)
                        .build(),
                TopicBuilder.name(KAFKA_SHUTDOWN_APPLICATION)
                        .build(),
                TopicBuilder.name(KAFKA_ACTUAL_SUBSCRIPTION)
                        .partitions(5)
                        .build());
    }

    public void notifyModifiedSubscription(UserSubscriptionInfo usi) {
        getProducer().send(KAFKA_MODIFIED_SUBSCRIPTIONS, usi);
    }

    public void existSubscription(UserSubscriptionInfo usi) {
        getProducer().send(KAFKA_ACTUAL_SUBSCRIPTION, usi);
    }

    public void notifyModifiedAndroidDevice(KafkaUserDeviceAndroid uda) {
        getProducer().send(KAFKA_MODIFIED_DEVICES_ANDROID, uda);
    }

    public void notifyModifiedIosDevice(KafkaUserDeviceIos udi) {
        getProducer().send(KAFKA_MODIFIED_DEVICES_IOS, udi);
    }

    public void registryApplication(RegistryApplication registryApplication) {
//        runNewThread(() -> {
//            sleep(5000);
//            assertNotNull(KafkaTemplate.class, getProducer(), "Bean kafka template");
//            getProducer().send(KAFKA_REGISTRY_APPLICATION, registryApplication);
//        });
    }

    public void shutDownApplication(ShutDownApplication shutDownApplication) {
        getProducer().send(KAFKA_SHUTDOWN_APPLICATION, shutDownApplication);
    }

    @Override
    protected Map<String, Class<? extends KafkaMessage>> mappings() {
        return createMap(m -> {
            m.put(UserSubscriptionInfo.class.getSimpleName(), UserSubscriptionInfo.class);
            m.put(KafkaUserDeviceAndroid.class.getSimpleName(), KafkaUserDeviceAndroid.class);
            m.put(KafkaUserDeviceIos.class.getSimpleName(), KafkaUserDeviceIos.class);
            m.put(RegistryApplication.class.getSimpleName(), RegistryApplication.class);
        });
    }

    @PostConstruct
    public void postConstruct() {

        final LocalDateTime nextReboot = long2LocalDateTime(applicationValidator.getNextRebootTime());

        assertNotNull(Long.class, nextReboot, "nextRebootTime is not defined ${applicationValidator.getNextRebootTime()}");

        final LocalDateTime rebootDeadLine = long2LocalDateTime(applicationValidator.getNextRebootDeadLine());

        registryApplication(create(RegistryApplication.class, registryApplication -> {

            final String hostName = InetAddress.getLocalHost().getHostName();

            fullAddress = String.format("http%s://%s:%s",
                    sslEnabled ? "s" : "",
                    hostName.contains(".") ? hostName : "127.0.0.1",
                    serverPort > 0 ? serverPort : 0);

            registryApplication.setPid(ProcessHandle.current().pid());
            registryApplication.setAddress(fullAddress);
            registryApplication.setApplicationName(format("host: %s, application: %s", hostName, WaMonitoring.class.getCanonicalName()));
            registryApplication.setCountryCode(hostCountry);
            registryApplication.setUserCapacity(usersCapacity);
            registryApplication.setNextReboot(localDateTime2long(nextReboot));
            registryApplication.setRebootDeadLine(localDateTime2long(rebootDeadLine));
            log.info("registry server: {}", registryApplication);
        }));

    }

    @Override
    public void destroy() {

        shutDownApplication(create(ShutDownApplication.class, shutDownApplication -> {
            shutDownApplication.setAddress(fullAddress);
            shutDownApplication.setPid(ProcessHandle.current().pid());
            shutDownApplication.setNote("bye");
            log.info("stop server: {}", shutDownApplication);
        }));
        super.destroy();

    }
}
