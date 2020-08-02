/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.api;

import com.kdg.fs24.entity.attr.EntAttr;
import com.kdg.fs24.entity.attr.EntityAttrId;
import com.kdg.fs24.entity.attr.EntityAttribute;
import com.kdg.fs24.entity.attr.template.AbstractEntityAttrsTemplate;
import com.kdg.fs24.entity.attr.template.EntAttrTemplate;
import com.kdg.fs24.entity.attr.template.EntityAttrTemplateId;
import com.kdg.fs24.entity.attr.template.EntityAttrRow;

/**
 *
 * @author Козыро Дмитрий
 */
public final class EntityReferenceConst {

    public static final Class<EntAttr> ENT_ATTR_CLASS = EntAttr.class;
    public static final Class<EntityAttrId> ENT_ATTR_ID_ANN = EntityAttrId.class;
    public static final Class<EntityAttribute> ENT_ATTRIBUTE_CLASS = EntityAttribute.class;
    
    public static final Class<AbstractEntityAttrsTemplate> ENT_LIST_ATTR_TEMPLATE_CLASS = AbstractEntityAttrsTemplate.class;
    public static final Class<EntAttrTemplate> ENT_ATTR_TEMPLATE_CLASS = EntAttrTemplate.class;
    public static final Class<EntityAttrTemplateId> ENT_ATTR_TEMPLATE_ID_ANN = EntityAttrTemplateId.class;
    public static final Class<EntityAttrRow> ENT_ATTR_ROW_ANN = EntityAttrRow.class;

    public static final String ENTITY_ATTRS_CLASSES_PACKAGE = "com.kdg.fs24.entity.core.attr.list";
    public static final String ENTITY_ATTR_TEMPLATE_CLASSES_PACKAGE = "com.kdg.fs24.entity.attrs.template";    
}
