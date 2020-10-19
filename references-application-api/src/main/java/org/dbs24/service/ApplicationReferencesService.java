/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.dbs24.persistence.core.PersistenceEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.dbs24.references.application.currency.Currency;
import java.util.stream.Collectors;


/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Service
public class ApplicationReferencesService extends AbstractApplicationService {

    @Autowired
    private PersistenceEntityManager PersistenceEntityManager;

    //==========================================================================
    public final void createCurrency(final Integer currencyId,
            final String currencyIso, final String currencyName) {

        PersistenceEntityManager.<Currency>mergePersistenceEntity(Currency.class,
                currency -> {

                    currency.setCurrencyId(currencyId);
                    currency.setCurrencyIso(currencyIso);
                    currency.setCurrencyName(currencyName);

                });
    }
    //==========================================================================
//    public Currency getCurrency(final Integer currencyId) {
//
//        return ServiceFuncs.getMapValue(this.getREF_CACHE(), mapEntry -> mapEntry.getKey().equals(Currency.class))
//                .get()
//                .stream()
//                .map(x -> (Currency) x)
//                .collect(Collectors.toList())
//                .stream()
//                .filter(currency -> currency.getCurrencyId().equals(currencyId))
//                .findFirst()
//                .get();
//    }    
}
