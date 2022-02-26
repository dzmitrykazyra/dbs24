package org.dbs24.kafka;

import lombok.Data;
import org.dbs24.kafka.api.KafkaMessage;

@Data
public class ShutDownApplication extends KafkaMessage {

    private String address;
    private Long pid;
    private String note;

}
