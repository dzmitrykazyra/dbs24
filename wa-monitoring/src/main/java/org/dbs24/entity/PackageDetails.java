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
@Table(name = "wa_package_details")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class PackageDetails extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "package_name", updatable = false)
    private String packageName;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @Column(name = "company_name")
    private String companyName;

    @NotNull
    @Column(name = "app_name")
    private String appName;

    @NotNull
    @Column(name = "contact_info")
    private String contactInfo;
}
