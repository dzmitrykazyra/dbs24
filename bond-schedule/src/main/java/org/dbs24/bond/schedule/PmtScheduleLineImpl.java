/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.bond.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.dbs24.bond.schedule.api.PmtScheduleLine;
import org.dbs24.api.SysConst;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author kazyra_d
 */
public class PmtScheduleLineImpl implements PmtScheduleLine {

    private Integer schedule_id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDate actual_date;

    private LocalDate from_date;

    private LocalDate to_date;

    private LocalDate appear_date;

    private BigDecimal pay_sum;

    private LocalDate calc_date;

    //==========================================================================
    public PmtScheduleLineImpl() {
        super();
    }

    @Override
    public Integer getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(final Integer schedule_id) {
        this.schedule_id = schedule_id;
    }

    @Override
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate getActual_date() {
        return actual_date;
    }

    public void setActual_date(final LocalDate actual_date) {
        this.actual_date = actual_date;
    }

    @Override
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate getFrom_date() {
        return from_date;
    }

    public void setFrom_date(final LocalDate from_date) {
        this.from_date = from_date;
    }

    @Override
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate getTo_date() {
        return to_date;
    }

    public void setTo_date(final LocalDate to_date) {
        this.to_date = to_date;
    }

    @Override
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate getAppear_date() {
        return appear_date;
    }

    public void setAppear_date(final LocalDate appear_date) {
        this.appear_date = appear_date;
    }

    @Override
    public BigDecimal getPay_sum() {
        return pay_sum;
    }

    public void setPay_sum(final BigDecimal pay_sum) {
        this.pay_sum = pay_sum;
    }

    @Override
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate getCalc_date() {
        return calc_date;
    }

    public void setCalc_date(final LocalDate calc_date) {
        this.calc_date = calc_date;
    }

}
