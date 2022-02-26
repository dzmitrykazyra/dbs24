/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.entity;

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
@Table(name = "inst_accounts_hist")
@IdClass(AccountHistPK.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AccountHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "account_id", updatable = false)
    private Integer accountId;

    @Id
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

    //==========================================================================
    public void assign(Account account) {
        this.accountId = account.getAccountId();
        this.accountNotes = account.getAccountNotes();
        this.accountStatus = account.getAccountStatus();
        this.actualDate = account.getActualDate();
        this.batch = account.getBatch();
        this.createDate = account.getCreateDate();
        this.email = account.getEmail();
        this.fakedEmail = account.getFakedEmail();
        this.login = account.getLogin();
        this.proxy = account.getProxy();
    }
}
