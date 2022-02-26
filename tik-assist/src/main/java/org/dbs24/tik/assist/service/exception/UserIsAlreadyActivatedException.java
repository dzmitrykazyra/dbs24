package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserIsAlreadyActivatedException extends ResponseStatusException {

    public UserIsAlreadyActivatedException(HttpStatus status) {
        super(status);
    }
}
