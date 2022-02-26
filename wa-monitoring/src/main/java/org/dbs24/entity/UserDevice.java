/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
@Table(name = "wa_users_devices")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserDevice extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_wa_userdevices")
    @SequenceGenerator(name = "seq_wa_userdevices", sequenceName = "seq_wa_userdevices", allocationSize = 1)
    @NotNull
    @Column(name = "device_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer deviceId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "device_type_id", referencedColumnName = "device_type_id")
    private DeviceType deviceType;

    @Column(name = "app_name")
    private String appName;
    
    @Column(name = "app_version")
    private String appVersion;   
    
    @Column(name = "mac_addr")
    private String macAddr;
}
