package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserIsNotActivatedException extends ResponseStatusException {

    public UserIsNotActivatedException(HttpStatus status) {
        super(status);
    }
}
