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
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;
import static org.dbs24.tik.dev.consts.TikDevApiConsts.Databases.SEQ_GENERAL;

@Data
@Entity
@Table(name = "tda_tariff_plans")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TariffPlan extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ_GENERAL)
    @SequenceGenerator(name = SEQ_GENERAL, sequenceName = SEQ_GENERAL, allocationSize = 1)
    @NotNull
    @Column(name = "tariff_plan_id", updatable = false)
    @EqualsAndHashCode.Include
    private Long tariffPlanId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tariff_plan_status_id", referencedColumnName = "tariff_plan_status_id")
    private TariffPlanStatus tariffPlanStatus;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tariff_plan_type_id", referencedColumnName = "tariff_plan_type_id")
    private TariffPlanType tariffPlanType;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tariff_limit_id", referencedColumnName = "tariff_limit_id")
    private TariffLimit tariffLimit;

    @NotNull
    @Column(name = "tp_name")
    private String tpName;

    @NotNull
    @Column(name = "tp_note")
    private String tpNote;

}
