/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.api;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import java.util.Collection;
import org.dbs24.application.core.nullsafe.NullSafe;

/**
 *
 * @author kazyra_d
 */
public abstract class AbstractCardFile<T extends ReferenceRec> {

    private Collection<T> cardFiles;

    public AbstractCardFile() {
        super();
        //loadReference(BOOLEAN_TRUE, 0);
    }

    //==========================================================================
    protected abstract void loadReference( Boolean needReload, Long id);

    //==========================================================================
    public Collection<T> getCardFiles() {
        if (NullSafe.isNull(cardFiles)) {
            cardFiles = (Collection<T>) ServiceFuncs.<T>createCollection();
        }
        return cardFiles;
    }

    public void setCardFiles( Collection<T> cardFiles) {
        this.cardFiles = cardFiles;
    }

}
