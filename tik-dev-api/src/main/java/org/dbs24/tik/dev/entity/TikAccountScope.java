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
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;
import static org.dbs24.tik.dev.consts.TikDevApiConsts.Databases.SEQ_GENERAL;

@Data
@Entity
@Table(name = "tda_ta_granted_scopes")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TikAccountScope extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ_GENERAL)
    @SequenceGenerator(name = SEQ_GENERAL, sequenceName = SEQ_GENERAL, allocationSize = 1)
    @NotNull
    @Column(name = "grant_id", updatable = false)
    @EqualsAndHashCode.Include
    private Long grantId;

    @NotNull
    @Column(name = "grant_date", updatable = false)
    private LocalDateTime grantDate;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tik_account_id", referencedColumnName = "tik_account_id")
    private TikAccount tikAccount;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "endpoint_scope_id", referencedColumnName = "endpoint_scope_id")
    private EndpointScope endpointScope;

    @NotNull
    @Column(name = "is_granted")
    private Boolean isGranted;

}
