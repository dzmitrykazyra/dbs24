/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.contracts.actions;

import org.dbs24.application.core.nullsafe.NullSafe;
import static org.dbs24.bond.schedule.api.BondScheduleConst.*;
import org.dbs24.entity.contracts.AbstractEntityContract;
import org.dbs24.entity.contracts.AbstractEntityServiceContract;
import org.dbs24.entity.core.AbstractAction;
import org.dbs24.entity.status.EntityStatus;
import org.dbs24.spring.core.api.ServiceLocator;
import org.dbs24.service.AbstractActionExecutionService;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.dbs24.entity.bondschedule.builders.BondScheduleBuilder;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
public abstract class ActCreateOrUpdateContract<T extends AbstractEntityServiceContract>
        extends AbstractAction<T> {
    
    @Autowired
    private BondScheduleBuilder bondScheduleBuilder;
    
    @Override
    public void doUpdate() {
        
        final T t = (T) this.getEntity();

        // только создан
        if (t.justCreated()) {
            
            if (NullSafe.notNull(t.getPmtSchedules())) {
                t.getPmtSchedules().clear();
            }

            // создание графиков платежей
            t.setPmtSchedules(bondScheduleBuilder.createBondschedules(t));

            // сохранение графиков платежей
            if (NullSafe.notNull(t.getPmtSchedules())) {
                final AbstractActionExecutionService aes = ServiceLocator.<AbstractActionExecutionService>findService(AbstractActionExecutionService.class);
                // инициализация
                t.getPmtSchedules()
                        .stream()
                        .forEach(schedule -> {
                            schedule.setEntityStatus(EntityStatus.findEntityStatus(BONDSCHEDULE, ES_DEFAULT_STATUS));
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
                            aes.executeAction(schedule, ACT_SAVE_BONDSCHEDULE, null);
                        });
//            }
            }
        }
    }
}
