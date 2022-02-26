/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.entity;

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
@IdClass(AppPackageHistPK.class)
@Table(name = "pr_packages_hist")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AppPackageHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "package_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer packageId;

    @Id
    @NotNull
    @Column(name = "actual_date")
    @EqualsAndHashCode.Include
    private LocalDateTime actualDate;

    @Column(name = "package_name")
    private String packageName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "provider_id", referencedColumnName = "provider_id")
    private Provider provider;

    @Column(name = "is_actual")
    private Boolean isActual;

    @Column(name = "package_note")
    private String packageNote;

}
