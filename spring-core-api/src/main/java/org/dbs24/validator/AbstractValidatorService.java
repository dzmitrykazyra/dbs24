/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.validator;

import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.validator.exception.GeneralValidatorException;

import java.util.Collection;

@Log4j2
public abstract class AbstractValidatorService<T> extends AbstractApplicationService {

    //==========================================================================    
    @Override
    public void initialize() {
        final String className = this.getClass().getSimpleName();
        log.info("Validator '{}' is activated", className.indexOf("$$") > 0 ? className.substring(0, className.indexOf("$$")) : className);
    }

    public abstract Collection<ErrorInfo> validate(T validatedEntity);

    protected void assignDefaults(T validatedEntity) {

    }

    public void validateThrow(T validatedEntity) {
        validate(validatedEntity)
                .stream()
                .findFirst()
                .ifPresent(err -> {
                    log.error("{}: {}", this.getClass().getSimpleName(), err);
                    throw new GeneralValidatorException(String.format("Error %s, '%s' ", err.getError(), err.getErrorMsg()));
                });
    }

    //==========================================================================
    protected ErrorInfo createErrMsg(Error error, String errorMsg) {

        return StmtProcessor.create(ErrorInfo.class, ei -> {
            ei.setErrorMsg(errorMsg);
            ei.setError(error);
        });
    }

    //==========================================================================
    public Collection<ErrorInfo> validateConditional(T validatedEntity, ValidateAction<T> validAction, InvalidAction invalidAction) {

        // can be overriden
        assignDefaults(validatedEntity);

        final Collection<ErrorInfo> errors = validate(validatedEntity);

        if (errors.isEmpty()) {
            validAction.action(validatedEntity);
        } else {
            invalidAction.invalidAction(errors);
        }

        if (!errors.isEmpty()) {
            log.error("{}: there is/are error(s): {}", validatedEntity.getClass().getSimpleName(), errors.size());
            log.error("{}: entity errors: {}", validatedEntity.getClass().getSimpleName(), errors);
            log.error("{}: invalid entity attrs: {}", validatedEntity.getClass().getSimpleName(), validatedEntity);
        }

        return errors;
    }

    //==========================================================================
    public Collection<ErrorInfo> validateConditional(T validatedEntity, ValidateAction<T> validAction, InvalidAction invalidAction, ExceptionAction exceptionAction) {

        return ServiceFuncs.<ErrorInfo>createCollection(errors ->

                StmtProcessor.createCatch(() -> validateConditional(validatedEntity, validAction, invalidAction)
                        .forEach(error -> errors.add(error)), throwable -> exceptionAction.processException(throwable))
        );
    }
}
