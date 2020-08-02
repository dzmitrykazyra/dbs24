/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.rest.exception;

/**
 *
 * @author kazyra_d
 */
public class JSONConversionError extends Exception {

    public JSONConversionError(final String message) {
        super(message);
    }

}
