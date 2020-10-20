/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.application.functionsgroups;

import org.dbs24.references.api.ReferenceRec;
import org.dbs24.references.api.AbstractRefRecord;
import java.util.Map;

/**
 *
 * @author kazyra_d
 */
public class FunctionGroup extends AbstractRefRecord implements ReferenceRec {

    private Integer function_group_id;
    private String function_group_code;
    private String function_group_name;

    public FunctionGroup() {
        super();
    }

    //==========================================================================
    public String getFunction_group_code() {
        return function_group_code;
    }

    public FunctionGroup setFunction_group_code(final String function_group_code) {
        this.function_group_code = function_group_code;
        return this;
    }

    public String getFunction_group_name() {
        return function_group_name;
    }

    public FunctionGroup setFunction_group_name(final String function_group_name) {
        this.function_group_name = function_group_name;
        return this;
    }

    public Integer getFunction_group_id() {
        return function_group_id;
    }

    public FunctionGroup setFunction_group_id(final Integer function_group_id) {
        this.function_group_id = function_group_id;
        return this;
    }
}
