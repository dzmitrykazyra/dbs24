/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.test.api.exception;

import com.kdg.fs24.application.core.exception.api.InternalAppException;

/**
 *
 * @author Козыро Дмитрий
 */
public class TestExecutionException extends InternalAppException {

    public TestExecutionException(final String message) {
        super(message);
    }
}
