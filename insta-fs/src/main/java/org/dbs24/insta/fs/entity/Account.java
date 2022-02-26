/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.fs.entity;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "ifs_accounts")
public class Account extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ifs_accounts")
    @SequenceGenerator(name = "seq_ifs_accounts", sequenceName = "seq_ifs_accounts", allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "account_id", updatable = false)
    private Long accountId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "account_status_id", referencedColumnName = "account_status_id")
    private AccountStatus accountStatus;

    @Column(name = "insta_id", updatable = false)
    private Long instaId;

    @NotNull
    @Column(name = "user_name")
    private String userName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "media_count")
    private Integer mediaCount;

    @Column(name = "followers_count")
    private Integer followers;

    @Column(name = "followees_count")
    private Integer followees;

    @Column(name = "biography")
    private String biography;

    @Column(name = "is_private")
    private Boolean isPrivate;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "profile_pic_url")
    private String profilePicUrl;

    @Column(name = "profile_pic_hd_url")
    private String profilePicHdUrl;
}
