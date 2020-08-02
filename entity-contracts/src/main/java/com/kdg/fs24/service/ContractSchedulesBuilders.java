/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.service;

import com.kdg.fs24.application.core.exception.api.InternalAppException;
import com.kdg.fs24.application.core.nullsafe.NullSafe;
import com.kdg.fs24.application.core.service.funcs.AnnotationFuncs;
import com.kdg.fs24.application.core.service.funcs.ReflectionFuncs;
import com.kdg.fs24.application.core.service.funcs.ServiceFuncs;
import java.util.Collection;
import com.kdg.fs24.entity.bondschedule.PmtScheduleBuilder;
//import com.kdg.fs24.spring.core.api.ApplicationService;
import com.kdg.fs24.spring.core.bean.AbstractApplicationBean;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import com.kdg.fs24.bond.schedule.api.PmtScheduleCalcAlgId;
import com.kdg.fs24.entity.bondschedule.PmtSchedule;
import com.kdg.fs24.references.bond.schedule.api.PmtScheduleAlg;
import com.kdg.fs24.references.bond.schedule.api.PmtScheduleTerm;
import com.kdg.fs24.references.tariffs.serv.TariffServ;
import com.kdg.fs24.application.core.sysconst.SysConst;

/**
 *
 * @author Козыро Дмитрий
 *
 * коллекция классов построителей графиков
 *
 */
//@Data
@Service
public class ContractSchedulesBuilders extends AbstractApplicationBean {

    // коллекция алгоритмов-построителей графиков
    private final Collection<Class<PmtScheduleBuilder>> scheduleBuilders = ServiceFuncs.createCollection();

    @Override
    public void initialize() {

        final Class<PmtScheduleBuilder> clazz = (PmtScheduleBuilder.class);
        final Class<PmtScheduleCalcAlgId> annClazz = PmtScheduleCalcAlgId.class;

        // значения для справочника берутся из аннотаций классов
        ReflectionFuncs.processPkgClassesCollection(SysConst.ENTITY_PACKAGE, clazz, annClazz,
                (entClazz) -> {
                    scheduleBuilders.add(entClazz);
                });

        if (scheduleBuilders.isEmpty()) {
            class NoPmtScheduleBuilderAvailAble extends InternalAppException {

                public NoPmtScheduleBuilderAvailAble(final String message) {
                    super(message);
                }
            }
            throw new NoPmtScheduleBuilderAvailAble(String.format("No schedule builders defined for '%s' ",
                    ContractSchedulesBuilders.class.getCanonicalName()));
        }
    }

    //==========================================================================
    public PmtSchedule buildSchedule(final Integer algId,
            final PmtScheduleAlg pmtScheduleAlg,
            final PmtScheduleTerm pmtScheduleTerm,
            final Integer scheduleKind,
            final LocalDate D1,
            final LocalDate D2
    ) {

        final Class<PmtScheduleBuilder> builder = ServiceFuncs.<Class<PmtScheduleBuilder>>findCollectionElement(
                scheduleBuilders,
                srv -> algId.equals(AnnotationFuncs.getAnnotation(srv, PmtScheduleCalcAlgId.class).calcAlgId()),
                String.format("%s: Can't find schedule builder 'algId=%d' ",
                        this.getClass().getSimpleName(),
                        algId));

        final PmtScheduleBuilder pmtScheduleBuilder = NullSafe.createObject(builder);

        pmtScheduleBuilder.setPmtScheduleAlg(pmtScheduleAlg);
        pmtScheduleBuilder.setPmtScheduleTerm(pmtScheduleTerm);
        pmtScheduleBuilder.setEntityKind(scheduleKind);
        pmtScheduleBuilder.setFrom_date(D1);
        pmtScheduleBuilder.setLast_date(D2);

        final PmtSchedule pmtSchedule = pmtScheduleBuilder.createSchedule();

        pmtSchedule
                .getPmtScheduleLines()
                .stream()
                .forEach((line) -> {
                    line.setPmtSchedule(pmtSchedule);
                });
        return pmtSchedule;
    }
}
