/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.persistence.core;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.persistence.EntityManager;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;

@Data
@Log4j2
public final class TransactionEntityManager<T extends EntityManager> {
    
    private final AtomicBoolean isBusy = StmtProcessor.create(AtomicBoolean.class);
    
    private final T entityManager;
    
    public TransactionEntityManager(T t) {
        this.entityManager = t;
        isBusy.set(Boolean.FALSE);
    }
    
    public static TransactionEntityManager create(EntityManager entityManager) {
        return new TransactionEntityManager(entityManager);
    }
}
