package org.dbs24.tik.mobile.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tm_device_types_ref")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceType {

    @NotNull
    @Id
    @Column(name = "device_type_id", updatable = false)
    private Integer deviceTypeId;

    @NotNull
    @Column(name = "device_type_name")
    private String deviceTypeName;
}
