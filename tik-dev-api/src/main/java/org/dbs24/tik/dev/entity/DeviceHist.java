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
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "tda_devices_hist")
@IdClass(DeviceHistPK.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class DeviceHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @Column(name = "device_id", updatable = false)
    @EqualsAndHashCode.Include
    private Long deviceId;

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "device_status_id", referencedColumnName = "device_status_id")
    private DeviceStatus deviceStatus;

    @Column(name = "device_id_str")
    private String deviceIdStr;

    @Column(name = "install_id")
    private String installId;

    @Basic(fetch = LAZY)
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "apk_attrs")
    private byte[] apkAttrs;


    @Column(name = "apk_hash_id")
    private String apkHashId;

}
