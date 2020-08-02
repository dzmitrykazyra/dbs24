/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.fields.desc;

/**
 *
 * @author kazyra_d
 */
public class FieldDescriptionImpl implements FieldDescription {

    private String field_name;
    private String field_caption;
    private String field_tooltip;

    public FieldDescriptionImpl() {
        super();
    }

    //==========================================================================
    @Override
    public String getField_name() {
        return field_name;
    }

    public void setField_name(final String field_name) {
        this.field_name = field_name;
    }

    @Override
    public String getField_caption() {
        return field_caption;
    }

    public void setField_caption(final String field_caption) {
        this.field_caption = field_caption;
    }

    @Override
    public String getField_tooltip() {
        return field_tooltip;
    }

    public void setField_tooltip(final String field_tooltip) {
        this.field_tooltip = field_tooltip;
    }

}
