package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ActionTypeMismatchException extends ResponseStatusException {

    public ActionTypeMismatchException(HttpStatus status) {
        super(status);
    }
}
