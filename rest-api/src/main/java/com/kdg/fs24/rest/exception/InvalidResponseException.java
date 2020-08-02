/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.rest.exception;

public class InvalidResponseException extends Exception {
    public InvalidResponseException() {
    }

    public InvalidResponseException(final String message) {
        super(message);
    }

    public InvalidResponseException(final String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidResponseException(final Throwable cause) {
        super(cause);
    }

    public InvalidResponseException(final String message, Throwable cause, final Boolean enableSuppression, final Boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

