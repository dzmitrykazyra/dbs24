package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailIsAlreadyRegisteredException extends ResponseStatusException {

    public EmailIsAlreadyRegisteredException(HttpStatus status) {
        super(status);
    }
}
