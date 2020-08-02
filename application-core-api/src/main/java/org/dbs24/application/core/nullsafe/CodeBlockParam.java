/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.nullsafe;

/**
 *
 * @author kazyra_d
 */
@FunctionalInterface
public interface CodeBlockParam {

    void someCode(Object object) throws Throwable;
}
