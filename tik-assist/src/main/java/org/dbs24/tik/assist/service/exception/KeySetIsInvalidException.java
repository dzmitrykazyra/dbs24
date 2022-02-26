package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class KeySetIsInvalidException extends ResponseStatusException {

    public KeySetIsInvalidException(HttpStatus status) {
        super(status);
    }
}
