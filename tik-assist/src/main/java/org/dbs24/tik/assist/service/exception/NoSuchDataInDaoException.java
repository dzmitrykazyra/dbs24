package org.dbs24.tik.assist.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoSuchDataInDaoException extends ResponseStatusException {

    public NoSuchDataInDaoException(HttpStatus status) {
        super(status);
    }
}
