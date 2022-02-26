package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailIsNotRegisteredException extends ResponseStatusException {

    public EmailIsNotRegisteredException(HttpStatus status) {
        super(status);
    }
}
