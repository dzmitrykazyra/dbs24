/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.tariffs.kind;

import java.math.BigDecimal;
import java.time.LocalDate;
/**
 *
 * @author Козыро Дмитрий
 */
public interface TariffRowCalculator {
    BigDecimal calculate(LocalDate ld, BigDecimal debtSum,  BigDecimal debtRate);
}
