/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.bond.schedule.api;

/**
 *
 * @author kazyra_d
 */
import java.time.LocalDate;
import java.math.BigDecimal;

public interface PmtScheduleLine {

    Integer getSchedule_id();

    LocalDate getActual_date();

    LocalDate getFrom_date();

    LocalDate getTo_date();

    LocalDate getAppear_date();

    BigDecimal getPay_sum();

    LocalDate getCalc_date();

}
