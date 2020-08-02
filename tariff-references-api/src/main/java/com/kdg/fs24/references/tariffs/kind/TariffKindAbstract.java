/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.tariffs.kind;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.kdg.fs24.references.api.AbstractRefRecord;
import com.kdg.fs24.references.api.ReferenceRec;
import java.util.Map;
import com.kdg.fs24.references.tariffs.serv.TariffServ;
import com.kdg.fs24.application.core.sysconst.SysConst;
import com.kdg.fs24.entity.core.api.ActionEntity;
import java.time.LocalDate;
import com.kdg.fs24.application.core.service.funcs.GenericFuncs;
import com.kdg.fs24.application.core.api.ObjectRoot;
import java.math.BigDecimal;
import com.kdg.fs24.application.core.nullsafe.NullSafe;
import javax.persistence.*;
import lombok.Data;

/**
 *
 * @author kazyra_d
 */
@Deprecated
public abstract class TariffKindAbstract<TS extends TariffServ, E extends ActionEntity, TR extends TariffRateRecord, TB extends TariffBox>
        extends AbstractRefRecord
        implements TariffKindOld<TS, E, TR>, ReferenceRec {

    private Integer tariff_calc_id;
    private Integer tariff_kind_id;
    private Integer tariff_serv_id;
    private Integer tariff_scheme_id;
    //private Integer rate_id;
    private String tariff_kind_name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SysConst.DATE_FORMAT)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate actual_date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SysConst.DATE_FORMAT)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate close_date;
    // тарифицируемая услуга
    private TS tariffServ;
    // ставки тарифа
//    private TariffRate<TR> tariffRate;

    public <ANN extends TariffKindId> TariffKindAbstract() {
        super();
//        this.tariffRate = null;
    }

//    public TariffKindAbs(E entity) {
//        this.entity = entity;
//    }
    //==========================================================================
    public TS getTariffServ() {
        return tariffServ;
    }

    @Override
    public Integer getTariff_kind_id() {
        return tariff_kind_id;
    }

    public TariffKindAbstract setTariff_kind_id(final Integer tariff_kind_id) {
        this.tariff_kind_id = tariff_kind_id;
        return this;
    }

    @Override
    public Integer getTariff_serv_id() {
        return tariff_serv_id;
    }

    public TariffKindAbstract setTariff_serv_id(final Integer tariff_serv_id) {
        this.tariff_serv_id = tariff_serv_id;
        return this;
    }

    public String getTariff_kind_name() {
        return tariff_kind_name;
    }

    public TariffKindAbstract setTariff_kind_name(final String tariff_kind_name) {
        this.tariff_kind_name = tariff_kind_name;
        return this;
    }

    @Override
    public void record2Map(final Map<String, Integer> map) {
        map.put(String.format("%d - %s", this.getTariff_serv_id(), this.getTariff_kind_name()), this.getTariff_serv_id());
    }

    @Override
    public LocalDate getActual_date() {

        if (NullSafe.isNull(actual_date)) {
            actual_date = LocalDate.now();
        }

        return actual_date;
    }

    public void setActual_date(final LocalDate actual_date) {
        this.actual_date = actual_date;
    }

    @Override
    public LocalDate getClose_date() {
        return close_date;
    }

    public void setClose_date(final LocalDate close_date) {
        this.close_date = close_date;
    }

    @Override
    public Integer getTariff_scheme_id() {
        return tariff_scheme_id;
    }

    public TariffKindAbstract setTariff_scheme_id(final Integer tariff_scheme_id) {
        this.tariff_scheme_id = tariff_scheme_id;
        return this;
    }

    //==========================================================================
//    @Override
//    public TariffRate<TR> getTariffRate() {
//
//        return NullSafe.create(this.tariffRate)
//                .whenIsNull(() -> {
//                    this.tariffRate = NullSafe.createObject(TariffRate.class, tariff_serv_id);
//                    this.tariffRate.setKind_id(tariff_kind_id);
//                    return this.tariffRate;
//                }).<TariffRate<TR>>getObject();
//    }

    //==========================================================================
    @Override
    public <TR extends TariffRateRecord> TR buildTariffRateRecord() {

        return NullSafe.create()
                .execute2result(() -> {
                    final Class clazz = GenericFuncs.getTypeParameterClass(this.getClass(), 2);
                    return NullSafe.createObject(clazz);
                }).<TR>getObject();
    }

    //==========================================================================
//    @Override
//    public void store(final Long plan_id) {
//
//        final Integer newRateId = ObjectRoot
//                .getStaticDbService()
//                .createCallQuery("{:RES = call tariff_insertorupdate_TariffRates(:RATE, :PLAN, :SERV, :KIND, :SHEME, :RNAME, :AD, :CD)} ")
//                .setParamByNameAsOutput("RES", SysConst.INTEGER_ZERO)
//                .setParamByName("RATE", this.getTariffRate().getRate_id())
//                .setParamByName("PLAN", plan_id)
//                .setParamByName("SERV", this.tariff_serv_id)
//                .setParamByName("KIND", this.tariff_kind_id)
//                .setParamByName("SHEME", this.tariff_scheme_id)
//                .setParamByName("RNAME", this.tariff_kind_name)
//                .setParamByName("AD", this.actual_date)
//                .setParamByName("CD", this.close_date)
//                .<Integer>getSingleFieldValue();
//
//        if (NullSafe.isNull(this.getTariffRate().getRate_id())) {
//            this.getTariffRate().setRate_id(newRateId);
////            this.getTariffRate().setKind_id(this.tariff_kind_id);
//        };
//
//        this.getTariffRate().store();
//    }

    //==========================================================================
    @Override
    public TariffKindOld createTariffKindCopy() throws CloneNotSupportedException {

//        Entity newEntity;
//
//        newEntity = (Entity) super.clone();
        return (TariffKindOld) super.clone();

    }

    //==========================================================================
    // рассчитать сумму тарифа за период
//    @Override
//    public Collection<TariffCalcSum> calculateTariff(final E entity, final LocalDate D1, final LocalDate D2) {
//        LogService.LogInfo(this.getClass(), String.format("Calculate tariff for %s ",
//                entity.getClass().getCanonicalName()));
//        // заполнили в entity детализацию тарифа
//        return null;
//    }
    //==========================================================================
    // получить сумму тарифа за период
//    @Override
//    public final BigDecimal getTariffSum(
//            final TariffCalculations tc,
//            final E entity_contract,
//            final LocalDate D1,
//            final LocalDate D2) {
//
//        return (BigDecimal) NullSafe.create()
//                .initWatcher()
//                .execute2result(() -> {
//                    // получаем сумму тарифа
//                    return tc.getCalculationSum(tariffRate,
//                            () -> {
//                                return this.calculateTariff(entity_contract, D1, D2);
//                            },
//                            D1, D2);
//                })
//                .throwException()
//                .showResult(String.format("\n%s: Сумма тарифа [%s, %s]= %%f",
//                        this.getClass().getCanonicalName(),
//                        NLS.getStringDate(D1),
//                        NLS.getStringDate(D2)))
//                .getObject();
//
//    }
//==========================================================================
//    private void storeCalcRecord() {
//        final Integer newTariff_calc_id = ObjectRoot
//                .getStaticDbService()
//                .createCallQuery("{:RES = call tariff_insertorupdate_calc_record(:ID, :SERV, :KIND, :ENT, :CURR)} ")
//                .setParamByNameAsOutput("RES", SysConst.INT_ZERO)
//                .setParamByName("ID", this.getTariff_calc_id())
//                .setParamByName("SERV", this.tariff_serv_id)
//                .setParamByName("KIND", this.tariff_kind_id)
//                .setParamByName("CURR", 933)
//                .getIntFieldValue();
//
//        if (NullSafe.isNull(this.getTariff_calc_id())) {
//            this.setTariff_calc_id(newTariff_calc_id);
//        };
//    }
//==========================================================================
// сохранить рассчитанную сумму тарифа за период    
//    @Override
//    public void storeTariffSums(final LocalDate D1, final LocalDate D2) {
//
//        this.storeCalcRecord();
//
//        ObjectRoot
//                .getStaticDbService()
//                .createCallBath("{call tariff_insertorupdate_calc_sum(:TCID, :TCD, :TCS )}")
//                .execBatch(stmt -> {
//
//                    this.getTariffCalsSums()
//                            .stream()
//                            .filter(tcs -> tcs.getTariff_calc_date().isAfter(D1)
//                            && tcs.getTariff_calc_date().isBefore(D2))
//                            .forEach((tcs) -> {
//                                stmt.setParamByName("TCID", this.getTariff_calc_id());
//                                stmt.setParamByName("TCD", tcs.getTariff_calc_date());
//                                stmt.setParamByName("TCS", tcs.getTariff_sum());
//                                stmt.addBatch();
//                            });
//                });
//
//    }
    public Integer getTariff_calc_id() {
        return tariff_calc_id;
    }

    public void setTariff_calc_id(final Integer tariff_calc_id) {
        this.tariff_calc_id = tariff_calc_id;
    }

    //==========================================================================
//    protected TariffCalcSumOld addTariffCalcSum(final LocalDate ld, final BigDecimal bd) {
//        return new TariffCalcSumImpl()
//                .setTariff_calc_date(ld)
//                .setAccrualBasis(SysConst.BIGDECIMAL_ZERO)
//                .setPercRate(SysConst.BIGDECIMAL_ZERO)
//                .setTariff_sum(bd)
//                .setTax_sum(SysConst.BIGDECIMAL_NULL);
//
//    }

    //==========================================================================
//    protected TariffBoxStd createTariffCalcTable(
//            final LocalDate D1,
//            final LocalDate D2,
//            final Collection<LiasDebtRest> liasDebtRest,
//            final TariffRate tariffRate,
//            final TariffRowCalculator tariffRowCalculator) {
//        return new TariffBoxStd(D1, D2, liasDebtRest, tariffRate, tariffRowCalculator);
//    }
    //==========================================================================
    public TB buildTariffBox() {

        return (TB) NullSafe.create()
                .execute2result(() -> NullSafe.createObject(GenericFuncs.getTypeParameterClass(this.getClass(), 3))).<TB>getObject();
    }
    //==========================================================================
}
