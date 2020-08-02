/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.bond.schedule.collection;

import com.kdg.fs24.exception.api.InternalAppException;

/**
 *
 * @author Козыро Дмитрий
 */
public class AlgBuilderNotFound extends InternalAppException {

    public AlgBuilderNotFound(final String message) {
        super(message);
    }

}
