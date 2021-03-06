/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.tariffs.kind;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.api.ReferenceRec;
import java.util.Map;
import org.dbs24.references.tariffs.serv.TariffServ;
import static org.dbs24.consts.SysConst.*;
import org.dbs24.entity.core.api.ActionEntity;
import java.time.LocalDate;
import org.dbs24.application.core.service.funcs.GenericFuncs;
import org.dbs24.application.core.api.ObjectRoot;
import java.math.BigDecimal;
import org.dbs24.application.core.nullsafe.NullSafe;
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
    //--------------------------------------------------------------------------
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class) 
    private LocalDate actual_date;
    //--------------------------------------------------------------------------
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class) 
    private LocalDate close_date;
    //--------------------------------------------------------------------------
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

    public TariffKindAbstract setTariff_kind_id( Integer tariff_kind_id) {
        this.tariff_kind_id = tariff_kind_id;
        return this;
    }

    @Override
    public Integer getTariff_serv_id() {
        return tariff_serv_id;
    }

    public TariffKindAbstract setTariff_serv_id( Integer tariff_serv_id) {
        this.tariff_serv_id = tariff_serv_id;
        return this;
    }

    public String getTariff_kind_name() {
        return tariff_kind_name;
    }

    public TariffKindAbstract setTariff_kind_name( String tariff_kind_name) {
        this.tariff_kind_name = tariff_kind_name;
        return this;
    }

    @Override
    public LocalDate getActual_date() {

        if (NullSafe.isNull(actual_date)) {
            actual_date = LocalDate.now();
        }

        return actual_date;
    }

    public void setActual_date( LocalDate actual_date) {
        this.actual_date = actual_date;
    }

    @Override
    public LocalDate getClose_date() {
        return close_date;
    }

    public void setClose_date( LocalDate close_date) {
        this.close_date = close_date;
    }

    @Override
    public Integer getTariff_scheme_id() {
        return tariff_scheme_id;
    }

    public TariffKindAbstract setTariff_scheme_id( Integer tariff_scheme_id) {
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
//    public void store( Long plan_id) {
//
//        final Integer newRateId = ObjectRoot
//                .getStaticDbService()
//                .createCallQuery("{:RES = call tariff_insertorupdate_TariffRates(:RATE, :PLAN, :SERV, :KIND, :SHEME, :RNAME, :AD, :CD)} ")
//                .setParamByNameAsOutput("RES", INTEGER_ZERO)
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
//    public Collection<TariffCalcSum> calculateTariff( E entity, LocalDate D1, LocalDate D2) {
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
//                .setParamByNameAsOutput("RES", INT_ZERO)
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
//    public void storeTariffSums( LocalDate D1, LocalDate D2) {
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

    public void setTariff_calc_id( Integer tariff_calc_id) {
        this.tariff_calc_id = tariff_calc_id;
    }

    //==========================================================================
//    protected TariffCalcSumOld addTariffCalcSum( LocalDate ld, BigDecimal bd) {
//        return new TariffCalcSumImpl()
//                .setTariff_calc_date(ld)
//                .setAccrualBasis(BIGDECIMAL_ZERO)
//                .setPercRate(BIGDECIMAL_ZERO)
//                .setTariff_sum(bd)
//                .setTax_sum(BIGDECIMAL_NULL);
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
