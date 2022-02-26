/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.fs.consts;

import static org.dbs24.consts.RestHttpConsts.URI_API;

public final class IfsConst {

    // Package consts
    public static class PkgConsts {

        public static final String IFS_PACKAGE = "org.dbs24.insta.fs";
        public static final String IFS_PACKAGE_REPO = IFS_PACKAGE + ".repo";
    }

    // URI
    public static class UriConsts {

        public static final String URI_CREATE_OR_UPDATE_ACCOUNT = URI_API.concat("/createOrUpdateAccount");
        public static final String URI_VALIDATE_ACCOUNT = URI_API.concat("/validateAccount");
        public static final String URI_CREATE_OR_UPDATE_POST = URI_API.concat("/createOrUpdatePost");
        public static final String URI_CREATE_OR_UPDATE_SOURCE = URI_API.concat("/createOrUpdateSource");
        public static final String URI_CREATE_OR_UPDATE_FACE = URI_API.concat("/createOrUpdateFace");

    }

    public static class RestQueryParams {

        public static final String QP_INSTA_ID = "instaId";
    }

    public static class References {

        public static class ActionType {

            public static final Integer AT_CREATE_ACCOUNT = Integer.valueOf("10");
            public static final Integer AS_CLOSE_ACCOUNT = Integer.valueOf("100");

            public static final String[][] ALL_ACTION_TYPES = new String[][]{
                {String.valueOf(AT_CREATE_ACCOUNT), "Create or update account"},
                {String.valueOf(AS_CLOSE_ACCOUNT), "Close account"}};

        }        
        
        public static class ActionResult {

            public static final Integer AR_OK = Integer.valueOf("1");
            public static final Integer AR_FAIL = Integer.valueOf("-1");

            public static final String[][] ALL_ACTION_RESULTS = new String[][]{
                {String.valueOf(AR_OK), "OK"},
                {String.valueOf(AR_FAIL), "Fail"}};

        }
        
        public static class TaskStatuses {

            public static final Integer TS_CREATED = Integer.valueOf("1");
            public static final Integer TS_IN_PROGRESS = Integer.valueOf("10");
            public static final Integer TS_POST_PONED = Integer.valueOf("100");
            public static final Integer TS_FINISHED = Integer.valueOf("200");
            public static final Integer TS_ABORTED = Integer.valueOf("500");            

            public static final String[][] ALL_TASK_STATUSES = new String[][]{
                {String.valueOf(TS_CREATED), "Created"},
                {String.valueOf(TS_IN_PROGRESS), "In progress"},
            {String.valueOf(TS_POST_PONED), "Post poned"},
            {String.valueOf(TS_FINISHED), "Finished"},
            {String.valueOf(TS_ABORTED), "Aborted"}};

        }        
        
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

    }
}
