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
import java.time.LocalDateTime;

@Data
@Entity
@IdClass(TariffLimitHistPK.class)
@Table(name = "tda_tariff_limits_hist")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TariffLimitHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @Column(name = "tariff_limit_id")
    @EqualsAndHashCode.Include
    private Long tariffLimitId;

    @Id
    @Column(name = "actual_date")
    @EqualsAndHashCode.Include
    private LocalDateTime actualDate;

    @Column(name = "daily_endpoints_limit")
    private Integer dailyEndpointsLimit;

    @Column(name = "oauth_users_limit")
    private Integer oauthUsersLimit;

    @Column(name = "bandwidth_mb_limit")
    private Integer bandwidthMbLimit;

    @Column(name = "use_premium_points")
    private Boolean usePremiumPoints;

}
