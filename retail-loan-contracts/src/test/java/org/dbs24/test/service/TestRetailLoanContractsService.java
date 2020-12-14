/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import static org.dbs24.consts.SecurityConst.*;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.application.core.service.funcs.TestFuncs;
import static org.dbs24.consts.BondScheduleConst.BS_ALG_BYREST;
import org.dbs24.consts.RetailLoanContractConst;
import static org.dbs24.consts.SysConst.LOCALDATE_NULL;
import static org.dbs24.consts.SysConst.MAX_BIGDECIMAL;
import static org.dbs24.consts.SysConst.SERVICE_USER_ID;
import org.dbs24.consts.TariffConst;
import org.dbs24.entity.ContractSubject;
import org.dbs24.entity.Counterparty;
import org.dbs24.entity.Currency;
import org.dbs24.entity.RetailLoanContract;
import org.dbs24.entity.kind.EntityKind;
import org.dbs24.entity.tariff.AbstractTariffPlan;
import org.dbs24.references.bond.schedule.api.PmtScheduleAlg;
import org.dbs24.references.bond.schedule.api.PmtScheduleTerm;
import org.dbs24.references.loan.api.LoanSource;
import org.dbs24.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.Data;

@Data
@Service
public class TestRetailLoanContractsService extends AbstractTestService<RetailLoanContract> {

    @Autowired
    private RetailLoanContractActionsService retailLoanContractActionsService;

    @Autowired
    private CounterpartyActionsService counterpartyActionsService;

    @Autowired
    private TariffCoreService tariffCoreActionsService;

    @Autowired
    private TariffReferencesService tariffReferencesService;

    @Autowired
    private EntityReferencesService entityReferencesService;

    @Autowired
    private EntityContractReferencesService entityContractReferencesService;

    @Autowired
    private ApplicationReferencesService applicationReferencesService;

    @Autowired
    private RetailLoanContractReferencesService retailLoanContractReferencesService;

    @Autowired
    private BondScheduleReferencesService bondScheduleReferencesService;

    @Autowired
    private SecurityActionsService securityActionsService;

    //==========================================================================
    public RetailLoanContract createTestContract_1Y_840() {
        final String testString = TestFuncs.generateTestString20();

        final ContractSubject contractSubject = entityContractReferencesService.findContractSubject(210001);

        final EntityKind entityKind = entityReferencesService.findEntityKind(RetailLoanContractConst.LOAN2INDIVIDUAL_CARD);

        final Counterparty counterparty = this.counterpartyActionsService.createCounterparty(testString, testString, testString);

        final Currency currency = applicationReferencesService.findCurrency(840);

        final AbstractTariffPlan tariffPlan = null; //this.createTestTariffPlan_rate_1(); //this.getRetailLoanContractActionsService().<AbstractTariffPlan>findActionEntity(AbstractTariffPlan.class, Long.valueOf(68338)).get();
        final String contractNum = testString;
        final LocalDate contractDate = LocalDate.now();
        final LocalDate beginDate = LocalDate.now();
        final LocalDate endDate = beginDate.plusYears(1);
        final BigDecimal contractSumm = MAX_BIGDECIMAL;
        final LoanSource loanSource = retailLoanContractReferencesService.findLoanSource(102);
        // код графика выплат ОД
        final PmtScheduleAlg pmtScheduleAlg = bondScheduleReferencesService.findPmtScheduleAlg(BS_ALG_BYREST);
        // срок
        final PmtScheduleTerm pmtScheduleTerm = bondScheduleReferencesService.findPmtScheduleTerm(30);

        return this.retailLoanContractActionsService
                .createRetailLoanContract(contractSubject,
                        counterparty,
                        entityKind,
                        currency,
                        tariffPlan,
                        contractNum,
                        contractDate,
                        beginDate,
                        endDate,
                        contractSumm,
                        loanSource,
                        pmtScheduleAlg,
                        pmtScheduleTerm);
    }

    //==========================================================================
    protected AbstractTariffPlan createTestTariffPlan_rate_1() {
        final String testString = TestFuncs.generateTestString20();
        final Integer kindId = TariffConst.EK_TP_FOR_RETAIL_LOAN_CONTRACT;

        final EntityKind entityKind = entityReferencesService.findEntityKind(kindId);

        if (NullSafe.isNull(entityKind)) {
            throw new RuntimeException(String.format("EntityKind is not found(%d)", kindId));
        }

        final AbstractTariffPlan tariffPlanImpl = tariffCoreActionsService
                .createTariffPlan(testString, testString, entityKind, LocalDate.now(), LocalDate.now(),
                        (plan) -> {

                            plan.addServKindId(tariffReferencesService.findTariffKind(TariffConst.TK_CURRENT_RESTS),
                                    LocalDate.now(),
                                    LOCALDATE_NULL,
                                    (tariffPlan2Serv) -> {
                                        // добавление услуги в тарифный план
                                        tariffPlan2Serv.addTariffRate(tariffReferencesService.findTariffAccretionScheme(1),
                                                tariffReferencesService.findTariffKind(TariffConst.TK_CURRENT_RESTS), LocalDate.now(), LOCALDATE_NULL, testString,
                                                (tariffRate_1) -> {

                                                    final LocalDate ld = LocalDate.now();

                                                    tariffRate_1.addTariffRate_1(ld.minusDays(50), BigDecimal.valueOf(1.9), applicationReferencesService.findCurrency(840));
                                                    tariffRate_1.addTariffRate_1(ld.minusDays(40), BigDecimal.valueOf(2.9), applicationReferencesService.findCurrency(840));
                                                    tariffRate_1.addTariffRate_1(ld, BigDecimal.valueOf(3.3), applicationReferencesService.findCurrency(840));
                                                    tariffRate_1.addTariffRate_1(ld.plusDays(30), BigDecimal.valueOf(3.9), applicationReferencesService.findCurrency(840));
                                                    tariffRate_1.addTariffRate_1(ld.plusDays(40), BigDecimal.valueOf(4.9), applicationReferencesService.findCurrency(840));
                                                    tariffRate_1.addTariffRate_1(ld.plusDays(60), BigDecimal.valueOf(5.9), applicationReferencesService.findCurrency(840));
                                                });
                                    });
                        });

        tariffCoreActionsService.executeAction(tariffPlanImpl, TariffConst.ACT_MODIFY_TARIFF_PLAN, null);
        tariffCoreActionsService.executeAction(tariffPlanImpl, TariffConst.ACT_AUTHORIZE_TARIFF_PLAN, null);

        return tariffPlanImpl;
    }
}
