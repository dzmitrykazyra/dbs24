/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.bond.schedule.api;

import java.util.Map;
import com.kdg.fs24.references.api.ReferenceRec;
import com.kdg.fs24.references.api.AbstractRefRecord;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

/**
 *
 * @author kazyra_d
 */
@Data
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "core_pmtScheduleAlgsRef")
public class PmtScheduleAlg extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "schedule_alg_id", updatable = false)
    private Integer scheduleAlgId;
    @Column(name = "schedule_alg_name")
    private String scheduleAlgName;
    @Column(name = "is_actual")
    private Boolean isActual;

    @Override
    public void record2Map(final Map<String, Integer> map) {
        map.put(this.toString(), this.getScheduleAlgId());
    }

    //==========================================================================
    public final static PmtScheduleAlg findLoanSource(final Integer PmtScheduleAlgId) {
        return AbstractRefRecord.<PmtScheduleAlg>getRefeenceRecord(PmtScheduleAlg.class,
                record -> record.getScheduleAlgId().equals(PmtScheduleAlgId));
    }
}
