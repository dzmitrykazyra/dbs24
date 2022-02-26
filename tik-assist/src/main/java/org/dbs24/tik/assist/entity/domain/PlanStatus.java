package org.dbs24.tik.assist.entity.domain;

import lombok.Data;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "tik_plan_statuses_ref")
public class PlanStatus extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "plan_status_id", updatable = false)
    private Integer planStatusId;

    @NotNull
    @Column(name = "plan_status_name", updatable = false)
    private String planStatusName;
}