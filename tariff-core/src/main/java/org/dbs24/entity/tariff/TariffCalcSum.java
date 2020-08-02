/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.tariff;

import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.persistence.api.PersistenceEntity;
import javax.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;
import org.dbs24.references.tariffs.kind.TariffCalcSumExtended;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Entity
@Table(name = "tariffCalcSum")
@IdClass(TariffCalcSumPK.class)
@org.hibernate.annotations.BatchSize(size = 1000)
public class TariffCalcSum extends ObjectRoot implements PersistenceEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "tariff_calc_id", referencedColumnName = "tariff_calc_id")
    private TariffCalcRecord tariffCalcRecord;

    @Id
    @Column(name = "tariff_calc_date")
    private LocalDate tariffCalcDate;

    @Column(name = "tariff_summ")
    private BigDecimal tariffSumm;

    //==========================================================================
    public static TariffCalcSum create(final TariffCalcSumExtended tariffCalcSumExtended) {
        final TariffCalcSum tariffCalcSum = NullSafe.createObject(TariffCalcSum.class);

        tariffCalcSum.setTariffCalcDate(tariffCalcSumExtended.getTariff_calc_date());
        tariffCalcSum.setTariffSumm(tariffCalcSumExtended.getTariff_sum());

        return tariffCalcSum;
    }

}
