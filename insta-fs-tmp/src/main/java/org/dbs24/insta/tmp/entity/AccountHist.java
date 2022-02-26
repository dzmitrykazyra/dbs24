/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import static javax.persistence.FetchType.LAZY;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

@Data
@Entity
@Deprecated
@Table(name = "ifs_account_hist")
@IdClass(AccountHistPK.class)
public class AccountHist extends ObjectRoot implements PersistenceEntity {
    
    @Id
    @NotNull
    @Column(name = "account_id", updatable = false)
    private Long accountId;
    
    @Id
    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;
    
    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "account_status_id", referencedColumnName = "account_status_id")
    private AccountStatus accountStatus;
    
    @Column(name = "insta_id", updatable = false)
    private Long instaId;
    
    @Column(name = "user_name")
    private String userName;
    
    @Column(name = "full_name")
    private String fullName;
    
    @Column(name = "mediacount")
    private Integer mediaCount;
    
    @Column(name = "followers")
    private Integer followers;
    
    @Column(name = "followees")
    private Integer followees;
    
    @Column(name = "biography")
    private String biography;
    
    @Column(name = "is_private")
    private Integer isPrivate;
    
    @Column(name = "is_verified")
    private Integer isVerified;
    
    @Column(name = "profile_pic_url")
    private String profilePicUrl;
    
    @Column(name = "profile_pic_hd_url")
    private String profilePicUrlHd;
    
    //==========================================================================
    public void assign(Account account) {
        this.setAccountId(account.getAccountId());
        this.setAccountStatus(account.getAccountStatus());
        this.setActualDate(account.getActualDate());
        this.setBiography(account.getBiography());
        this.setFollowees(account.getFollowees());
        this.setFollowers(account.getFollowers());
        this.setFullName(account.getFullName());
        this.setInstaId(account.getInstaId());
        this.setIsPrivate(account.getIsPrivate());
        this.setIsVerified(account.getIsVerified());
        this.setMediaCount(account.getMediaCount());
        this.setProfilePicUrlHd(account.getProfilePicUrlHd());
        this.setProfilePicUrl(account.getProfilePicUrl());
        this.setUserName(account.getUserName());
    }
}
