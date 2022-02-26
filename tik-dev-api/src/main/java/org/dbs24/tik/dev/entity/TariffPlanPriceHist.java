/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;
import org.dbs24.tik.dev.entity.reference.TariffPlanType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "tda_tariff_plans_prices_hist")
@IdClass(TariffPlanPriceHistPK.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TariffPlanPriceHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @Column(name = "tariff_price_id", updatable = false)
    @EqualsAndHashCode.Include
    private Long tariffPriceId;

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "tariff_begin_date")
    private LocalDateTime tariffBeginDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tariff_plan_type_id", referencedColumnName = "tariff_plan_type_id")
    private TariffPlanType tariffPlanType;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "currency_iso")
    private String currencyIso;

    @Column(name = "summ")
    private BigDecimal summ;

}
