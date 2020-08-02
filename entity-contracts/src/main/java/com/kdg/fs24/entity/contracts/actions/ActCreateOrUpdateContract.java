/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.contracts.actions;

import com.kdg.fs24.application.core.nullsafe.NullSafe;
import com.kdg.fs24.bond.schedule.api.BondScheduleConst;
import com.kdg.fs24.entity.contracts.AbstractEntityContract;
import com.kdg.fs24.entity.contracts.AbstractEntityServiceContract;
import com.kdg.fs24.entity.core.AbstractAction;
import com.kdg.fs24.entity.status.EntityStatus;
import com.kdg.fs24.spring.core.api.ServiceLocator;
import com.kdg.fs24.service.ActionExecutionService;
import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
public abstract class ActCreateOrUpdateContract<T extends AbstractEntityServiceContract>
        extends AbstractAction<T> {

    @Override
    public void doUpdate() {

        final T t = (T) this.getEntity();

        // только создан
        if (t.justCreated()) {

            if (NullSafe.notNull(t.getPmtSchedules())) {
                t.getPmtSchedules().clear();
            }

            t.createBondschedules();

            // сохранение графиков платежей
            if (NullSafe.notNull(t.getPmtSchedules())) {
                final ActionExecutionService aes = ServiceLocator.<ActionExecutionService>findService(ActionExecutionService.class);
                // инициализация
                t.getPmtSchedules()
                        .stream()
                        .forEach(schedule -> {
                            schedule.setEntityStatus(EntityStatus.findEntityStatus(BondScheduleConst.BONDSCHEDULE, BondScheduleConst.ES_DEFAULT_STATUS));
                            schedule.setEntityContract((AbstractEntityContract) this.getEntity());
                            schedule.setCreation_date(LocalDateTime.now());
                            //aes.executeAction(schedule, BondScheduleConst.ACT_SAVE_BONDSCHEDULE);
                        });
                // выполнение
                t.getPmtSchedules()
                        .stream()
                        .forEach(schedule -> {
//                            schedule.setEntityStatus(EntityStatus.findEntityStatus(BondScheduleConst.BONDSCHEDULE, BondScheduleConst.ES_DEFAULT_STATUS));
//                            schedule.setEntityContract((AbstractEntityContract) this.getEntity());
                            //schedule.setCreation_date(LocalDateTime.MIN);
                            aes.executeAction(schedule, BondScheduleConst.ACT_SAVE_BONDSCHEDULE, null);
                        });
//            }
            }
        }
    }
}
