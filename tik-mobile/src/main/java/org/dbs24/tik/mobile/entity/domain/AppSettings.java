package org.dbs24.tik.mobile.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "tm_app_settings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tm_app_settings")
    @SequenceGenerator(name = "seq_tm_app_settings", sequenceName = "seq_tm_app_settings", allocationSize = 1)
    @NotNull
    @Column(name = "app_settings_id")
    private Integer appSettingsId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @Column(name = "package_name")
    private String packageName;

    @NotNull
    @Column(name = "company_name")
    private String companyName;

    @NotNull
    @Column(name = "app_name")
    private String appName;

    @NotNull
    @Column(name = "min_version_code")
    private String minVersionCode;

    @NotNull
    @Column(name = "min_version")
    private String minVersion;

    @NotNull
    @Column(name = "site_url")
    private String siteUrl;

    @Column(name = "email")
    private String email;

    @Column(name = "whatsapp_id")
    private String whatsappId;

    @Column(name = "note")
    private String note;
}
