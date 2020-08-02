/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.bond.schedule.builders;

import org.dbs24.bond.schedule.api.BondScheduleConst;
import org.dbs24.bond.schedule.api.PmtSchedule;
import org.dbs24.bond.schedule.api.PmtScheduleCalcAlgId;
import org.dbs24.bond.schedule.PmtScheduleImpl;
import org.dbs24.bond.schedule.PmtScheduleLineImpl;
import org.dbs24.bond.schedule.api.PmtScheduleLine;
import org.dbs24.exception.api.CreateEntityException;
import org.dbs24.log.mgmt.LogService;
import org.dbs24.sysconst.api.SysConst;
import org.dbs24.tce.core.NullSafe;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Козыро Дмитрий
 */
@PmtScheduleCalcAlgId(calcAlgId = BondScheduleConst.BS_ALG_ANNUITET)
public class PmtScheduleBuilderByAnnuitet extends PmtScheduleBuilderImpl {

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

            cntLd = cntLd.plusDays(this.getPmtScheduleTerm().getPmt_term_id());

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
