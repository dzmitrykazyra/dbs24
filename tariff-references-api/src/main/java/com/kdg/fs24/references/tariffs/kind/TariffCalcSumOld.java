/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.tariffs.kind;

/**
 *
 * @author Козыро Дмитрий
 */
import java.time.LocalDate;
import java.math.BigDecimal;

@Deprecated
public interface TariffCalcSumOld {

    LocalDate getTariff_calc_date();

    BigDecimal getAccrualBasis();

    BigDecimal getPercRate();

    BigDecimal getTariff_sum();

    BigDecimal getTax_sum();

    TariffCalcSumExtended incTariff_sum(BigDecimal tariff_sum);

}
