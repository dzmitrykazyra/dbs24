/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.actions;

import org.dbs24.application.core.nullsafe.NullSafe;
import static org.dbs24.consts.BondScheduleConst.*;
import org.dbs24.entity.AbstractEntityContract;
import org.dbs24.entity.core.AbstractAction;
import org.dbs24.entity.status.EntityStatus;
import org.dbs24.spring.core.api.ServiceLocator;
import org.dbs24.service.AbstractActionExecutionService;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.dbs24.entity.bondschedule.builders.BondScheduleBuilder;
import org.dbs24.service.EntityReferencesService;

@Data
public abstract class ActCreateOrUpdateContract<T extends AbstractEntityContract>
        extends AbstractAction<T> {
    
    @Autowired
    private BondScheduleBuilder bondScheduleBuilder;
    
    @Autowired
    private EntityReferencesService entityReferencesService;
    
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
                            schedule.setEntityStatus(entityReferencesService.findEntityStatus(BONDSCHEDULE, ES_DEFAULT_STATUS));
                            schedule.setEntityContract((AbstractEntityContract) this.getEntity());
                            schedule.setCreationDate(LocalDateTime.now());
                            //aes.executeAction(schedule, ACT_SAVE_BONDSCHEDULE);
                        });
                // выполнение
                t.getPmtSchedules()
                        .stream()
                        .forEach(schedule -> {
//                            schedule.setEntityStatus(EntityStatus.findEntityStatus(BONDSCHEDULE, ES_DEFAULT_STATUS));
//                            schedule.setEntityContract((AbstractEntityContract) this.getEntity());
                            //schedule.setCreationDate(LocalDateTime.MIN);
                            aes.executeAction(schedule, ACT_SAVE_BONDSCHEDULE, this.getEntityManager());
                        });
//            }
            }
        }
    }
}
