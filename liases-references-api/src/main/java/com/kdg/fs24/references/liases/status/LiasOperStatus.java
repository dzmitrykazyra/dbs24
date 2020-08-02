/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.liases.status;

import com.kdg.fs24.references.api.AbstractRefRecord;
import com.kdg.fs24.references.api.ReferenceRec;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;
/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "liasFinOperStatusesRef")

public class LiasOperStatus extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "fin_oper_status_id", updatable = false)
    private Integer finOperStatusId;
    @Column(name = "fin_oper_status_name")
    private String finOperStatusName;

    @Override
    public void record2Map(final Map<String, Integer> map) {
//        map.put(String.format("%d - %s", this.getDebtStateId(), this.getDebtStateName()), this.getDebtStateId());
    }

    public final static LiasOperStatus findLiasOperStatus(final Integer liasOperStatusId) {
        return AbstractRefRecord.<LiasOperStatus>getRefeenceRecord(LiasOperStatus.class,
                record -> record.getFinOperStatusId().equals(liasOperStatusId));
    }
}
