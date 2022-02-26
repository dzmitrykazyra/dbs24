package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TiktokAccountAlsoHaveSubscriptionException extends ResponseStatusException {

    public TiktokAccountAlsoHaveSubscriptionException(HttpStatus status) {
        super(status);
    }
}
