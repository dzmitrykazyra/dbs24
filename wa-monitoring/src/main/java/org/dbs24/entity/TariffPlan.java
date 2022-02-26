package org.dbs24.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "wa_tariffs_plans")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TariffPlan extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_wa_tariffplans")
    @SequenceGenerator(name = "seq_wa_tariffplans", sequenceName = "seq_wa_tariffplans", allocationSize = 1)
    @NotNull
    @Column(name = "tariff_plan_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer tariffPlanId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "contract_type_id", referencedColumnName = "contract_type_id")
    private ContractType contractType;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "tariff_plan_status_id", referencedColumnName = "tariff_plan_status_id")
    private TariffPlanStatus tariffPlanStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "device_type_id", referencedColumnName = "device_type_id")
    private DeviceType deviceType;

    @Column(name = "sku")
    private String sku;

    @NotNull
    @Column(name = "duration_hours")
    private Integer durationHours;

    @NotNull
    @Column(name = "subscriptions_amount")
    private Integer subscriptionsAmount;

    // future offset of trial contract
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Transient
    private Integer futureOffset;
}