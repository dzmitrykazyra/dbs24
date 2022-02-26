/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.validator;

import org.dbs24.spring.core.api.RefEnum;

public enum Error implements RefEnum {

    GENERAL_ERROR("general.error", 1),
    MONITORING_ERROR_STARTING_APPLICATION("app.error", 2),
//    MONITORING_SUCCESS_STARTING_APPLICATION("app.finish", 3),
//    MONITORING_FINISH_APPLICATION("app.finish", 4),
//    MONITORING_ABSTRACT_ERROR("app.abstract.error", 100),
//    MONITORING_LIVENESS("app.abstract.error", 200),
//    MONITORING_TOKEN("app.token.exception", 1100),
//    MONITORING_TOKEN_EXCEPTION("app.token", 1110);
    INVALID_ENTITY_ATTR("entity.attrs", 100001);
    

    //==========================================================================
    private final Integer code;
    private final String value;

    Error(String value, int code) {
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
