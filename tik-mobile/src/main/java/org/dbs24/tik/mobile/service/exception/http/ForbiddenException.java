package org.dbs24.tik.mobile.service.exception.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ForbiddenException extends ResponseStatusException {

    public ForbiddenException() {

        super(HttpStatus.FORBIDDEN);
    }
}
