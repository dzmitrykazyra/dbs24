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
import org.dbs24.tik.dev.entity.reference.TariffPlanStatus;
import org.dbs24.tik.dev.entity.reference.TariffPlanType;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "tda_tariff_plans_hist")
@IdClass(TariffPlanHistPK.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TariffPlanHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "tariff_plan_id", updatable = false)
    private Long tariffPlanId;

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tariff_plan_status_id", referencedColumnName = "tariff_plan_status_id")
    private TariffPlanStatus tariffPlanStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tariff_plan_type_id", referencedColumnName = "tariff_plan_type_id")
    private TariffPlanType tariffPlanType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tariff_limit_id", referencedColumnName = "tariff_limit_id")
    private TariffLimit tariffLimit;

    @Column(name = "tp_name")
    private String tpName;

    @Column(name = "tp_note")
    private String tpNote;

}
