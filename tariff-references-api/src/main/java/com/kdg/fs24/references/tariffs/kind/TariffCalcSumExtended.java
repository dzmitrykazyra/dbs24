/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.tariffs.kind;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

/**
 *
 * @author kazyra_d
 */
@Data
public class TariffCalcSumExtended {

    private LocalDate tariff_calc_date;
    private BigDecimal accrualBasis;
    private BigDecimal percRate;
    private BigDecimal tariff_sum;
    private BigDecimal tax_sum;

    public static TariffCalcSumExtended create(final LocalDate tariff_calc_date,
            final BigDecimal accrualBasis,
            final BigDecimal percRate,
            final BigDecimal tariff_sum,
            final BigDecimal tax_sum) {

        final TariffCalcSumExtended tariffCalcSumExtended = new TariffCalcSumExtended();

        tariffCalcSumExtended.setTariff_calc_date(tariff_calc_date);
        tariffCalcSumExtended.setAccrualBasis(accrualBasis);
        tariffCalcSumExtended.setPercRate(percRate);
        tariffCalcSumExtended.setTariff_sum(tariff_sum);
        tariffCalcSumExtended.setTax_sum(tax_sum);

        return tariffCalcSumExtended;
    }

    //==========================================================================
    public void incTariff_sum(final BigDecimal tariff_sum) {
        this.tariff_sum.add(tariff_sum);
    }
}
