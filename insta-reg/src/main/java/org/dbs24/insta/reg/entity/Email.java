/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.consts.SysConst;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inst_emails")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Email extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "email_address", updatable = false)
    private String emailAddress;
   
    @NotNull
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "email_status_id", referencedColumnName = "email_status_id")
    private EmailStatus emailStatus;

    @NotNull
    @Column(name = "pwd", updatable = false)
    private String pwd;
    
    @Column(name = "account_notes")
    private String accountNotes;
    
    //==========================================================================
    @Transient
    private Integer usedTimes = 0;

    @Transient
    private String lastKnownValidationCode = SysConst.EMPTY_STRING; 
    
    @Transient
    private Boolean isUnknow = false;

    @Transient
    private Boolean inUse = false;
    
}
