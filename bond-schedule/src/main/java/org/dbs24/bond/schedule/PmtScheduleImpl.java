/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.bond.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.dbs24.bond.schedule.api.BondScheduleConst;
import org.dbs24.bond.schedule.api.PmtSchedule;
import org.dbs24.bond.schedule.api.PmtScheduleLine;
import org.dbs24.bond.schedule.collection.BondSchedulesActionClassesService;
import org.dbs24.jdbc.api.exception.QueryExecutionException;
import org.dbs24.bond.schedule.references.api.PmtScheduleAlg;
import org.dbs24.bond.schedule.references.api.PmtScheduleTerm;
import java.time.LocalDate;
import java.util.List;
import org.dbs24.entity.core.AbstractEntityImpl;
import org.dbs24.entity.core.api.ActionClassesCollectionLink;
import org.dbs24.entity.core.api.DefaultEntityStatus;
import org.dbs24.entity.core.api.EntityKindId;
import org.dbs24.entity.core.api.EntityTypeId;
import org.dbs24.exception.api.CreateEntityException;
import org.dbs24.sysconst.api.SysConst;
import org.dbs24.entity.core.api.EntityKindsRef;

/**
 *
 * @author kazyra_d
 */
@EntityTypeId(entity_type_id = BondScheduleConst.BONDSCHEDULE, entity_type_name = "График погашения обязательства")
@EntityKindsRef(
        kind_id = {
            @EntityKindId(entity_kind_id = BondScheduleConst.EK_BONDSCHEDULE_MAIN_DEBT,
                    entity_type_id = BondScheduleConst.BONDSCHEDULE,
                    entity_kind_name = "График погашения основного долга")
            ,
            @EntityKindId(entity_kind_id = BondScheduleConst.EK_BONDSCHEDULE_PERC,
                    entity_type_id = BondScheduleConst.BONDSCHEDULE,
                    entity_kind_name = "График погашения %")
            ,
                        @EntityKindId(entity_kind_id = BondScheduleConst.EK_BONDSCHEDULE_COMM,
                    entity_type_id = BondScheduleConst.BONDSCHEDULE,
                    entity_kind_name = "График погашения комиссии")
        })
@ActionClassesCollectionLink(collection_service = BondSchedulesActionClassesService.class)
@DefaultEntityStatus(entity_status = BondScheduleConst.ES_DEFAULT_STATUS)
public final class PmtScheduleImpl extends AbstractEntityImpl implements PmtSchedule {

    private Long schedule_id;
    private Long contract_id;

    private Integer entityKindId;

    private PmtScheduleTerm pmtScheduleTerm;

    private PmtScheduleAlg pmtScheduleAlg;

    private LocalDate from_date;

    private LocalDate last_date;

    private List<PmtScheduleLine> pmtScheduleLines;

    //==========================================================================
    public PmtScheduleImpl() {
        super();
    }

    @Override
    public Long getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(final Long schedule_id) {
        this.schedule_id = schedule_id;
    }

    @Override
    public Long getContract_id() {
        return contract_id;
    }

    @Override
    public void setContract_id(final Long contract_id) {
        this.contract_id = contract_id;
    }

    @Override
    public Integer getEntityKindId() {
        return entityKindId;
    }

    public void setEntityKindId(final Integer entityKindId) {
        this.entityKindId = entityKindId;
    }

    @Override
    public PmtScheduleTerm getPmtScheduleTerm() {
        return pmtScheduleTerm;
    }

    public void setPmtScheduleTerm(PmtScheduleTerm pmtScheduleTerm) {
        this.pmtScheduleTerm = pmtScheduleTerm;
    }

    @Override
    public PmtScheduleAlg getPmtScheduleAlg() {
        return pmtScheduleAlg;
    }

    public void setPmtScheduleAlg(PmtScheduleAlg pmtScheduleAlg) {
        this.pmtScheduleAlg = pmtScheduleAlg;
    }

    @Override
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SysConst.DATE_FORMAT)
    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate getFrom_date() {
        return from_date;
    }

    public void setFrom_date(final LocalDate from_date) {
        this.from_date = from_date;
    }

    @Override
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SysConst.DATE_FORMAT)
    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate getLast_date() {
        return last_date;
    }

    public void setLast_date(final LocalDate last_date) {
        this.last_date = last_date;
    }

    @Override
    public List<PmtScheduleLine> getPmtScheduleLines() throws QueryExecutionException {
        return pmtScheduleLines;
    }

    public void setPmtScheduleLines(final List<PmtScheduleLine> pmtScheduleLines) {
        this.pmtScheduleLines = pmtScheduleLines;
    }
//    @Override
//    public void build(PmtScheduleAlg alg) {
//        
//    }

    @Override
    public void saveEntityInstance() {

        this.getDbService()
                .createCallQuery("{call core_pmt_sched_save(:SH, :C, :K, :T, :S, :F, :L)}")
                .setParamByName("SH", this.getEntity_id())
                .setParamByName("C", this.getContract_id())
                .setParamByName("K", this.getEntityKindId())
                .setParamByName("T", this.getPmtScheduleTerm().getPmt_term_id())
                .setParamByName("S", this.getPmtScheduleAlg().getSchedule_alg_id())
                .setParamByName("F", this.getFrom_date())
                .setParamByName("L", this.getLast_date())
                .execCallStmt();

        this.getDbService()
                .createCallBath("{call core_pmt_schedlines_save(:SH,:AC,:FR,:TO,:AP, :PS, :CD)}")
                .execBatch(stmt -> {

                    pmtScheduleLines.forEach(line -> {
                        stmt.setParamByName("SH", this.getEntity_id());
                        stmt.setParamByName("AC", line.getActual_date());
                        stmt.setParamByName("FR", line.getFrom_date());
                        stmt.setParamByName("TO", line.getTo_date());
                        stmt.setParamByName("AP", line.getAppear_date());
                        stmt.setParamByName("PS", line.getPay_sum());
                        stmt.setParamByName("CD", line.getCalc_date());
                        stmt.addBatch();
                    });
                });

    }

}
