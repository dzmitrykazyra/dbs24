/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity;

import javax.persistence.*;
import lombok.Data;
import javax.validation.constraints.NotNull;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "appUser")
public class AppUser extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Integer id;

    @NotNull
    @Column(name = "REG_TIME")
    private LocalDateTime regTime;

    @NotNull
    @Column(name = "AUTH_TOKEN")
    private String authToken;

    @NotNull
    @Column(name = "GCM_TOKEN")
    private String gcmTokeN;

    @NotNull
    @Column(name = "APP_NAME")
    private String appName;

    @NotNull
    @Column(name = "APP_VERSION")
    private String appVersion;

    @NotNull
    @Column(name = "ANDROID_SECURE_ID")
    private String andriodSecureId;

    @NotNull
    @Column(name = "DEVICE_FINGERPRINT")
    private String deviceFingerPring;

    @NotNull
    @Column(name = "GSF_ID")
    private String gsfId;

    @NotNull
    @Column(name = "COUNTRY_CODE")
    private String countryCode;

    @NotNull
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
}
