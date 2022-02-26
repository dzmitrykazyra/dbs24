package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoActiveUserSubscriptionException extends ResponseStatusException {

    public NoActiveUserSubscriptionException(HttpStatus status) {
        super(status);
    }
}
