/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.bond.schedule.api;

import java.util.Map;
import org.dbs24.references.api.ReferenceRec;
import org.dbs24.references.api.AbstractRefRecord;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;


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
}
