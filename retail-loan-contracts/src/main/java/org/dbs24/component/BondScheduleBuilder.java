/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import org.dbs24.spring.core.bean.AbstractApplicationBean;
import org.springframework.stereotype.Component;
import org.dbs24.entity.RetailLoanContract;
import org.dbs24.entity.bondschedule.PmtSchedule;
import java.util.Collection;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.bond.schedule.api.BondScheduleConst;
import org.dbs24.service.ContractSchedulesBuilders;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author N76VB
 */
@Component
public class BondScheduleBuilder extends AbstractApplicationBean {

    private final ContractSchedulesBuilders contractSchedulesBuilders;

    @Autowired
    public BondScheduleBuilder(ContractSchedulesBuilders contractSchedulesBuilders) {
        this.contractSchedulesBuilders = contractSchedulesBuilders;
    }

    //==========================================================================
    public Collection<PmtSchedule> createBondschedules(RetailLoanContract retailLoanContract) {

        final Collection<PmtSchedule> pmtSchedules = ServiceFuncs.<PmtSchedule>getOrCreateCollection(ServiceFuncs.COLLECTION_NULL);

        pmtSchedules.add(contractSchedulesBuilders.buildSchedule(
                BondScheduleConst.BS_ALG_BYREST,
                retailLoanContract.getPmtScheduleAlg(),
                retailLoanContract.getPmtScheduleTerm(),
                BondScheduleConst.EK_BONDSCHEDULE_MAIN_DEBT,
                retailLoanContract.getBeginDate(),
                retailLoanContract.getEndDate()));

        pmtSchedules.add(contractSchedulesBuilders.buildSchedule(
                BondScheduleConst.BS_ALG_BYREST,
                retailLoanContract.getPmtScheduleAlg(),
                retailLoanContract.getPmtScheduleTerm(),
                BondScheduleConst.EK_BONDSCHEDULE_PERC,
                retailLoanContract.getBeginDate(),
                retailLoanContract.getEndDate()));

        return pmtSchedules;
    }
}
