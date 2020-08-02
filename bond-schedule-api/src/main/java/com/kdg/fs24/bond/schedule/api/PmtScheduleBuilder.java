/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.bond.schedule.api;

//import com.kdg.fs24.entity.bondschedule.PmtSchedule;
import com.kdg.fs24.references.bond.schedule.api.PmtScheduleTerm;
import com.kdg.fs24.references.bond.schedule.api.PmtScheduleAlg;
import com.kdg.fs24.entity.core.api.ActionEntity;
import java.time.LocalDate;

/**
 *
 * @author Козыро Дмитрий
 */
@Deprecated
public interface PmtScheduleBuilder {

    PmtScheduleBuilder setEntity(ActionEntity entity);

    PmtScheduleBuilder setPmtScheduleTerm(PmtScheduleTerm pmtScheduleTerm);

    PmtScheduleBuilder setPmtScheduleAlg(PmtScheduleAlg pmtScheduleAlg);    
    
    PmtScheduleBuilder setScheduleKindId(final Integer entityKind);    
    
    PmtScheduleBuilder setFromDate(LocalDate from_date);

    PmtScheduleBuilder setLasDate(LocalDate last_date);

    //PmtSchedule createSchedule();

}
