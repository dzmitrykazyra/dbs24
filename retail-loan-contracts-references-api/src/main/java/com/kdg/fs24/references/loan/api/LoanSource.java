/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.loan.api;

import com.kdg.fs24.application.core.service.funcs.ServiceFuncs;
import com.kdg.fs24.references.api.ReferenceRec;
import java.util.Map;
import com.kdg.fs24.references.api.AbstractRefRecord;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * @author kazyra_d
 */
@Data
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "rlc_loanSourcesRef")
public class LoanSource extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "loan_source_id", updatable = false)
    private Integer loanSourceId;
    @Column(name = "loan_source_name")
    private String loanSourceName;

    @Override
    public void record2Map(final Map<String, Integer> map) {
        map.put(String.format("%d - %s", this.getLoanSourceId(), this.toString()), this.getLoanSourceId());
    }

    //==========================================================================
    public final static LoanSource findLoanSource(final Integer LoanSourceId) {
        return AbstractRefRecord.<LoanSource>getRefeenceRecord(LoanSource.class,
                record -> record.getLoanSourceId().equals(LoanSourceId));
    }
}
