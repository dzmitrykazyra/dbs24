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
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;
import static org.dbs24.tik.dev.consts.TikDevApiConsts.Databases.SEQ_GENERAL;

@Data
@Entity
@Table(name = "tda_tariff_plans_prices")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TariffPlanPrice extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ_GENERAL)
    @SequenceGenerator(name = SEQ_GENERAL, sequenceName = SEQ_GENERAL, allocationSize = 1)
    @NotNull
    @Column(name = "tariff_price_id", updatable = false)
    @EqualsAndHashCode.Include
    private Long tariffPriceId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @Column(name = "tariff_begin_date")
    private LocalDateTime tariffBeginDate;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tariff_plan_type_id", referencedColumnName = "tariff_plan_type_id")
    private TariffPlanType tariffPlanType;

    @NotNull
    @Column(name = "country_code")
    private String countryCode;

    @NotNull
    @Column(name = "currency_iso")
    private String currencyIso;

    @NotNull
    @Column(name = "summ")
    private BigDecimal summ;

}
