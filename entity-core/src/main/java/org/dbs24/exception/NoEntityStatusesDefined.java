/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.exception;

import org.dbs24.application.core.exception.api.InternalAppException;

public class NoEntityStatusesDefined extends InternalAppException {

    public NoEntityStatusesDefined(String message) {
        super(message);
    }
}
