package org.dbs24.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "wa_tariffs_plan_statuses_ref")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TariffPlanStatus extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "tariff_plan_status_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer tariffPlanStatusId;

    @NotNull
    @Column(name = "tariff_plan_status_name")
    private String tariffPlanStatusName;
}
