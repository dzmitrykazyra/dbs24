package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PasswordMismatchException extends ResponseStatusException {

    public PasswordMismatchException(HttpStatus status) {
        super(status);
    }
}
