package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidOrderDataException extends ResponseStatusException {

    public InvalidOrderDataException(HttpStatus status) {
        super(status);
    }
}
