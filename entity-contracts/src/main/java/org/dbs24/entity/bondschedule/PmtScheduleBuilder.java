/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.bondschedule;

import org.dbs24.references.bond.schedule.api.PmtScheduleTerm;
import org.dbs24.references.bond.schedule.api.PmtScheduleAlg;
//import org.dbs24.entity.core.api.Entity;
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
