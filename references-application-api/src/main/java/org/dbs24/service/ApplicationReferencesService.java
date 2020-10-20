/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.dbs24.persistence.core.PersistenceEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.dbs24.references.application.currency.Currency;

@Data
@Service
public class ApplicationReferencesService extends AbstractApplicationService {

    final PersistenceEntityManager persistenceEntityManager;

    @Autowired
    public ApplicationReferencesService(PersistenceEntityManager persistenceEntityManager) {
        this.persistenceEntityManager = persistenceEntityManager;
    }

    //==========================================================================
    public final void createCurrency(final Integer currencyId,
            final String currencyIso, final String currencyName) {

        persistenceEntityManager.<Currency>mergePersistenceEntity(Currency.class,
                currency -> {

                    currency.setCurrencyId(currencyId);
                    currency.setCurrencyIso(currencyIso);
                    currency.setCurrencyName(currencyName);

                });
    } 
}
