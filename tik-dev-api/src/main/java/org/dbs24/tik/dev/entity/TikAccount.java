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
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;
import static org.dbs24.tik.dev.consts.TikDevApiConsts.Databases.SEQ_GENERAL;

@Data
@Entity
@Table(name = "tda_tik_accounts")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TikAccount extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ_GENERAL)
    @SequenceGenerator(name = SEQ_GENERAL, sequenceName = SEQ_GENERAL, allocationSize = 1)
    @NotNull
    @Column(name = "tik_account_id", updatable = false)
    @EqualsAndHashCode.Include
    private Long tikAccountId;

    @NotNull
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tik_account_status_id", referencedColumnName = "tik_account_status_id")
    private TikAccountStatus tikAccountStatus;

    @NotNull
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
