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
import org.dbs24.tik.dev.entity.reference.TikAccountStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "tda_tik_accounts_hist")
@IdClass(TikAccountHistPK.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TikAccountHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @Column(name = "tik_account_id", updatable = false)
    @EqualsAndHashCode.Include
    private Long tikAccountId;

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "actual_date", updatable = false)
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tik_account_status_id", referencedColumnName = "tik_account_status_id")
    private TikAccountStatus tikAccountStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "developer_id", referencedColumnName = "developer_id")
    private Developer developer;

    @Column(name = "tik_login")
    private String tikLogin;

    @Column(name = "tik_pwd")
    private String tikPwd;

    @Column(name = "tik_email")
    private String tikEmail;

    @Column(name = "tik_auth_key")
    private String tikAuthKey;

}
