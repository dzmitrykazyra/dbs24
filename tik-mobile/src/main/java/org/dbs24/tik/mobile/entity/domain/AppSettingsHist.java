package org.dbs24.tik.mobile.entity.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.stmt.StmtProcessor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@IdClass(AppSettingsHistPK.class)
@Table(name = "tm_app_settings_hist")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AppSettingsHist {

    @Id
    @NotNull
    @Column(name = "app_settings_id", updatable = false)
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

    public static AppSettingsHist toHist(AppSettings appSettings) {

        return StmtProcessor.create(AppSettingsHist.class, appSettingsHist -> {
            appSettingsHist.setAppSettingId(appSettings.getAppSettingsId());
            appSettingsHist.setActualDate(appSettings.getActualDate());
            appSettingsHist.setPackageName(appSettings.getPackageName());
            appSettingsHist.setEmail(appSettings.getEmail());
            appSettingsHist.setMinVersionCode(appSettings.getMinVersionCode());
            appSettingsHist.setMinVersion(appSettings.getMinVersion());
            appSettingsHist.setCompanyName(appSettings.getCompanyName());
            appSettingsHist.setAppName(appSettings.getAppName());
            appSettingsHist.setSiteUrl(appSettings.getSiteUrl());
            appSettingsHist.setWhatsappId(appSettings.getWhatsappId());
            appSettingsHist.setNote(appSettings.getNote());
        });
    }

}

@Data
@EqualsAndHashCode
class AppSettingsHistPK implements Serializable {
    private Integer appSettingId;
    private LocalDateTime actualDate;

}