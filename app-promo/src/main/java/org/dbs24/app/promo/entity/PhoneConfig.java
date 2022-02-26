package org.dbs24.app.promo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "pr_phone_configs_ref")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneConfig {

    @Id
    @NotNull
    @Column(name = "phone_config_id", updatable = false)
    private Integer phoneConfigId;

    @NotNull
    @Column(name = "phone_config_name")
    private String phoneConfigName;
}
