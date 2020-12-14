/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.fields.desc;

import org.dbs24.application.core.exception.api.InternalAppException;

/**
 *
 * @author kazyra_d
 */
public class InvalidFieldDescription extends InternalAppException {

    public InvalidFieldDescription( String message) {
        super(message);
    }

}
