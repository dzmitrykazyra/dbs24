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
@IdClass(AppSettingsHistPK.class)
@Table(name = "wa_app_settings_hist")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AppSettingsHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "app_setting_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer appSettingId;

    @Id
    @Column(name = "actual_date")
    @EqualsAndHashCode.Include
    private LocalDateTime actualDate;

    @Column(name = "package_name")
    private String packageName;

    @Column(name = "email")
    private String email;

    @Column(name = "min_version_code")
    private String minVersionCode;

    @Column(name = "min_version")
    private String minVersion;

    @NotNull
    @Column(name = "company_name")
    private String companyName;

    @NotNull
    @Column(name = "app_name")
    private String appName;

    @Column(name = "site_url")
    private String siteUrl;

    @Column(name = "whatsapp_id")
    private String whatsappId;

    @Column(name = "note")
    private String note;

    private String diff;

}
