/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.attr;

import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.api.ReferenceRec;
import java.util.Map;

/**
 *
 * @author kazyra_d
 */
public class EntAttr extends AbstractRefRecord implements ReferenceRec {

    private Integer attr_id;
    private String attr_code;
    private String attr_name;

    public Integer getAttr_id() {
        return attr_id;
    }

    public EntAttr setAttr_id(Integer attr_id) {
        this.attr_id = attr_id;
        return this;
    }

    public String getAttr_name() {
        return attr_name;
    }

    public EntAttr setAttr_name(String attr_name) {
        this.attr_name = attr_name;
        return this;
    }

    protected String getAttr_code() {
        return attr_code;
    }

    protected EntAttr setAttr_code(String attr_code) {
        this.attr_code = attr_code;
        return this;
    }

    @Override
    public void record2Map(final Map<String, Integer> map) {
        map.put(String.format("%d - %s", this.getAttr_id(), this.getAttr_name()), this.getAttr_id());
    }

}
