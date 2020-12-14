/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.tariff;

import org.dbs24.application.core.nullsafe.NullSafe;
import java.time.LocalDate;
import java.util.Collection;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import java.util.Map;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import org.dbs24.application.core.api.ObjectRoot;
import static org.dbs24.consts.SysConst.*;
import org.dbs24.application.core.nullsafe.NullSafe;

/**
 *
 * @author Козыро Дмитрий
 */
@Deprecated
public class TariffCalculations {

    private Long entity_id;
    private Map<TariffRate, Collection<TariffCalcSum>> tariffCalsSums;
    private Integer tariff_calc_id;

    //==========================================================================
    public TariffCalculations( Long entity_id) {
        this.entity_id = entity_id;
    }

    public Map<TariffRate, Collection<TariffCalcSum>> getTariffCalsSums() {
        return NullSafe.create(this.tariffCalsSums)
                .whenIsNull(() -> {
                    this.tariffCalsSums = ServiceFuncs.<TariffRate, Collection<TariffCalcSum>>
                    getOrCreateMap(ServiceFuncs.MAP_NULL);
                    return this.tariffCalsSums;
                }).<Map<TariffRate, Collection<TariffCalcSum>>>getObject();
    }

    public void setTariffCalsSums( Map<TariffRate, Collection<TariffCalcSum>> tariffCalsSums) {
        this.tariffCalsSums = tariffCalsSums;
    }

//    public void setTariffCalsSums(Map<TariffRate, Collection<TariffCalcSum>> tariffCalsSums) {
//        this.tariffCalsSums = tariffCalsSums;
//    }
    //==========================================================================
    private BigDecimal getInternalCalculationSum(
            final TariffRate tariffRate,
            final LocalDate D1,
            final LocalDate D2) {

        if (NullSafe.isNull(this.getTariffCalsSums())) {
            return BIGDECIMAL_NULL;
        } else {

            return ((Collection<TariffCalcSum>) this.getTariffCalsSums()
                    .entrySet()
                    .stream()
                    .unordered()
                    .filter(tr -> tr.getKey().getRateId().equals(tariffRate.getRateId()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                    .get(tariffRate))
                    .stream()
                    .unordered()
                    .filter(tcs -> tcs.getTariffCalcDate().isAfter(D1.minusDays(1))
                    && tcs.getTariffCalcDate().isBefore(D2.plusDays(1)))
                    .map(mapper -> mapper.getTariffSumm())
                    .reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
        }
    }

    //==========================================================================
    public BigDecimal getCalculationSum(
            final TariffRate tariffRate,
            final TarrifGenerator tarrifGenerator,
            final LocalDate D1,
            final LocalDate D2) {

        return NullSafe.create(BIGDECIMAL_ZERO, IS_SILENT_EXECUTE)
                .execute2result(() -> {
                    return this.getInternalCalculationSum(tariffRate, D1, D2);
                })
                .whenIsNull(() -> {
                    //this.setTariffCalsSums(tarrifGenerator.execute());
                    this.getTariffCalsSums().put(tariffRate, tarrifGenerator.execute());
                    return this.getInternalCalculationSum(tariffRate, D1, D2);
                })
                .<BigDecimal>getObject();
    }

//==========================================================================
//    private void storeCalcRecord( Integer rate_id) {
//        final Integer newTariff_calc_id = ObjectRoot
//                .getStaticDbService()
//                .createCallQuery("{:RES = call tariff_insertorupdate_calc_record(:ID, :RATE, :ENT)} ")
//                .setParamByNameAsOutput("RES", INTEGER_ZERO)
//                .setParamByName("ID", this.getTariff_calc_id())
//                .setParamByName("RATE", rate_id)
//                .setParamByName("ENT", this.getEntity_id())
//                .<Integer>getSingleFieldValue();
//
//        //if (NullSafe.isNull(this.getTariff_calc_id())) {
//        this.setTariff_calc_id(newTariff_calc_id);
//        //};
//    }
    //==========================================================================
//    public void store( LocalDate D1, LocalDate D2) {
//
//        tariffCalsSums
//                .entrySet()
//                .stream()
//                .forEach(rec -> {
//                    storeCalcRecord(rec.getKey().getRate_id());
//
//                    ObjectRoot
//                            .getStaticDbService()
//                            .createCallBath("{call tariff_insertorupdate_calc_sum(:TCID, :TCD, :TCS )}")
//                            .execBatch(stmt -> {
//
//                                rec.getValue()
//                                        .stream()
//                                        .filter(tcs -> tcs.getTariff_calc_date().isAfter(D1)
//                                        && tcs.getTariff_calc_date().isBefore(D2))
//                                        .forEach((tcs) -> {
//                                            stmt.setParamByName("TCID", this.getTariff_calc_id());
//                                            stmt.setParamByName("TCD", tcs.getTariff_calc_date());
//                                            stmt.setParamByName("TCS", tcs.getTariff_sum());
//                                            stmt.addBatch();
//                                        });
//                            });
//                });
//    }
    //==========================================================================
    public TariffCalculations merge( TariffCalculations tc, LocalDate D1, LocalDate D2) {

        tc.tariffCalsSums
                .entrySet()
                .stream()
                .forEach(calc -> {

                    final Map<TariffRate, Collection<TariffCalcSum>> mapExistTariffRate
                            = this.getTariffCalsSums()
                                    .entrySet()
                                    .stream()
                                    .unordered()
                                    .filter(tr -> tr.getKey().equals(calc.getKey()))
                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                    if (!mapExistTariffRate.isEmpty()) {
                        // накатываем обновления
                        final Collection<TariffCalcSum> existsTariffSums = mapExistTariffRate.get(0);

                        calc.getValue()
                                .stream()
                                .forEach((newSum) -> {

//                        existsTariffSums
//                                .stream()
//                                .filter(ex -> ex.getTariff_calc_date().equals(newSum.getTariff_calc_date()))
//                                .forEach(exx -> {
//                                    exx.s(ts.getTariff_sum());
//                                })
                                });

                    } else {
                        this.getTariffCalsSums().put(calc.getKey(), calc.getValue());
                    }

                });

        return this;
    }

    //==========================================================================
    public void add( TariffRate tariffRate, Collection<TariffCalcSum> cs) {
        //this.getTariffCalsSums().putIfAbsent(tariffRate, cs);
        NullSafe.create(cs)
                .safeExecute(() -> {
                    this.getTariffCalsSums().put(tariffRate, cs);
                });
    }

    //==========================================================================
//    private final Comparator<TariffAccretionHisory> cmp = (TariffAccretionHisory tah1, TariffAccretionHisory tah2) -> tah1.getAccretion_date().compareTo(tah2.getAccretion_date());
//
//    private LocalDate getLastAccretionDate(
//            Collection<TariffAccretionHisory> tah,
//            final LocalDate defaultStartDate,
//            final LocalDate nextAccretionDate) {
//        // получаем дату последнего начисления
//
//        return (LocalDate) NullSafe.create(defaultStartDate, NullSafe.DONT_THROW_EXCEPTION)
//                .execute2result(() -> {
//
//                    return NullSafe.<LocalDate>nvl(tah
//                            .stream()
//                            .unordered()
//                            .filter((fltr) -> {
//                                return fltr.getAccretion_date().isBefore(nextAccretionDate);
//                            })
//                            .max(this.cmp)
//                            .get()
//                            .getAccretion_date(), defaultStartDate);
//                }).getObject();
//    }
    //==========================================================================
//    public Collection<NewLiasOper> createFinOpers(
//            final LiasOpersTemplate lot,
//            final LocalDate defaultStartDate,
//            final LocalDate accretionDate,
//            final Collection<TariffAccretionHisory> tah) {
//
//        //LiasOper2Tariff lo2t;
//        // получили список повторяющихся аннотаций
//        final Collection<LiasOper2Tariff> col = AnnotationFuncs.<LiasOper2Tariff>getRepeatedAnnotation(lot.getClass(), LiasOper2Tariff.class);
//        // список новых финопераций
//        final Collection<NewLiasOper> loc = ServiceFuncs.<NewLiasOper>createCollection();
//
//        // пробежали по списку и получили значения аннотиаций
//        col.stream()
//                .forEach((lo2t) -> {
//                    // формируем операции
//                    LogService.LogInfo(this.getClass(), String.format("serv_id = %d ", lo2t.tariff_serv_id()));
//
//                    // находим рассчитанные суммы калькуляции по коду услуги
//                    Collection<TariffCalcSum> colTCS
//                            = ServiceFuncs.<TariffRate, Collection<TariffCalcSum>>getMapElement(this.tariffCalsSums,
//                                    mapEntry -> mapEntry.getKey().getServ_id().equals(lo2t.tariff_serv_id()),
//                                    String.format("Calculation is not found (serv_id = %d)", lo2t.tariff_serv_id()),
//                                    ServiceFuncs.SF_DONT_THROW_EXC);
//
//                    final LocalDate lastAccretionDate;
//
//                    // есть начисление
//                    if (null != colTCS) {
//
//                        // получаем дату последнего начисления
//                        lastAccretionDate = this.getLastAccretionDate(tah, defaultStartDate, accretionDate);
//
//                    } else {
//                        // по умолчанию = дате начала договора
//                        lastAccretionDate = defaultStartDate;
//                    }
//
//                    LogService.LogInfo(this.getClass(), String.format("Accretion period (d1,d2) = %s, %s ", lastAccretionDate, accretionDate));
//                    
//                    // добавили в коллекцию
//                    loc.add(new NewLiasOperImpl()
//                            .<>addAttr(tah)
//                    );
//
//                });
//
//        return loc;
//    }
    public Integer getTariff_calc_id() {
        return tariff_calc_id;
    }

    public void setTariff_calc_id( Integer tariff_calc_id) {
        this.tariff_calc_id = tariff_calc_id;
    }

    public void setEntity_id( Long entity_id) {
        this.entity_id = entity_id;
    }

    public Long getEntity_id() {
        return entity_id;
    }
}
