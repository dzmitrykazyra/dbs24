/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.ad.server.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.ad.server.dto.AdSettingDetails;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ads_common_settings_hist")
@IdClass(AdsSettingsHistPK.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AdsSettingsHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "setting_id", updatable = false)
    private Integer settingId;

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "modify_date")
    private LocalDateTime modifyDate;

    @Column(name = "is_actual")
    private Boolean isActual;

    @Embedded
    @Column(name = "setting_body")
    private AdSettingDetails adSettingDetails;

    @Column(name = "packages")
    private String appPackages;

}
