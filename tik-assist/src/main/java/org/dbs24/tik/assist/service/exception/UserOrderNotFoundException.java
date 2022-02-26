package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserOrderNotFoundException extends ResponseStatusException {

    public UserOrderNotFoundException(HttpStatus status) {
        super(status);
    }
}
