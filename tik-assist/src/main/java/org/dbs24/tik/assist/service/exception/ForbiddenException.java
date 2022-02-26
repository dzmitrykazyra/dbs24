package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ForbiddenException extends ResponseStatusException {

    public ForbiddenException(HttpStatus status) {
        super(status);
    }
}
