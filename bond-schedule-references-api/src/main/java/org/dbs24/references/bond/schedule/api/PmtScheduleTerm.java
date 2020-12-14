/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.bond.schedule.api;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import java.util.Map;
import org.dbs24.references.api.ReferenceRec;
import org.dbs24.references.api.AbstractRefRecord;
import java.util.stream.Collectors;
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
@Table(name = "core_pmtScheduleTermsRef")
public class PmtScheduleTerm extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "pmt_term_id")
    private Integer pmtTermId;
    @Column(name = "pmt_term_name")
    private String pmtTermName;
    @Column(name = "is_actual")
    private Boolean isActual;
}
