/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.attr.template;

import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.api.ReferenceRec;
import java.util.Map;


public class EntAttrTemplate extends AbstractRefRecord implements ReferenceRec {

    private Integer attr_template_id;
    private Integer attr_id;

    protected Integer getAttr_template_id() {
        return attr_template_id;
    }

    protected EntAttrTemplate setAttr_template_id( Integer attr_template_id) {
        this.attr_template_id = attr_template_id;
        return this;
    }

    protected Integer getAttr_id() {
        return attr_id;
    }

    protected EntAttrTemplate setAttr_id( Integer attr_id) {
        this.attr_id = attr_id;
        return this;
    }
}
