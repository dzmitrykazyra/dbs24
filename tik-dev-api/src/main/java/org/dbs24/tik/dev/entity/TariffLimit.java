/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.dbs24.tik.dev.consts.TikDevApiConsts.Databases.SEQ_GENERAL;

@Data
@Entity
@Table(name = "tda_tariff_limits")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TariffLimit extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ_GENERAL)
    @SequenceGenerator(name = SEQ_GENERAL, sequenceName = SEQ_GENERAL, allocationSize = 1)
    @NotNull
    @Column(name = "tariff_limit_id", updatable = false)
    @EqualsAndHashCode.Include
    private Long tariffLimitId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @Column(name = "daily_endpoints_limit")
    private Integer dailyEndpointsLimit;

    @NotNull
    @Column(name = "oauth_users_limit")
    private Integer oauthUsersLimit;

    @NotNull
    @Column(name = "bandwidth_mb_limit")
    private Integer bandwidthMbLimit;

    @NotNull
    @Column(name = "use_premium_points")
    private Boolean usePremiumPoints;

}
