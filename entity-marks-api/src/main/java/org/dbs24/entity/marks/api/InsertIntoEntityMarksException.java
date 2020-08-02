/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.marks.api;

import org.dbs24.application.core.exception.api.InternalAppException;

/**
 *
 * @author kazyra_d
 */
public class InsertIntoEntityMarksException extends InternalAppException {

    public InsertIntoEntityMarksException(final String message) {
        super(message);
    }

}
