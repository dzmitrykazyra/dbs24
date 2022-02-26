/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "pmt_payers")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Payer extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pmt_payers")
    @SequenceGenerator(name = "seq_pmt_payers", sequenceName = "seq_pmt_payers", allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "payer_id", updatable = false)
    private Integer payerId;
    
    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "application_id", referencedColumnName = "application_id")
    private Application application;
    
    @NotNull
    @Column(name = "login_token", updatable = false)
    private String loginToken;

}
