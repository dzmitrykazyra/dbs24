/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import java.util.Collection;
import lombok.Data;
import org.dbs24.component.PersistenceEntityManager;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.core.CachedReferencesClasses;
import org.dbs24.references.bond.schedule.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.dbs24.consts.BondScheduleReferencesConsts.*;
import static org.dbs24.consts.BondScheduleConst.*;
import static org.dbs24.consts.SysConst.*;

@Data
@Service
@CachedReferencesClasses(classes = {PmtScheduleAlg.class, PmtScheduleTerm.class})
public class BondScheduleReferencesService extends AbstractReferencesService {

    final PersistenceEntityManager persistenceEntityManager;

    @Autowired
    public BondScheduleReferencesService(PersistenceEntityManager persistenceEntityManager) {
        this.persistenceEntityManager = persistenceEntityManager;
    }

    //==========================================================================
    public final PmtScheduleAlg findPmtScheduleAlg(Integer pmtScheduleAlgId) {
        return AbstractRefRecord.<PmtScheduleAlg>getRefeenceRecord(PmtScheduleAlg.class,
                record -> record.getScheduleAlgId().equals(pmtScheduleAlgId));
    }

    //==========================================================================
    public final PmtScheduleTerm findPmtScheduleTerm(Integer pmtScheduleTermId) {
        return AbstractRefRecord.<PmtScheduleTerm>getRefeenceRecord(PmtScheduleTerm.class,
                record -> record.getPmtTermId().equals(pmtScheduleTermId));
    }

    //==========================================================================
    public static final Collection<PmtScheduleAlg> getPmtScheduleAlgCollection() {

        return AbstractReferencesService.<PmtScheduleAlg>getGenericCollection(
                PMT_SCHEDULE_ALG_CLASS, new String[][]{
                    {String.valueOf(BS_ALG_BYREST), "алгоритм расчета графика 1"},
                    {String.valueOf(BS_ALG_ANNUITET), "Алгоритм - 2"},
                    {"1003", "Алгоритм - 3"}
                }, (record, stringRow) -> {
                    record.setScheduleAlgId(Integer.valueOf(stringRow[0]));
                    record.setScheduleAlgName(stringRow[1]);
                });
    }

    //==========================================================================
    public static final Collection<PmtScheduleTerm> getPmtScheduleTermCollection() {

        return AbstractReferencesService.<PmtScheduleTerm>getGenericCollection(
                PMT_SCHEDULE_TERM_CLASS, new String[][]{
                    {"30", "30 дней"},
                    {"60", "60 дней"},
                    {"90", "90 дней"}
                }, (record, stringRow) -> {
                    record.setPmtTermId(Integer.valueOf(stringRow[0]));
                    record.setPmtTermName(stringRow[1]);
                    record.setIsActual(BOOLEAN_TRUE);
                });
    }
}
