/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.consts;

import org.dbs24.entity.core.api.ActionClassesCollectionLink;
import org.dbs24.entity.core.api.ViewField;


public final class EntityConst {

    public static final int ES_ACTUAL = 0;
    public static final int ES_CLOSED = 1;
    public static final int ES_CANCELLED = -1;

    public static final int ACT_SAVE_DEFAULT_ENTITY_FIELDS = 501;
    public static final int ACT_RESET_DEFAULT_ENTITY_FIELDS = 502;    
    
//    public static final Boolean IS_AUTHORIZED = Boolean.TRUE;
//    public static final Boolean NOT_AUTHORIZED = Boolean.FALSE;

//    public static final int MR_AUTHORIZE_ENTITY = 1000001;
//    public static final int MR_AUTHORIZE_ENTITY_AUTH = 100000101;
//    public static final int MR_AUTHORIZE_ENTITY_NOT_AUTH = 100000102;

    public static final Boolean VIEW_ACTIONS_ONLY = Boolean.TRUE;
    public static final Boolean GET_ALL_ACTIONS = Boolean.FALSE;

    public static final Class<ViewField> VIEW_FIELD_CLASS = ViewField.class;
    public static final Class<ActionClassesCollectionLink> ACTION_CLASSES_COLLECTION_LINK_CLASS = ActionClassesCollectionLink.class;

}
