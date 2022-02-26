package org.dbs24.tik.mobile.service.exception.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestException extends ResponseStatusException {

    public BadRequestException() {

        super(HttpStatus.BAD_REQUEST);
    }
}
