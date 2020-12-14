/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.persistence.core;

import javax.persistence.EntityManager;

@FunctionalInterface
public interface TransactionStmts {
    void execute(EntityManager entityManager);
}
