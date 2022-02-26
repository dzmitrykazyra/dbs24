package org.dbs24.impl.api;

public final class MultiApiConsts {

    public static class Consumers {

        public static final String PRODUCER_GROUP_ID = "multi-api";
        public static final Integer ATTEMPTS_RETRY = 10;
    }

    public static class KafkaTopics {

        public static final String KAFKA_REGISTRY_APPLICATION = "ma_registry_application_dev05";
        public static final String KAFKA_SHUTDOWN_APPLICATION = "ma_shutdown_application_dev05";

    }

}
