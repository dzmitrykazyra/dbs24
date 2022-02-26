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

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@IdClass(UserSubscriptionHistPK.class)
@Table(name = "wa_users_subscriptions_hist")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserSubscriptionHist extends ObjectRoot implements PersistenceEntity {
    
    @Id
    @NotNull
    @Column(name = "subscription_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer subscriptionId;
    
    @Id
    @NotNull
    @Column(name = "actual_date")
    @EqualsAndHashCode.Include
    private LocalDateTime actualDate;

    @Column(name = "last_status_change_date")
    private LocalDateTime lastStatusChangeDate;

    //@NotNull
    @Column(name = "subscription_name")
    private String subscriptionName;
    
    @NotNull
    @Column(name = "phone_num")
    private String phoneNum;
    
    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
    
    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "agent_id", referencedColumnName = "agent_id")
    private Agent agent;
    
    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "contract_id", referencedColumnName = "contract_id")
    private UserContract userContract;
    
    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "subscription_status_id", referencedColumnName = "subscription_status_id")
    private SubscriptionStatus subscriptionStatus;
    
    @NotNull
    @Column(name = "online_notify")
    private Boolean onlineNotify;
    
    @Basic(fetch = LAZY)
    @Column(name = "avatar_img")
    private byte[] avatar;

    @Basic(fetch = LAZY)
    @Column(name = "custom_avatar_img")
    private byte[] customAvatar;
    
    @Column(name = "avatar_id")
    private Long avatarId;

    @Column(name = "avatar_modify_date")
    private LocalDateTime avatarModifyDate;

}
