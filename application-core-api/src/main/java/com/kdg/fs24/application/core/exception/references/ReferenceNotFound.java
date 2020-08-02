/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.exception.references;

import com.kdg.fs24.application.core.exception.api.InternalAppException;

/**
 *
 * @author kazyra_d
 */
public class ReferenceNotFound extends InternalAppException {

    public ReferenceNotFound() {
        super();

    }

    public ReferenceNotFound(final String aErrMsg) {
        super(aErrMsg);
    }

}