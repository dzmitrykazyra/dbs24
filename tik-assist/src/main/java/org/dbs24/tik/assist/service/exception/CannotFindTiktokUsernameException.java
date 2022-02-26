package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CannotFindTiktokUsernameException extends ResponseStatusException {

    public CannotFindTiktokUsernameException(HttpStatus status) {
        super(status);
    }
}
