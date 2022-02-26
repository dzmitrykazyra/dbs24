/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.rest.api.consts.RestApiConst.RestOperCode;
import org.dbs24.spring.core.api.EntityInfo;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.validator.ErrorInfo;
import org.dbs24.validator.WarnInfo;

import java.util.Collection;

@Data
@Log4j2
public class ResponseBody<T extends EntityInfo> {

    private RestOperCode code;
    private String message;
    private String error;
    private boolean isCompleted = false;
    private T createdEntity;
    private Collection<ErrorInfo> errors;
    private Collection<WarnInfo> warnings;

    public void assign(ResponseBody<T> epr) {
        code = epr.getCode();
        error = epr.getError();
        message = epr.getMessage();
        createdEntity = epr.getCreatedEntity();
        errors = epr.getErrors();
        warnings = epr.getWarnings();
    }

    public void assignException(Throwable throwable) {
        code = RestOperCode.OC_GENERAL_ERROR;
        message = RestOperCode.OC_GENERAL_ERROR.getValue();
        error = throwable.getMessage();
    }

    public Integer addErrorInfo(ErrorInfo errorInfo) {

        StmtProcessor.ifNull(errors, () -> errors = ServiceFuncs.createCollection());
        errors.add(errorInfo);
        return errors.size();
    }

    public Integer addWarnInfo(WarnInfo warnInfo) {

        StmtProcessor.ifNull(warnings, () -> warnings = ServiceFuncs.createCollection());
        warnings.add(warnInfo);
        return warnings.size();
    }

    public void complete() {
        isCompleted = true;
    }

    public void setErrors(Collection<ErrorInfo> errors) {
        this.errors = errors;

        StmtProcessor.ifTrue(!errors.isEmpty(),
                () -> {
                    complete();
                    final Boolean singleError = (errors.size() == 1);
                    log.error("There {} error{}: {}",
                            singleError ? "is" : "are",
                            singleError ? "" : "s",
                            errors);
                });
    }
}
