/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.thread;

import org.dbs24.application.core.exception.api.InternalAppException;

/**
 *
 * @author Козыро Дмитрий
 */
public class AbstractThreadException extends InternalAppException {

    public AbstractThreadException( String message) {
        super(message);
    }
}
