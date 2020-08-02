/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.application.core.thread;

import com.kdg.fs24.application.core.exception.api.InternalAppException;

/**
 *
 * @author Козыро Дмитрий
 */
public class AbstractThreadException extends InternalAppException {

    public AbstractThreadException(final String message) {
        super(message);
    }
}
