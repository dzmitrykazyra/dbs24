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
import java.util.Collection;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "wa_users_contracts")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserContract extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_wa_usercontracts")
    @SequenceGenerator(name = "seq_wa_usercontracts", sequenceName = "seq_wa_usercontracts", allocationSize = 1)
    @NotNull
    @Column(name = "contract_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer contractId;

    @NotNull
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "contract_type_id", referencedColumnName = "contract_type_id")
    private ContractType contractType;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "contract_status_id", referencedColumnName = "contract_status_id")
    private ContractStatus contractStatus;

    @NotNull
    @Column(name = "begin_date")
    private LocalDateTime beginDate;

    //@NotNull
    @Column(name = "end_date")
    private LocalDateTime endDate;

    //@NotNull
    @Column(name = "cancel_date")
    private LocalDateTime cancelDate;

    @NotNull
    @Column(name = "subscriptions_amount")
    private Integer subscriptionsAmount;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userContract")
    private Collection<UserSubscription> subscriptions;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "modify_reason_id", referencedColumnName = "modify_reason_id")
    private ModifyReason modifyReason;

    @Column(name = "edit_note")
    private String editNote;

}
