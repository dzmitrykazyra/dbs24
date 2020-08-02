/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.calculations;

import com.kdg.fs24.application.core.log.LogService;
import com.kdg.fs24.application.core.nullsafe.NullSafe;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import com.kdg.fs24.entity.debts.LiasDebtRest;
import com.kdg.fs24.application.core.sysconst.SysConst;
import com.kdg.fs24.entity.tariff.TariffRate;
import com.kdg.fs24.entity.tariff.TariffRecordAbstract;
import com.kdg.fs24.references.tariffs.kind.TariffCalcSumExtended;
import com.kdg.fs24.references.tariffs.kind.TariffRowCalculator;
import java.util.Iterator;
import java.util.List;
import lombok.Data;
import reactor.core.publisher.Flux;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 *
 * @author kazyra_d
 */
@Data
public final class TariffRestBox extends TariffBoxAbstract {

    //==========================================================================
    public void createCalculationsOld(final LocalDate D1,
            final LocalDate D2,
            final Collection<LiasDebtRest> liasDebtRest,
            final TariffRate tariffRate,
            final TariffRowCalculator tariffRowCalculator) {

        Collections.sort((List<LiasDebtRest>) liasDebtRest, this.RDC);
        Collections.sort((List<TariffRecordAbstract>) tariffRate.getTariffRates(), this.TRRC);

        LocalDate ld1 = D1;
        LocalDate ld2 = D2.plusDays(1);
        BigDecimal dbIteratorBasis;
        BigDecimal percRate;

        final Iterator<LiasDebtRest> restsIterator = liasDebtRest.iterator();
        final Iterator<TariffRecordAbstract> rateIterator = tariffRate.getTariffRates().iterator();

        // начальный базис
        LiasDebtRest v_liasDebtRest = restsIterator.next();
        dbIteratorBasis = v_liasDebtRest.getRest();

        if (restsIterator.hasNext()) {
            while (v_liasDebtRest.getRestDate().isBefore(ld1)) {
                dbIteratorBasis = v_liasDebtRest.getRest();
                if (restsIterator.hasNext()) {
                    v_liasDebtRest = restsIterator.next();
                }
            }
        }

        // начальная ставка     
        TariffRecordAbstract v_tariffRateRecord = rateIterator.next();
        percRate = v_tariffRateRecord.getRateValue();

        if (rateIterator.hasNext()) {
            while (v_tariffRateRecord.getRateDate().isBefore(ld1)) {
                percRate = v_tariffRateRecord.getRateValue();
                if (rateIterator.hasNext()) {
                    v_tariffRateRecord = rateIterator.next();
                }
            }
        }
        while (ld1.isBefore(ld2)) {

            if (ld1.equals(v_liasDebtRest.getRestDate())) {
                dbIteratorBasis = v_liasDebtRest.getRest();
                if (restsIterator.hasNext()) {
                    v_liasDebtRest = restsIterator.next();
                }
            }

            if (ld1.equals(v_tariffRateRecord.getRateDate())) {
                percRate = v_tariffRateRecord.getRateValue();
                if (rateIterator.hasNext()) {
                    v_tariffRateRecord = rateIterator.next();
                }
            }

            final LocalDate ld4add = ld1;
            final BigDecimal accrualbasis = dbIteratorBasis;
            final BigDecimal accrualRate = percRate;
            final BigDecimal accrualSum;

            if (null != tariffRowCalculator) {
                accrualSum = tariffRowCalculator.calculate(ld4add, accrualbasis, percRate);
            } else {
                accrualSum = null;
            }

            this.addTariffSum(ld4add, accrualbasis, accrualRate, accrualSum, SysConst.BIGDECIMAL_NULL);
            ld1 = ld1.plusDays(1);
        }
    }
//===========================================================================

    final Collection<LiasDebtRest> liasDebtRest;
    final TariffRate tariffRate;
    final TariffRowCalculator tariffRowCalculator;
    final Iterator<LiasDebtRest> restsIterator;
    final Iterator<TariffRecordAbstract> rateIterator;

    public TariffRestBox(Collection<LiasDebtRest> liasDebtRest,
            TariffRate tariffRate,
            TariffRowCalculator tariffRowCalculator) {
        this.liasDebtRest = liasDebtRest;
        this.tariffRate = tariffRate;
        this.tariffRowCalculator = tariffRowCalculator;

        Collections.sort((List<LiasDebtRest>) this.liasDebtRest, this.RDC);
        Collections.sort((List<TariffRecordAbstract>) this.tariffRate.getTariffRates(), this.TRRC);

        this.restsIterator = this.liasDebtRest.iterator();
        this.rateIterator = this.tariffRate.getTariffRates().iterator();

        liasDebtRest_last = this.restsIterator.next();
        tariffRateRecord_last = this.rateIterator.next();

    }

    //--------------------------------------------------------------------------
    private LiasDebtRest liasDebtRest_last;

    private BigDecimal getAccrualBasis(final LocalDate ld) {
        // начальный базис
        if (restsIterator.hasNext()) {
            while (liasDebtRest_last.getRestDate().isBefore(ld)
                    || liasDebtRest_last.getRestDate().equals(ld)) {
                if (restsIterator.hasNext()) {
                    liasDebtRest_last = restsIterator.next();
                }
            }
        }

        return liasDebtRest_last.getRest();
    }

    //--------------------------------------------------------------------------
    private TariffRecordAbstract tariffRateRecord_last;

    private BigDecimal getActualRate(final LocalDate ld) {
        // начальный базис

        if (rateIterator.hasNext()) {
            while (tariffRateRecord_last.getRateDate().isBefore(ld)
                    || tariffRateRecord_last.getRateDate().equals(ld)) {
                if (rateIterator.hasNext()) {
                    tariffRateRecord_last = rateIterator.next();
                }
            }
        }

        return tariffRateRecord_last.getRateValue();
    }

    //--------------------------------------------------------------------------
    public void createCalculations(final LocalDate D1,
            final LocalDate D2) {
//        LocalDate ld1 = D1;
        LocalDate ld2 = D2.plusDays(1);

        this.setFluxTariffSums(Flux.generate(
                AtomicInteger::new,
                (state, sink) -> {

                    final LocalDate ld4add = D1.plusDays(state.getAndIncrement());

                    //sink.next("3 x " + i + " = " + 3*i);                  
                    final BigDecimal accrualRate = this.getActualRate(ld4add);
                    //final BigDecimal accrualRate = BigDecimal.valueOf(1);
                    //LogService.LogInfo(this.getClass(), () -> String.format("rate: %s++%s", ld4add, accrualRate));
                    final BigDecimal accrualBasis = this.getAccrualBasis(ld4add);
                    //LogService.LogInfo(this.getClass(), () -> String.format("basis: %s++%s", ld4add, accrualBasis));
                    final BigDecimal accrualSum;

                    //LogService.LogInfo(this.getClass(), () -> "Calc  accr sum");
                    if (NullSafe.notNull(tariffRowCalculator)) {
                        accrualSum = tariffRowCalculator.calculate(ld4add, accrualBasis, accrualRate);
                    } else {
                        accrualSum = null;
                    }

                    sink.next(TariffCalcSumExtended.create(ld4add, accrualBasis, accrualRate, accrualSum, null));

                    if (ld4add.equals(ld2)) {
                        sink.complete();
                    }
                    return state;
                }));

        this.getFluxTariffSums().subscribe().dispose();

    }
}
