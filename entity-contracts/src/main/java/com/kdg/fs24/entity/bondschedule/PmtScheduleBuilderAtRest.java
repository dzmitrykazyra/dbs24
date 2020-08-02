/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.bondschedule;

import com.kdg.fs24.bond.schedule.api.PmtScheduleCalcAlgId;
import com.kdg.fs24.bond.schedule.api.BondScheduleConst;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import com.kdg.fs24.application.core.log.LogService;
import com.kdg.fs24.application.core.nullsafe.NullSafe;
import com.kdg.fs24.application.core.sysconst.SysConst;
import com.kdg.fs24.entity.kind.EntityKind;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@PmtScheduleCalcAlgId(calcAlgId = BondScheduleConst.BS_ALG_BYREST)
public class PmtScheduleBuilderAtRest extends PmtScheduleBuilder {

    @Override
    public PmtSchedule createSchedule() {
//        LogService.LogInfo(this.getClass(),
//                () -> LogService.getCurrentObjProcName(this));

        final PmtSchedule pmtSchedule = NullSafe.createObject(PmtSchedule.class);

        //pmtSchedule.setContract_id(this.getEntity().getEntity_id());
        pmtSchedule.setEntityKind(EntityKind.findEntityKind(this.getEntityKind()));
        pmtSchedule.setPmtScheduleAlg(this.getPmtScheduleAlg());
        pmtSchedule.setPmtScheduleTerm(this.getPmtScheduleTerm());
        pmtSchedule.setFromDate(this.getFrom_date());
        pmtSchedule.setLastDate(this.getLast_date());

        // строки графика
        List<PmtScheduleLine> lines = NullSafe.createObject(ArrayList.class);

        LocalDate cntLd = this.getFrom_date();
        LocalDate fromLd = LocalDate.now();

        do {

            //cntLd = cntLd.plusDays(this.getPmtScheduleTerm().getPmt_term_id());
            cntLd = cntLd.plusMonths(this.getPmtScheduleTerm().getPmtTermId() / 30);

            if (cntLd.isAfter(cntLd)) {
                cntLd = this.getLast_date();
            }

            // строка графика
            final PmtScheduleLine pmtScheduleLine = NullSafe.createObject(PmtScheduleLine.class);

            pmtScheduleLine.setActualDate(LocalDate.now());
            pmtScheduleLine.setFromDate(fromLd);
            pmtScheduleLine.setToDate(cntLd.minusDays(1));
            pmtScheduleLine.setAppearDate(cntLd);
            pmtScheduleLine.setPaySum(SysConst.BIGDECIMAL_ZERO);
            pmtScheduleLine.setCalcDate(LocalDate.now());

            // добавили строку графика
            lines.add(pmtScheduleLine);

            fromLd = cntLd;

        } while (cntLd.isBefore(this.getLast_date()));

        pmtSchedule.setPmtScheduleLines(lines);

        return pmtSchedule;
    }
}
