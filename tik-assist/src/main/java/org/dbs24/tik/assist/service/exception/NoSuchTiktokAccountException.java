package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoSuchTiktokAccountException extends ResponseStatusException {

    public NoSuchTiktokAccountException(HttpStatus status) {
        super(status);
    }
}
