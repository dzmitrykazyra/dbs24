package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IncorrectPasswordException extends ResponseStatusException {

    public IncorrectPasswordException(HttpStatus status) {
        super(status);
    }
}
