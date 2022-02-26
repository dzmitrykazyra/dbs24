package org.dbs24.tik.mobile.service.exception.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoContentException extends ResponseStatusException {

    public NoContentException() {

        super(HttpStatus.NO_CONTENT);
    }
}
