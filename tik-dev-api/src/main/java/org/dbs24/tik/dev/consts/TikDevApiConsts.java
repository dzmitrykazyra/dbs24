package org.dbs24.tik.dev.consts;

import static org.dbs24.consts.RestHttpConsts.URI_API;

public class TikDevApiConsts {

    public static class UriConsts {

        public static final String URI_CREATE_OR_UPDATE_DEVELOPER = URI_API + "/createOrUpdateDeveloper";
        public static final String URI_CREATE_OR_UPDATE_CONTRACT = URI_API + "/createOrUpdateContract";
        public static final String URI_CREATE_OR_UPDATE_TIK_ACCOUNT = URI_API + "/createOrUpdateTikAccount";
        public static final String URI_CREATE_OR_UPDATE_TIK_ACCOUNT_SCOPE = URI_API + "/createOrUpdateTikAccountScope";
        public static final String URI_CREATE_OR_UPDATE_TARIFF_LIMIT = URI_API + "/createOrUpdateTariffLimit";
        public static final String URI_CREATE_OR_UPDATE_TARIFF_PLAN = URI_API + "/createOrUpdateTariffPlan";
        public static final String URI_CREATE_OR_UPDATE_TARIFF_PLAN_PRICE = URI_API + "/createOrUpdateTariffPlanPrice";
        public static final String URI_CREATE_OR_UPDATE_DEVICE = URI_API + "/createOrUpdateDevice";
        public static final String URI_CREATE_ENDPOINT_ACTION = URI_API + "/createEndpointAction";
    }

    public static final class Databases {
        public static final String SEQ_GENERAL = "seq_tda_main";
    }

    public static final class Caches {
        public static final String CACHE_DEVELOPER_STATUS = "cacheDeveloperStatus";
        public static final String CACHE_CONTRACT_STATUS = "cacheContractStatus";
        public static final String CACHE_TIK_ACCOUNT_STATUS = "cacheTikAccountStatus";
        public static final String CACHE_TIK_ENDPOINT_SCOPE = "cacheEndpointScope";
        public static final String CACHE_TP_STATUS = "cacheTariffPlanStatus";
        public static final String CACHE_TP_TYPE = "cacheTariffPlanType";
        public static final String CACHE_DEVICE_STATUS = "cacheDeviceStatus";
        public static final String CACHE_ENDPOINT = "cacheEndPoint";
        public static final String CACHE_ENDPOINT_RESULT = "cacheEndPointResult";
    }

}
