/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.entity.dto;

import org.dbs24.insta.tmp.entity.*;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import static javax.persistence.FetchType.LAZY;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

@Data
@Entity
@Table(name = "ACCOUNTS")
public class AccountDto extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_accounts")
    @SequenceGenerator(name = "seq_accounts", sequenceName = "seq_accounts", allocationSize = 1)
    @Column(name = "ACCOUNT_ID", updatable = false)
    private Long accountId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "account_status_id", referencedColumnName = "account_status_id")
    private AccountStatus accountStatus;

    @Column(name = "insta_id")
    private Long instaId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "MEDIA_COUNT")
    private Integer mediaCount;

    @Column(name = "FOLLOWERS_CNT")
    private Integer followers;

    @Column(name = "FOLLOWING_CNT")
    private Integer followees;

    @Column(name = "biography")
    private String biography;

    @Column(name = "is_private")
    private Integer isPrivate;

    @Column(name = "is_verified")
    private Integer isVerified;

    @Column(name = "profile_pic_url")
    private String profilePicUrl;

    @Column(name = "PROFILE_PIC_URL_HD")
    private String profilePicUrlHd;
}
