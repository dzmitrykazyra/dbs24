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
import org.dbs24.tik.dev.entity.reference.DeviceStatus;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;
import static org.dbs24.tik.dev.consts.TikDevApiConsts.Databases.SEQ_GENERAL;

@Data
@Entity
@Table(name = "tda_devices")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Device extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ_GENERAL)
    @SequenceGenerator(name = SEQ_GENERAL, sequenceName = SEQ_GENERAL, allocationSize = 1)
    @NotNull
    @Column(name = "device_id", updatable = false)
    @EqualsAndHashCode.Include
    private Long deviceId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "device_status_id", referencedColumnName = "device_status_id")
    private DeviceStatus deviceStatus;

    @NotNull
    @Column(name = "device_id_str")
    private String deviceIdStr;

    @NotNull
    @Column(name = "install_id")
    private String installId;

    @NotNull
    @Basic(fetch = LAZY)
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "apk_attrs")
    private byte[] apkAttrs;

    @NotNull
    @Column(name = "apk_hash_id")
    private String apkHashId;

}
