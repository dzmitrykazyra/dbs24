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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "wa_users_devices_android")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserDeviceAndroid extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "device_id")
    @EqualsAndHashCode.Include
    private Integer deviceId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
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

}
