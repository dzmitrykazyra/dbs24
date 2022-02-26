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
@IdClass(TariffPlanHistPK.class)
@Table(name = "wa_tariffs_plans_hist")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TariffPlanHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "tariff_plan_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer tariffPlanId;

    @Id
    @NotNull
    @Column(name = "actual_date")
    @EqualsAndHashCode.Include
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "contract_type_id", referencedColumnName = "contract_type_id")
    private ContractType contractType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tariff_plan_status_id", referencedColumnName = "tariff_plan_status_id")
    private TariffPlanStatus tariffPlanStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "device_type_id", referencedColumnName = "device_type_id")
    private DeviceType deviceType;

    @Column(name = "sku")
    private String sku;

    @Column(name = "duration_hours")
    private Integer durationHours;

    @Column(name = "subscriptions_amount")
    private Integer subscriptionsAmount;
}