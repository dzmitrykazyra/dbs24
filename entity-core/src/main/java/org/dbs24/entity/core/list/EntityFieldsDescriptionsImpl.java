/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.core.list;

/**
 *
 * @author kazyra_d
 */
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.entity.core.api.EntityFieldsDescriptions;
import java.util.Collection;
import org.dbs24.fields.desc.FieldDescription;
import org.dbs24.application.core.nullsafe.NullSafe;

public class EntityFieldsDescriptionsImpl implements EntityFieldsDescriptions {

    private final Collection<FieldDescription> fieldsList = ServiceFuncs.<FieldDescription>createCollection();

    public Collection<FieldDescription> getFieldsList() {
        return fieldsList;
    }

    public void addFieldsDescription( String fldName, String fldCaption, String fldToolTip) {

        getFieldsList().add(new FieldDescription() {

            @Override
            public String getField_name() {
                return fldName;
            }

            @Override
            public String getField_caption() {
                return fldCaption;
            }

            @Override
            public String getField_tooltip() {
                return fldToolTip;
            }

        });
    }

    //==========================================================================
    private FieldDescription findFieldDescription( String fldName) {

        return ServiceFuncs.<FieldDescription>getCollectionElement(this.getFieldsList(),
                p -> p.getField_name().equals(fldName),
                String.format("field is not found (%s)", fldName));
    }

    //==========================================================================
    @Override
    public String get_field_caption( String fldName) {

        String field_caption = null;
        FieldDescription fieldDescription = null;

        fieldDescription = this.findFieldDescription(fldName);

        if (NullSafe.notNull(fieldDescription)) {
            field_caption = fieldDescription.getField_caption();
        }

        return field_caption;
    }

    //==========================================================================
    @Override
    public String get_field_tool_tip( String fldName) {

        String field_tool_tip = null;
        FieldDescription fieldDescription = null;

        fieldDescription = this.findFieldDescription(fldName);

        if (NullSafe.notNull(fieldDescription)) {
            field_tool_tip = fieldDescription.getField_tooltip();
        }

        return field_tool_tip;
    }
}
