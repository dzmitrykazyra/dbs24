package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TiktokAccountIsNotLinkedException extends ResponseStatusException {

    public TiktokAccountIsNotLinkedException(HttpStatus status) {
        super(status);
    }
}
