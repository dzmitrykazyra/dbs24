/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.calculations;

import org.dbs24.application.core.service.funcs.CustomCollectionImpl;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.entity.debts.LiasDebtRest;
import org.dbs24.entity.debts.LiasAction;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.log.LogService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.references.tariffs.kind.TariffBox;
import org.dbs24.references.tariffs.kind.TariffRateRecord;
import org.dbs24.references.tariffs.kind.TariffKind;
import org.dbs24.references.tariffs.kind.TariffCalcSumExtended;
import org.dbs24.entity.tariff.TariffCalcSum;
import java.util.stream.Collectors;
import lombok.Data;
import reactor.core.publisher.Flux;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
public abstract class TariffBoxAbstract implements TariffBox {

    private final Collection<TariffCalcSumExtended> tariffSums = ServiceFuncs.<TariffCalcSumExtended>createCollection();
    protected final Comparator<LiasDebtRest> RDC = (LiasDebtRest rd1, LiasDebtRest rd2) -> rd1.getRestDate().compareTo(rd2.getRestDate());
    protected final Comparator<LiasAction> LAC = (LiasAction la1, LiasAction la2) -> la1.getLiasDate().compareTo(la2.getLiasDate());
    protected final Comparator<TariffRateRecord> TRRC = (TariffRateRecord rd1, TariffRateRecord rd2) -> rd1.getRateDate().compareTo(rd2.getRateDate());
    private Flux<TariffCalcSumExtended> fluxTariffSums;

    @Override
    public void printCalculations(final TariffKind tariffKind) {

        LogService.LogInfo(this.getClass(), () -> this.tariffSums.stream()
                //LogService.LogInfo(this.getClass(), () -> this.fluxTariffSums.toStream()
                .sorted((r1, r2) -> r1.getTariff_calc_date().compareTo(r2.getTariff_calc_date()))
                .map(tsc
                        -> String.format("tariff_calc_date: %s; accrual_basis: %s; tariff_perc_rate: %s; tariff_sum: %f, tax_sum: %f\n",
                        NLS.getStringDate(tsc.getTariff_calc_date()),
                        tsc.getAccrualBasis(),
                        tsc.getPercRate(),
                        tsc.getTariff_sum(),
                        tsc.getTax_sum()))
                .reduce(String.format("%s:: Flux<TariffCalcSumExtended> (%d records)\n",
                        tariffKind.getClass().getCanonicalName(),
                        tariffSums.size()),
                        (x, y) -> x.concat(y)));
    }
    //==========================================================================

    @Override
    public Collection<TariffCalcSumExtended> getTariffSums() {
        return tariffSums;
    }

    //==========================================================================
    public Collection<TariffCalcSum> getTariffCalcSum() {
        return tariffSums
                .stream()
                .map(mapper -> TariffCalcSum.create(mapper))
                .collect(Collectors.toList());

    }

    //==========================================================================
    protected void addTariffSum(
            final LocalDate calc_date,
            final BigDecimal accrualBasis,
            final BigDecimal accrualRate,
            final BigDecimal accrualSum,
            final BigDecimal taxSum) {

        tariffSums.add(TariffCalcSumExtended.create(calc_date, accrualBasis, accrualRate, accrualSum, taxSum));

    }

    //==========================================================================
    protected void addOrReplaceTariffSum(
            final LocalDate calc_date,
            final BigDecimal accrualBasis,
            final BigDecimal accrualRate,
            final BigDecimal accrualSum,
            final BigDecimal taxSum) {

        NullSafe.create(ServiceFuncs.<TariffCalcSumExtended>getCollectionElement(this.tariffSums,
                ts -> ts.getTariff_calc_date().equals(calc_date)).orElse(null))
                .whenIsNull(() -> {
                    tariffSums.add(TariffCalcSumExtended.create(calc_date, accrualBasis, accrualRate, accrualSum, taxSum));
                })
                .safeExecute((ns_TariffSumm) -> {
                    ((TariffCalcSumExtended) ns_TariffSumm).incTariff_sum(accrualSum);
                });
    }
    //==========================================================================

}
