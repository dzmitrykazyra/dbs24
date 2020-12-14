/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.liases.debtstate;

import java.util.Map;
import org.dbs24.references.api.ReferenceRec;
import org.dbs24.references.api.AbstractRefRecord;
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
@Table(name = "liasDebtStatesRef")
public class LiasDebtState extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "debt_state_id", updatable = false)
    private Integer debtStateId;
    @Column(name = "debt_state_name")
    private String debtStateName;

    public static final LiasDebtState findLiasDebtState( Integer liasLiasDebtStateId) {
        return AbstractRefRecord.<LiasDebtState>getRefeenceRecord(LiasDebtState.class,
                record -> record.getDebtStateId().equals(liasLiasDebtStateId));
    }
}
