package org.dbs24.tik.mobile.service.exception.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnprocessableEntityException extends ResponseStatusException {

    public UnprocessableEntityException() {

        super(HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
