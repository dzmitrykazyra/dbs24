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
@Table(name = "wa_uss_activities_arc")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class SubscriptionActivityArc extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "activity_id")
    @EqualsAndHashCode.Include
    private Long activityId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "subscription_id", updatable = false)
    private UserSubscription userSubscription;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @Column(name = "is_online")
    private Boolean isOnline;

}
