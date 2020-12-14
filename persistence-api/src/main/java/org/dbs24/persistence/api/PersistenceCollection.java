/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.persistence.api;

import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import static org.dbs24.consts.SysConst.*;
import lombok.Data;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
public class PersistenceCollection<T extends PersistenceEntity> {

    private final AtomicBoolean isValid = NullSafe.createObject(AtomicBoolean.class);
    private final Collection<T> persistenceCollection = ServiceFuncs.createCollection();

    public Collection<T> getPersistenceCollection() {

        synchronized (this.isValid) {
            if (this.isValid.compareAndSet(BOOLEAN_TRUE, BOOLEAN_FALSE)) {

                persistenceCollection.clear();
                

                this.isValid.set(BOOLEAN_TRUE);
            }
        }

        return persistenceCollection;
    }
}
