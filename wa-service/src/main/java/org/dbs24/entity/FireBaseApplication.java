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
@Table(name = "wa_firebase_apps")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class FireBaseApplication extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_wa_firebaseapps")
    @SequenceGenerator(name = "seq_wa_firebaseapps", sequenceName = "seq_wa_firebaseapps", allocationSize = 1)
    @NotNull
    @Column(name = "firebase_app_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer firebaseAppId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @Column(name = "admin_sdk")
    private String adminSdk;

    @NotNull
    @Column(name = "package_name")
    private String packageName;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "db_url")
    private String dbUrl;

    @NotNull
    @Column(name = "is_actual")
    private Boolean isActual;
}
