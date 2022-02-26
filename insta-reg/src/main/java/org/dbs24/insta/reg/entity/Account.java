/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.insta.reg.action.BatchFetchWeb;
import org.dbs24.insta.reg.action.CsrfRecords;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "inst_accounts")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Account extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_inst_accounts")
    @SequenceGenerator(name = "seq_inst_accounts", sequenceName = "seq_inst_accounts", allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "account_id", updatable = false)
    private Integer accountId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @NotNull
    @Column(name = "login", updatable = false)
    private String login;

    @NotNull
    @Column(name = "pwd", updatable = false)
    private String pwd;

    @NotNull
    @Column(name = "first_name", updatable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", updatable = false)
    private String lastName;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "email_address", referencedColumnName = "email_address")
    private Email email;

    @NotNull
    @Column(name = "faked_email", updatable = false)
    private String fakedEmail;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "account_status_id", referencedColumnName = "account_status_id")
    private AccountStatus accountStatus;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "proxy_id", referencedColumnName = "proxy_id")
    private Proxy proxy;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "batch_id", referencedColumnName = "batch_id")
    private Batch batch;

    @Column(name = "account_notes")
    private String accountNotes;

    @Column(name = "used_ip")
    private String usedIp;

    //==========================================================================    
    @Transient
    private String signUpCode;
    
    @Transient
    private String validationCode;

    @Transient
    private String instaMid;

    @Transient
    private CsrfRecords instaCsrf;

    @Transient
    private BatchFetchWeb batchFetchWeb;

    @Transient
    private String encPwd;

    @Transient
    private int year;

    @Transient
    private int month;

    @Transient
    private int day;

}
