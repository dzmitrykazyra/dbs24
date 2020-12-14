/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.stmt;

@FunctionalInterface
public interface Stmt1<T, P1> {

    T execute(P1 p1) throws Throwable;
}
