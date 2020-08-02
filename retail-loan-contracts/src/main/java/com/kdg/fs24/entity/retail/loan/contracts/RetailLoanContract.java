/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.retail.loan.contracts;

import com.kdg.fs24.consts.RetailLoanContractConst;
import com.kdg.fs24.application.core.sysconst.SysConst;
import com.kdg.fs24.bond.schedule.api.BondScheduleConst;
import com.kdg.fs24.entity.core.api.ActionClassesPackages;
import com.kdg.fs24.entity.core.api.DefaultEntityStatus;
import com.kdg.fs24.entity.core.api.EntityKindId;
import com.kdg.fs24.entity.core.api.EntityStatusesRef;
import com.kdg.fs24.entity.core.api.EntityTypeId;
import com.kdg.fs24.entity.status.EntityStatusId;
import com.kdg.fs24.references.loan.api.LoanSource;
import com.kdg.fs24.references.bond.schedule.api.PmtScheduleAlg;
import com.kdg.fs24.references.bond.schedule.api.PmtScheduleTerm;
import com.kdg.fs24.service.ContractSchedulesBuilders;
import com.kdg.fs24.spring.core.api.ServiceLocator;
import javax.persistence.*;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Entity
@Data
@Table(name = "rlc_loanContracts")
@PrimaryKeyJoinColumn(name = "contract_id", referencedColumnName = "contract_id")
@EntityTypeId(entity_type_id = RetailLoanContractConst.LOAN2INDIVIDUAL,
        entity_type_name = "Кредиты физическим лицам")
@EntityKindId(entity_kind_id = RetailLoanContractConst.LOAN2INDIVIDUAL_CARD,
        entity_type_id = RetailLoanContractConst.LOAN2INDIVIDUAL,
        entity_kind_name = "Кредит ФЛ на карточку")
@EntityStatusesRef(
        entiy_status = {
            @EntityStatusId(
                    entity_type_id = RetailLoanContractConst.LOAN2INDIVIDUAL,
                    entity_status_id = SysConst.ES_VALID,
                    entity_status_name = "Действующая сделка")
            ,
            @EntityStatusId(
                    entity_type_id = RetailLoanContractConst.LOAN2INDIVIDUAL,
                    entity_status_id = SysConst.ES_CLOSED,
                    entity_status_name = "Закрытая сделка")
            ,
            @EntityStatusId(
                    entity_type_id = RetailLoanContractConst.LOAN2INDIVIDUAL,
                    entity_status_id = SysConst.ES_CANCELLED,
                    entity_status_name = "Аннулированная сделка")
        })
@DefaultEntityStatus(entity_status = SysConst.ES_VALID)
@ActionClassesPackages(pkgList = {"com.kdg.fs24.entity.retail.loan.actions",
    "com.kdg.fs24.entity.contracts.actions"})
public class RetailLoanContract extends AbstractRetailLoanContract {

    @ManyToOne
    @JoinColumn(name = "loan_source_id", referencedColumnName = "loan_source_id")
    private LoanSource loanSource;
    @ManyToOne
    @JoinColumn(name = "schedule_alg_id", referencedColumnName = "schedule_alg_id")
    private PmtScheduleAlg pmtScheduleAlg;
    @ManyToOne
    @JoinColumn(name = "pmt_term_id", referencedColumnName = "pmt_term_id")
    private PmtScheduleTerm pmtScheduleTerm;

    @Override
    public void createBondschedules() {
        super.createBondschedules();

        final ContractSchedulesBuilders csb = ServiceLocator.<ContractSchedulesBuilders>findService(ContractSchedulesBuilders.class);

        this.getPmtSchedules().add(csb.buildSchedule(
                BondScheduleConst.BS_ALG_BYREST,
                this.getPmtScheduleAlg(),
                this.getPmtScheduleTerm(),
                 BondScheduleConst.EK_BONDSCHEDULE_MAIN_DEBT,
                 this.getBeginDate(),
                 this.getEndDate()));
        
        this.getPmtSchedules().add(csb.buildSchedule(
                BondScheduleConst.BS_ALG_BYREST,
                this.getPmtScheduleAlg(),
                this.getPmtScheduleTerm(),
                BondScheduleConst.EK_BONDSCHEDULE_PERC,
                this.getBeginDate(),
                this.getEndDate()));      

        // график выплат основного долга
//        loanContract.getPmtScheduleBuilders()
//                .add(ServiceLocator.find(BondScheduleCalgAlgClassesService.class)
//                        .getScheduleBuilder(loanContract.getScheduleAlg(),
//                                loanContract.getScheduleTerm(),
//                                BondScheduleConst.EK_BONDSCHEDULE_MAIN_DEBT,
//                                loanContract.getContract_date(),
//                                loanContract.getEnd_date()));
//        // график выплат процентов
//        loanContract.getPmtScheduleBuilders()
//                .add(ServiceLocator.find(BondScheduleCalgAlgClassesService.class)
//                        .getScheduleBuilder(loanContract.getScheduleAlg(),
//                                loanContract.getScheduleTerm(),
//                                BondScheduleConst.EK_BONDSCHEDULE_PERC,
//                                loanContract.getContract_date(),
//                                loanContract.getEnd_date()));

    }
}
