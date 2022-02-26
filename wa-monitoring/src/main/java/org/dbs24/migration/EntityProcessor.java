/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.migration;

@FunctionalInterface
public interface EntityProcessor {
    boolean perform(Integer id1, Integer id2);
}
