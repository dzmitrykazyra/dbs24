/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.exception;

import org.dbs24.application.core.exception.api.InternalAppException;

public class EntityReactorException extends InternalAppException {

    public EntityReactorException(String message) {
        super(message);
    }    
}
