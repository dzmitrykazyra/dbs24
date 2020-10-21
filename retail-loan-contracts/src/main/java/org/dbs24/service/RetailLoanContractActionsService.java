/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.references.bond.schedule.api.PmtScheduleAlg;
import org.dbs24.references.bond.schedule.api.PmtScheduleTerm;
import org.dbs24.entity.core.api.EntityClassesPackages;
import org.dbs24.entity.kind.EntityKind;
import org.dbs24.entity.status.EntityStatus;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.dbs24.entity.RetailLoanContract;
import java.time.LocalDate;
import org.dbs24.references.liases.debtstate.LiasDebtState;
import org.dbs24.references.liases.kind.LiasKind;
import org.dbs24.references.liases.type.LiasType;
import org.dbs24.references.liases.baseassettype.LiasBaseAssetType;
import org.dbs24.references.liases.finopercode.LiasFinOperCode;
import org.dbs24.references.liases.status.LiasOperStatus;
import org.dbs24.entity.contract.subjects.ContractSubject;
import org.dbs24.entity.counterparties.api.Counterparty;
import org.dbs24.references.loan.api.LoanSource;
import org.dbs24.references.application.currency.Currency;
import org.dbs24.entity.tariff.AbstractTariffPlan;
import org.dbs24.references.tariffs.accretionscheme.TariffAccretionScheme;
import java.math.BigDecimal;
import org.dbs24.references.core.CachedReferencesClasses;
import org.dbs24.consts.RetailLoanContractConst;
import org.dbs24.references.liases.actiontype.LiasActionType;
import org.dbs24.references.documents.docstatus.DocStatus;
import org.dbs24.references.tariffs.serv.TariffServ;
import org.dbs24.references.tariffs.kind.TariffKind;
import org.dbs24.references.documents.doctemplate.DocTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.dbs24.references.documents.docattr.DocAttr;
import static org.dbs24.application.core.sysconst.SysConst.*;
import org.dbs24.component.RetailLoanBondScheduleBuilder;
import org.springframework.context.annotation.Import;
import org.dbs24.config.*;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Service
@EntityClassesPackages(pkgList = {ENTITY_PACKAGE})
@CachedReferencesClasses(classes = {ContractSubject.class, LoanSource.class, PmtScheduleAlg.class,
    PmtScheduleTerm.class, Currency.class, LiasDebtState.class, LiasKind.class, LiasType.class,
    LiasBaseAssetType.class, LiasFinOperCode.class, LiasOperStatus.class, LiasActionType.class,
    DocStatus.class, DocTemplate.class, DocAttr.class, TariffAccretionScheme.class, TariffServ.class, TariffKind.class})
@Import({RetailLoanContractCommonConfig.class, RetailLoanContractWebSecurityConfig.class})
public class RetailLoanContractActionsService extends AbstractActionExecutionService {

    final ContractSchedulesBuilders contractSchedulesBuilders;
    final LiasDocumentBuilders documentBuilders;
    final RetailLoanBondScheduleBuilder bondScheduleBuilder;

    @Autowired
    public RetailLoanContractActionsService(ContractSchedulesBuilders contractSchedulesBuilders,
            LiasDocumentBuilders documentBuilders,
            RetailLoanBondScheduleBuilder bondScheduleBuilder) {
        this.contractSchedulesBuilders = contractSchedulesBuilders;
        this.documentBuilders = documentBuilders;
        this.bondScheduleBuilder = bondScheduleBuilder;
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
                    retailLoanContract.setEntityStatus(EntityStatus.findEntityStatus(RetailLoanContractConst.LOAN2INDIVIDUAL, 0));
                    // построить графики погашения
                    retailLoanContract.setPmtSchedules(bondScheduleBuilder.createBondschedules(retailLoanContract));
                });
    }
}
