/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.fields.desc;


import java.util.Map;
import org.dbs24.references.api.ReferenceRec;
import org.dbs24.references.api.AbstractRefRecord;

/**
 *
 * @author kazyra_d
 */
public class AppFieldCaption  extends AbstractRefRecord implements ReferenceRec {

    private Long user_id;
    private Integer app_id;
    private String field_name;
    private String field_caption;
    private String field_tooltip;

    public AppFieldCaption() {
        super();
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(final Long user_id) {
        this.user_id = user_id;
    }

    public Integer getApp_id() {
        return app_id;
    }

    public void setApp_id(final Integer app_id) {
        this.app_id = app_id;
    }

    public String getField_name() {
        return field_name;
    }

    public void setField_name(final String field_name) {
        this.field_name = field_name;
    }

    public String getField_caption() {
        return field_caption;
    }

    public void setField_caption(final String field_caption) {
        this.field_caption = field_caption;
    }

    public String getField_tooltip() {
        return field_tooltip;
    }

    public void setField_tooltip(final String field_tooltip) {
        this.field_tooltip = field_tooltip;
    }
}
