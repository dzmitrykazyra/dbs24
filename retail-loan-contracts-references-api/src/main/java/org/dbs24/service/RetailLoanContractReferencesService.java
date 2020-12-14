/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import java.util.Collection;
import lombok.Data;
import org.dbs24.component.PersistenceEntityManager;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.core.CachedReferencesClasses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.dbs24.references.loan.api.LoanSource;
import static org.dbs24.consts.RetailLoanContractReferencesConst.*;

@Data
@Service
@CachedReferencesClasses(classes = {LoanSource.class})
public class RetailLoanContractReferencesService extends AbstractReferencesService {

    final PersistenceEntityManager persistenceEntityManager;

    @Autowired
    public RetailLoanContractReferencesService(PersistenceEntityManager persistenceEntityManager) {
        this.persistenceEntityManager = persistenceEntityManager;
    }

    //==========================================================================
    public final LoanSource findLoanSource(Integer loanSourceId) {
        return AbstractRefRecord.<LoanSource>getRefeenceRecord(LoanSource.class,
                record -> record.getLoanSourceId().equals(loanSourceId));
    }

    //==========================================================================
    public static final Collection<LoanSource> getLoanSourceCollection() {

        return AbstractReferencesService.<LoanSource>getGenericCollection(
                LOAN_SOURSE_CLASS, new String[][]{
                    {"101", "Источник кредитования - 1"},
                    {"102", "Источник кредитования - 2"},
                    {"103", "Источник кредитования - 3"}
                },
                (record, stringRow) -> {
                    record.setLoanSourceId(Integer.valueOf(stringRow[0]));
                    record.setLoanSourceName(stringRow[1]);
                });
    }
}
