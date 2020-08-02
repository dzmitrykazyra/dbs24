/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.service;

import com.kdg.fs24.application.core.service.funcs.ServiceFuncs;
import lombok.Data;
import org.springframework.stereotype.Service;
import com.kdg.fs24.persistence.core.PersistanceEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.kdg.fs24.references.application.currency.Currency;
import java.util.stream.Collectors;


/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Service
public class ApplicationReferencesService extends AbstractApplicationService {

    @Autowired
    private PersistanceEntityManager persistanceEntityManager;

    //==========================================================================
    public final void createCurrency(final Integer currencyId,
            final String currencyIso, final String currencyName) {

        persistanceEntityManager.<Currency>mergePersistenceEntity(Currency.class,
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
