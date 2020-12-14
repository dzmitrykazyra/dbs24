/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test.api;

/**
 *
 * @author Козыро Дмитрий
 */
@FunctionalInterface
public interface PropertyHandler {
    Object assign_property( String propertyName, Object value);
}
