/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tariff;

import org.dbs24.application.core.log.LogService;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.consts.TariffConst;
import org.dbs24.entity.tariff.TariffKindService;
import org.dbs24.references.tariffs.kind.TariffKindId;
import org.dbs24.entity.tariff.TariffRate_1;
import org.dbs24.entity.AbstractRetailLoanContract;
import org.dbs24.test.api.TestConst;
import org.dbs24.references.serv.TariffServMainPerc;
import java.util.Collection;
import java.time.LocalDate;
import org.dbs24.entity.tariff.TariffCalcSum;
import org.dbs24.entity.debts.LiasDebt;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import static org.dbs24.application.core.sysconst.SysConst.*;
import org.dbs24.references.api.LiasesConst;
import org.dbs24.entity.calculations.TariffRestBox;
import java.math.BigDecimal;
import org.dbs24.entity.tariff.TariffRate;
import org.dbs24.references.tariffs.kind.TariffRowCalculator;
import org.dbs24.repository.LoanContractRepository;
import org.dbs24.spring.core.api.ApplicationBean;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.dbs24.service.TariffStdRates;

/**
 *
 * @author Козыро Дмитрий
 */
@TariffKindId(
        //        tariff_serv_class = TariffServCashBack.class,
        tariff_serv_id = TariffConst.TS_MAIN_PERCENTS,
        tariff_kind_id = TariffConst.TK_CURRENT_RESTS,
        tariff_kind_name = "От фактического остатка на счете",
        tariff_scheme_id = TariffConst.SCH_30_365)
@Data
public class TariffKindMainPercAtRest extends TariffKindService<TariffServMainPerc, AbstractRetailLoanContract, TariffRate_1, TariffRestBox>
        implements ApplicationBean {

    // ссылка на сервис с курсами
    @Autowired
    private TariffStdRates tariffStdRates;
    
//    // тестовая ссылка на репозиторий
//    @Autowired
//    private LoanContractRepository loanContractRepository;

    @Override
    public Collection<TariffCalcSum> calculateTariff(
            final AbstractRetailLoanContract contract,
            final TariffRate tariffRate,
            final LocalDate D1, final LocalDate D2) {

        if (this.getTariffDebug()) {

            this.setStopWatcher(StopWatcher.create(""));

//            LogService.LogInfo(this.getClass(), () -> String.format("%s: %d entries ",
//                    "LoanContractRepository",
//                    loanContractRepository.count()));            
            
            LogService.LogInfo(this.getClass(), () -> String.format("Calculate tariff for %s(%s) [%s:%s]",
                    contract.getClass().getCanonicalName(),
                    this.getClass().getCanonicalName(),
                    D1, D2));
        }

        // остатки по основному долгу
        final LiasDebt liasDebt = contract
                .getContractDebts()
                .stream()
                .filter(debt -> debt.getLiasKind().getLiasKindId().equals(LiasesConst.LKI_RETURN_MAIN_DEBT))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Can't find debt rests (lias_kind_id = %d)", LiasesConst.LKI_RETURN_MAIN_DEBT)));

        if (this.getTariffDebug()) {
            liasDebt.printLiasDebtRests();
            tariffRate.printRates();
        }

        // способ подсчета суммы ежедневного дохода
        final TariffRowCalculator tariffRowCalculator = (rateDate, rateBasis, rate)
                -> BigDecimal.valueOf((double) ((rateBasis.doubleValue()) * rate.doubleValue()) / 36000);

        final TariffRestBox tariffBoxStd = new TariffRestBox(liasDebt.getLiasDebtRests(),
                tariffRate,
                tariffRowCalculator);

        //tariffBoxStd.createCalculations(D1, D2);
        tariffBoxStd.createCalculationsOld(D1, D2, liasDebt.getLiasDebtRests(), tariffRate, tariffRowCalculator);
        if (this.getTariffDebug()) {
            // печать остатков
            tariffBoxStd.printCalculations(this);

            LogService.LogInfo(this.getClass(), () -> String.format("%s %s(%s) [%s:%s]",
                    this.getStopWatcher().getStringExecutionTime(),
                    contract.getClass().getCanonicalName(),
                    this.getClass().getCanonicalName(),
                    D1, D2));

        }

        //return tariffBoxStd.getTariffSums();
        return tariffBoxStd.getTariffCalcSum();
    }
}
