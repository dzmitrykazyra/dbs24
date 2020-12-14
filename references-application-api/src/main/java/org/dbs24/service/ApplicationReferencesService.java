/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import java.util.Collection;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.dbs24.component.PersistenceEntityManager;
import static org.dbs24.consts.ReferenceApplicationConst.*;
import org.dbs24.entity.*;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.core.CachedReferencesClasses;

@Data
@Service
@CachedReferencesClasses(classes = {Currency.class})
public class ApplicationReferencesService extends AbstractReferencesService {

    final PersistenceEntityManager persistenceEntityManager;

    @Autowired
    public ApplicationReferencesService(PersistenceEntityManager persistenceEntityManager) {
        this.persistenceEntityManager = persistenceEntityManager;
    }

    //==========================================================================
//    @Deprecated
//    public final void createCurrency(Integer currencyId,
//            final String currencyIso, String currencyName) {
//
//        persistenceEntityManager.<Currency>mergePersistenceEntity(Currency.class,
//                currency -> {
//                    currency.setCurrencyId(currencyId);
//                    currency.setCurrencyIso(currencyIso);
//                    currency.setCurrencyName(currencyName);
//                });
//    }

    //==========================================================================
    public static final Currency findCurrency(Integer currencyId) {
        return AbstractRefRecord.<Currency>getRefeenceRecord(Currency.class,
                record -> record.getCurrencyId().equals(currencyId));
    }

    //==========================================================================
    public static final Collection<Currency> getCurrencyCollection() {

        return getGenericCollection(CURRENCY_CLASS, new String[][]{
            {"840", "USD", "Доллар США"},
            {"978", "EUR", "Евро"},
            {"933", "BYN", "Белорусский рубль"},
            {"999", "999", "9999"}
        }, (record, stringRow) -> {
            record.setCurrencyId(Integer.valueOf(stringRow[0]));
            record.setCurrencyIso(stringRow[1]);
            record.setCurrencyName(stringRow[2]);
        });
    }
}
