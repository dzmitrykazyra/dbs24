/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.bond.schedule.builders;

import com.kdg.fs24.bond.schedule.PmtScheduleImpl;
import com.kdg.fs24.bond.schedule.api.PmtSchedule;
import com.kdg.fs24.bond.schedule.api.PmtScheduleCalcAlgId;
import com.kdg.fs24.bond.schedule.api.BondScheduleConst;
import com.kdg.fs24.bond.schedule.api.PmtScheduleLine;
import com.kdg.fs24.bond.schedule.PmtScheduleLineImpl;
import com.kdg.fs24.log.mgmt.LogService;
import com.kdg.fs24.sysconst.api.SysConst;
import com.kdg.fs24.tce.core.NullSafe;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

/**
 *
 * @author Козыро Дмитрий
 */
@PmtScheduleCalcAlgId(calcAlgId = BondScheduleConst.BS_ALG_BYREST)
public class PmtScheduleBuilderAtRest extends PmtScheduleBuilderImpl {

    @Override
    public PmtSchedule createSchedule() {
        LogService.LogInfo(this.getClass(),
                () -> LogService.getCurrentObjProcName(this));

        PmtScheduleImpl pmtSchedule = NullSafe.createObject(PmtScheduleImpl.class);

        //pmtSchedule.setContract_id(this.getEntity().getEntity_id());
        pmtSchedule.setEntityKindId(this.getEntityKind());
        pmtSchedule.setPmtScheduleAlg(this.getPmtScheduleAlg());
        pmtSchedule.setPmtScheduleTerm(this.getPmtScheduleTerm());
        pmtSchedule.setFrom_date(this.getFrom_date());
        pmtSchedule.setLast_date(this.getLast_date());

        // строки графика
        List<PmtScheduleLine> lines = NullSafe.createObject(ArrayList.class);

        LocalDate cntLd = this.getFrom_date();
        LocalDate fromLd = LocalDate.now();

        do {

            //cntLd = cntLd.plusDays(this.getPmtScheduleTerm().getPmt_term_id());
            cntLd = cntLd.plusMonths(this.getPmtScheduleTerm().getPmt_term_id() / 30);

            if (cntLd.isAfter(cntLd)) {
                cntLd = this.getLast_date();
            }

            // строка графика
            PmtScheduleLineImpl pmtScheduleLine = NullSafe.createObject(PmtScheduleLineImpl.class);

            pmtScheduleLine.setActual_date(LocalDate.now());
            pmtScheduleLine.setFrom_date(fromLd);
            pmtScheduleLine.setTo_date(cntLd.minusDays(1));
            pmtScheduleLine.setAppear_date(cntLd);
            pmtScheduleLine.setPay_sum(SysConst.BIGDECIMAL_ZERO);
            pmtScheduleLine.setCalc_date(LocalDate.now());

            // добавили строку графика
            lines.add(pmtScheduleLine);

            fromLd = cntLd;

        } while (cntLd.isBefore(this.getLast_date()));

        pmtSchedule.setPmtScheduleLines(lines);

        return pmtSchedule;
    }

}
