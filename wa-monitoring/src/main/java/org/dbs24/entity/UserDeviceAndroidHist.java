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

@Data
@Entity
@IdClass(UserDeviceAndroidHistPK.class)
@Table(name = "wa_users_devices_android_hist")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserDeviceAndroidHist extends ObjectRoot implements PersistenceEntity {

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

    @Column(name = "gsf_id")
    private String gsfId;
    
    @Column(name = "fcm_token")
    private String fcmToken;
   
    @Column(name = "secure_id")
    private String secureId;

    @Column(name = "device_fingerprint")
    private String deviceFingerprint;

    @Column(name = "version_sdk_int")
    private String versionSdkInt;

    @Column(name = "version_release")
    private String versionRelease;

    @Column(name = "board")
    private String board;

    @Column(name = "manufacter")
    private String manufacter;

    @Column(name = "supported_abis")
    private String supportedAbis;
    
    //==========================================================================
    public void assign(UserDeviceAndroid userDeviceAndroid) {
        this.setActualDate(userDeviceAndroid.getActualDate());
        this.setBoard(userDeviceAndroid.getBoard());
        this.setDeviceFingerprint(userDeviceAndroid.getDeviceFingerprint());
        this.setDeviceId(userDeviceAndroid.getDeviceId());
        this.setFcmToken(userDeviceAndroid.getFcmToken());
        this.setGsfId(userDeviceAndroid.getGsfId());
        this.setManufacter(userDeviceAndroid.getManufacter());
        this.setSecureId(userDeviceAndroid.getSecureId());
        this.setSupportedAbis(userDeviceAndroid.getSupportedAbis());
        this.setVersionRelease(userDeviceAndroid.getVersionRelease());
        this.setVersionSdkInt(userDeviceAndroid.getVersionSdkInt());
    }    
}
