/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.liases.exception;

import com.kdg.fs24.application.core.exception.api.InternalAppException;

/**
 *
 * @author Козыро Дмитрий
 */
public class LiasRestIsNegative extends InternalAppException {

    public LiasRestIsNegative(final String message) {
        super(message);
    }
}
