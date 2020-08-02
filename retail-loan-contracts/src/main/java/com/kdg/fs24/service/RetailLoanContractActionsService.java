/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.service;

import com.kdg.fs24.application.core.service.funcs.ServiceFuncs;
import com.kdg.fs24.references.bond.schedule.api.PmtScheduleAlg;
import com.kdg.fs24.references.bond.schedule.api.PmtScheduleTerm;
import com.kdg.fs24.entity.core.api.EntityClassesPackages;
import com.kdg.fs24.entity.kind.EntityKind;
import com.kdg.fs24.entity.status.EntityStatus;
import lombok.Data;
import org.springframework.stereotype.Service;
import com.kdg.fs24.entity.retail.loan.contracts.RetailLoanContract;
import java.time.LocalDate;
import com.kdg.fs24.references.liases.debtstate.LiasDebtState;
import com.kdg.fs24.references.liases.kind.LiasKind;
import com.kdg.fs24.references.liases.type.LiasType;
import com.kdg.fs24.references.liases.baseassettype.LiasBaseAssetType;
import com.kdg.fs24.references.liases.finopercode.LiasFinOperCode;
import com.kdg.fs24.references.liases.status.LiasOperStatus;
import com.kdg.fs24.entity.contract.subjects.ContractSubject;
import com.kdg.fs24.entity.counterparties.api.Counterparty;
import com.kdg.fs24.references.loan.api.LoanSource;
import com.kdg.fs24.references.application.currency.Currency;
import com.kdg.fs24.entity.tariff.AbstractTariffPlan;
import com.kdg.fs24.references.tariffs.accretionscheme.TariffAccretionScheme;
import java.math.BigDecimal;
import com.kdg.fs24.entity.core.api.CachedReferencesClasses;
import com.kdg.fs24.consts.RetailLoanContractConst;
import com.kdg.fs24.references.liases.actiontype.LiasActionType;
import com.kdg.fs24.references.documents.docstatus.DocStatus;
import com.kdg.fs24.references.tariffs.serv.TariffServ;
import com.kdg.fs24.references.tariffs.kind.TariffKind;
import com.kdg.fs24.references.documents.doctemplate.DocTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.kdg.fs24.references.documents.docattr.DocAttr;
import com.kdg.fs24.application.core.sysconst.SysConst;
import org.springframework.context.annotation.Import;
import com.kdg.fs24.config.*;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Service
@EntityClassesPackages(pkgList = {SysConst.ENTITY_PACKAGE})
@CachedReferencesClasses(classes = {ContractSubject.class, LoanSource.class, PmtScheduleAlg.class,
    PmtScheduleTerm.class, Currency.class, LiasDebtState.class, LiasKind.class, LiasType.class,
    LiasBaseAssetType.class, LiasFinOperCode.class, LiasOperStatus.class, LiasActionType.class,
    DocStatus.class, DocTemplate.class, DocAttr.class, TariffAccretionScheme.class, TariffServ.class, TariffKind.class})
@Import({RetailLoanContractCommonConfig.class, RetailLoanContractWebSecurityConfig.class})
public class RetailLoanContractActionsService extends ActionExecutionService {

    @Autowired
    private ContractSchedulesBuilders contractSchedulesBuilders;

    @Autowired
    private LiasDocumentBuilders documentBuilders;

    //==========================================================================
    public RetailLoanContract createRetailLoanContract(final ContractSubject contractSubject,
            final Counterparty counterparty,
            final EntityKind entityKind,
            final Currency currency,
            final AbstractTariffPlan tariffPlan,
            final String contractNum,
            final LocalDate contractDate,
            final LocalDate beginDate,
            final LocalDate endDate,
            final BigDecimal contractSumm,
            final LoanSource loanSource,
            final PmtScheduleAlg pmtScheduleAlg,
            final PmtScheduleTerm pmtScheduleTerm) {

        final RetailLoanContract rlc = this.<RetailLoanContract>createActionEntity(RetailLoanContract.class,
                (retailLoanContract) -> {
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
                    //retailLoanContract.setCreation_date(LocalDateTime.now());
                    retailLoanContract.setEntityStatus(EntityStatus.findEntityStatus(RetailLoanContractConst.LOAN2INDIVIDUAL, 0));
                    //EntityStatus.getExistEntityStatus(TariffConst.ENTITY_TARIFF_PLAN, 0));
                    // построить графики погашения
                    //retailLoanContract.createBondschedules();
                });

        return rlc;
    }
}
