/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.bond.schedule.actions;

import com.kdg.fs24.entity.core.AbstractAction;
import com.kdg.fs24.entity.core.api.ActionCodeId;
import com.kdg.fs24.bond.schedule.api.BondScheduleConst;
import com.kdg.fs24.entity.core.api.AlwaysAllowedAction;
import com.kdg.fs24.tce.core.NullSafe;

/**
 *
 * @author Козыро Дмитрий
 */
@ActionCodeId(action_code = BondScheduleConst.ACT_SAVE_BONDSCHEDULE,
        action_name = "Редактировать график")
@AlwaysAllowedAction
public final class ActSaveBondSchedule extends AbstractAction {

    @Override
    protected void doUpdate() {
        NullSafe.create()
                .execute(() -> {
                    super.doUpdate();
                    //сохранение графика
                    this.getEntity().saveEntityInstance();
                }).throwException();
    }
}
