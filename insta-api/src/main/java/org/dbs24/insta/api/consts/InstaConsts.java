/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.api.consts;

import java.util.Arrays;
import java.util.Collection;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.stmt.StmtProcessor;

public final class InstaConsts {

    // Package consts
    public static class PkgConsts {

        public static final String TIK_PACKAGE = "org.dbs24.insta.api";
        public static final String TIK_PACKAGE_REPO = TIK_PACKAGE + ".repo";
    }

    public static class Topics {

        public static final String IFS_ACCOUNT = "ifs_Account";
        public static final String IFS_POST = "ifs_Post";
        public static final String IFS_SOURCE = "ifs_Source";
        public static final String IFS_IMAGE = "ifs_Image";
        public static final String IFS_VECTOR = "ifs_Vector";
        public static final String IFS_SERVICE_TASK = "ifs_ServiceTask";
        public static final String IFS_CLIENT_SEARCH = "ifs_ClientSearch";
        public static final String IFS_AGENT = "ifs_Agents";
        public static final String IFS_TASK_BUILDER = "ifs_TaskBuilder";
        public static final String IFS_ACCOUNT_IS_CREATED = "ifs_AccountIsCreated";
        public static final String IFS_GET_FOLLOWERS_TASK = "ifs_GetFollowersTask";
        public static final String IFS_CREATE_FOLLOWERS_LIST = "ifs_ReceiveFollowersList";
                
    }

    // Types
    public enum Topic {

        IFS_ACCOUNT("ifs_Account", 10),
        IFS_POST("ifs_Post", 20),
        IFS_SOURCE("ifs_Source", 30),
        IFS_IMAGE("ifs_Image", 40),
        IFS_VECTOR("ifs_Vector", 50),
        IFS_SERVICE_TASK("ifs_ServiceTask", 60),
        IFS_CLIENT_SEARCH("ifs_ClientSearch", 70),
        IFS_AGENT("ifs_Agents", 80),
        IFS_TASK_BUILDER("ifs_TaskBuilder", 200),
        IFS_ACCOUNT_IS_CREATED("ifs_AccountIsCreated", 210),
        IFS_GET_FOLLOWERS_TASK("ifs_GetFollowersTask", 220),
        IFS_RECEIVE_FOLLOWERS_LIST("ifs_GetFollowersTask", 220);

//        public static final Collection<ProxyType> PROXY_TYPES = ServiceFuncs.<ProxyType>createCollection(cp -> Arrays.stream(ProxyTypeEnum.values())
//                .map(stringRow -> StmtProcessor.create(ProxyType.class, record -> {
//            record.setProxyTypeId(stringRow.getCode());
//            record.setProxyTypeName(stringRow.getValue());
//        })).forEach(ref -> cp.add(ref))
//        );
//
//        public static final Collection<Integer> PROXY_TYPES_IDS = ServiceFuncs.createCollection(cp -> PROXY_TYPES.stream().map(t -> t.getProxyTypeId()).forEach(ref -> cp.add(ref)));
        private final int code;
        private final String value;

        Topic(String value, int code) {
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

    // Consumers
    public static class Consumers {

        public static final String CON_GROUP_ID = "instaGroup";
    }

    // Receivers
    public static class Receivers {

    }

}
