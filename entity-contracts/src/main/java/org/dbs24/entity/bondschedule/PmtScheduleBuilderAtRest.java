/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.bondschedule;

import org.dbs24.bond.schedule.api.PmtScheduleCalcAlgId;
import static org.dbs24.consts.BondScheduleConst.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import org.dbs24.application.core.nullsafe.NullSafe;
import static org.dbs24.consts.SysConst.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
@PmtScheduleCalcAlgId(calcAlgId = BS_ALG_BYREST)
public class PmtScheduleBuilderAtRest extends PmtScheduleBuilder {

//    final EntityReferencesService entityReferencesService;
//    @Autowired
//    public PmtScheduleBuilderAtRest(EntityReferencesService entityReferencesService) {
//        this.entityReferencesService = entityReferencesService;
//    }
    @Override
    public PmtSchedule createSchedule() {
        
        final PmtSchedule pmtSchedule = NullSafe.createObject(PmtSchedule.class);

//        pmtSchedule.setEntityKind(entityReferencesService.findEntityKind(this.getEntityKind()));
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
            pmtScheduleLine.setPaySum(BIGDECIMAL_ZERO);
            pmtScheduleLine.setCalcDate(LocalDate.now());
            pmtScheduleLine.setPmtSchedule(pmtSchedule);

            // добавили строку графика
            lines.add(pmtScheduleLine);
            
            fromLd = cntLd;
            
        } while (cntLd.isBefore(this.getLast_date()));
        
        pmtSchedule.setPmtScheduleLines(lines);
        
        return pmtSchedule;
    }
}
