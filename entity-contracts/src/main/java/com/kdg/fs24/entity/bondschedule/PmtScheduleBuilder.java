/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.bondschedule;

import com.kdg.fs24.references.bond.schedule.api.PmtScheduleTerm;
import com.kdg.fs24.references.bond.schedule.api.PmtScheduleAlg;
//import com.kdg.fs24.entity.core.api.Entity;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
public abstract class PmtScheduleBuilder {

//    private Entity entity;
    private PmtScheduleTerm pmtScheduleTerm;
    private PmtScheduleAlg pmtScheduleAlg;
    private Integer entityKind;
    private LocalDate from_date;
    private LocalDate last_date;

    public abstract PmtSchedule createSchedule();
}
