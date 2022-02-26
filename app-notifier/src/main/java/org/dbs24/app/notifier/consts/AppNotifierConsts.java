/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.notifier.consts;

import static org.dbs24.consts.RestHttpConsts.URI_API;

public final class AppNotifierConsts {

    public static class Packages {

        public static final String ALL_PACKAGES = "org.dbs24.app.notifier";
        public static final String REPOSITORY_PACKAGE = ALL_PACKAGES + ".repo";
    }

    // Account processing
    public static class Uri {

        // agent
        public static final String URI_CREATE_OR_UPDATE_MESSAGE = URI_API + ("/createOrUpdateMessage");
        public static final String URI_GET_MESSAGE = URI_API + ("/getMessages");
    }

    // Caches
    public static class Caches {

        public static final String CACHE_MESSAGE = "cacheMessage";
        public static final String CACHE_APPLICATION = "cacheApplication";
        public static final String CACHE_MESSAGE_FORMAT = "cacheMessageFormat";

    }

    public static class Sequences {

        public static final String SEQ_MAIN = "seq_note";

    }

    public static class RestQueryParams {

        public static final String QP_START_DATE = "startDate";
        public static final String QP_LOGIN_TOKEN = "loginToken";
        public static final String QP_PACKAGE = "package";
        public static final String QP_VERSION = "version";
    }
}
