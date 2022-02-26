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

import static javax.persistence.GenerationType.SEQUENCE;
import static org.dbs24.ad.server.consts.AdConsts.Sequences.SEQ_MAIN;

@Data
@Entity
@Table(name = "ads_common_settings")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AdsSettings extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ_MAIN)
    @SequenceGenerator(name = SEQ_MAIN, sequenceName = SEQ_MAIN, allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "setting_id", updatable = false)
    private Integer settingId;

    @NotNull
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @NotNull
    @Column(name = "modify_date")
    private LocalDateTime modifyDate;

    @NotNull
    @Column(name = "is_actual")
    private Boolean isActual;

    @NotNull
    @Column(name = "packages")
    private String appPackages;

    @NotNull
    @Embedded
    @Column(name = "setting_body")
    private AdSettingDetails adSettingDetails;
}
