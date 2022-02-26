/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.consts;

import static org.dbs24.consts.RestHttpConsts.URI_API;
import org.dbs24.entity.marks.*;
import org.dbs24.entity.attr.EntAttr;
import org.dbs24.entity.attr.EntityAttrId;
import org.dbs24.entity.attr.template.EntAttrTemplate;
import org.dbs24.entity.attr.template.EntityAttrTemplateId;
import org.dbs24.entity.attr.template.EntityAttrRow;

public final class EntityReferenceConst {

    public static final Boolean IS_AUTHORIZED = Boolean.TRUE;
    public static final Boolean NOT_AUTHORIZED = Boolean.FALSE;

    public static final int MR_AUTHORIZE_ENTITY = 1000001;
    public static final int MR_AUTHORIZE_ENTITY_AUTH = 100000101;
    public static final int MR_AUTHORIZE_ENTITY_NOT_AUTH = 100000102;

    public static final int ACT_AUTHORIZE_CONTRACT = 100000001;
    public static final int ACT_FINISH_CONTRACT = 100000010;
    public static final int ACT_CANCEL_CONTRACT = 100000011;
    public static final int ACT_REOPEN_CONTRACT = 100000012;
    public static final int ACT_CALCULATE_TARIFFS = 100000020;
    public static final int ACT_CALCULATE_TARIFFS2 = 100000021;

    public static final int ACT_ACCRUE_INTEREST = 100000100;

    public static final String URI_CREATE_LOAN_CONTRACT = URI_API.concat("/createRetailLoanContract");
    public static final String URI_EXECUTE_ACTION = URI_API.concat("/executeAction");    
    
    public static final Class<EntAttr> ENT_ATTR_CLASS = EntAttr.class;
    public static final Class<EntityAttrId> ENT_ATTR_ID_ANN = EntityAttrId.class;
    public static final Class<EntAttrTemplate> ENT_ATTR_TEMPLATE_CLASS = EntAttrTemplate.class;
    public static final Class<EntityAttrTemplateId> ENT_ATTR_TEMPLATE_ID_ANN = EntityAttrTemplateId.class;
    public static final Class<EntityAttrRow> ENT_ATTR_ROW_ANN = EntityAttrRow.class;
    public static final Class<Mark> MARK_CLASS = Mark.class;
    public static final Class<MarkValue> MARK_VALUE_CLASS = MarkValue.class;

    public static final String ENTITY_ATTRS_CLASSES_PACKAGE = "org.dbs24.entity.core.attr.list";
    public static final String ENTITY_ATTR_TEMPLATE_CLASSES_PACKAGE = "org.dbs24.entity.attrs.template";
}
