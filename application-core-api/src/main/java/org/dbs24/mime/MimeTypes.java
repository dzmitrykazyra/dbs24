/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.mime;

public enum MimeTypes {

    ENTITY_ACTION_ID("entity/action.id", (byte) 0x10),
    ENTITY_USER_ID("entity/user.id", (byte) 0x11),
    ENTITY_CLASS_NAME("entity/class.name", (byte) 0x12);

    //==========================================================================
    private final byte code;
    private final String value;

    MimeTypes(String value, byte code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public byte getCode() {
        return code;
    }

}
