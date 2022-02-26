/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api.consts;

import org.dbs24.spring.core.api.RefEnum;

public class RestApiConst {

    public static class OperCode {

        public static final int OC_OK = 0;
        public static final String OC_OK_STR = "OK";
        public static final int OC_GENEARL_ERROR = -100;
        public static final String OC_GENERAL_ERROR_STR = "FAIL";


    }

    public enum RestOperCode implements RefEnum {

        OC_OK("OK", 200),
        OC_GENERAL_ERROR("GENERAL ERROR", 501),
        OC_UNKNOWN_ERROR("UNKNOWN ERROR", 502),
        OC_INVALID_ENTITY_ATTRS("There is|are error(s)", 1001),
        // 

        OC_NO_PROXY_AVAILABLE("No proxy", 1000001);

        //======================================================================
        private final Integer code;
        private final String value;

        RestOperCode(String value, Integer code) {
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
}
