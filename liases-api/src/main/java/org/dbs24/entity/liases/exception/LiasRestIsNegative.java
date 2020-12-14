/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.liases.exception;

import org.dbs24.application.core.exception.api.InternalAppException;

/**
 *
 * @author Козыро Дмитрий
 */
public class LiasRestIsNegative extends InternalAppException {

    public LiasRestIsNegative( String message) {
        super(message);
    }
}
