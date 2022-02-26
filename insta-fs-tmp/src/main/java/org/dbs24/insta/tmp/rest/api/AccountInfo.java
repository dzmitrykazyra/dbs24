/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.rest.api;

import lombok.Data;
import org.dbs24.application.core.locale.NLS;

import org.dbs24.insta.tmp.entity.Account;
import org.dbs24.stmt.StmtProcessor;

@Data
public class AccountInfo {

    private Long accountId;
    private Long actualDate;
    private Integer accountStatusId;
    private Long instaId;
    private String userName;
    private String fullName;
    private Integer mediaCount;
    private Integer followers;
    private Integer followees;
    private String biography;
    private Boolean isPrivate;
    private Boolean isVerified;
    private String profilePicUrl;
    private String profilePicHdUrl;

    public void assign(Account account) {
        setAccountId(account.getAccountId());
        setAccountStatusId(account.getAccountStatus().getAccountStatusId());
        setActualDate(NLS.localDateTime2long(account.getActualDate()));
        setBiography(account.getBiography());
        setFollowees(account.getFollowees());
        setFollowers(account.getFollowers());
        setFullName(account.getFullName());
        setInstaId(account.getInstaId());
        setIsPrivate(account.getIsPrivate().equals(1));
        setIsVerified(account.getIsVerified().equals(1));
        setMediaCount(account.getMediaCount());
        setProfilePicHdUrl(account.getProfilePicUrlHd());
        setProfilePicUrl(account.getProfilePicUrl());
        setUserName(account.getUserName());
    }

    public static AccountInfo createAccountInfo(Account account) {
        return StmtProcessor.create(AccountInfo.class, ai -> ai.assign(account));
    }

}
