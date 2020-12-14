/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.AnnotationFuncs;
import org.dbs24.application.core.service.funcs.ReflectionFuncs;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import java.util.Collection;
import org.dbs24.entity.bondschedule.*;
import org.dbs24.exception.*;
import org.dbs24.spring.core.api.AbstractApplicationBean;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.dbs24.bond.schedule.api.PmtScheduleCalcAlgId;
import org.dbs24.entity.bondschedule.PmtSchedule;
import org.dbs24.references.bond.schedule.api.PmtScheduleAlg;
import org.dbs24.references.bond.schedule.api.PmtScheduleTerm;
import org.springframework.beans.factory.annotation.Autowired;
import static org.dbs24.consts.SysConst.*;

@Service
public class ContractSchedulesBuilders extends AbstractApplicationBean {

    // коллекция алгоритмов-построителей графиков
    private final Collection<Class<PmtScheduleBuilder>> scheduleBuilders = ServiceFuncs.createCollection();

    final EntityReferencesService entityReferencesService;

    @Autowired
    public ContractSchedulesBuilders(EntityReferencesService entityReferencesService) {
        this.entityReferencesService = entityReferencesService;
    }

    @Override
    public void initialize() {

        final Class<PmtScheduleBuilder> clazz = (PmtScheduleBuilder.class);
        final Class<PmtScheduleCalcAlgId> annClazz = PmtScheduleCalcAlgId.class;

        // значения для справочника берутся из аннотаций классов
        ReflectionFuncs.processPkgClassesCollection(ENTITY_PACKAGE, clazz, annClazz,
                entClazz -> scheduleBuilders.add(entClazz));

        scheduleBuilders
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoPmtScheduleBuilderAvailAble(
                String.format("No schedule builders defined for '%s' ",
                        clazz.getCanonicalName())));
    }

    //==========================================================================
    public PmtSchedule buildSchedule(Integer algId,
            final PmtScheduleAlg pmtScheduleAlg,
            final PmtScheduleTerm pmtScheduleTerm,
            final Integer scheduleKind,
            final LocalDate d1,
            final LocalDate d2
    ) {

        final PmtSchedule pmtSchedule
                = NullSafe.createObject(scheduleBuilders
                        .stream()
                        .filter(alg -> algId.equals(AnnotationFuncs.getAnnotation(alg, PmtScheduleCalcAlgId.class).calcAlgId()))
                        .findFirst()
                        .orElseThrow(() -> new AlgBuilderNotFound(String.format("%s: Can't find schedule builder 'algId=%d' ",
                        this.getClass().getSimpleName(),
                        algId))), builder -> {

                    builder.setPmtScheduleAlg(pmtScheduleAlg);
                    builder.setPmtScheduleTerm(pmtScheduleTerm);
                    builder.setFrom_date(d1);
                    builder.setLast_date(d2);

                }).createSchedule();

        pmtSchedule.setEntityKind(entityReferencesService.findEntityKind(scheduleKind));

        return pmtSchedule;
    }
}
