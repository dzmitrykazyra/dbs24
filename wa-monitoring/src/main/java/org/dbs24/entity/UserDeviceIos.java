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
@Table(name = "wa_users_devices_ios")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserDeviceIos extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "device_id")
    @EqualsAndHashCode.Include
    private Integer deviceId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @Column(name = "apple_id")
    private String appleId;

    @NotNull
    @Column(name = "identifier_for_vendor")
    private String identifierForVendor;
    
    @Column(name = "system_version")
    private String systemVersion;

    @Column(name = "model")
    private String model;

    @Column(name = "ustname_release")
    private String ustnameRelease;

    @Column(name = "ustname_version")
    private String ustnameVersion;

    @Column(name = "ustname_machine")
    private String ustnameMachine;
    
    @Column(name = "icm_token")
    private String icmToken;    
}
