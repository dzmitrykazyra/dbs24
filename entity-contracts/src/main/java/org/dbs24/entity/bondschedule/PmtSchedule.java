/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.bondschedule;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import static org.dbs24.consts.BondScheduleConst.*;
import org.dbs24.references.bond.schedule.api.PmtScheduleAlg;
import org.dbs24.references.bond.schedule.api.PmtScheduleTerm;
import org.dbs24.entity.core.AbstractActionEntity;
import java.time.LocalDate;
import java.util.Collection;
import org.dbs24.entity.core.api.DefaultEntityStatus;
import org.dbs24.entity.core.api.EntityKindId;
import org.dbs24.entity.core.api.EntityTypeId;
import org.dbs24.entity.core.api.EntityKindsRef;
import org.dbs24.entity.kind.EntityKind;
import org.dbs24.entity.AbstractEntityContract;
import org.dbs24.entity.core.api.ActionClassesPackages;
import org.dbs24.entity.core.api.EntityStatusesRef;
import org.dbs24.entity.status.EntityStatusId;
import javax.persistence.*;
import lombok.Data;

@EntityTypeId(entity_type_id = BONDSCHEDULE, entity_type_name = "График погашения обязательства")
@EntityKindsRef(
        kind_id = {
            @EntityKindId(entity_kind_id = EK_BONDSCHEDULE_MAIN_DEBT,
                    entity_type_id = BONDSCHEDULE,
                    entity_kind_name = "График погашения основного долга"),
            @EntityKindId(entity_kind_id = EK_BONDSCHEDULE_PERC,
                    entity_type_id = BONDSCHEDULE,
                    entity_kind_name = "График погашения %"),
            @EntityKindId(entity_kind_id = EK_BONDSCHEDULE_COMM,
                    entity_type_id = BONDSCHEDULE,
                    entity_kind_name = "График погашения комиссии")
        })
@EntityStatusesRef(
        entiy_status = {
            @EntityStatusId(
                    entity_type_id = BONDSCHEDULE,
                    entity_status_id = ES_DEFAULT_STATUS,
                    entity_status_name = "Действующий график")
        })
@DefaultEntityStatus(entity_status = ES_DEFAULT_STATUS)
@Data
@Entity
@Table(name = "core_PmtSchedules")
@PrimaryKeyJoinColumn(name = "schedule_id", referencedColumnName = "entity_id")
@ActionClassesPackages(pkgList = {"org.dbs24.entity.bondschedule.actions"})
public class PmtSchedule extends AbstractActionEntity {

    @ManyToOne
    @JoinColumn(name = "contract_id", referencedColumnName = "contract_id", updatable = false)
    @JsonIgnore
    private AbstractEntityContract entityContract;

    @ManyToOne
    @JoinColumn(name = "entity_kind_id", referencedColumnName = "entity_kind_id", updatable = false)
    private EntityKind entityKind;

    @ManyToOne
    @JoinColumn(name = "pmt_term_id", referencedColumnName = "pmt_term_id", updatable = false)
    private PmtScheduleTerm pmtScheduleTerm;

    @ManyToOne
    @JoinColumn(name = "schedule_alg_id", referencedColumnName = "schedule_alg_id", updatable = false)
    private PmtScheduleAlg pmtScheduleAlg;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "last_date")
    private LocalDate lastDate;

    @OneToMany(mappedBy = "pmtSchedule", cascade = CascadeType.ALL)
    private Collection<PmtScheduleLine> pmtScheduleLines;
}
