/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.bond.schedule.actions;

import org.dbs24.entity.core.AbstractAction;
import org.dbs24.entity.core.api.ActionCodeId;
import org.dbs24.bond.schedule.api.BondScheduleConst;
import org.dbs24.entity.core.api.AlwaysAllowedAction;
import org.dbs24.tce.core.NullSafe;

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
