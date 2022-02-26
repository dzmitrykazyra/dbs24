package org.dbs24.tik.mobile.entity.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tm_firebase_apps")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class FireBaseApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tm_firebase_apps")
    @SequenceGenerator(name = "seq_tm_firebase_apps", sequenceName = "seq_tm_firebase_apps", allocationSize = 1)
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