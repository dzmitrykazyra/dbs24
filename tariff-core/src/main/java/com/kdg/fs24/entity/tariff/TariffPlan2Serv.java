/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.tariff;

/**
 *
 * @author Козыро Дмитрий
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kdg.fs24.application.core.nullsafe.NullSafe;
import com.kdg.fs24.application.core.service.funcs.ServiceFuncs;
import com.kdg.fs24.persistence.api.PersistenceEntity;
import com.kdg.fs24.references.tariffs.serv.TariffServ;
import com.kdg.fs24.references.tariffs.kind.TariffKind;
import java.time.LocalDate;
import javax.persistence.*;
import lombok.Data;
import java.util.Collection;
import com.kdg.fs24.references.tariffs.accretionscheme.TariffAccretionScheme;

@Data
@Entity
@Table(name = "TariffPlan2ServId")
@IdClass(TariffPlan2ServPK.class)
public class TariffPlan2Serv implements PersistenceEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "tariff_plan_id")
    @JsonIgnore
    private AbstractTariffPlan tariffPlan;

    @Id
    @ManyToOne
    @JoinColumn(name = "tariff_serv_id")
    private TariffServ tariffServ;

    @ManyToOne
    @JoinColumn(name = "tariff_kind_id")
    private TariffKind tariffKind;

    @Column(name = "actual_date")
    private LocalDate actualDate;

    @Column(name = "close_date")
    private LocalDate closeDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tariffPlan2Serv")
    private Collection<TariffRate> tariffRates = ServiceFuncs.<TariffRate>createCollection();

    //==========================================================================
    public void addTariffRate(final TariffAccretionScheme tariffAccretionScheme,
            final TariffKind tariffKind,
            final LocalDate actualDate,
            final LocalDate closeDate,
            final String rateName,
            final TariffRateProcessor tariffRateProcessor) {

        final TariffRate tariffRate = NullSafe.createObject(TariffRate.class);

        tariffRate.setTariffAccretionScheme(tariffAccretionScheme);
        tariffRate.setTariffPlan2Serv(this);
        tariffRate.setTariffKind(tariffKind);
        tariffRate.setRateName(rateName);
        tariffRate.setActualDate(actualDate);
        tariffRate.setCloseDate(closeDate);

        tariffRateProcessor.processTariffRate(tariffRate);

        tariffRates.add(tariffRate);
    }
}
