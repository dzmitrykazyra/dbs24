/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.consts;

import java.util.Collection;
import static org.dbs24.consts.RestHttpConsts.URI_API;
import org.dbs24.insta.tmp.rest.api.BotInfo;

public final class IfsConst {

    // Package consts
    public static class PkgConsts {

        public static final String IFS_PACKAGE = "org.dbs24.insta.tmp";
        public static final String IFS_PACKAGE_REPO = IFS_PACKAGE + ".repo";
    }

    // URI
    public static class UriConsts {

        // accounts
        public static final String URI_CREATE_OR_UPDATE_ACCOUNT = URI_API + "/createOrUpdateAccount";
        public static final String URI_VALIDATE_ACCOUNT = URI_API + "/validateAccount";
        public static final String URI_GET_ACCOUNT = URI_API + "/getAccount";
        public static final String URI_GET_ACCOUNTS = URI_API + "/getAccounts";
        public static final String URI_DELETE_ALL = URI_API + "/deleteAll";
        // posts
        public static final String URI_CREATE_OR_UPDATE_POST = URI_API + "/createOrUpdatePost";
        public static final String URI_CREATE_OR_UPDATE_SOURCE = URI_API + "/createOrUpdateSource";
        public static final String URI_CREATE_OR_UPDATE_FACE = URI_API + "/createOrUpdateFace";
        public static final String URI_CREATE_OR_UPDATE_BOT = URI_API + "/createOrUpdateBot";
        public static final String URI_GET_BOT = URI_API + "/getBot";
        public static final String URI_GET_BOTS_LIST = URI_API + "/getBotsList";
        public static final String URI_CREATE_OR_UPDATE_TASK = URI_API + "/createOrUpdateTask";
        public static final String URI_GET_TASKS = URI_API + "/getTasks";

    }

    public static class RestQueryParams {

        public static final String QP_ACCOUNT_ID = "accountId";
        public static final String QP_INSTA_ID = "instaId";
        public static final String QP_BOT_ID = "botId";
        public static final String QP_BOTS_LIMIT = "botsLimit";
        public static final String QP_BOTS_STATUS_ID = "botStatusId";
        public static final String QP_TASK_RESULT_ID = "taskResultId";
    }

    public static class References {

        public static class AccountStatuses {

            public static final Integer AS_ACTUAL = Integer.valueOf("1");
            public static final Integer AS_CLOSED = Integer.valueOf("2");

            public static final String[][] ALL_ACCOUNT_STATUSES = new String[][]{
                {String.valueOf(AS_ACTUAL), "Actual"},
                {String.valueOf(AS_CLOSED), "Closed"}};

        }

        //======================================================================
        public static class PostTypes {

            public static final Integer PT_IMAGE = Integer.valueOf("1");
            public static final Integer PT_VIDEO = Integer.valueOf("2");
            public static final Integer PT_CAROUSEL = Integer.valueOf("3");

            public static final String[][] ALL_POST_TYPES = new String[][]{
                {String.valueOf(PT_IMAGE), "Image"},
                {String.valueOf(PT_VIDEO), "Video"},
                {String.valueOf(PT_CAROUSEL), "CaroUsel"}
            };
        }

        public static class PostStatuses {

            public static final Integer PS_ACTUAL = Integer.valueOf("1");
            public static final Integer PS_CLOSED = Integer.valueOf("2");

            public static final String[][] ALL_POST_STATUSES = new String[][]{
                {String.valueOf(PS_ACTUAL), "Actual"},
                {String.valueOf(PS_CLOSED), "Closed"}};

        }

        public static class BotStatuses {

            public static final Integer BS_ACTUAL = Integer.valueOf("1");
            public static final Integer BS_CLOSED = Integer.valueOf("2");
            public static final Integer BS_QUARANTINE = Integer.valueOf("3");

            public static final String[][] ALL_BOT_STATUSES = new String[][]{
                {String.valueOf(BS_ACTUAL), "Actual"},
                {String.valueOf(BS_CLOSED), "Closed"},
                {String.valueOf(BS_QUARANTINE), "Quarantine"}};

        }

        //======================================================================
        public static class TaskTypes {

            public static final Integer TT_TYPE1 = Integer.valueOf("1");
            public static final Integer TT_TYPE2 = Integer.valueOf("2");

            public static final String[][] ALL_TASK_TYPES = new String[][]{
                {String.valueOf(TT_TYPE1), "Type1"},
                {String.valueOf(TT_TYPE2), "Type2"}
            };
        }

        //======================================================================
        public static class TaskResult {

            public static final Integer TR_RESULT1 = Integer.valueOf("1");
            public static final Integer TR_RESULT2 = Integer.valueOf("2");

            public static final String[][] ALL_TASK_RESULTS = new String[][]{
                {String.valueOf(TR_RESULT1), "Result1"},
                {String.valueOf(TR_RESULT2), "Result2"}
            };
        }

        //======================================================================
//        public static class SourceTypes {
//
//            public static final Integer ST_IMAGE = Integer.valueOf("1");
//            public static final Integer ST_VIDEO = Integer.valueOf("2");
//
//
//            public static final String[][] ALL_SOURCE_TYPES = new String[][]{
//                {String.valueOf(ST_IMAGE), "Image"},
//                {String.valueOf(ST_VIDEO), "Video"}
//            };
//        }
        public static class SourceStatuses {

            public static final Integer SS_ACTUAL = Integer.valueOf("1");
            public static final Integer SS_CLOSED = Integer.valueOf("2");

            public static final String[][] ALL_SOURCE_STATUSES = new String[][]{
                {String.valueOf(SS_ACTUAL), "Actual"},
                {String.valueOf(SS_CLOSED), "Closed"}};

        }

    }

    public static class Caches {

        public static final String CACHE_ACCOUNT_STATUS = "accountStatusesRef";
        public static final String CACHE_POST_STATUS = "postStatusesRef";
        public static final String CACHE_POST_TYPE = "postTypeRef";
        public static final String CACHE_SOURCE_STATUS = "sourceStatusesRef";
        public static final String CACHE_SOURCE_TYPE = "sourceTypeRef";
        public static final String CACHE_BOT_STATUS = "botStatusRef";
        public static final String CACHE_TASK_TYPE = "taskTypeRef";
        public static final String CACHE_TASK_RESULT = "taskResultRef";
    }

    // Kafka topics
    public static class Kafka {

        public static final String KAFKA_ACCOUNTS = "insta_Accounts";
        public static final String KAFKA_POSTS = "insta_Posts";
        public static final String KAFKA_SOURCES = "insta_Sources";
        public static final String KAFKA_SOURCES_TASKS = "insta_SourcesTasks";
        public static final String KAFKA_VECTORS = "insta_Vectors";
        public static final String KAFKA_PICTURES = "insta_Pictures";
    }
}
