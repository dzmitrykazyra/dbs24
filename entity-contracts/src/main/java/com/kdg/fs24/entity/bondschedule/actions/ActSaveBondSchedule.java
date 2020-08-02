/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.bondschedule.actions;

/**
 *
 * @author Козыро Дмитрий
 */
import com.kdg.fs24.entity.core.AbstractAction;
import com.kdg.fs24.entity.core.api.ActionCodeId;
import com.kdg.fs24.bond.schedule.api.BondScheduleConst;
import com.kdg.fs24.entity.core.api.RefreshEntity;

@ActionCodeId(action_code = BondScheduleConst.ACT_SAVE_BONDSCHEDULE,
        action_name = "Редактировать график")
public class ActSaveBondSchedule extends AbstractAction {

//    @Override
//    protected void doUpdate() {
//        NullSafe.create()
//                .execute(() -> {
//                    super.doUpdate();
//                    //сохранение графика
//                    this.getEntity().saveEntityInstance();
//                }).throwException();
//    }
//    @Override
//    public void doUpdate() {
//        super.doUpdate();
//
//        final PmtSchedule pmtSchedule = (PmtSchedule) this.getEntity();
//
//        if (pmtSchedule.justCreated()) {
//
//            pmtSchedule
//                    .getPmtScheduleLines()
//                    .stream()
//                    .forEach((line) -> {
//
//                        //                    line.setSchedule_id(pmtSchedule.getEntity_id());
//                        line.setPmtSchedule(pmtSchedule);
//
//                    });
//        }
//    }
}
