/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api.action;

import org.dbs24.spring.core.api.RefEnum;

public enum RestAction implements RefEnum {

    CREATE_ENTITY("entity.crete", 1000000),
    MODIFY_ENTITY("entity.modify", 1000001);

    //==========================================================================
    private final Integer code;
    private final String value;

    RestAction(String value, Integer code) {
        this.value = value;
        this.code = code;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
