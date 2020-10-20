/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.application.currency;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.references.api.ReferenceRec;
import java.util.Map;
import org.dbs24.references.api.AbstractRefRecord;
import java.util.stream.Collectors;
import javax.persistence.*;
import lombok.Data;

/**
 *
 * @author kazyra_d
 */
@Data
@Entity
@Table(name = "core_CurrenciesRef")
public class Currency extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "currency_id")
    private Integer currencyId;
    @Column(name = "currency_iso")
    private String currencyIso;
    @Column(name = "currency_name")
    private String currencyName;
   
    public static final Currency findCurrency(final Integer currencyId) {
        return AbstractRefRecord.<Currency>getRefeenceRecord(Currency.class,
                record -> record.getCurrencyId().equals(currencyId));
    }
}
