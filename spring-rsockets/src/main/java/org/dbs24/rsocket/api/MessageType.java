/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rsocket.api;

public enum MessageType {

    MONITORING_START_APPLICATION("app.start", 1),
    MONITORING_ERROR_STARTING_APPLICATION("app.error", 2),
    MONITORING_SUCCESS_STARTING_APPLICATION("app.finish", 3),
    MONITORING_FINISH_APPLICATION("app.finish", 4),
    MONITORING_ABSTRACT_ERROR("app.abstract.error", 100),
    MONITORING_LIVENESS("app.abstract.error", 200),
    MONITORING_TOKEN("app.token.exception", 1100),
    MONITORING_TOKEN_EXCEPTION("app.token", 1110);
    

    //==========================================================================
    private final int code;
    private final String value;

    MessageType(String value, int code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }
}
