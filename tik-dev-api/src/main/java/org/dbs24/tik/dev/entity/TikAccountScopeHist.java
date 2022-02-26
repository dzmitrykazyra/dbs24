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
import org.dbs24.tik.dev.entity.reference.EndpointScope;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "tda_ta_granted_scopes_hist")
@IdClass(TikAccountScopeHistPK.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TikAccountScopeHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @Column(name = "grant_id")
    @EqualsAndHashCode.Include
    private Long grantId;

    @Id
    @Column(name = "grant_date")
    @EqualsAndHashCode.Include
    private LocalDateTime grantDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tik_account_id", referencedColumnName = "tik_account_id")
    private TikAccount tikAccount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "endpoint_scope_id", referencedColumnName = "endpoint_scope_id")
    private EndpointScope endpointScope;

    @Column(name = "is_granted")
    private Boolean isGranted;

}
