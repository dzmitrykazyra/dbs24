/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.validator;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;

@Data
public class ErrorInfo {

    private Error error;
    private Field field;
    private String errorMsg;

    public static ErrorInfo create(Error error, Field field, String errorMsg) {
        return StmtProcessor.create(ErrorInfo.class, ei -> {
            ei.setErrorMsg(errorMsg);
            ei.setField(field);
            ei.setError(error);
        });
    }
}
