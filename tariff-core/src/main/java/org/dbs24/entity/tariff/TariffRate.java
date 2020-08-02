/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.tariff;

/**
 *
 * @author Козыро Дмитрий
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.dbs24.references.tariffs.accretionscheme.TariffAccretionScheme;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.persistence.api.PersistenceEntity;
import org.dbs24.references.tariffs.kind.TariffKind;
import org.dbs24.references.application.currency.Currency;
import java.math.BigDecimal;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.Data;

@Data
@Entity
//@MappedSuperclass
@Table(name = "TariffRates")
public class TariffRate extends ObjectRoot implements PersistenceEntity {

    @Id
    @Column(name = "rate_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_TariffRates")
    @SequenceGenerator(name = "seq_TariffRates", sequenceName = "seq_TariffRates", allocationSize = 1)
    private Integer rateId;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "tariff_plan_id", referencedColumnName = "tariff_plan_id")
        , @JoinColumn(name = "tariff_serv_id", referencedColumnName = "tariff_serv_id")})
    @JsonIgnore
    private TariffPlan2Serv tariffPlan2Serv;

    @ManyToOne
    @JoinColumn(name = "tariff_kind_id", referencedColumnName = "tariff_kind_id")
    private TariffKind tariffKind;

    @ManyToOne
    @JoinColumn(name = "tariff_scheme_id", referencedColumnName = "tariff_scheme_id")
    private TariffAccretionScheme tariffAccretionScheme;

    @Column(name = "rate_name")
    private String rateName;

    @Column(name = "actual_date")
    private LocalDate actualDate;

    @Column(name = "close_date")
    private LocalDate closeDate;
    //==========================================================================

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tariffRate")
    private Collection<TariffRate_1> tariffRates_1 = ServiceFuncs.<TariffRate_1>createCollection();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tariffRate")
    private Collection<TariffRate_2> tariffRates_2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tariffRate")
    private Collection<TariffRate_3> tariffRates_3;

    //==========================================================================
    @Transient
    private Collection<TariffRecordAbstract> tariffRates;

    public Collection<TariffRecordAbstract> getTariffRates() {
        // приведение к типу TariffRecordAbstract
        if (NullSafe.isNull(tariffRates)) {

            if (NullSafe.notNull(tariffRates_1)) {
                tariffRates = tariffRates_1
                        .stream()
                        .map(mapper -> (TariffRecordAbstract) mapper)
                        .collect(Collectors.toList());
            }

            if (NullSafe.isNull(tariffRates)) {
                if (NullSafe.notNull(tariffRates_2)) {
                    tariffRates = tariffRates_2
                            .stream()
                            .map(mapper -> (TariffRecordAbstract) mapper)
                            .collect(Collectors.toList());
                }
            }

            if (NullSafe.isNull(tariffRates)) {
                if (NullSafe.notNull(tariffRates_3)) {
                    tariffRates = tariffRates_3
                            .stream()
                            .map(mapper -> (TariffRecordAbstract) mapper)
                            .collect(Collectors.toList());
                }
            }

            // ставки не заданы
            if (NullSafe.isNull(tariffRates)) {
                throw new RuntimeException("Ставки не заданы!");
            }
        }

        return tariffRates;
    }

    //--------------------------------------------------------------------------
    public void addTariffRate_1(final LocalDate rateDate, final BigDecimal rateValue, final Currency rateCurrency) {
        final TariffRate_1 tariffRate_1 = NullSafe.createObject(TariffRate_1.class);

        tariffRate_1.setRateDate(rateDate);
        tariffRate_1.setRateValue(rateValue);
        tariffRate_1.setTariffRate(this);
        tariffRate_1.setCurrency(rateCurrency);

        this.tariffRates_1.add(tariffRate_1);
    }

    //==========================================================================
    public void printRates() {

    }
}
