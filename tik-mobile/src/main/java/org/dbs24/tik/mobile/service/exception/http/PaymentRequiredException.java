package org.dbs24.tik.mobile.service.exception.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PaymentRequiredException extends ResponseStatusException {

    public PaymentRequiredException() {
        super(HttpStatus.PAYMENT_REQUIRED);
    }
}
