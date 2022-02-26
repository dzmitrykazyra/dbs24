/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.auth.server.consts;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.auth.server.entity.ServerEvent;
import org.dbs24.auth.server.entity.ServerStatus;

import java.util.Collection;
import java.util.function.Predicate;

import static java.util.Arrays.stream;
import static org.dbs24.application.core.service.funcs.ServiceFuncs.createCollection;
import static org.dbs24.stmt.StmtProcessor.create;

public final class AuthConsts {

    // AccountStatuses
    public static class Packages {

        public static final String ALL_PACKAGES = "org.dbs24.auth.server";
        public static final String REPOSITORY_PACKAGE = ALL_PACKAGES + ".repo";
    }

    // Caches
    public static class Caches {

        public static final String CACHE_APPLICATION = "applicationsRef";
        public static final String CACHE_SERVER_STATUS = "serversStatusesRef";
        public static final String CACHE_SERVER_EVENTS = "serversEventsRef";

    }

    public static class Sequences {

        public static final String SEQ_CARDS = "seq_tnk_card_id";
        public static final String SEQ_SERVER = "seq_tkn_servers";

    }

    // Caches
    public static class Applications {
        public static final int WA_MONITORING = 10;
    }

    // ServerStatuses
    public enum ServerStatusEnum {

        SS_ACTIVE("SS_ACTIVE", 10),
        SS_CLOSED("SS_CLOSED", 20);

        public static final Collection<ServerStatus> SERVER_STATUSES_LIST = ServiceFuncs.<ServerStatus>createCollection(cp -> stream(ServerStatusEnum.values())
                .map(stringRow -> create(ServerStatus.class, record -> {
                    record.setServerStatusId(stringRow.getCode());
                    record.setServerStatusName(stringRow.getValue());
                })).forEach(ref -> cp.add(ref))
        );

        public static final Collection<Integer> SERVER_STATUSES_IDS = createCollection(cp -> SERVER_STATUSES_LIST.stream().map(t -> t.getServerStatusId()).forEach(ref -> cp.add(ref)));

        private final Integer code;
        private final String value;

        ServerStatusEnum(String value, Integer code) {
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

    // ServerEvents
    public enum ServerEventEnum {

        SE_CONNETION_TIME_OUT("SE_CONNETION_TIME_OUT", -101),
        SE_CONNETION_ERROR("SE_CONNECTION_ERROR", -100),
        SE_REGISTERED("SE_REGISTERED", 100),
        SE_REBOOT("SE_REBOOT", 200),
        SE_SHUTDOWN("SE_SHUTDOWN", 300);

        public static final Collection<ServerEvent> SERVER_EVENTS_LIST = ServiceFuncs.<ServerEvent>createCollection(cp -> stream(ServerEventEnum.values())
                .map(stringRow -> create(ServerEvent.class, record -> {
                    record.setServerEventId(stringRow.getCode());
                    record.setServerEventName(stringRow.getValue());
                })).forEach(ref -> cp.add(ref))
        );

        public static final Predicate<ServerEventEnum> CONNECT_TIME_OUT = ctu -> ctu.code.equals(SE_CONNETION_TIME_OUT.getCode());
        public static final Predicate<ServerEventEnum> SHUTDOWN = ctu -> ctu.code.equals(SE_SHUTDOWN.getCode());

        public static final Collection<Integer> SERVER_EVENTS_IDS = createCollection(cp -> SERVER_EVENTS_LIST.stream().map(t -> t.getServerEventId()).forEach(ref -> cp.add(ref)));

        private final Integer code;
        private final String value;

        ServerEventEnum(String value, Integer code) {
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
