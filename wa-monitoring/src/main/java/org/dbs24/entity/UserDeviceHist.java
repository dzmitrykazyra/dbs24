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
@Table(name = "wa_users_devices_hist")
@IdClass(UserDeviceHistPK.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserDeviceHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "device_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer deviceId;

    @Id
    @NotNull
    @Column(name = "actual_date")
    @EqualsAndHashCode.Include
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
    
    //==========================================================================
    public void assign(UserDevice userDevice) {
        this.setDeviceId(userDevice.getDeviceId());
        this.setActualDate(userDevice.getActualDate());
        this.setAppName(userDevice.getAppName());
        this.setAppVersion(userDevice.getAppVersion());
        this.setDeviceType(userDevice.getDeviceType());
        this.setMacAddr(userDevice.getMacAddr());
        this.setUser(userDevice.getUser());
    }      
}
