package org.dbs24.google.api;

public final class OrderActionsConsts {

    public static class Consumers {

        public static final String PRODUCER_GROUP_ID = "application-promo";
    }

    public static class KafkaTopics {

        public static final String KAFKA_ORDER_ACTION = "pr_order_action_dev07";
        public static final String KAFKA_ACTION_RESULT = "pr_order_result_dev07";
    }

    public enum ActionEnum {

        INSTALL_ACTION("install", 100),
        RATE_COMMENT_ACTION("rate_comment", 200),
        REVIEW_ACTION("review", 300),
        FLAG_CONTENT("flag_content", 400),
        APPLICATION_ACTIVITY("application_activity", 500),
        USER_SEARCH("search", 1000);

        private final Integer code;
        private final String value;

        ActionEnum(String value, Integer code) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public Integer getCode() {
            return code;
        }
    }

    public enum ActionResultEnum {

        AR_CREATED("CREATED", 1),
        //AR_WARN_NOT_STARTED("WARN (not started)", 2),
        AR_IN_PROGRESS("IN PROGRESS", 3),
        //AR_WAIT_FOR_BOT("WAIT FOR BOT", 4),
        AR_FUTURED("FUTURED", 5),
        OK_FINISHED("OK (finished)", 10),
        FAIL_GENERAL("FAIL (general error)", -1),
        WARN_FINISH("WARN (finished)", -2),
        FAIL_URI_NOT_EXIST("FAIL (uri not exist)", -3),
        FAIL_UNKNOWN_ACTION("FAIL (unknown action)", -4),
        FAIL_AGENT_IS_EXPIRED("FAIL (bot is expired)", -5);
        //FAIL_UNKNOWN_TASK("FAIL (task not exists)", -6);

        private final Integer code;
        private final String value;

        ActionResultEnum(String value, Integer code) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public Integer getCode() {
            return code;
        }
    }
}
