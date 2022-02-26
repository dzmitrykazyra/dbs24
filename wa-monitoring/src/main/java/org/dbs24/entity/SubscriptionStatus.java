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


@Data
@Entity
@Table(name = "wa_subscription_statuses_ref")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class SubscriptionStatus extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "subscription_status_id", updatable = false)
    @EqualsAndHashCode.Include
    private Byte subscriptionStatusId;

    @NotNull
    @Column(name = "subscription_status_name")
    private String subscriptionStatusName;
}
