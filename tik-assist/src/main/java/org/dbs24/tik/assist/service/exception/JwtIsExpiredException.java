package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class JwtIsExpiredException extends ResponseStatusException {

    public JwtIsExpiredException(HttpStatus status) {
        super(status);
    }
}
