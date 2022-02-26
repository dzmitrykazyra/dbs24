package org.dbs24.exception;

import org.dbs24.application.core.exception.api.InternalAppException;

public class BadEqualsAndHashCodeConfig extends InternalAppException {

    public BadEqualsAndHashCodeConfig(String message) {
        super(message);
    }
}
