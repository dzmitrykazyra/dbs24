/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.bond.schedule.collection;

import com.kdg.fs24.bond.schedule.api.PmtScheduleBuilder;
import com.kdg.fs24.bond.schedule.api.PmtScheduleCalcAlgId;
import com.kdg.fs24.bond.schedule.builders.PmtScheduleBuilderImpl;
import com.kdg.fs24.bond.schedule.references.api.PmtScheduleAlg;
import com.kdg.fs24.bond.schedule.references.api.PmtScheduleTerm;
import com.kdg.fs24.entity.classes.AbstractClassesCollection;
import com.kdg.fs24.entity.classes.EntityClassesService;
import com.kdg.fs24.log.mgmt.LogService;
import com.kdg.fs24.annotation.api.AnnotationFuncs;
import com.kdg.fs24.common.api.ServiceFuncs;
import com.kdg.fs24.reflection.api.ReflectionFuncs;
import java.time.LocalDate;

import com.kdg.fs24.services.api.Service;
import com.kdg.fs24.services.api.ServiceLocator;
import com.kdg.fs24.tce.core.NullSafe;

/**
 *
 * @author kazyra_d
 */
public class BondScheduleCalgAlgClassesService extends AbstractClassesCollection implements Service {


    @Override
    public void initializeService() {
        ServiceLocator.find(EntityClassesService.class).registerPackageEntityClasses("com.kdg.fs24.bond.schedule");

        this.registerPackageBuilderClasses("com.kdg.fs24.bond.schedule.builders");
        Service.super.initializeService();

    }

    public <T extends PmtScheduleBuilder> void RegisterCalcAlgClass(final Class<T> algClass) {
        LogService.LogInfo(this.getClass(), LogService.getCurrentObjProcName(this),
                () -> String.format("Register calc alg builder {%s}", algClass.getCanonicalName()));
        this.RegisterAbstractClass(algClass);
    }

    //==========================================================================
    private <T extends PmtScheduleBuilder> Class<T> findCalcAlgClass(final int schedule_alg_id) throws CalcAlgClassNotFoundException {

        return ServiceFuncs.<Class<T>>getCollectionElement(this.getObjectList(),
                p -> this.getScheduleAlgId(p).equals(schedule_alg_id),
                String.format("%s exception: Unknown algId type (%d)",
                        LogService.getCurrentObjProcName(this),
                        schedule_alg_id));
    }

    //==========================================================================
    // найти номер алгоритма из аннотации
    private <T extends PmtScheduleBuilder> Integer getScheduleAlgId(final Class<T> entClass) {
        return (Integer) (NullSafe.create()
                .execute2result(() -> {
                    return ((PmtScheduleCalcAlgId) AnnotationFuncs.getAnnotation(entClass, PmtScheduleCalcAlgId.class)).calcAlgId();
                }))
                .catchException((e) -> {
                    assert (true) :
                            String.format("%s: (%s) - %s (%s) ", LogService.getCurrentObjProcName(this),
                                    entClass.getSimpleName(),
                                    "CalcAlgId is null or annotation is not defined ",
                                    NullSafe.getErrorMessage(e));
                })
                .<Integer>getObject();
    }

    //==========================================================================
    public <T extends PmtScheduleBuilder> T getScheduleBuilder(
            PmtScheduleAlg pmtScheduleAlg,
            PmtScheduleTerm pmtScheduleTerm,
            Integer entityKind,
            LocalDate from_date,
            LocalDate last_date) throws AlgBuilderNotFound {
        PmtScheduleBuilder pmtScheduleBuilder = (PmtScheduleBuilder) (NullSafe.create()
                .execute2result(() -> {
                    return ((PmtScheduleBuilder) NullSafe.createObject((this.findCalcAlgClass(pmtScheduleAlg.getSchedule_alg_id()))))
                            .setPmtScheduleTerm(pmtScheduleTerm)
                            .setPmtScheduleAlg(pmtScheduleAlg)
                            .setScheduleKindId(entityKind)
                            .setFromDate(from_date)
                            .setLasDate(last_date);
                })).<T>getObject();

        if (NullSafe.isNull(pmtScheduleBuilder)) {
            throw new AlgBuilderNotFound(String.format(" AlgBuilderNotFound (algId=%d)",
                    pmtScheduleAlg.getSchedule_alg_id()));
        }

        return (T) pmtScheduleBuilder;
    }

    //==========================================================================
    protected void registerPackageBuilderClasses(final String modulePackage) {

        ReflectionFuncs.createPkgClassesCollection(modulePackage, PmtScheduleBuilderImpl.class)
                .stream()
                .unordered()
                .filter(p -> AnnotationFuncs.isAnnotated(p, PmtScheduleCalcAlgId.class))
                .forEach((clazz) -> {
                    this.RegisterCalcAlgClass(clazz);
                });
        //this.updateActionCodesRef();
    }

    //==========================================================================    
    @Override
    protected void registerReferences() {

    }

}
