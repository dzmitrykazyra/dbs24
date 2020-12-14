/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import org.dbs24.references.bond.schedule.api.PmtScheduleAlg;
import org.dbs24.references.bond.schedule.api.PmtScheduleTerm;
import org.dbs24.entity.core.api.EntityClassesPackages;
import org.dbs24.entity.kind.EntityKind;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.dbs24.entity.RetailLoanContract;
import java.time.LocalDate;
import org.dbs24.entity.ContractSubject;
import org.dbs24.entity.Counterparty;
import org.dbs24.references.loan.api.LoanSource;
import org.dbs24.entity.Currency;
import org.dbs24.entity.tariff.AbstractTariffPlan;
import java.math.BigDecimal;
import lombok.extern.log4j.Log4j2;
import org.dbs24.consts.RetailLoanContractConst;
import org.springframework.beans.factory.annotation.Autowired;
import static org.dbs24.consts.SysConst.*;
import org.dbs24.component.RetailLoanBondScheduleBuilder;
import org.springframework.context.annotation.Import;
import org.dbs24.config.*;

@Data
@Service
@EntityClassesPackages(pkgList = {ENTITY_PACKAGE})
@Log4j2
//@CachedReferencesClasses(classes = {ContractSubject.class, LoanSource.class, PmtScheduleAlg.class,
//    PmtScheduleTerm.class, Currency.class, LiasDebtState.class, LiasKind.class, LiasType.class,
//    LiasBaseAssetType.class, LiasFinOperCode.class, LiasOperStatus.class, LiasActionType.class,
//    DocStatus.class, DocTemplate.class, DocAttr.class, TariffAccretionScheme.class, TariffServ.class, TariffKind.class})
@Import({RetailLoanContractCommonConfig.class,
    RetailLoanContractWebSecurityConfig.class,
    RetailLoanContractRSocketConfig.class
})
public class RetailLoanContractActionsService extends AbstractActionExecutionService {

    final ContractSchedulesBuilders contractSchedulesBuilders;
    final LiasDocumentBuilders documentBuilders;
    final RetailLoanBondScheduleBuilder bondScheduleBuilder;
    final EntityReferencesService entityReferencesService;

    @Autowired
    public RetailLoanContractActionsService(ContractSchedulesBuilders contractSchedulesBuilders,
            LiasDocumentBuilders documentBuilders,
            RetailLoanBondScheduleBuilder bondScheduleBuilder,
            EntityReferencesService entityReferencesService) {
        this.contractSchedulesBuilders = contractSchedulesBuilders;
        this.documentBuilders = documentBuilders;
        this.bondScheduleBuilder = bondScheduleBuilder;
        this.entityReferencesService = entityReferencesService;
    }

    //==========================================================================
    public RetailLoanContract createRetailLoanContract(ContractSubject contractSubject,
            Counterparty counterparty,
            EntityKind entityKind,
            Currency currency,
            AbstractTariffPlan tariffPlan,
            String contractNum,
            LocalDate contractDate,
            LocalDate beginDate,
            LocalDate endDate,
            BigDecimal contractSumm,
            LoanSource loanSource,
            PmtScheduleAlg pmtScheduleAlg,
            PmtScheduleTerm pmtScheduleTerm) {

        return this.<RetailLoanContract>createActionEntity(RetailLoanContract.class,
                retailLoanContract -> {
                    retailLoanContract.setContractSubject(contractSubject);
                    retailLoanContract.setCounterparty(counterparty);
                    retailLoanContract.setCurrency(currency);
                    retailLoanContract.setTariffPlan(tariffPlan);
                    retailLoanContract.setContractNum(contractNum);
                    retailLoanContract.setContractDate(contractDate);
                    retailLoanContract.setBeginDate(beginDate);
                    retailLoanContract.setEndDate(endDate);
                    retailLoanContract.setPmtScheduleAlg(pmtScheduleAlg);
                    retailLoanContract.setPmtScheduleTerm(pmtScheduleTerm);
                    retailLoanContract.setEntityKind(entityKind);
                    retailLoanContract.setContractSumm(contractSumm);
                    retailLoanContract.setLoanSource(loanSource);
                    retailLoanContract.setEntityStatus(entityReferencesService.findEntityStatus(RetailLoanContractConst.LOAN2INDIVIDUAL, 0));
                    // построить графики погашения
                    //retailLoanContract.setPmtSchedules(bondScheduleBuilder.createBondschedules(retailLoanContract));
                });
    }
}
