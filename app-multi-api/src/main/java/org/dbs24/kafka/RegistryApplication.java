package org.dbs24.kafka;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.kafka.api.KafkaMessage;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegistryApplication extends KafkaMessage {

    private Long pid;
    private String address;
    private String applicationName;
    private Integer userCapacity;
    private Long nextReboot;
    private Long rebootDeadLine;
    private String countryCode;

}
