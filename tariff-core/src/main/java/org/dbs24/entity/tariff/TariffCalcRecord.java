/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.tariff;

import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.entity.core.AbstractPersistenceEntity;
import org.dbs24.persistence.api.PersistenceEntity;
import java.util.Collection;
import javax.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import org.dbs24.application.core.service.funcs.FilterComparator;
import java.util.Comparator;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Entity
@Table(name = "TariffCalcRecords")
public class TariffCalcRecord extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_TariffCalcRecords")
    @SequenceGenerator(name = "seq_TariffCalcRecords", sequenceName = "seq_TariffCalcRecords", allocationSize = 1)
    @Column(name = "tariff_calc_id")
    private Integer tariffCalcId;
    //--------------------------------------------------------------------------
    @ManyToOne
    @JoinColumn(name = "rate_id", referencedColumnName = "rate_id")    
    private TariffRate tariffRate;
    //--------------------------------------------------------------------------
    @ManyToOne
    @JoinColumn(name = "entity_id", referencedColumnName = "entity_id")
    private AbstractPersistenceEntity entity;
    //--------------------------------------------------------------------------
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tariffCalcRecord")
    private Collection<TariffCalcSum> tariffSums = ServiceFuncs.<TariffCalcSum>createCollection();

    //==========================================================================
    public void mergeRecord(final TariffCalcRecord tariffCalcRecord, final LocalDate D1, final LocalDate D2) {
        if (NullSafe.isNull(this.getTariffRate())) {
            this.setTariffRate(tariffCalcRecord.getTariffRate());
        }
        if (NullSafe.isNull(this.getEntity())) {
            this.setEntity(tariffCalcRecord.getEntity());
        }

        final LocalDate d1 = D1.minusDays(1);
        final LocalDate d2 = D2.plusDays(1);

        final FilterComparator<TariffCalcSum> TCSC = (TariffCalcSum tcs) -> (tcs.getTariffCalcDate().isAfter(d1) && tcs.getTariffCalcDate().isBefore(d2));

        // коллекция элементов для удаления
        final Collection<TariffCalcSum> forDelete
                = this.getTariffSums()
                        .stream()
                        .filter(tcs -> TCSC.getFilter(tcs))
                        .collect(Collectors.toList());
        forDelete
                .stream()
                .forEach(sum -> this.getTariffSums().remove(sum));
        // Добавить новые
        tariffCalcRecord
                .getTariffSums()
                .stream()
                .filter(tcs -> TCSC.getFilter(tcs))
                .forEach(sum -> { 
                    sum.setTariffCalcRecord(this);
                    this.getTariffSums().add(sum);                     
                });
    }

}
