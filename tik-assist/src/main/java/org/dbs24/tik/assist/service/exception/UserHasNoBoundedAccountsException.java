package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserHasNoBoundedAccountsException extends ResponseStatusException {

    public UserHasNoBoundedAccountsException(HttpStatus status) {
        super(status);
    }
}
