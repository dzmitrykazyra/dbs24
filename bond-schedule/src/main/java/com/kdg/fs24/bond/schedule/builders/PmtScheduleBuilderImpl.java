/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.bond.schedule.builders;

import com.kdg.fs24.bond.schedule.api.PmtScheduleBuilder;
import com.kdg.fs24.bond.schedule.references.api.PmtScheduleTerm;
import com.kdg.fs24.bond.schedule.references.api.PmtScheduleAlg;
import com.kdg.fs24.entity.core.api.Entity;
import java.time.LocalDate;

/**
 *
 * @author Козыро Дмитрий
 */
public abstract class PmtScheduleBuilderImpl<T extends PmtScheduleBuilder>
        implements PmtScheduleBuilder {

    private Entity entity;
    private PmtScheduleTerm pmtScheduleTerm;
    private PmtScheduleAlg pmtScheduleAlg;
    private Integer entityKind;
    private LocalDate from_date;
    private LocalDate last_date;

    @Override
    public PmtScheduleBuilder setEntity(Entity entity) {
        this.entity = entity;
        return (T) this;
    }

    @Override
    public PmtScheduleBuilder setPmtScheduleTerm(PmtScheduleTerm pmtScheduleTerm) {
        this.pmtScheduleTerm = pmtScheduleTerm;
        return (T) this;
    }

    @Override
    public PmtScheduleBuilder setPmtScheduleAlg(PmtScheduleAlg pmtScheduleAlg) {
        this.pmtScheduleAlg = pmtScheduleAlg;
        return (T) this;
    }

    @Override
    public PmtScheduleBuilder setScheduleKindId(final Integer entityKind) {
        this.entityKind = entityKind;
        return (T) this;
    }

    @Override
    public PmtScheduleBuilder setFromDate(final LocalDate from_date) {
        this.from_date = from_date;
        return (T) this;
    }

    @Override
    public PmtScheduleBuilder setLasDate(final LocalDate last_date) {
        this.last_date = last_date;
        return (T) this;
    }

    protected Entity getEntity() {
        return entity;
    }

    protected PmtScheduleTerm getPmtScheduleTerm() {
        return pmtScheduleTerm;
    }

    protected PmtScheduleAlg getPmtScheduleAlg() {
        return pmtScheduleAlg;
    }

    protected Integer getEntityKind() {
        return entityKind;
    }

    protected LocalDate getFrom_date() {
        return from_date;
    }

    protected LocalDate getLast_date() {
        return last_date;
    }
}
