/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.ad.server.consts;

import static org.dbs24.consts.RestHttpConsts.URI_API;

public final class AdConsts {

    public static class Packages {

        public static final String ALL_PACKAGES = "org.dbs24.ad.server";
        public static final String REPOSITORY_PACKAGE = ALL_PACKAGES + ".repo";
    }

    // Account processing
    public static class Uri {

        // agent
        public static final String URI_CREATE_OR_UPDATE_ADS_SETTINGS = URI_API + ("/createOrUpdateAdsSettings");
        public static final String URI_GET_ADS_SETTINGS = URI_API + ("/getAdsSettings");
    }

    // Caches
    public static class Caches {

        public static final String CACHE_APPLICATION = "cacheApplication";
        public static final String CACHE_ADS_SETTINGS = "cacheAdsSettings";

    }

    public static class Sequences {

        public static final String SEQ_MAIN = "seq_ads";

    }

    public static class RestQueryParams {

        public static final String QP_ACTUAL_DATE = "actualDate";
        public static final String QP_PACKAGE_NAME = "packageName";

    }
}
