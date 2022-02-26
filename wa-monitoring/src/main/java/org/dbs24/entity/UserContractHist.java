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
@Table(name = "wa_users_contracts_hist")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@IdClass(UserContractHistPK.class)
public class UserContractHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "contract_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer contractId;

    @Id
    @NotNull
    @Column(name = "actual_date")
    @EqualsAndHashCode.Include
    private LocalDateTime actualDate;

    @ManyToOne
    //@NotNull
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "contract_type_id", referencedColumnName = "contract_type_id")
    private ContractType contractType;

    @ManyToOne
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "modify_reason_id", referencedColumnName = "modify_reason_id")
    private ModifyReason modifyReason;

    @Column(name = "edit_note")
    private String editNote;

}
