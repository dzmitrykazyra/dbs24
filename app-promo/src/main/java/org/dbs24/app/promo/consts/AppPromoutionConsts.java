/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.consts;

import org.dbs24.app.promo.entity.*;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.google.api.OrderActionsConsts;
import org.dbs24.stmt.StmtProcessor;

import java.util.Arrays;
import java.util.Collection;

import static org.dbs24.consts.RestHttpConsts.URI_API;

public final class AppPromoutionConsts {

    // Package consts
    public static class PkgConsts {

        public static final String PROXY_PACKAGE = "org.dbs24.card.pmt";
        public static final String PROXY_PACKAGE_REPO = PROXY_PACKAGE + ".repo";
    }

    // Db Sequences
    public static class DbSequences {

        public static final String SEQ_GENERAL = "seq_pr_general";
    }

    // URI
    public static class UriConsts {

        public static final String URI_CREATE_OR_UPDATE_BOT = URI_API + "/createOrUpdateBot";
        public static final String URI_CREATE_OR_UPDATE_COMMENT = URI_API + "/createOrUpdateComment";
        public static final String URI_CREATE_OR_UPDATE_PACKAGE = URI_API + "/createOrUpdatePackage";
        public static final String URI_CREATE_OR_UPDATE_BATCH_SETUP = URI_API + "/createOrUpdateBatchSetup";
        public static final String URI_CREATE_OR_UPDATE_BATCH_TEMPLATE = URI_API + "/createOrUpdateBatchTemplate";
        public static final String URI_CREATE_OR_UPDATE_ORDER = URI_API + "/createOrUpdateOrder";
        public static final String URI_CREATE_OR_UPDATE_ORDER_ACTON = URI_API + "/createOrUpdateOrderAction";
    }

    public static class RestQueryParams {

        public static final String QP_PROXY_ID = "proxyId";
        public static final String QP_TOKEN = "token";
    }

    public enum ProviderEnum {

        GOOGLE_PLAY("GOOGLE_PLAY", 10);

        public static final Collection<Provider> PROVIDERS_LIST = ServiceFuncs.<Provider>createCollection(cp -> Arrays.stream(ProviderEnum.values())
                .map(stringRow -> StmtProcessor.create(Provider.class, record -> {
                    record.setProviderId(stringRow.getCode());
                    record.setProviderName(stringRow.getValue());
                })).forEach(ref -> cp.add(ref))
        );

        public static final Collection<Integer> PROVIDERS_LIST_IDS = ServiceFuncs.createCollection(cp -> PROVIDERS_LIST.stream().map(t -> t.getProviderId()).forEach(ref -> cp.add(ref)));

        private final Integer code;
        private final String value;

        ProviderEnum(String value, Integer code) {
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


    public static final Collection<Action> ACTIONS_LIST = ServiceFuncs.<Action>createCollection(cp -> Arrays.stream(OrderActionsConsts.ActionEnum.values())
            .map(stringRow -> StmtProcessor.create(Action.class, record -> {
                record.setActRefId(stringRow.getCode());
                record.setActRefName(stringRow.getValue());
            })).forEach(ref -> cp.add(ref))
    );

    public static final Collection<Integer> ACTIONS_LIST_IDS = ServiceFuncs.createCollection(cp -> ACTIONS_LIST.stream().map(t -> t.getActRefId()).forEach(ref -> cp.add(ref)));


    public static final Collection<ActionResult> ACTIONS_RESULTS_LIST = ServiceFuncs.<ActionResult>createCollection(cp -> Arrays.stream(OrderActionsConsts.ActionResultEnum.values())
            .map(stringRow -> StmtProcessor.create(ActionResult.class, record -> {
                record.setActionResultId(stringRow.getCode());
                record.setActionResultName(stringRow.getValue());
            })).forEach(ref -> cp.add(ref))
    );

    public static final Collection<Integer> ACTIONS_RESULTS_LIST_IDS = ServiceFuncs.createCollection(cp -> ACTIONS_RESULTS_LIST.stream().map(t -> t.getActionResultId()).forEach(ref -> cp.add(ref)));

    public enum BotStatusEnum {

        BS_ACTIVE("ACTIVE", 1),
        BS_BANNED("Banned", -6);

        public static final Collection<BotStatus> BOT_STATUSES_LIST = ServiceFuncs.<BotStatus>createCollection(cp -> Arrays.stream(BotStatusEnum.values())
                .map(stringRow -> StmtProcessor.create(BotStatus.class, record -> {
                    record.setBotStatusId(stringRow.getCode());
                    record.setBotStatusName(stringRow.getValue());
                })).forEach(ref -> cp.add(ref))
        );

        public static final Collection<Integer> BOT_STATUSES_LIST_IDS = ServiceFuncs.createCollection(cp -> BOT_STATUSES_LIST.stream().map(t -> t.getBotStatusId()).forEach(ref -> cp.add(ref)));

        private final Integer code;
        private final String value;

        BotStatusEnum(String value, Integer code) {
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

    public enum OrderStatusEnum {

        OS_CREATED("CREATED", 1),
        OS_IN_PROGRESS("IN PROGRESS", 10),
        OS_POST_PONED("POST PONED", 20),
        OS_FINISHED("FINISHED", 100),
        OS_FAIL_GENERAL("OS FAIL GENERAL", -100),
        OS_FAIL_GENERAL_101("OS FAIL GENERAL - 101", -101);

        public static final Collection<OrderStatus> ORDER_STATUSES_LIST = ServiceFuncs.<OrderStatus>createCollection(cp -> Arrays.stream(OrderStatusEnum.values())
                .map(stringRow -> StmtProcessor.create(OrderStatus.class, record -> {
                    record.setOrderStatusId(stringRow.getCode());
                    record.setOrderStatusName(stringRow.getValue());
                })).forEach(ref -> cp.add(ref))
        );

        public static final Collection<Integer> ORDER_STATUSES_LIST_IDS = ServiceFuncs.createCollection(cp -> ORDER_STATUSES_LIST.stream().map(t -> t.getOrderStatusId()).forEach(ref -> cp.add(ref)));

        private final Integer code;
        private final String value;

        OrderStatusEnum(String value, Integer code) {
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

    public enum BatchTypeEnum {

        BT_GOOGLE_ORDINARY("GOOGLE ORDINARY", 1000),
        BT_GOOGLE_ADVANCED("GOOGLE ADVANCED", 1100);

        public static final Collection<BatchType> BATCH_TYPES_LIST = ServiceFuncs.<BatchType>createCollection(cp -> Arrays.stream(BatchTypeEnum.values())
                .map(stringRow -> StmtProcessor.create(BatchType.class, record -> {
                    record.setBatchTypeId(stringRow.getCode());
                    record.setBatchTypeName(stringRow.getValue());
                })).forEach(ref -> cp.add(ref))
        );

        public static final Collection<Integer> BATCH_TYPES_LIST_IDS = ServiceFuncs.createCollection(cp -> BATCH_TYPES_LIST.stream().map(t -> t.getBatchTypeId()).forEach(ref -> cp.add(ref)));

        private final Integer code;
        private final String value;

        BatchTypeEnum(String value, Integer code) {
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


    // Caches
    public static class Caches {

        public static final String CACHE_BOT_STATUS = "botStatusRef";
        public static final String CACHE_APP_PROVIDER = "appProviderRef";
        public static final String CACHE_BATCH_TYPE = "batchTypesRef";
        public static final String CACHE_ACTION = "actionsRef";
        public static final String CACHE_ORDER_STATUS = "orderStatusRef";
        public static final String CACHE_ACTION_RESULT = "actionResultRef";
        public static final String CACHE_ACTION_RESULT_ENUM = "actionResultEnumRef";
        public static final String CACHE_BATCHES_SETUP = "batchesSetup";
        public static final String CACHE_ORDER = "cacheOrder";
        public static final String CACHE_PACKAGE = "cachePackage";
        public static final String CACHE_BOT = "cacheBot";

    }

    public enum ActionEventEnum {

        AE_CREATE_ORDER("CREATE_ORDER", 100),
        AE_BAN_BOT("BAN_BOT", 200),
        AE_FINISH_ACTION("FINISH_ACTION", 300);

        private final Integer code;
        private final String value;

        ActionEventEnum(String value, Integer code) {
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

    //==========================================================================
    // Messages templated
    public static class ErrMsg {

        public static final String FIELD_NOT_FOUND = "mandatory field is not defined - ";
        public static final String INVALID_FIELD_VALUE = "invalid field value - ";

    }
}
