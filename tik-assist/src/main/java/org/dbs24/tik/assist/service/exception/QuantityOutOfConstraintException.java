package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class QuantityOutOfConstraintException extends ResponseStatusException {

    public QuantityOutOfConstraintException(HttpStatus status) {
        super(status);
    }
}
