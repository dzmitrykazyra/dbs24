/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.api;

@FunctionalInterface
public interface ReferenceProcessor<T extends AbstractRefRecord> {
    void processRef(T record, String[] stringRecord);
}
