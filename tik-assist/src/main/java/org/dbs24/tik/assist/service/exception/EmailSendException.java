package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailSendException extends ResponseStatusException {

    public EmailSendException(HttpStatus status) {
        super(status);
    }
}
