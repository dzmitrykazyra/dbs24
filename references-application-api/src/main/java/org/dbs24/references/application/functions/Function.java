/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.application.functions;

import org.dbs24.references.api.ReferenceRec;
import java.util.Map;
import org.dbs24.references.api.AbstractRefRecord;

/**
 *
 * @author kazyra_d
 */
public class Function extends AbstractRefRecord implements ReferenceRec {

    private Integer function_id;
    private Integer function_group_id;
    private String function_code;
    private String function_name;

    public Function() {
        super();
    }

    //==========================================================================
    public Integer getFunction_id() {
        return function_id;
    }

    public Function setFunction_id( Integer function_id) {
        this.function_id = function_id;
        return this;
    }

    public String getFunction_code() {
        return function_code;
    }

    public Function setFunction_code( String function_code) {
        this.function_code = function_code;
        return this;
    }

    public String getFunction_name() {
        return function_name;
    }

    public Function setFunction_name( String function_name) {
        this.function_name = function_name;
        return this;
    }

    public Integer getFunction_group_id() {
        return function_group_id;
    }

    public Function setFunction_group_id( Integer function_group_id) {
        this.function_group_id = function_group_id;
        return this;
    }
}
