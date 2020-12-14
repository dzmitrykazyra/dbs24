/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.tariffs.kind;

//import org.dbs24.entity.tariff.TariffRate;
import org.dbs24.entity.core.api.ActionEntity;
import java.math.BigDecimal;
import org.dbs24.references.tariffs.serv.TariffServ;
import java.time.LocalDate;
import java.util.Collection;

/**
 *
 * @author Козыро Дмитрий
 */
@Deprecated
public interface TariffKindOld<T extends TariffServ, E extends ActionEntity, TR extends TariffRateRecord>
        extends Cloneable {

//    TariffRate getTariffRate();
//    BigDecimal getTariffSum( TariffCalculations tc, E entity, LocalDate D1, LocalDate D2);

    Integer getTariff_serv_id();

    Integer getTariff_scheme_id();

    Integer getTariff_kind_id();

    LocalDate getActual_date();

    LocalDate getClose_date();

//    TariffRate<TR> getTariffRate();

    <TR1 extends TariffRateRecord> TR1 buildTariffRateRecord();

    void store(Long plan_id);

    TariffKindOld createTariffKindCopy() throws CloneNotSupportedException;

    Collection<TariffCalcSumOld> calculateTariff(E entity, LocalDate D1, LocalDate D2);

    //void storeTariffSums( LocalDate D1, LocalDate D2);

}
